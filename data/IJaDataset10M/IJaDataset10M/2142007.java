package org.colombbus.tangara.commons.resinject;

@SuppressWarnings("serial")
public class InvalidResourceException extends RuntimeException {

    public InvalidResourceException() {
    }

    public InvalidResourceException(String message) {
        super(message);
    }

    public InvalidResourceException(Throwable cause) {
        super(cause);
    }

    public InvalidResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
