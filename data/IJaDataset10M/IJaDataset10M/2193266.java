package genesis.common.persistence.api;

/**
 * The exception that only threw by a class belongs of persistence-layer. 
 * 
 * @author Bruno Cartaxo
 * @since 1.0
 */
public class PersistenceException extends Exception {

    /**
	 * The universal identification number for a serializable class.
	 */
    private static final long serialVersionUID = -8351146953088682935L;

    /**
	 * The default constructor.
	 */
    public PersistenceException() {
        super("An exception occurred at the persistence layer.");
    }

    /**
	 * Constructs a persistence exception with he given error message and cause.
	 * 
	 * @param message The error message.
	 * @param cause The cause.
	 */
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Constructs a persistence exception with the given error message.
	 * 
	 * @param message The error message.
	 */
    public PersistenceException(String message) {
        super(message);
    }

    /**
	 * Constructs a persistence exception with the given cause.
	 * 
	 * @param cause The cause.
	 */
    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
