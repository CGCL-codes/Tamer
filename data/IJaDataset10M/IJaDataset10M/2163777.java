package de.fzj.roctopus.exceptions;

public class RoctopusLocationSyntaxException extends RoctopusException {

    /**
   * 
   */
    public RoctopusLocationSyntaxException() {
        super();
    }

    /**
   * @param message
   * @param cause
   */
    public RoctopusLocationSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
   * @param message
   */
    public RoctopusLocationSyntaxException(String message) {
        super(message);
    }

    /**
   * @param cause
   */
    public RoctopusLocationSyntaxException(Throwable cause) {
        super(cause);
    }
}
