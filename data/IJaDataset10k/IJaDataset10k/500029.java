package ru.wsdev.kelvina.system.exception;

/**
 * KelvinaException is a system exception, which actually wraps any other exception.
 * @author Aleksandr Akhtyamov
 *
 */
public class KelvinaException extends Exception {

    /**
    * Universal class version identifier
    */
    private static final long serialVersionUID = -1048772643662266620L;

    /**
    * @see java.lang.Exception#Exception()
    */
    public KelvinaException() {
        super();
    }

    /**
    * @param message the detail message
    * @see java.lang.Exception#Exception(String)
    */
    public KelvinaException(String message) {
        super(message);
    }

    /**
    * @param message the detail message
    * @param cause the cause
    * @see java.lang.Exception#Exception(String, Throwable)
    */
    public KelvinaException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
    * @param cause the cause
    * @see java.lang.Exception#Exception(Throwable)
    */
    public KelvinaException(Throwable cause) {
        super(cause);
    }
}
