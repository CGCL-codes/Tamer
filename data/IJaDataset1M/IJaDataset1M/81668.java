package org.ops4j.peaberry;

/**
 * Exception thrown when a requested service is not available.
 * 
 * @author mcculls@gmail.com (Stuart McCulloch)
 */
public final class ServiceUnavailableException extends ServiceException {

    private static final long serialVersionUID = 9207041522801931804L;

    /**
   * Constructs a {@code ServiceUnavailableException} with no message or cause.
   */
    public ServiceUnavailableException() {
        super();
    }

    /**
   * Constructs a {@code ServiceUnavailableException} with a specific message.
   * 
   * @param message detailed message
   */
    public ServiceUnavailableException(final String message) {
        super(message);
    }

    /**
   * Constructs a {@code ServiceUnavailableException} with a specific cause.
   * 
   * @param cause underlying cause
   */
    public ServiceUnavailableException(final Throwable cause) {
        super(cause);
    }

    /**
   * Constructs a {@code ServiceUnavailableException} with message and cause.
   * 
   * @param message detailed message
   * @param cause underlying cause
   */
    public ServiceUnavailableException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
