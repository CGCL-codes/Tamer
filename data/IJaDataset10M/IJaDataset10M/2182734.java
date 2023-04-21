package org.akrogen.core.logs;

/**
 * Log Akrogen.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 *
 */
public interface IAkrogenLog {

    /**
	 * Log info message.
	 * @param message
	 */
    public void logInfo(String message);

    /**
	 * Log warn message.
	 * @param message
	 * @param e
	 */
    public void logWarning(String message, Throwable e);

    /**
	 * Log error message.
	 * @param message
	 * @param e
	 */
    public void logError(String message, Throwable e);
}
