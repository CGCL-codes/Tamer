package gleam.executive.service.callback;

public class ExecutiveServiceCallbackException extends Exception {

    private static final long serialVersionUID = 1L;

    public ExecutiveServiceCallbackException() {
        super();
    }

    public ExecutiveServiceCallbackException(String message) {
        super(message);
    }

    public ExecutiveServiceCallbackException(Throwable cause) {
        super(cause);
    }

    public ExecutiveServiceCallbackException(String message, Throwable cause) {
        super(message, cause);
    }
}
