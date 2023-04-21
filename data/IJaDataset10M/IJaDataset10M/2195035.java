package org.nomicron.suber.exception;

/**
 * Standard exception for the suber project--extend this for specific exceptions.
 */
public class SuberException extends Exception {

    /**
     * Default Constructor.
     */
    public SuberException() {
        super();
    }

    /**
     * Construct the exception with the given message.
     *
     * @param inMessage Message to be associated with the exception.
     */
    public SuberException(String inMessage) {
        super(inMessage);
    }

    /**
     * Construct the exception wrapping the given exception.
     *
     * @param inException Exception to be chained to this exception
     */
    public SuberException(Exception inException) {
        super(inException);
    }

    /**
     * Construct the exception with the given message, and wrapping the
     * given exception.
     *
     * @param inMessage   Message to be associated with the exception
     * @param inException exception to be chained to this exception
     */
    public SuberException(String inMessage, Exception inException) {
        super(inMessage, inException);
    }
}
