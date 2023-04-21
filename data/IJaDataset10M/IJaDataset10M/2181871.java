package org.mobicents.servlet.sip.restcomm.interpreter;

public final class InterpreterException extends Exception {

    private static final long serialVersionUID = 1L;

    public InterpreterException() {
        super();
    }

    public InterpreterException(final String message) {
        super(message);
    }

    public InterpreterException(final Throwable cause) {
        super(cause);
    }

    public InterpreterException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
