package ch.iserver.ace.algorithm.test;

/**
 * Base exception thrown by this package.
 */
public class ScenarioException extends RuntimeException {

    /**
	 * 
	 */
    public ScenarioException() {
        super();
    }

    /**
	 * @param message the exception message
	 */
    public ScenarioException(String message) {
        super(message);
    }

    /**
	 * @param message the exception message
	 * @param cause the exception cause
	 */
    public ScenarioException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * @param cause the exception cause
	 */
    public ScenarioException(Throwable cause) {
        super(cause);
    }
}
