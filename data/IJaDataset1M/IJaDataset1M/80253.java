package net.sf.oval.exception;

/**
 * The root exception of all custom exceptions thrown by OVal
 * 
 * @author Sebastian Thomschke
 */
public class OValException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OValException(final String message) {
        super(message);
    }

    public OValException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public OValException(final Throwable cause) {
        super(cause);
    }
}
