package org.mobicents.servlet.sip.restcomm.xml;

public final class UnsupportedAttributeException extends Exception {

    private static final long serialVersionUID = 1L;

    public UnsupportedAttributeException() {
        super();
    }

    public UnsupportedAttributeException(final String message) {
        super(message);
    }

    public UnsupportedAttributeException(final Throwable cause) {
        super(cause);
    }

    public UnsupportedAttributeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
