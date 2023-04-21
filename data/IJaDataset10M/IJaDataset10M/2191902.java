package javax.webbeans;

public class IllegalProductException extends ExecutionException {

    private static final long serialVersionUID = 1490337449170464997L;

    public IllegalProductException() {
        super();
    }

    public IllegalProductException(String message) {
        super(message);
    }

    public IllegalProductException(Throwable e) {
        super(e);
    }

    public IllegalProductException(String message, Throwable e) {
        super(message, e);
    }
}
