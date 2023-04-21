package org.aphis.core;

/**
 * An exception relating to the ability to use the Alarm API
 * 
 * @author Greg
 * @version 1.0
 */
public class AphisAlarmException extends Exception {

    /**
	 * Public default constructor
	 * 
	 */
    public AphisAlarmException() {
        super();
    }

    /**
	 * Constructor allowing the user to set a message
	 * 
	 * @param message the String message to store in this exception
	 * @see java.lang.Exception#Exception(java.lang.String)
	 */
    public AphisAlarmException(String message) {
        super(message);
    }

    /**
	 * Constructor allowing the user to set a message in addition to a nested Throwable
	 * 
	 * @param message an exception related message
	 * @param cause a nested Throwable
	 * @see {@link java.lang.Exception#Exception(java.lang.String, java.lang.Throwable)}
	 */
    public AphisAlarmException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Constructor allowing the user to set a nested Throwable 
	 * 
	 * @param cause
	 * @see {@link java.lang.Exception#Exception(java.lang.Throwable)}
	 */
    public AphisAlarmException(Throwable cause) {
        super(cause);
    }
}
