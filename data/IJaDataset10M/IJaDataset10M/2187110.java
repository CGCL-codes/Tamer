package alx.library;

public class IndexNotExistsException extends Exception {

    private static final long serialVersionUID = 1L;

    public IndexNotExistsException() {
        super();
    }

    public IndexNotExistsException(String arg0) {
        super(arg0);
    }

    public IndexNotExistsException(Throwable arg0) {
        super(arg0);
    }

    public IndexNotExistsException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
