package org.deft.operation.linebreak.debug;

/**
 * Abstract base class for most of the wrapper's classes that allows to "log"
 * information categorized by error, warning and simple message. Furthermore,
 * debug information can be logged and selectively enabled or disabled per
 * object.
 * 
 * @author Christoph Seidl
 */
public abstract class AbstractDebuggable {

    private boolean debugActivated;

    /**
	 * Creates a new <code>AbstractDebuggable</code>.
	 */
    public AbstractDebuggable() {
        debugActivated = true;
    }

    /**
	 * If debug messages are enabled for this object, this method logs
	 * a message that is intended for debug purposes only.
	 * 
	 * @param message The message.
	 */
    protected void debug(String message) {
        if (debugActivated) {
            log("Debug: " + message);
        }
    }

    /**
	 * Sets whether this object allows logging of debug messages or not. By
	 * default, logging is enabled.
	 * 
	 * @param debugActivated True if this object should allow logging of debug messages.
	 */
    protected void setDebugActivated(boolean debugActivated) {
        this.debugActivated = debugActivated;
    }

    /**
	 * Logs an error message. 
	 * 
	 * @param message An error message.
	 */
    protected void logError(String message) {
        log("Error: " + message);
    }

    /**
	 * Logs a warning message.
	 * 
	 * @param message A warning message.
	 */
    protected void logWarning(String message) {
        log("Warning: " + message);
    }

    /**
	 * Logs a simple message.
	 * 
	 * @param message A simple message.
	 */
    protected void log(String message) {
        System.out.println("[" + getClass().getSimpleName() + "] " + message);
    }
}
