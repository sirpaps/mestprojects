import java.io.*;
import java.util.*;

public class ZerplyAssignment_Kingsley
{

	static final String USERS_DATA = "user.csv";
	static final String PROFILES_DATA = "profile.csv";

	public static void main(String[] args) {

		userMenu();

		System.exit(0);
	}

	static void userMenu()
	{
		System.out.print("Howdy! Welcome To The Awesome Experience "+ "Are you a new user: y/n:");

		Scanner scanner = new Scanner(System.in);

		String input = scanner.nextLine();		

		if (input.equals("y")){

			String firstName = askForStringInput("Great!!! Now please emter your first name: ");

			boolean nameIsValid = validateAlpha(firstName);

			String lastName  = askForStringInput("WOW!!! You are on a role here. "+ "Please enter your last name");

			nameIsValid = validateAlpha(lastName);

			String email = askForStringInput("Incredible! Now enter your email address ");

			boolean emailIsValid = validateEmail(email);

			String [] userInfo = readInputFile(USERS_DATA);

				if (checkExistingUser(email, userInfo)){
					email = askForStringInput("Great Snakes!! Somebody already took that email "+ "Please enter another email address:");
				} else {
					email = askForStringInput("Ding Dong!! Check Your syntax:");
				}	


			String password = askForPassword("Please enter your password: ");	    	
			String pswrdConfirm = askForPassword("Please confirm your password: ");	    	

			boolean isPassEqual = validatePassword(password, pswrdConfirm);

				if (password == null || password.isEmpty()){
					System.out.println("Nope Nope Nope!!! Passwords cannot be empty, please enter them again.");
				} else {
					System.out.println("This is tiring I guess. Passwords did not match, please try again.");
				} 


			String storeUserResult = storeUser(email, password, firstName, lastName);

			outputInfo(storeUserResult, USERS_DATA); //This is where I keep the user information

			System.out.println("*-----------------------------*");
			System.out.println("Awesome!! Your Account has been created!.");
			System.out.println("*-----------------------------*");	    	
			System.out.println("Ready to Setup Your Profile? Here We Go...");

			String profile = email + storeUserInfo();

			outputInfo(profile, PROFILES_DATA); //This is where I store the user profile information.

			System.out.println("OMG!! Thank You So Much For Signing Up! You Officially ROCK!!!.");

		} else if (input.equals("n")) {

			String email  = askForStringInput("Please enter your email address: ");
			String password  = askForPassword("Please enter your password: ");	    		    	

			//This method here checks for exisiting user.

			String [] userInfo = readInputFile(USERS_DATA);

			boolean loginStatus = userLogIn(email, password, userInfo);

			if (loginStatus){

				System.out.println("You Just Logged In To The House Of Awesomeness.");

				printUserProfile(PROFILES_DATA, email);

			} else {
				System.out.println("Username or password did not match.");
			}

		} 	
	}

	static String [] readInputFile (String filename) { 

		String [] records = new String [5]; 

		File userDB = new File(filename);

		String content = "";

		try{
			Scanner textScanner = new Scanner(userDB);

			while (textScanner.hasNextLine()){

				content+=textScanner.nextLine();

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		records = content.split(";");

		return records; 

	}

	static void outputInfo (String record, String filename) {

		PrintWriter out = null;

		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
			out.write(record);
		} catch (IOException e) {
			System.out.println("Damn Robots!!! They broke down again! Now we can't store your data");
		} finally {
			if (out != null){
				out.close();
			}
		}

	}	

	static boolean checkExistingUser (String email, String [] userInfo) { 

		String [] records = new String[100];

		for (int i=0; i < userInfo.length; i++){

			records = userInfo[i].split(",");

			if (email.equals(records[0])){
				return true;
			} 
		}

		return false; 
	}

	 static String encryptPassword (String password){

		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

		return hashed;

	 }

	 static boolean comparePasswords (String candidate, String hashed){

		if (BCrypt.checkpw(candidate, hashed)){
		return true;
		} else {
			return false;
		}

	}	 	

	//Validation

	//*** HELPER METHODS ***

	//Over here I am validating the first and last names)
	static boolean validateAlpha(String name) { 

		if (name.matches("[a-zA-Z]+")){
			return true;
		} 

		return  false;
	}
	//This helps to validate github and twitter
	static boolean validateAlphaNum(String input) { 

		if (input.matches("[a-zA-Z0-9_\\.]+")){
			return true;
		} 

		return  false;
	}

	//More validation for company, work etc
	static boolean validateAlphaNumSpace(String input){

		if (input.matches("^[\\w\\.\\s_]+[\\w\\.]+$")){
			return true;
		} 

		return  false;		
	}

	//Validating si done for stored email address
	static boolean validateEmail (String email) { 

		if (email.matches("^[a-z\\.]+[a-z]+@[a-z]+(\\.[a-z]{2,3})$"))
		{
			return true;
		}

		return false; 
	}

	//Validation is being done for stored passwords
	static boolean validatePassword (String password1, String password2) { 

		if (password1 != null && !password1.isEmpty()){

			if (password1.equals(password2)){
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	//Website validation
	static boolean validateWebsite(String input) { 

		if (input.matches("^(http://)?[w]{3}\\.[a-z]+(\\.[a-z]{2,3})$")){
			return true;
		} 

		return  false;
	}


	//Phone number validation
	static boolean validatePhoneNumber(String input) { 

		if (input.matches("^[0-9]{3}-[0-9]{3}-[0-9]{4}$")){
			return true;
		} 

		return  false;
	}		

	static String storeUser(String email, String password, String firstName, String lastName) {

		password = encryptPassword(password);

		return email+","+password+","+firstName+","+lastName+";"; 

	}

	//Validating profile info using comma seperated string
	static String storeUserInfo() {

			 String twitterHandle = askForStringInput("Please enter your twitter handle: ");

			 boolean inputIsValid = validateAlphaNum(twitterHandle);

			 String ghUsername = askForStringInput("Please enter your GitHub username: ");

			 inputIsValid = validateAlphaNum(ghUsername);


			String websiteName = askForStringInput("Please enter your website address: ");

			inputIsValid = validateWebsite(websiteName);


			String universityName = askForStringInput("Please enter your university name: ");

			inputIsValid = validateAlphaNumSpace(universityName);

			String majorName = askForStringInput("Please enter your major name: ");

			inputIsValid = validateAlphaNumSpace(majorName);

			String currentCompany = askForStringInput("Please enter your current company: ");

			inputIsValid = validateAlpha(currentCompany);

			String currentTitle = askForStringInput("Please enter your current title: ");

			inputIsValid = validateAlphaNumSpace(currentTitle);

			String phoneNumber = askForStringInput("Please enter your phone number (use format: ###-###-####): ");

			inputIsValid = validatePhoneNumber(phoneNumber);

			String result = ",Twitter: "+twitterHandle+",";

			result += "Github: "+ghUsername+",Website: "+websiteName+",";

			result += "University: "+universityName+",Major: "+majorName+",Company: "+currentCompany+",";

			result += "Title: "+currentTitle+",Phone: "+phoneNumber+";";

		 return result; 
	}

	//This method is check aginst the database for user name, email etc.
	static boolean userLogIn (String email, String password, String [] userInfo) { 

		String [] record = new String[100];

		for (int i=0; i < userInfo.length; i++){

			record = userInfo[i].split(",");

			if ( email.equals(record[0]) &&  comparePasswords(password, record[1]) ){
				return true;
			} 

		}

		return false; 
	}

	//This method pulls user profile after taking username
	static void printUserProfile (String filename, String email) {

		String [] records = readInputFile(filename);

		String [] userInfo = new String[100];

		for (int i=0; i < records.length; i++){

			userInfo = records[i].split(",");

			if (email.equals(userInfo[0])){

				for (int j=1; j < userInfo.length; j++ ){
					System.out.println(userInfo[j]);
				}

			} 
		}
	}

	static String askForStringInput(String message){

		Scanner scanner = new Scanner(System.in);

		System.out.print(message);

		String input = scanner.nextLine();

		return input;

	}

	static String capitalizeWord(String word){

		if (word.length() > 1){
			return word.substring(0, 1).toUpperCase() + word.substring(1);			
		}

		return word;
	}

	static public String askForPassword(String message) {  

		Console console = System.console();

		if (console == null) {
			System.out.println("System did not Initialize. Goodbye");
			System.exit(0);
		}

		char passwordArray[] = console.readPassword(message);

		return new String(passwordArray);

	}

}