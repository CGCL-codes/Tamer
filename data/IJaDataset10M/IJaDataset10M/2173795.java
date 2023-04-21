package securus.services;

/**
 * 
 * @author m.kanel
 * 
 */
public class SecurusException extends RuntimeException {

    public enum ErrorCode {

        UNKNOWN, INVALID_CREDENTIALS, INVALID_DEVICE, INVALID_EMAIL, INVALID_SHARE, INVALID_USER, USER_SUSPENDED, USER_NOT_ACTIVE, USER_NOT_FOUND
    }

    private ErrorCode errorCode;

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    private static final long serialVersionUID = -826240512176508821L;

    public SecurusException(String message, Throwable cause) {
        super(message, cause);
        errorCode = ErrorCode.UNKNOWN;
    }

    public SecurusException(String message, Throwable cause, ErrorCode code) {
        super(message, cause);
        errorCode = code;
    }

    public SecurusException(String message, ErrorCode code) {
        super(message);
        errorCode = code;
    }

    public SecurusException(String message) {
        super(message);
    }

    public SecurusException(Exception cause) {
        super(cause);
    }
}
