package db.exceptions;

public class DataAccessException extends RuntimeException {

    private static final long serialVersionUID = 1101454226626418885L;

    public DataAccessException() {
        super();
    }

    public DataAccessException(String _message) {
        super(_message);
    }

    public DataAccessException(Throwable _cause) {
        super("Error while accessing database.", _cause);
    }

    public DataAccessException(String _message, Throwable _cause) {
        super(_message, _cause);
    }
}
