package org.commerce.mismo;

import java.util.Date;

/**
 * An interface to a resident alien card.  This card is
 * needed by a borrower if he/she is a permanent resident alien.
 *
 * @version $Id: ResidentAlienCard.java,v 1.1.1.1 2007/04/16 05:07:03 clafonta Exp $
 */
public interface ResidentAlienCard {

    /**
     * @return the resident alien card number
     */
    public abstract String getCardNumber();

    /**
     * @return the date the card will expire
     */
    public abstract Date getExpirationDate();

    /**
     * @return the date the card was issued
     */
    public abstract Date getIssueDate();

    /**
     * @param cardNumber The resident alien card number
     */
    public abstract void setCardNumber(String cardNumber);

    /**
     * @param date The date the card will expire
     */
    public abstract void setExpirationDate(Date date);

    /**
     * @param date The date the card was issued
     */
    public abstract void setIssueDate(Date date);
}
