package dsb.mbc.exceptions;

public class MBCException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MBCException() {
        super();
    }

    public MBCException(String message) {
        super(message);
    }

    public MBCException(Throwable t) {
        super(t);
    }

    public MBCException(String message, Throwable t) {
        super(message, t);
    }
}
