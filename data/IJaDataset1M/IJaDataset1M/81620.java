package org.crypthing.things.validator.pkibr;

/**
 * Thrown if an invalid input parameter is received.
 * @author yorickflannagan
 * @version 1.0
 *
 */
public class InvalidInputParamException extends PKIBRValidatorException {

    private static final long serialVersionUID = 7877663158970461328L;

    /**
   * Constructs a new exception with null as its detail message.
   */
    public InvalidInputParamException() {
        super();
    }

    /**
   * Constructs a new exception with the specified detail message.
   * @param message - the detail message.
   */
    public InvalidInputParamException(String message) {
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
    public InvalidInputParamException(Throwable cause) {
        super(cause);
    }

    /**
   * Constructs a new exception with the specified detail message and cause.
   * @param message - the detail message.
   * @param cause - the cause (which is saved for later retrieval by the 
   * Throwable.getCause() method). (A null value is permitted, and indicates 
   * that the cause is nonexistent or unknown.)
   */
    public InvalidInputParamException(String message, Throwable cause) {
        super(message, cause);
    }
}
