package org.briareo.commons.jms.exception;

/**
 * @author Javier Aparicio
 * 
 */
public class InvalidQueueNameException extends Exception {

    /**
   * 
   */
    private static final long serialVersionUID = -9002942643751461290L;

    /**
   * 
   */
    public InvalidQueueNameException() {
    }

    /**
   * @param message
   */
    public InvalidQueueNameException(String message) {
        super(message);
    }

    /**
   * @param cause
   */
    public InvalidQueueNameException(Throwable cause) {
        super(cause);
    }

    /**
   * @param message
   * @param cause
   */
    public InvalidQueueNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
