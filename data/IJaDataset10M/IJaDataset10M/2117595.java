package org.kablink.util;

import org.kablink.util.api.ApiErrorCodeSupport;

/**
 * @author jong
 *
 */
public abstract class VibeRuntimeException extends RuntimeException implements HttpStatusCodeSupport, ApiErrorCodeSupport {

    public VibeRuntimeException() {
        super();
    }

    public VibeRuntimeException(String message) {
        super(message);
    }

    public VibeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public VibeRuntimeException(Throwable cause) {
        super(cause);
    }
}
