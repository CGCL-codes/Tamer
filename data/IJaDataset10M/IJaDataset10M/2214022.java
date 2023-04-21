package org.nexusbpm.common.data;

public class ObjectConversionException extends Exception {

    private static final long serialVersionUID = 1l;

    public ObjectConversionException() {
        super();
    }

    public ObjectConversionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ObjectConversionException(final String message) {
        super(message);
    }

    public ObjectConversionException(final Throwable cause) {
        super(cause);
    }
}
