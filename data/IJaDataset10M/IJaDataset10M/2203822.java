package com.excelsior.core;

/**
 * The Class CannotStartApplicationException.
 */
public class CannotStartApplicationException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -622443991562418725L;

    /**
	 * Instantiates a new cannot start application exception.
	 */
    public CannotStartApplicationException() {
    }

    /**
	 * Instantiates a new cannot start application exception.
	 * 
	 * @param message the message
	 */
    public CannotStartApplicationException(String message) {
        super(message);
    }

    /**
	 * Instantiates a new cannot start application exception.
	 * 
	 * @param cause the cause
	 */
    public CannotStartApplicationException(Throwable cause) {
        super(cause);
    }

    /**
	 * Instantiates a new cannot start application exception.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
    public CannotStartApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
