package de.fzj.roctopus.exceptions;

public class RoctopusIdentityException extends RoctopusException {

    /**
   * 
   */
    public RoctopusIdentityException() {
        super();
    }

    /**
   * @param message
   * @param cause
   */
    public RoctopusIdentityException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
   * @param message
   */
    public RoctopusIdentityException(String message) {
        super(message);
    }

    /**
   * @param cause
   */
    public RoctopusIdentityException(Throwable cause) {
        super(cause);
    }
}
