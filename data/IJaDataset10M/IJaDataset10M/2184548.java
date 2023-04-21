package forms.table;

public class GenericModelException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GenericModelException() {
        super();
    }

    public GenericModelException(String message) {
        super(message);
    }

    public GenericModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericModelException(Throwable cause) {
        super(cause);
    }
}
