package de.sooja.framework.core.exceptions;

/**
 * @author Marc Griesser <marc.griesser@sooja.de>
 *
 * <h2> headline </h2>
 *
 * <p> long description </p>
 */
public class WebserverHandlerException extends Exception {

    /**
   * 
   */
    public WebserverHandlerException() {
        super();
    }

    /**
   * @param message
   */
    public WebserverHandlerException(String message) {
        super(message);
    }

    /**
   * @param message
   * @param cause
   */
    public WebserverHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
   * @param cause
   */
    public WebserverHandlerException(Throwable cause) {
        super(cause);
    }
}
