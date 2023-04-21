package com.telstra.dynamicrva.lock.database.spi;

/**
 *
 * @author c957258
 */
public class LockDatabaseConnectionManagerException extends java.lang.Exception {

    /**
     * Creates a new instance of <code>LockDatabaseConnectionManagerException</code> without detail message.
     */
    public LockDatabaseConnectionManagerException() {
        super();
    }

    /**
     * Constructs an instance of <code>LockDatabaseConnectionManagerException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public LockDatabaseConnectionManagerException(String msg) {
        super(msg);
    }

    public LockDatabaseConnectionManagerException(String msg, Throwable t) {
        super(msg, t);
    }
}
