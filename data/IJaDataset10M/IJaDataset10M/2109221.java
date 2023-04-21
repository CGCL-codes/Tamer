package fr.macymed.commons.clp;

/**
 * <p>
 * Thrown when an error occurs while parsing Command-Line.
 * </p>
 * @author <a href="mailto:alexandre.cartapanis@macymed.fr">Cartapanis Alexandre</a>
 * @version 1.0.0
 * @version Commons - CommandLineParser API 1.0
 */
public class ParserException extends RuntimeException {

    /** The serial version UID. */
    private static final long serialVersionUID = -2569147728713876221L;

    /**
     * <p>
     * Creates a new ParserException.
     * </p>
     */
    public ParserException() {
        super();
    }

    /**
     * <p>
     * Constructs a new ParserException with the specified detail message.
     * </p>
     * @param _message The detail message.
     */
    public ParserException(String _message) {
        super(_message);
    }

    /**
     * <p>
     * Constructs a new ParserException with the specified detail message and cause.
     * </p>
     * @param _message The detail message.
     * @param _excp The cause.
     */
    public ParserException(String _message, Throwable _excp) {
        super(_message, _excp);
    }

    /**
     * <p>
     * Constructs a new ParserException with the specified cause.
     * </p>
     * @param _excp The cause.
     */
    public ParserException(Throwable _excp) {
        super(_excp);
    }
}
