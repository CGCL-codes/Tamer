package com.googlecode.voctopus.request.validation;

/**
 * This is an abstract HttpRequestExpression to be evaluated by the Interpreter.
 * @author marcello
 * Feb 17, 2008 11:34:56 PM
 */
public abstract class AbstractHttpRequestExpression {

    /**
     * The request lines sent by the connection with the client.
     */
    private HttpRequestInterpreterContext context;

    /**
     * Token to be evaluated
     */
    protected String evaluatedToken;

    /**
     * Constructs a new instance of this class using the context.
     * @param context is the context from the HttpRequest. 
     */
    public AbstractHttpRequestExpression(HttpRequestInterpreterContext context, String token) {
        this.context = context;
        this.evaluatedToken = token;
    }

    /**
     * @return The context associated with the Http Interpreter.
     */
    public HttpRequestInterpreterContext getContext() {
        return this.context;
    }

    /**
     * @return the token evaluated on the expression.
     */
    public String getEvaluatedToken() {
        return this.evaluatedToken;
    }

    /**
     * @return an instance of the updated HttpRequest from the interpretation.
     * @throws HttpRequestInterpreterException in case the interpretation is incorrect.
     */
    public abstract void interpret() throws HttpRequestInterpreterException;

    @Override
    public String toString() {
        return this.getEvaluatedToken();
    }
}
