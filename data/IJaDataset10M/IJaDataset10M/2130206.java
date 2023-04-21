package bebops.util.exception;

public class BebopsException extends Exception {

    public BebopsException() {
        super();
    }

    public BebopsException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public BebopsException(String arg0) {
        super(arg0);
    }

    public BebopsException(Throwable arg0) {
        super(arg0);
    }
}
