package com.db4o.ext;

/**
 * The requested operation is not valid in the current state but the database
 * continues to operate.
 */
public class Db4oIllegalStateException extends Db4oRecoverableException {

    public Db4oIllegalStateException(String message) {
        super(message);
    }

    public Db4oIllegalStateException(Throwable cause) {
        super(cause);
    }
}
