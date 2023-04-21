package com.dreamlizard.miles.exception;

/**
 * Standard runtime exception for the miles project--extend this for specific exceptions.
 */
public class MilesRuntimeException extends RuntimeException {

    /**
     * Default Constructor.
     */
    public MilesRuntimeException() {
        super();
    }

    /**
     * Construct the exception with the given message.
     *
     * @param inMessage Message to be associated with the exception.
     */
    public MilesRuntimeException(String inMessage) {
        super(inMessage);
    }

    /**
     * Construct the exception wrapping the given exception.
     *
     * @param inException Exception to be chained to this exception
     */
    public MilesRuntimeException(Exception inException) {
        super(inException);
    }

    /**
     * Construct the exception with the given message, and wrapping the
     * given exception.
     *
     * @param inMessage   Message to be associated with the exception
     * @param inException exception to be chained to this exception
     */
    public MilesRuntimeException(String inMessage, Exception inException) {
        super(inMessage, inException);
    }
}
