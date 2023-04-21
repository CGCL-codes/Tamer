package ch.isbiel.oois.common.service;

/**
 * This class implements an exception which can wrap a lower-level exception.
 * 
 * @author $Author$
 * @version $Revision$
 */
public class ServiceLocatorException extends Exception {

    private Exception _exception;

    /**
   * Creates a new ServiceLocatorException wrapping another exception, and with a detail message.
   * @param message the detail message.
   * @param exception the wrapped exception.
   */
    public ServiceLocatorException(String message, Exception exception) {
        super(message);
        _exception = exception;
    }

    /**
   * Creates a ServiceLocatorException with the specified detail message.
   * @param message the detail message.
   */
    public ServiceLocatorException(String message) {
        this(message, null);
    }

    /**
   * Creates a new ServiceLocatorException wrapping another exception, and with no detail message.
   * @param exception the wrapped exception.
   */
    public ServiceLocatorException(Exception exception) {
        this(null, exception);
    }

    /**
   * Gets the wrapped exception.
   *
   * @return the wrapped exception.
   */
    public Exception getException() {
        return _exception;
    }

    /**
   * Retrieves (recursively) the root cause exception.
   *
   * @return the root cause exception.
   */
    public Exception getRootCause() {
        if (_exception instanceof ServiceLocatorException) {
            return ((ServiceLocatorException) _exception).getRootCause();
        }
        return _exception == null ? this : _exception;
    }

    public String toString() {
        if (_exception instanceof ServiceLocatorException) {
            return ((ServiceLocatorException) _exception).toString();
        }
        return _exception == null ? super.toString() : _exception.toString();
    }
}
