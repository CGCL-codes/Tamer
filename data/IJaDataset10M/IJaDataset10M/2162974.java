package org.golibri.common.contact;

import java.util.regex.*;

/**
 * The class EmailAddress is an immutable class and represents email addresses.
 */
public final class EmailAddress {

    /**
	 * Pattern created with a regular expression to verify email addresses.
	 */
    private static Pattern pattern = Pattern.compile("[\\w]+[\\.\\w]*[\\w]+@{1}[\\w]+[\\.\\w]*[\\w]{2,6}");

    /**
	 * String containing the email address.
	 */
    private String emailAddress = null;

    /**
	 * Constructor.
	 * The constructor verifies the validity of the email address that is passed in.
	 * 
	 * @param emailAddress
	 * 				A String containing the email address
	 * @throws	NullPointerException
	 * 				If the parameter emailAddress is null
	 * @throws	IllegalArgumentException
	 * 				If the parameter emailAddress does not contain a valid email address
	 * 
	 */
    public EmailAddress(String emailAddress) {
        if (emailAddress == null) {
            throw new NullPointerException("The parameter emailAddress is null.");
        }
        if (!EmailAddress.isValid(emailAddress)) throw new IllegalArgumentException("'" + emailAddress + "' is not a valid email address.");
        this.emailAddress = emailAddress;
    }

    /**
	 * Static method to verify whether a given email address is valid.
	 * 
	 * @param emailAddress
	 * 				The email address to verify.
	 * @return	true if the email address is valid, false otherwise
	 */
    public static boolean isValid(String emailAddress) {
        if (emailAddress == null) return false;
        Matcher matcher = pattern.matcher(emailAddress);
        if (matcher.matches()) return true; else return false;
    }

    /**
	 * Returns the email address as a String
	 * 
	 * @return	The email address
	 */
    public String getEmailAddress() {
        return this.emailAddress;
    }

    /**
	 * @see Object#toString()
	 */
    public String toString() {
        return this.emailAddress;
    }

    /**
	 * @see Object#hashCode()
	 */
    public int hashCode() {
        return this.emailAddress.toUpperCase().hashCode();
    }

    /**
	 * @see Object#equals(java.lang.Object)
	 */
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof EmailAddress)) {
            return false;
        }
        EmailAddress emailAddress = (EmailAddress) object;
        return this.getEmailAddress().equalsIgnoreCase(emailAddress.getEmailAddress());
    }
}
