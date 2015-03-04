package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Random;

import javax.swing.GroupLayout.Alignment;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Main extends Application {
	
	//database connection info
	static final String DB_URL = "jdbc:mysql://localhost:3306/database1";
	public static final String USER = "root";
    public static final String PASSWORD = "Welcome1";
    
    //tax
    static final double TAX_RATE = .08;
    
    //Arrays used to store the combo box items
	String[] countryList = { "USA", "Canada"};
	String[] usaStates = { "Alabama", "Alaska", "Arizona", "Arkansas"};
	String[] provinces = {"Quebec","Ontario","British Columbia", "Alberta"};
	
	//components for displaying all information within the app
	ComboBox<String> countries, states;
	TextField cardNumberTF, expDateMTF, expDateYTF, cvvTF, nameTF, addressTF, cityTF, zipTF, phoneTF;
	Text invoiceAmount, invoiceNumber,message, totalTaxAmount, totalAmountValue;
	Button exit, pay, next, back;
	RadioButton mcRB, visaRB, discoverRB;
	
	
	@Override
	public void start(Stage primaryStage)
	{
		try {
			//the built gridpane
			GridPane root = addGridPane();
			//set scene size
			Scene scene = new Scene(root, 700, 700);
			primaryStage.setScene(scene);
			primaryStage.show();
			setAmountOfTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	//This method creates random information to simulate the data of a transaction
	//by creating a random number for the invoice number and transaction amount...
	public void setAmountOfTransaction() {
		
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		
		//compute random #s for payment details
		Random r = new Random();
		int invoiceN = Math.abs(r.nextInt(100000));
		r = new Random();
		double invoiceA = r.nextDouble() * 100.0;
		double tax = invoiceA * TAX_RATE;
		double total = invoiceA + tax;
		//set data for all the display fields
		invoiceNumber.setText(String.valueOf(invoiceN));
		invoiceAmount.setText(nf.format(invoiceA));
		totalTaxAmount.setText(nf.format(tax));
		totalAmountValue.setText(nf.format(total));
	}
	
	//used to clear the text fields when needed
	public void resetAllFields(){
		
		cardNumberTF.setText("");
		visaRB.setSelected(true);
		expDateMTF.setText("");
		expDateYTF.setText("");
		cvvTF.setText("");
		nameTF.setText("");
		addressTF.setText("");
		cityTF.setText("");
		zipTF.setText("");
		phoneTF.setText("");
		setAmountOfTransaction();
		
	}

	//used for validation of the input components to make sure all have had something entered so that
	//none are blank
	private boolean validate() {
		
		//card number not blank
		if(cardNumberTF.getText().length() <= 0){
			showValidationErrror("Card Number Field");
			return false;
		}
		
		//exp date m not blank
		if(expDateMTF.getText().length() <= 0){
			showValidationErrror("Experation Date Month Field");
			return false;
		}
		
		//exp date y not blank
		if(expDateYTF.getText().length() <= 0){
			showValidationErrror("Experation Date Year Field");
			return false;
		}
				
		//cvv not blank
		if(cvvTF.getText().length() <= 0){
			showValidationErrror("CVV Field");
			return false;
		}
		
		//name not blank
		if(nameTF.getText().length() <= 0){
			showValidationErrror("Customer Name Field");
			return false;
		}

		//address not blank
		if(addressTF.getText().length() <= 0){
			showValidationErrror("Address Field");
			return false;
		}
		
		//city not blank
		if(cityTF.getText().length() <= 0){
			showValidationErrror("City Field");
			return false;
		}
		
		//zip not blank
		if(zipTF.getText().length() <= 0){
			showValidationErrror("Zip/Postal Code Field");
			return false;
		}
		
		//phone not blank
		if(phoneTF.getText().length() <= 0){
			showValidationErrror("Phone Field");
			return false;
		}
		
		return true;
	
	}
	
	//for showing a validation error
	private void showValidationErrror(String problem) {
		
		message.setText("Error in validation of: " + problem);
		
	}

	//builds the gridpane to be shown
	public GridPane addGridPane() {

		GridPane grid = new GridPane();
		//formatting of gridpane
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(0, 10, 0, 10));

		//create components needd for input
		
		Text pageTitle = new Text("Payment Page");
		pageTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		grid.add(pageTitle, 0, 0);

		Text instructions = new Text("Enter credit card details");
		instructions.setFont(Font.font("Arial", FontWeight.BOLD, 15));
		grid.add(instructions, 0, 1);

		Text manFields = new Text("* Mandatory fields");
		manFields.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(manFields, 1, 1, 2, 1);

		Text cardNumber = new Text("* Card Number");
		cardNumber.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(cardNumber, 0, 2);

		cardNumberTF = new TextField();
		cardNumberTF.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(cardNumberTF, 1, 2);

		Text cardType = new Text("* Card type");
		cardType.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(cardType, 0, 3);

		//for formatting the card area and radiobuttons
		GridPane cards = new GridPane();
		// cards.setAlignment(Pos.BOTTOM_RIGHT);
		cards.setHgap(5);
		cards.setVgap(5);
		Text visa = new Text("Visa");
		Text mc = new Text("MasterCard");
		Text discover = new Text("Discover");

		visaRB = new RadioButton();
		GridPane.setHalignment(visaRB, HPos.CENTER);
		cards.add(visaRB, 0, 1);

		mcRB = new RadioButton();
		GridPane.setHalignment(mcRB, HPos.CENTER);
		cards.add(mcRB, 1, 1);

		discoverRB = new RadioButton();
		GridPane.setHalignment(discoverRB, HPos.CENTER);
		cards.add(discoverRB, 2, 1);

		ToggleGroup group = new ToggleGroup();
		visaRB.setToggleGroup(group);
		mcRB.setToggleGroup(group);
		discoverRB.setToggleGroup(group);
		visaRB.setSelected(true);
		cards.add(visa, 0, 0);
		cards.add(mc, 1, 0);
		cards.add(discover, 2, 0);

		grid.add(cards, 1, 3);

		Text expDate = new Text("* Experation date");
		expDate.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(expDate, 0, 4);

		expDateMTF = new TextField();
		expDateMTF.setMaxWidth(35);
		expDateMTF.setFont(Font.font("Arial", FontWeight.BOLD, 10));

		expDateYTF = new TextField();
		expDateYTF.setMaxWidth(35);
		expDateYTF.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		
		//used for formatting of the experation date fields
		HBox expStuff = new HBox();
		expStuff.setSpacing(10);
		expStuff.getChildren().addAll(expDateMTF, new Text("/"), expDateYTF);

		grid.add(expStuff, 1, 4);

		Text cvv = new Text("* CVV");
		cvv.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(cvv, 0, 5);

		cvvTF = new TextField();
		cvvTF.setMaxSize(50, 20);
		cvvTF.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(cvvTF, 1, 5);

		Text name = new Text("* Cardholder name");
		name.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(name, 0, 6);

		nameTF = new TextField();
		nameTF.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(nameTF, 1, 6);

		Text billingInformation = new Text("Billing Information ");
		billingInformation.setFont(Font.font("Arial", FontWeight.BOLD, 15));
		grid.add(billingInformation, 0, 7);

		Text address = new Text("* Address");
		address.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(address, 0, 8);

		addressTF = new TextField();
		addressTF.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(addressTF, 1, 8);

		Text city = new Text("* City");
		city.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(city, 0, 9);

		cityTF = new TextField();
		cityTF.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(cityTF, 1, 9);

		Text country = new Text("* Country");
		country.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(country, 0, 10);

		//Countries combobox stuff
		ComboBox<String> countries = new ComboBox<String>();
		//add the countries from the countries array to the combobox
		countries.getItems().addAll(countryList);
		countries.setValue(countryList[0]);
		//event handler for combobox
		countries.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub

				//determine whether usa or canada is selected and use the correct
				//array for each instance
				if(countries.getSelectionModel().getSelectedItem().equals("USA")) {
					states.getItems().setAll(usaStates);
					states.setValue(states.getItems().get(0));
					
				}else if(countries.getSelectionModel().getSelectedItem().equals("Canada")) {
					states.getItems().setAll(provinces);
					states.setValue(states.getItems().get(0));
				}
				
			}
			
		});
		
		
		grid.add(countries, 1, 10);

		Text stateProvince = new Text("* State/province");
		stateProvince.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(stateProvince, 0, 11);

		states = new ComboBox<String>();
		states.getItems().addAll(usaStates);
		states.setValue(usaStates[0]);

		grid.add(states, 1, 11);

		Text zip = new Text("* ZIP/postal code");
		zip.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(zip, 0, 12);

		zipTF = new TextField();
		zipTF.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(zipTF, 1, 12);

		Text phone = new Text("* Phone");
		phone.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(phone, 0, 13);

		phoneTF = new TextField();
		phoneTF.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		grid.add(phoneTF, 1, 13);

		//this is the gridpane used to show the information about the transaction (price etc) on the right side
		GridPane rightGrid = new GridPane();
		rightGrid.setHgap(10);
		rightGrid.setVgap(10);
		rightGrid.setPadding(new Insets(0, 10, 0, 10));

		Text rightTopHeader = new Text("Payment Details");
		rightTopHeader.setFont(Font.font("Arial", FontWeight.BOLD, 15));
		rightGrid.add(rightTopHeader, 0, 0, 3, 1);

		Text invoice = new Text("Invoice");
		invoice.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		rightGrid.add(invoice, 0, 1);

		invoiceNumber = new Text("xxxx");
		invoiceNumber.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		rightGrid.add(invoiceNumber, 1, 1);

		invoiceAmount = new Text("0.00");
		invoiceAmount.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		rightGrid.add(invoiceAmount, 2, 1);

		Text totalTax = new Text("Total Tax: ");
		totalTax.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		rightGrid.add(totalTax, 0, 2);

		totalTaxAmount = new Text("0.00");
		totalTaxAmount.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		rightGrid.add(totalTaxAmount, 2, 2);

		Text totalAmount = new Text("Total Amount: ");
		totalAmount.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		rightGrid.add(totalAmount, 0, 3);

		totalAmountValue = new Text("0.00");
		totalAmountValue.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		rightGrid.add(totalAmountValue, 2, 3);

		Separator s = new Separator();
		s.setOrientation(Orientation.VERTICAL);
		grid.add(s, 2, 1, 1, 13);
		grid.add(rightGrid, 3, 1, 1, 13);

		//For display of buttons at the bottom
		HBox buttons = new HBox();
		exit = new Button("Exit");
		exit.setOnAction(new EventHandler<ActionEvent>() {

			
			
			@Override
			public void handle(ActionEvent arg0) {
				//exit button
				System.exit(0);
			}

		});
		
		
		pay = new Button("Pay");
		pay.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				//validate data first before proceeding
				if(validate()) {
					message.setText("");
					String driverName = "com.mysql.jdbc.Driver"; // for MySql
					try {
						// Load the JDBC driver
						Class.forName(driverName);
						Connection conn = DriverManager.getConnection(DB_URL, USER,
								PASSWORD);

						Statement stmt = conn.createStatement();
						//sql statement for inserting new data into the table
						String theQuery = "INSERT INTO transactions VALUES (";
						
						theQuery += "'" + cardNumberTF.getText() + "','" + getCardSelected() + "','"
								+ expDateMTF.getText();
						theQuery += "','" + expDateYTF.getText() + "','"
								+ cvvTF.getText() + "','"
								+ nameTF.getText() + "','" + nameTF.getText() + "','";
						theQuery += cityTF.getText() + "','" + countries.getSelectionModel().getSelectedItem()
								+ "','" + countries.getSelectionModel().getSelectedItem() + "','" + zipTF.getText()
								+ "','" + phoneTF.getText() + "'," + invoiceNumber.getText() + ",'";
						theQuery +=	invoiceAmount.getText() + "','" + totalTaxAmount.getText() + "','" 
								+  totalAmountValue.getText() + "')";

						stmt.execute(theQuery);

						conn.close();
						
						//since new payment was completed successfully, reset the fields
						resetAllFields();
						
						message.setText("Transaction has been successfully saved");
						
						
					} catch (Exception e) {
						// Could not find the database driver
						System.out.println("Problem : " + e.getMessage());

					}
				}
			}

		});

		//add the buttons
		buttons.getChildren().addAll(exit, pay);

		grid.add(buttons, 3, 14, 1, 1);
		//success message
		message = new Text();
		
		grid.add(message, 0,15,4,1);

		return grid;

	}
	
	//for getting which of the 3 credit cards are currently selected (utility method)
	private String getCardSelected() {
		
		if(visaRB.isSelected())
			return "visa";
		if(mcRB.isSelected())
			return "mastercard";
		else
			return "discover";
		
	}

	//main method to start execution
	public static void main(String[] args) {
		launch(args);
	}
}
