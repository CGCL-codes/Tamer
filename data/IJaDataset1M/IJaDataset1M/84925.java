package com.dcivision.mail.core;

/**
 * EmailMessageSenderComparator.java
 *
 * This class acts as a comparator for the sender of an email
 *
 * @author      Lun Au
 * @company     DCIVision Limited
 * @creation    28/03/2007
 * @version     $Revision: 1.1 $
 */
public class EmailMessageSenderComparator extends EmailMessageSubjectComparator {

    /**
     * The constructor
     * @param sortOrder The ordering of the sorting
     * @see EmailMessageComparator#SORT_ASC
     * @see EmailMessageComparator#SORT_DESC
     */
    public EmailMessageSenderComparator(String sortOrder) {
        super(sortOrder);
    }

    /**
     * Overrides the property of the email message used to sort
     * @return The property of the email message
     * @see EmailMessageComparator#PROP_NAME_SENDER
     */
    public String getProperty() {
        return EmailMessageComparator.PROP_NAME_SENDER;
    }
}
