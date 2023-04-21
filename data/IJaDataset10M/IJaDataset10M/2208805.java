package de.beas.explicanto.client.model;

/**
 * Thrown when the model.xml does not exist or cannot be parsed
 *
 * @author alexandru.georgescu
 * @version 1.0
 *
 */
public class ModelException extends Exception {

    /**
	 * 
	 */
    public ModelException() {
        super();
    }

    /**
	 * @param message
	 */
    public ModelException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public ModelException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
