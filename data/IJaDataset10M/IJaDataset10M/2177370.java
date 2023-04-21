package com.googlecode.voctopus.request.validation;

/**
 * The terminal expression of the Http Request Interpreter are the following entities:
 * Http-Version token and the header body values.
 * @author marcello
 * Feb 17, 2008 01:01:08 AM
 */
public abstract class HttpRequestTerminalExpression extends AbstractHttpRequestExpression {

    /**
     * Constructs a new Termina Expression of the Http Request Interpreter. The same context
     * is used.
     * @param context the lines of the HTTP request
     */
    public HttpRequestTerminalExpression(HttpRequestInterpreterContext context, String token) {
        super(context, token);
    }

    public abstract void interpret() throws HttpRequestInterpreterException;
}
