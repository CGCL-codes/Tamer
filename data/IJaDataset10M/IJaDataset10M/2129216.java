package org.mca.qmass.http.filters;

import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

/**
 * User: malpay
 * Date: 20.Haz.2011
 * Time: 15:42:31
 */
public class HttpSessionWrapper implements HttpSession {

    protected final YALog logger = YALogFactory.getLog(getClass());

    protected HttpServletRequest request;

    public HttpSessionWrapper(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public long getCreationTime() {
        return getSession().getCreationTime();
    }

    protected HttpSession getSession() {
        return request.getSession();
    }

    @Override
    public String getId() {
        return getSession().getId();
    }

    @Override
    public long getLastAccessedTime() {
        return getSession().getLastAccessedTime();
    }

    @Override
    public ServletContext getServletContext() {
        return getSession().getServletContext();
    }

    @Override
    public void setMaxInactiveInterval(int i) {
        getSession().setMaxInactiveInterval(i);
    }

    @Override
    public int getMaxInactiveInterval() {
        return getSession().getMaxInactiveInterval();
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return getSession().getSessionContext();
    }

    @Override
    public Object getAttribute(String name) {
        return getSession().getAttribute(name);
    }

    @Override
    public Object getValue(String name) {
        return getSession().getValue(name);
    }

    @Override
    public Enumeration getAttributeNames() {
        return getSession().getAttributeNames();
    }

    @Override
    public String[] getValueNames() {
        return getSession().getValueNames();
    }

    @Override
    public void setAttribute(String name, Object value) {
        getSession().setAttribute(name, value);
    }

    @Override
    public void putValue(String name, Object value) {
        getSession().putValue(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        getSession().removeAttribute(name);
    }

    @Override
    public void removeValue(String name) {
        getSession().removeValue(name);
    }

    @Override
    public void invalidate() {
        getSession().invalidate();
    }

    @Override
    public boolean isNew() {
        return getSession().isNew();
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}
