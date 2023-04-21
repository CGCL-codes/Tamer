package net.sf.balm.persistence.query;

import net.sf.balm.persistence.PersistenceException;

public class MissingMethodException extends PersistenceException {

    public MissingMethodException() {
        super();
    }

    public MissingMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingMethodException(String message) {
        super(message);
    }

    public MissingMethodException(Throwable cause) {
        super(cause);
    }
}
