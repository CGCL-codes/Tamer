package net.sf.orcc;

/**
 * An exception raised in Orcc.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class OrccException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Creates a new OrccException with the given message.
	 */
    public OrccException(String message) {
        super(message);
    }

    /**
	 * Creates a new OrccException with the given message, identified to have
	 * occurred in the given file at the given location.
	 */
    public OrccException(String fileName, int lineNumber, String message) {
        this(fileName, lineNumber, message, null);
    }

    /**
	 * Creates a new OrccException with the given message and cause, identified
	 * to have occurred in the given file at the given location.
	 */
    public OrccException(String fileName, int lineNumber, String message, Throwable cause) {
        super("File \"" + fileName + "\", " + lineNumber + "\n" + message, cause);
    }

    /**
	 * Creates a new OrccException with the given message and cause.
	 */
    public OrccException(String message, Throwable cause) {
        super(message, cause);
    }
}
