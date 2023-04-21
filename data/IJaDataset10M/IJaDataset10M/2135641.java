package APJP.HTTP11;

public class HTTPMessageException extends Exception {

    private static final long serialVersionUID = 1L;

    public HTTPMessageException() {
        super();
    }

    public HTTPMessageException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public HTTPMessageException(String message) {
        super(message);
    }

    public HTTPMessageException(Throwable throwable) {
        super(throwable);
    }
}
