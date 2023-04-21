package org.xmi.repository;

/**
 * Checked exception for the repository
 */
public class RepositoryException extends Exception {

    private static final long serialVersionUID = 1126584584094976725L;

    /**
	 * @param message
	 */
    public RepositoryException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public RepositoryException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
