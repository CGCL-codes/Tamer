package com.webstarsltd.common.pim.contact;

import com.webstarsltd.common.pim.common.*;

/**
 * An object representing an email. This class holds informations about the
 * type of the email number (businness, home etc.) and the email value itself
 *
 * @version $Id: Email.java,v 1.3 2007/06/18 12:40:52 luigiafassina Exp $
 */
public class Email extends TypifiedProperty {

    /**
     * Creates an empty email
     */
    public Email() {
        super();
    }

    /**
     * Creates an email (Property) with value.
     */
    public Email(String value) {
        super(value);
    }

    /**
     * Returns the email type for this email
     *
     * @return the email type for this email
     */
    public String getEmailType() {
        return propertyType;
    }

    /**
     * Sets the email type for this email
     *
     * @param emailType the email type to set
     */
    public void setEmailType(String emailType) {
        this.propertyType = emailType;
    }

    public String toString() {
        return this.getPropertyValueAsString();
    }
}
