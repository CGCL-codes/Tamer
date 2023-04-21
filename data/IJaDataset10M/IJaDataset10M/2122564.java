package de.fzi.harmonia.commons.basematchers;

/**
 * Base exception for all kinds of base matcher execeptions.
 * 
 * @author bock
 *
 */
public class BaseMatcherException extends Exception {

    /**
     * Auto-generated serial version ID.
     */
    private static final long serialVersionUID = -3087961766439792355L;

    /**
     * Creates a new <code>BaseMatcherException</code> with a message and a cause.
     * @param message Error message.
     * @param cause Cause of this exception.
     */
    public BaseMatcherException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new <code>BaseMatcherException</code> with a message.
     * @param message Error message.
     */
    public BaseMatcherException(String message) {
        super(message);
    }
}
