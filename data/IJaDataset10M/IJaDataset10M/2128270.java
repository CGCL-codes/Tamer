package com.dcivision.mail.core;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * EmailMessageSentDateComparator.java
 *
 * This class acts as a comparator for the sent date of an email.
 * It is also the default comparator of an email message.
 * The default sorting is {@link EmailMessageComparator#SORT_BY_SEND_DATE} {@link EmailMessageComparator#SORT_DESC}
 *
 * @author      Lun Au
 * @company     DCIVision Limited
 * @creation    28/03/2007
 * @version     $Revision: 1.1 $
 */
public class EmailMessageSentDateComparator implements EmailMessageComparator {

    /**
     * The logger
     */
    protected Log log = LogFactory.getLog(this.getClass().getName());

    /**
     * The orderFactor governing the returned compare value in ascending and descending.
     * When ordering descendingly, the factor should be -1
     */
    int orderFactor = 1;

    /**
     * The constructor
     * 
     * @param sortOrder The ordering of the sorting
     * @see EmailMessageComparator#SORT_ASC
     * @see EmailMessageComparator#SORT_DESC
     */
    public EmailMessageSentDateComparator(String sortOrder) {
        if (EmailMessageComparator.SORT_DESC.equals(sortOrder)) {
            orderFactor *= -1;
        }
    }

    /**
     * Gets the property of the email message used to sort
     * @return The property of the email message
     * @see EmailMessageComparator#PROP_NAME_SENT_DATE
     */
    public String getProperty() {
        return EmailMessageComparator.PROP_NAME_SENT_DATE;
    }

    /**
     * Compares the two email for order based on its property given by <code>getProperty()</code>.  
     * Returns a negative integer, zero, or a positive integer as the first email is less than, equal
     * to, or greater than the second.
     * 
     * @param o1 The first email to be compared
     * @param o2 The second email to be compared
     * @return a negative integer, zero, or a positive integer as the
     *         first email is less than, equal to, or greater than the
     *         second.
     * @see EmailMessageComparator 
     */
    public int compare(Object o1, Object o2) {
        String propName = this.getProperty();
        Object prop1 = o1;
        Object prop2 = o2;
        try {
            prop1 = PropertyUtils.getProperty(o1, propName);
            prop2 = PropertyUtils.getProperty(o2, propName);
        } catch (Exception ex) {
            log.warn("Can not access the property \"" + o1.getClass().getName() + "." + propName + "\"", ex);
        }
        if (prop1 instanceof java.util.Date) {
            return orderFactor * ((java.util.Date) prop1).compareTo((java.util.Date) prop2);
        } else if (prop1 instanceof java.lang.Integer) {
            return orderFactor * ((java.lang.Integer) prop1).compareTo((java.lang.Integer) prop2);
        } else if (prop1 instanceof java.lang.String) {
            return orderFactor * ((java.lang.String) prop1).compareTo((java.lang.String) prop2);
        } else {
            log.warn("Can not found suitable comparison type, using String as default");
            return orderFactor * ((java.lang.String) prop1).compareTo((java.lang.String) prop2);
        }
    }
}
