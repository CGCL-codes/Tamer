package org.gnu.amSpacks.exception.runtime;

public class APIException extends RuntimeException {

    private static final long serialVersionUID = -2683528533854027454L;

    public APIException() {
        super();
    }

    public APIException(String message, Throwable cause) {
        super(message, cause);
    }

    public APIException(String message) {
        super(message);
    }

    public APIException(Throwable cause) {
        super(cause);
    }
}
