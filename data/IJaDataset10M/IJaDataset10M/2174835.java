package org.t2framework.confeito.contexts.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.t2framework.confeito.contexts.Request;
import org.t2framework.confeito.contexts.Session;

/**
 * <#if locale="en">
 * <p>
 * Concrete class of org.t2framework.contexts.Session.
 * 
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 * @see org.t2framework.confeito.contexts.Session
 */
public class SessionImpl implements Session {

    protected Request request;

    protected HttpSession session;

    public SessionImpl(final Request request) {
        this.request = request;
        this.request.setSession(this);
        this.session = request.getNativeResource().getSession(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getAttributeNames() {
        List<String> ret = new ArrayList<String>();
        for (Enumeration<String> e = getNativeResource().getAttributeNames(); e.hasMoreElements(); ) {
            ret.add(e.nextElement());
        }
        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getAttributesAsMap() {
        Map<String, Object> ret = new HashMap<String, Object>();
        for (Enumeration<String> e = getNativeResource().getAttributeNames(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            Object value = getNativeResource().getAttribute(key);
            ret.put(key, value);
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V getAttribute(String key) {
        return (V) getNativeResource().getAttribute(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <V> V removeAttribute(String key) {
        Object ret = getAttribute(key);
        getNativeResource().removeAttribute(key);
        return (V) ret;
    }

    @Override
    public synchronized <V> void setAttribute(String key, V value) {
        getNativeResource().setAttribute(key, value);
    }

    @Override
    public HttpSession getNativeResource() {
        return session;
    }
}
