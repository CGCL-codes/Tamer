package org.ikasan.filter.duplicate.model;

import java.util.Date;

/**
 * Interface defining what a persisted message should look like.
 * 
 * @author Ikasan Development Team
 *
 */
public interface FilterEntry {

    /**
     * Constant property name for clientId field in any {@link FilterEntry}
     * implementors
     */
    public static final String CLIENT_ID_PROP_KEY = "clientId";

    /**
     * Constant property name for criteria field in any {@link FilterEntry}
     * implementors
     */
    public static final String CRITERIA_PROP_KEY = "criteria";

    /**
     * Constant property name for expiry field in any {@link FilterEntry}
     * implementors
     */
    public static final String EXPRIY_PROP_KEY = "expiry";

    /** 
     * Getter for a clientId variable. Together with the criteria,
     * it identifies a persisted {@link FilterEntry}
     * 
     * @return The client id
     */
    public String getClientId();

    /** Getter for a criteria variable: object unique about a message.
     * Together with clientId, it identifies a persisted {@link FilterEntry}
     * 
     * @return criteria object whatever it might be.
     */
    public Integer getCriteria();

    /**
     * Getter for {@link Date} object representing the date/time a {@link FilterEntry}
     * was persisted.
     * @return a creation date.
     */
    public Date getCreatedDateTime();

    /**
     * Getter for {@link Date} object representing the date/time a {@link FilterEntry}
     * is expired and can be removed from persistance.
     * @return an expiry date
     */
    public Date getExpiry();
}
