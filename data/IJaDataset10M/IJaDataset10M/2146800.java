package org.crypthing.things.config;

/**
 * Thrown if the configuration resource referenced by its URI was not found.
 * @author yorickflannagan
 * @version 1.0
 *
 */
public class ResourceNotFoundException extends ConfigException {

    private static final long serialVersionUID = 4696717778451210289L;

    /**
   * Constructs a new exception with null as its detail message.
   */
    public ResourceNotFoundException() {
        super();
    }

    /**
   * Constructs a new exception with the specified detail message.
   * @param message - the detail message.
   */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
   * Constructs a new exception with the specified cause and a detail message of 
   * (cause==null ? null : cause.toString())  (which typically contains the class 
   * and detail message of cause). 
   * @param cause - the cause (which is saved for later retrieval by the 
   * Throwable.getCause() method). (A null value is permitted, and indicates 
   * that the cause is nonexistent or unknown.)
   */
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
   * Constructs a new exception with the specified detail message and cause.
   * @param message - the detail message.
   * @param cause - the cause (which is saved for later retrieval by the 
   * Throwable.getCause() method). (A null value is permitted, and indicates 
   * that the cause is nonexistent or unknown.)
   */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
