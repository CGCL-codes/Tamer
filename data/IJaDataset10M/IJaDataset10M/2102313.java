package org.peaseplate.utils.resolver;

import java.io.IOException;

public class LocatorException extends IOException {

    private static final long serialVersionUID = 1L;

    public LocatorException(final String message) {
        super(message);
    }

    public LocatorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
