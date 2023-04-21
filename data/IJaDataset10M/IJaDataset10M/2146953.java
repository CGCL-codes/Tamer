package com.vlee.servlet.ecommerce;

import java.util.ArrayList;
import java.util.prefs.InvalidPreferencesFormatException;
import java.text.ParseException;

public class PasswordGenerator {

    private int length;

    private boolean lowercaseIncluded;

    private boolean numbersIncluded;

    private boolean othersIncluded;

    private boolean uppercaseIncluded;

    private String password;

    private String template;

    PasswordGenerator() {
        password = "";
        template = "";
        length = 6;
        lowercaseIncluded = true;
        uppercaseIncluded = true;
        numbersIncluded = true;
        othersIncluded = false;
        generatePassword();
    }

    /**
	 * @return true if at least one of the password generation flags is true,
	 *         otherwise returns false
	 */
    private boolean flagsOK() {
        return lowercaseIncluded || uppercaseIncluded || numbersIncluded || othersIncluded;
    }

    /**
	 * @return a random lowercase character from 'a' to 'z'
	 */
    public static char randomLowercase() {
        return (char) (97 + (int) (Math.random() * 26));
    }

    /**
	 * @return a random uppercase character from 'A' to 'Z'
	 */
    public static char randomUppercase() {
        return (char) (65 + (int) (Math.random() * 26));
    }

    /**
	 * @return a random character in this list: !"#$%&'()*+,-./
	 */
    public static char randomOther() {
        return (char) (33 + (int) (Math.random() * 15));
    }

    /**
	 * @return a random character from '0' to '9'
	 */
    public static char randomNumber() {
        return (char) (48 + (int) (Math.random() * 10));
    }

    public void generatePassword() {
        if (password.length() != 0) {
            password = "";
        }
        if (template.length() > 0) {
            length = template.length();
            for (int i = 0; i < length; i++) {
                switch(template.charAt(i)) {
                    case 'a':
                        password += randomLowercase();
                        break;
                    case 'A':
                        password += randomUppercase();
                        break;
                    case 'n':
                    case 'N':
                        password += randomNumber();
                        break;
                    case 'o':
                    case 'O':
                        password += randomOther();
                        break;
                }
            }
        } else {
            ArrayList flags = new ArrayList();
            if (lowercaseIncluded) {
                flags.add(new randomLowercase());
            }
            if (uppercaseIncluded) {
                flags.add(new randomUppercase());
            }
            if (othersIncluded) {
                flags.add(new randomOther());
            }
            if (numbersIncluded) {
                flags.add(new randomNumber());
            }
            int flagLength = flags.size();
            for (int i = 0; i < length; i++) {
                password += ((randomCharacter) flags.get((int) (Math.random() * flagLength))).execute();
            }
        }
    }

    /**
	 * @return the length of the generated password
	 */
    public int getLength() {
        return length;
    }

    /**
	 * @return the generated password
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * @return password template
	 */
    public String getTemplate() {
        return template;
    }

    /**
	 * @return lowercaseIncluded
	 */
    public boolean isLowercaseIncluded() {
        return lowercaseIncluded;
    }

    /**
	 * @return numbersIncluded
	 */
    public boolean isNumbersIncluded() {
        return numbersIncluded;
    }

    /**
	 * @return othersIncluded
	 */
    public boolean isOthersIncluded() {
        return othersIncluded;
    }

    /**
	 * @return uppercaseIncluded
	 */
    public boolean isUppercaseIncluded() {
        return uppercaseIncluded;
    }

    /**
	 * @param length,
	 *            enforced to be a positive integer >= 3.
	 */
    public void setLength(int length) {
        this.length = (length < 3) ? 3 : length;
    }

    /**
	 * @param b
	 */
    public void setLowercaseIncluded(boolean b) throws InvalidPreferencesFormatException {
        lowercaseIncluded = b;
        if (b == false && !flagsOK()) {
            lowercaseIncluded = true;
            throw new InvalidPreferencesFormatException("At least one flag must be on to generate a password");
        }
    }

    /**
	 * @param b
	 */
    public void setNumbersIncluded(boolean b) throws InvalidPreferencesFormatException {
        numbersIncluded = b;
        if (b == false && !flagsOK()) {
            numbersIncluded = true;
            throw new InvalidPreferencesFormatException("At least one flag must be on to generate a password");
        }
    }

    /**
	 * @param b
	 */
    public void setOthersIncluded(boolean b) throws InvalidPreferencesFormatException {
        othersIncluded = b;
        if (b == false && !flagsOK()) {
            othersIncluded = true;
            throw new InvalidPreferencesFormatException("At least one flag must be on to generate a password");
        }
    }

    /**
	 * @param string
	 */
    public void setTemplate(String template) throws ParseException {
        for (int i = 0; i < template.length(); i++) {
            switch(template.charAt(i)) {
                case 'a':
                case 'A':
                case 'n':
                case 'N':
                case 'o':
                case 'O':
                    break;
                default:
                    throw new ParseException("Password template contains an invalid character", i);
            }
        }
        this.template = template;
    }

    /**
	 * Clears the password template,making generation rely on the flags.
	 * 
	 */
    public void clearTemplate() {
        template = "";
    }

    /**
	 * @param b
	 */
    public void setUppercaseIncluded(boolean b) throws InvalidPreferencesFormatException {
        uppercaseIncluded = b;
        if (b == false && !flagsOK()) {
            uppercaseIncluded = true;
            throw new InvalidPreferencesFormatException("At least one flag must be on to generate a password");
        }
    }

    public static class randomLowercase implements randomCharacter {

        public char execute() {
            return PasswordGenerator.randomLowercase();
        }
    }

    public static class randomUppercase implements randomCharacter {

        public char execute() {
            return PasswordGenerator.randomUppercase();
        }
    }

    public static class randomOther implements randomCharacter {

        public char execute() {
            return PasswordGenerator.randomOther();
        }
    }

    public static class randomNumber implements randomCharacter {

        public char execute() {
            return PasswordGenerator.randomNumber();
        }
    }

    private static interface randomCharacter {

        char execute();
    }
}
