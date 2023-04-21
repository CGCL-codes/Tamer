package org.webical.plugin.registration;

/**
 * Thrown on registration errors
 * @author ivo
 *
 */
public class RegistrationException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
	 *
	 */
    public RegistrationException() {
    }

    /**
	 * @param message
	 */
    public RegistrationException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public RegistrationException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
