package net.sf.traser.numbering;

/**
 * Exception thrown when an identifier representation does not match the scheme
 * required by a <code>Resolver</code> implementation.
 * 
 * @author Marcell Szathmári
 * @see net.sf.traser.common.identification.Resolver
 */
public class SchemeException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -8541591553809004299L;

    /**
     * Constructor with a cause.
     * @param cause the cause
     */
    public SchemeException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor with message and cause.
     * @param message the message
     * @param cause the cause
     */
    public SchemeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with message.
     * @param message the message
     */
    public SchemeException(String message) {
        super(message);
    }

    /**
     * Constructor. Do not use because it does not help.
     */
    protected SchemeException() {
        super();
    }
}
