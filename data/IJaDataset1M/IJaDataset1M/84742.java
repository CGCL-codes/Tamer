package org.magicdroid.server.common;

import javax.servlet.http.HttpServletRequest;

public class RequestParameter extends ScopedAttribute<String> {

    public RequestParameter(String name) {
        super(name);
    }

    @Override
    public String get(HttpServletRequest request) {
        return request.getParameter(this.name());
    }

    public String[] list(HttpServletRequest request) {
        return request.getParameterValues(this.name());
    }

    @Override
    public String set(HttpServletRequest request, String value) {
        throw new UnsupportedOperationException();
    }
}
