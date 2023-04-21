package org.magicdroid.server.common;

import javax.servlet.http.HttpServletRequest;

public class SessionAttribute<T> extends ScopedAttribute<T> {

    public SessionAttribute(String name) {
        super(name);
    }

    public T get(HttpServletRequest request) {
        return (T) request.getSession().getAttribute(this.name());
    }

    public T set(HttpServletRequest request, T value) {
        request.getSession().setAttribute(this.name(), value);
        return value;
    }
}
