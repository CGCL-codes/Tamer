package org.learnaholic.application;

/**
 * Exception thrown in this package.
 */
public class LearnaholicException extends Exception {

    /**
	 * Constructor.
	 * @param message the error message.
	 */
    public LearnaholicException(String message) {
        super(message);
    }

    /**
	 * Constructor.
	 * @param message the error message.
	 * @param cause the parent cause of this exception.
	 */
    public LearnaholicException(String message, Throwable cause) {
        super(message, cause);
    }
}
