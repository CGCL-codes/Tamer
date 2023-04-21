package org.javalite.http;

/**
 * @author Igor Polevoy
 */
public class HttpException extends RuntimeException {

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }
}
