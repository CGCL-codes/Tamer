package org.argouml.model;

/**
 * Exception indicating an attempt to operate on an invalid model element.
 * It may indicate an element which used to be valid, but has since been
 * deleted..
 */
public class InvalidElementException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -5831736942969641257L;

    /**
     * Constructor the exception with a message.
     *
     * @param message the message
     */
    public InvalidElementException(String message) {
        super(message);
    }

    /**
     * Constructor the exception with a message and a causing exception.
     *
     * @param message the message
     * @param c the cause of the exception
     */
    public InvalidElementException(String message, Throwable c) {
        super(message, c);
    }

    /**
     * Constructor the exception a causing exception.
     *
     * @param c the cause of the exception
     */
    public InvalidElementException(Throwable c) {
        super(c);
    }
}
