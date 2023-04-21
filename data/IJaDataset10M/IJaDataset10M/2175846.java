package de.objectcode.time4u.client.store.api;

/**
 * Generic repository exception.
 * 
 * @author junglas
 */
public class RepositoryException extends Exception {

    private static final long serialVersionUID = 7040768442307080168L;

    public RepositoryException(final String message) {
        super(message);
    }

    public RepositoryException(final Throwable e) {
        super(e);
    }
}
