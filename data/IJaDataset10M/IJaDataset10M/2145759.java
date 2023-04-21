package org.jcvi.assembly.trim;

/**
 * @author dkatzel
 *
 *
 */
public class TrimmerException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -4627929382603482450L;

    /**
     * @param message
     * @param cause
     */
    public TrimmerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public TrimmerException(String message) {
        super(message);
    }
}
