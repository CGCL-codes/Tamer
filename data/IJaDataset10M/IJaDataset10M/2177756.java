package it.pronetics.madstore.repository.support;

/**
 * Runtime exception thrown by Atom repository methods.
 * @author Sergio Bossa
 */
public class AtomRepositoryException extends RuntimeException {

    public AtomRepositoryException(String message) {
        super(message);
    }

    public AtomRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
