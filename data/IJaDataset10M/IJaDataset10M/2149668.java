package john.ws.exception;

public class CaseException extends Exception {

    public CaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaseException(String message) {
        super(message);
    }

    public CaseException(Throwable cause) {
        super(cause);
    }

    public CaseException() {
        super();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
