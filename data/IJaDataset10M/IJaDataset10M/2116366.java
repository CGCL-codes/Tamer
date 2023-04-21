package org.apache.commons.httpclient.util;

/**
 * An exception to indicate an error parsing a date string.
 * 
 * @see DateUtil
 * 
 * @author Michael Becke
 */
public class DateParseException extends Exception {

    /**
     * 
     */
    public DateParseException() {
        super();
    }

    /**
     * @param message the exception message
     */
    public DateParseException(String message) {
        super(message);
    }
}
