package org.fcrepo.server.errors;

/**
 * Signals that a datastream was locked.
 *
 * @author Edwin Shin
 * @version $Id: DatastreamLockedException.java 8590 2010-05-28 06:28:35Z pangloss $
 */
public class DatastreamLockedException extends StorageException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a DatastreamLockedException.
     *
     * @param message
     *        An informative message explaining what happened and (possibly) how
     *        to fix it.
     */
    public DatastreamLockedException(String message) {
        super(message);
    }
}
