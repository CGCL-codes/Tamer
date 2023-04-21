package de.nava.informa.utils;

/**
 * Exception class, which is used if an invalid feed is tried to be loaded.
 *
 * @author Sam Newman
 * @see FeedManager
 */
public class FeedManagerException extends Exception {

    private static final long serialVersionUID = -1982751404099834335L;

    public FeedManagerException(Exception e) {
        super(e.getMessage());
        initCause(e);
    }
}
