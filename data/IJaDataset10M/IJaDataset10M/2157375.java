package com.ubx1.pdpscanner.shared.validation;

import com.ubx1.pdpscanner.shared.exceptions.BadCellException;
import com.ubx1.pdpscanner.shared.exceptions.BadEmailException;
import com.ubx1.pdpscanner.shared.exceptions.BadPasswordConfirmationException;
import com.ubx1.pdpscanner.shared.exceptions.BadPasswordLengthException;
import com.ubx1.pdpscanner.shared.exceptions.BadProjectNameException;
import com.ubx1.pdpscanner.shared.exceptions.BadReposAuthException;
import com.ubx1.pdpscanner.shared.exceptions.RequiredFieldException;

/**
 * Common validation code for both the server and the client
 * 
 * @author wbraik
 * 
 */
public class Validator {

    public static final int MIN_PASSWORD_LENGTH = 6;

    private Validator() {
    }

    /**
	 * Validate the new project's fields before trying to add it to the database
	 * 
	 * @param name
	 *            the name of the new project
	 * @param svnUrl
	 *            the url of the new project's repository
	 * @param svnUser
	 *            username, used for authentication on the repository's server
	 * @param svnPassword
	 *            password, used for authentication on the repository's server
	 * @return true if the new project is valid, false otherwise
	 */
    public static void validateProject(String name, String svnUrl, String svnUser, String svnPassword) throws Exception {
        if (!validateEmptyFields(name, svnUrl)) {
            throw new RequiredFieldException();
        }
        if (name.contains(" ")) {
            throw new BadProjectNameException(name, "Project name contains spaces");
        }
        if (svnUser.isEmpty() != svnPassword.isEmpty()) {
            throw new BadReposAuthException(svnUser, svnPassword, "Empty repository user or password");
        }
    }

    /**
	 * Validate the login fields before trying to log the user in
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return true if the login fields are valid, false otherwise
	 */
    public static void validateLogin(String username, String password) throws Exception {
        if (!validateEmptyFields(username, password)) {
            throw new RequiredFieldException();
        }
    }

    /**
	 * Validate the new user's fields before trying to add them to the database
	 * 
	 * @param name
	 *            the new user's name
	 * @param email
	 *            the new user's email
	 * @param username
	 *            the new user's username
	 * @param password
	 *            the new user's password
	 * @param confirmPassword
	 *            the new user's password confirmation
	 * @param cell
	 *            the new user's cell phone number
	 * @throws Exception
	 */
    public static void validateSignUp(String name, String email, String username, String password, String confirmPassword, String cell) throws Exception {
        if (!validateEmptyFields(name, email, username, password, confirmPassword)) {
            throw new RequiredFieldException();
        }
        if (!validateEmail(email)) {
            throw new BadEmailException(email, "Email format invalid");
        }
        if (!cell.isEmpty() && !validateCell(cell)) {
            throw new BadCellException(cell, "Cell format invalid");
        }
        if (!validatePassword(password)) {
            throw new BadPasswordLengthException(password, "Password is too short");
        }
        if (!validatePasswordConfirmation(password, confirmPassword)) {
            throw new BadPasswordConfirmationException(confirmPassword, confirmPassword, "Confirmation does not match password");
        }
    }

    /**
	 * Validate password format
	 * 
	 * @param password
	 *            the password
	 * @return true if password format is valid, false otherwise
	 */
    public static boolean validatePassword(String password) {
        return !(password.length() < MIN_PASSWORD_LENGTH);
    }

    /**
	 * Validate password confirmation
	 * 
	 * @param password
	 *            the password
	 * @param confirmPassword
	 *            the password confirmation
	 * @return true if password and confirmation match, false otherwise
	 */
    public static boolean validatePasswordConfirmation(String password, String confirmPassword) {
        return !password.isEmpty() && !confirmPassword.isEmpty() && password.equals(confirmPassword);
    }

    /**
	 * Validate email format
	 * 
	 * @param email
	 *            the email
	 * @return true if email format is valid, false otherwise
	 */
    public static boolean validateEmail(String email) {
        return email.matches("(?i)\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b");
    }

    /**
	 * Validate cell format
	 * 
	 * @param cell
	 *            the cell
	 * @return true if cell format is valid, false otherwise
	 */
    public static boolean validateCell(String cell) {
        cell = cell.replace(" ", "");
        return (cell.matches("[0-9]*") && cell.length() == 10) || (cell.matches("\\+[0-9]*") && cell.length() == 12);
    }

    /**
	 * Make sure all required fields are filled
	 * 
	 * @param fields
	 *            the field values
	 * @return true if all fields are filled, false otherwise
	 */
    public static boolean validateEmptyFields(String... fields) {
        for (String s : fields) {
            if (s.isEmpty()) return false;
        }
        return true;
    }
}
