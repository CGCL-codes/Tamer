package org.exist.http.sleepy;

/**
 *
 * @author Adam Retter <adam.retter@googlemail.com>
 */
public class RESTfulXQueryServiceCompilationException extends RESTfulXQueryServiceException {

    public RESTfulXQueryServiceCompilationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RESTfulXQueryServiceCompilationException(String message) {
        super(message);
    }
}
