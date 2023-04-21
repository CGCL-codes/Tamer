package dryven.request.controller;

public class ActionException extends RuntimeException {

    public ActionException() {
        super();
    }

    public ActionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionException(String message) {
        super(message);
    }

    public ActionException(Throwable cause) {
        super(cause);
    }
}
