package org.nakedobjects.plugins.nosql;

import org.nakedobjects.commons.exceptions.NakedObjectException;

public class NoSqlStoreException extends NakedObjectException {

    private static final long serialVersionUID = 1L;

    public NoSqlStoreException() {
        super();
    }

    public NoSqlStoreException(String messageFormat, Object... args) {
        super(messageFormat, args);
    }

    public NoSqlStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSqlStoreException(String message) {
        super(message);
    }

    public NoSqlStoreException(Throwable cause) {
        super(cause);
    }
}
