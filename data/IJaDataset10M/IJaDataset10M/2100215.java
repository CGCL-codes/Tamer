package org.crypthing.things.config;

/**
 * Thrown if configuration was not loaded by @see ConfigFactory.loadConfig()
 * @author yorickflannagan
 * @version 1.0
 *
 */
public class UnloadedConfigException extends ConfigException {

    private static final long serialVersionUID = -2391248368766292954L;

    /**
   * Constructs a new exception with null as its detail message.
   */
    public UnloadedConfigException() {
        super();
    }

    /**
   * Constructs a new exception with the specified detail message.
   * @param message - the detail message.
   */
    public UnloadedConfigException(String message) {
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
    public UnloadedConfigException(Throwable cause) {
        super(cause);
    }

    /**
   * Constructs a new exception with the specified detail message and cause.
   * @param message - the detail message.
   * @param cause - the cause (which is saved for later retrieval by the 
   * Throwable.getCause() method). (A null value is permitted, and indicates 
   * that the cause is nonexistent or unknown.)
   */
    public UnloadedConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
