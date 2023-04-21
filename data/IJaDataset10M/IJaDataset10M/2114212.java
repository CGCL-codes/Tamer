package uk.org.biotext.graphspider.parser;

/**
 * The Class PatternSyntaxException.
 */
public class PatternSyntaxException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new pattern syntax exception.
     */
    public PatternSyntaxException() {
        super();
    }

    /**
     * Instantiates a new pattern syntax exception.
     * 
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public PatternSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new pattern syntax exception.
     * 
     * @param message
     *            the message
     */
    public PatternSyntaxException(String message) {
        super(message);
    }

    /**
     * Instantiates a new pattern syntax exception.
     * 
     * @param cause
     *            the cause
     */
    public PatternSyntaxException(Throwable cause) {
        super(cause);
    }
}
