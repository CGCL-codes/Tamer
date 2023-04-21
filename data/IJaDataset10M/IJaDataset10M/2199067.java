package com.planetachewood.util;

/**
 * Exception thrown when an error occurs while retrieving data via HTTP.
 * 
 * @author <a href="mailto:mark.a.allen@gmail.com">Mark Allen</a>
 * @since 1.0
 * @since Oct 15, 2006
 * @version $Revision: 7 $ $Date: 2007-01-12 22:20:55 -0500 (Fri, 12 Jan 2007) $
 */
public class HttpException extends RuntimeException {

    /**
     * Creates an exception with a nested root exception and descriptive error
     * message.
     * 
     * @param message Descriptive error message.
     * @param throwable Root exception to wrap.
     */
    public HttpException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Creates an exception with a descriptive error message.
     * 
     * @param message Descriptive error message.
     */
    public HttpException(String message) {
        super(message);
    }

    /**
     * Creates an exception with a nested root exception.
     * 
     * @param throwable Root exception to wrap.
     */
    public HttpException(Throwable throwable) {
        super(throwable);
    }
}
