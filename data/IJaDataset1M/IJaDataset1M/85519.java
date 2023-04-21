package de.schlund.pfixcore.exception;

/**
 * Root class for runtime exceptions thrown by the Pustefix core. Usually these
 * exceptions signal an unexpected situation that ocurred at runtime.
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public class PustefixRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1875529624928272717L;

    public PustefixRuntimeException() {
        super();
    }

    public PustefixRuntimeException(String message) {
        super(message);
    }

    public PustefixRuntimeException(Throwable cause) {
        super(cause);
    }

    public PustefixRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
