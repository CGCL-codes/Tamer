package org.crypthing.signthing.client;

/**
 * Thrown if an invalid CMS file is specified.
 * @author yorickflannagan
 * @version 1.0
 *
 */
public class InvalidFileException extends SignthingClientException {

    private static final long serialVersionUID = 275892392304646506L;

    /**
   * Constructs a new exception with null as its detail message.
   */
    public InvalidFileException() {
        super();
    }

    /**
   * Constructs a new exception with the specified detail message.
   * @param message - the detail message.
   */
    public InvalidFileException(String message) {
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
    public InvalidFileException(Throwable cause) {
        super(cause);
    }

    /**
   * Constructs a new exception with the specified detail message and cause.
   * @param message - the detail message.
   * @param cause - the cause (which is saved for later retrieval by the 
   * Throwable.getCause() method). (A null value is permitted, and indicates 
   * that the cause is nonexistent or unknown.)
   */
    public InvalidFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
