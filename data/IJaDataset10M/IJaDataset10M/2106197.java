package com.googlecode.voctopus.request.validation;

public class HttpRequestHeaderFieldValueExpression extends HttpRequestTerminalExpression {

    public HttpRequestHeaderFieldValueExpression(HttpRequestInterpreterContext context, String token) {
        super(context, token);
    }

    @Override
    public void interpret() throws HttpRequestInterpreterException {
    }
}
