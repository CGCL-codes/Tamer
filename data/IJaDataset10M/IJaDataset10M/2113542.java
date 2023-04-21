package org.wikiup.servlet.exception;

public class CloseMarkupNotFoundException extends RuntimeException {

    static final long serialVersionUID = 3047447586423531142L;

    public CloseMarkupNotFoundException() {
        super();
    }

    public CloseMarkupNotFoundException(String message) {
        super(message);
    }

    public CloseMarkupNotFoundException(Throwable cause) {
        super(cause);
    }

    public CloseMarkupNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
