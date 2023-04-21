package de.uni_trier.st.nevada.io;

/**
 * @author mathias
 *
 * This exception is thrown by implementations of <code>Importer</code>
 * whenever a file contains invalid data that can not be recovered, i.e. missing
 * structural information or undeclard entities.
 */
public class InvalidDataException extends Exception {

    /**
	 *
	 */
    public InvalidDataException() {
    }

    /**
	 * @param message
	 */
    public InvalidDataException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public InvalidDataException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
