package org.columba.core.util;

/**
 * Thrown if a general internal error occurs.
 * 
 * @author fdietz
 */
public class InternalException extends Exception {

    public InternalException() {
        super();
    }

    public InternalException(String message) {
        super(message);
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalException(Throwable cause) {
        super(cause);
    }
}
