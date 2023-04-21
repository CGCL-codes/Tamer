package org.nakedobjects.nos.client.cli;

import org.nakedobjects.noa.NakedObjectRuntimeException;

public class IllegalDispatchException extends NakedObjectRuntimeException {

    private static final long serialVersionUID = 1L;

    public IllegalDispatchException() {
        super();
    }

    public IllegalDispatchException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public IllegalDispatchException(String msg) {
        super(msg);
    }

    public IllegalDispatchException(Throwable cause) {
        super(cause);
    }
}
