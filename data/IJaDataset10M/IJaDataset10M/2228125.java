package org.xmlsh.core;

@SuppressWarnings("serial")
public class UnimplementedException extends CoreException {

    public UnimplementedException() {
        super();
    }

    public UnimplementedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnimplementedException(String message) {
        super(message);
    }

    public UnimplementedException(Throwable cause) {
        super(cause);
    }
}
