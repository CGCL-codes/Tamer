package javax.jdo;

public class JDOFatalDataStoreException extends JDOFatalException {

    public JDOFatalDataStoreException() {
    }

    public JDOFatalDataStoreException(String msg) {
        super(msg);
    }

    public JDOFatalDataStoreException(String msg, Object failed) {
        super(msg, failed);
    }

    public JDOFatalDataStoreException(String msg, Throwable nested[]) {
        super(msg, nested);
    }

    public JDOFatalDataStoreException(String msg, Throwable nested) {
        super(msg, nested);
    }
}
