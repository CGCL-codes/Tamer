package uk.org.biotext.graphspider.parser;

/**
 * The Class DeclarationSyntaxException.
 */
public class DeclarationSyntaxException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new declaration syntax exception.
     */
    public DeclarationSyntaxException() {
        super();
    }

    /**
     * Instantiates a new declaration syntax exception.
     * 
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public DeclarationSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new declaration syntax exception.
     * 
     * @param message
     *            the message
     */
    public DeclarationSyntaxException(String message) {
        super(message);
    }

    /**
     * Instantiates a new declaration syntax exception.
     * 
     * @param cause
     *            the cause
     */
    public DeclarationSyntaxException(Throwable cause) {
        super(cause);
    }
}
