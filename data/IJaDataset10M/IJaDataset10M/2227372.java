package org.nakedobjects.nos.store.file;

import org.nakedobjects.noa.NakedObjectRuntimeException;

public class PersistorException extends NakedObjectRuntimeException {

    private static final long serialVersionUID = 1L;

    public PersistorException() {
        super();
    }

    public PersistorException(final String message) {
        super(message);
    }

    public PersistorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PersistorException(final Throwable cause) {
        super(cause);
    }
}
