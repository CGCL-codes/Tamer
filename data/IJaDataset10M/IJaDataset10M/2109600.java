package org.posterita.exceptions;

/**
 * @author ashley
 */
public class POException extends RuntimeException {

    private static final long serialVersionUID = 4178407778052352481L;

    /**
     * 
     */
    public POException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public POException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public POException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public POException(Throwable cause) {
        super(cause);
    }
}
