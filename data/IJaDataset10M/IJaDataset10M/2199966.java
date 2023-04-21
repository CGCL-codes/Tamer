package org.simpleframework.servlet.listener;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.http.HttpSessionAttributeListener;
import org.simpleframework.servlet.Definition;

class AttributeListener {

    private final List<ServletContextAttributeListener> context;

    private final List<ServletRequestAttributeListener> request;

    private final List<HttpSessionAttributeListener> session;

    private final Definition definition;

    public AttributeListener(Definition definition) {
        this.context = new ArrayList<ServletContextAttributeListener>();
        this.request = new ArrayList<ServletRequestAttributeListener>();
        this.session = new ArrayList<HttpSessionAttributeListener>();
        this.definition = definition;
    }

    public RequestAttributeListener getRequestAttributeListener() {
        return new RequestAttributeListener(definition, request);
    }

    public SessionAttributeListener getSessionAttributeListener() {
        return new SessionAttributeListener(session);
    }

    public ContextAttributeListener getContextAttributeListener() {
        return new ContextAttributeListener(definition, context);
    }

    public void register(Object listener) throws Exception {
        if (listener instanceof ServletContextAttributeListener) {
            register(listener, context);
        }
        if (listener instanceof ServletRequestAttributeListener) {
            register(listener, request);
        }
        if (listener instanceof HttpSessionAttributeListener) {
            register(listener, session);
        }
    }

    private void register(Object listener, List list) {
        list.add(listener);
    }
}
