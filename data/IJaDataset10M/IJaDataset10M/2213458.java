package net.ains.xml.exception;

/**
 * Dummy exception manager. Used by default in a context.
 *
 * @author Laurent AINS
 */
public class DummyExceptionManager implements ExceptionManager {

    /** @see ExceptionManager#getFatalException(String, Throwable) */
    public XMLRuntimeException getFatalException(String key, Throwable throwable) {
        throwError();
        return null;
    }

    /** @see ExceptionManager#getFatalException(String) */
    public XMLRuntimeException getFatalException(String key) {
        throwError();
        return null;
    }

    /** @see ExceptionManager#getFatalException(String, String, Throwable) */
    public XMLRuntimeException getFatalException(String key, String arg, Throwable throwable) {
        throwError();
        return null;
    }

    /** @see ExceptionManager#getFatalException(String, String) */
    public XMLRuntimeException getFatalException(String key, String arg) {
        throwError();
        return null;
    }

    /** @see ExceptionManager#getFatalException(String, String[], Throwable) */
    public XMLRuntimeException getFatalException(String key, String[] args, Throwable throwable) {
        throwError();
        return null;
    }

    /** @see ExceptionManager#getFatalException(String, String[]) */
    public XMLRuntimeException getFatalException(String key, String[] args) {
        throwError();
        return null;
    }

    /** Throw an error. */
    private void throwError() {
        throw new XMLRuntimeException("No exception manager has been defined.");
    }

    /** @see Object#toString() */
    public String toString() {
        return getClass().getName();
    }
}
