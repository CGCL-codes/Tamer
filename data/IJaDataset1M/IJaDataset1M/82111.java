package datadog.exceptions;

/**
 * All declared exceptions thrown by datadog must subclass DatadogException.
 *
 * @author Justin Tomich
 * @version $Id: DatadogException.java 170 2006-02-21 05:28:24Z tomichj $
 */
public class DatadogException extends Exception {

    public DatadogException() {
        super();
    }

    public DatadogException(String message) {
        super(message);
    }

    public DatadogException(Throwable throwable) {
        super(throwable);
    }

    public DatadogException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
