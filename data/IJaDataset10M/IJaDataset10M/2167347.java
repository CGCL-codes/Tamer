package com.amazon.carbonado;

/**
 * Thrown if a persist operation fails because lock acquisition timed out.
 *
 * @author Brian S O'Neill
 */
public class PersistTimeoutException extends PersistException {

    private static final long serialVersionUID = 1L;

    public PersistTimeoutException() {
        super();
    }

    public PersistTimeoutException(String message) {
        super(message);
    }

    public PersistTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistTimeoutException(Throwable cause) {
        super(cause);
    }

    @Override
    protected FetchException makeFetchException(String message, Throwable cause) {
        return new FetchTimeoutException(message, cause);
    }
}
