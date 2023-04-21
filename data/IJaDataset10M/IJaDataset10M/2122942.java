package org.sd_network.vfs;

/**
 * A exception for I/O.
 *
 * <p> $Id$
 *
 * @author Masatoshi Sato
 */
public class VfsIOException extends Exception {

    /**
     * Create a new instance with the given message.
     */
    public VfsIOException(String message) {
        super(message);
    }

    /**
     * Create a new instance from the given exception.
     */
    public VfsIOException(Exception exception) {
        super(exception);
    }

    /**
     * Create a new instance from the given exception with the given message.
     */
    public VfsIOException(String message, Exception exception) {
        super(message, exception);
    }
}
