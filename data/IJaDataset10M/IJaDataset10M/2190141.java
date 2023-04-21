package org.nakedobjects.webapp;

public class DispatchException extends ScimpiException {

    private static final long serialVersionUID = 1L;

    public DispatchException() {
    }

    public DispatchException(String message) {
        super(message);
    }

    public DispatchException(Throwable cause) {
        super(cause);
    }

    public DispatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
