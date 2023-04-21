package org.apache.james.mime4j.field;

import org.apache.james.mime4j.MimeException;

/**
 * This exception is thrown when parse errors are encountered.
 */
public class ParseException extends MimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new parse exception with the specified detail message.
     * 
     * @param message
     *            detail message
     */
    protected ParseException(String message) {
        super(message);
    }

    /**
     * Constructs a new parse exception with the specified cause.
     * 
     * @param cause
     *            the cause
     */
    protected ParseException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new parse exception with the specified detail message and
     * cause.
     * 
     * @param message
     *            detail message
     * @param cause
     *            the cause
     */
    protected ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
