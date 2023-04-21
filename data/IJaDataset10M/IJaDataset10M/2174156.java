package org.mobicents.servlet.sip.restcomm.interpreter;

public final class TagStrategyException extends Exception {

    private static final long serialVersionUID = 1L;

    public TagStrategyException() {
        super();
    }

    public TagStrategyException(final String message) {
        super(message);
    }

    public TagStrategyException(final Throwable cause) {
        super(cause);
    }

    public TagStrategyException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
