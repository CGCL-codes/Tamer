package org.jsmtpd.tools.rights;

public class RightException extends Exception {

    public RightException() {
        super();
    }

    public RightException(String message, Throwable cause) {
        super(message, cause);
    }

    public RightException(String message) {
        super(message);
    }

    public RightException(Throwable cause) {
        super(cause);
    }
}
