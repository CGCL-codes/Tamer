package com.j2biz.compote.exceptions;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 * 
 */
public class AssertationException extends RuntimeException {

    /**
     * @param message
     */
    public AssertationException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public AssertationException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public AssertationException(String message, Throwable cause) {
        super(message, cause);
    }
}
