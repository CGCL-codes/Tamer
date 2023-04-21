package org.nodevision.portal.api.impl;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;
import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.nodevision.portal.om.pagedefinition.Portlet;
import org.nodevision.portal.repositories.RepositoryBasic;
import org.nodevision.portal.utils.Constants;
import org.nodevision.portal.wrapper.NVHttpServletRequest;

public class NVPortletSession implements PortletSession {

    private HttpSession session;

    private Portlet portlet;

    private PortletContext portletContext;

    private NVPortletRequest renderRequest;

    public NVPortletSession(ServletContext servletContext, Portlet portlet, NVHttpServletRequest request, HttpServletResponse response, PortletContext portletContext, boolean newSession, NVPortletRequest renderRequest) {
        this.portlet = portlet;
        this.portletContext = portletContext;
        this.renderRequest = renderRequest;
        ServletContext servletContextPortlet = servletContext.getContext(RepositoryBasic.getWebapplications().getWebapplication(portlet.getWebApplication()).getContext());
        try {
            servletContextPortlet.getRequestDispatcher("/SessionProvider?createSession=" + newSession).include(request.request, response);
            session = (HttpSession) request.getAttribute(Constants.WEBAPP_SESSION);
            request.setSession(session);
        } catch (Exception e) {
            servletContext.log("Could not create session: " + e.toString());
        }
    }

    public Object getAttribute(String param, int scope) throws IllegalStateException, IllegalArgumentException {
        if (null == param) {
            throw new IllegalArgumentException("No null value allowed in PortletSession.getAttribute(attr).");
        }
        if (scope == PortletSession.PORTLET_SCOPE) {
            return session.getAttribute(Constants.SESSION_ATTRIBUTE_PREFIX + '.' + portlet.getId() + '?' + param);
        } else {
            return session.getAttribute(param);
        }
    }

    public Object getAttribute(String param) {
        return getAttribute(param, PortletSession.PORTLET_SCOPE);
    }

    public Enumeration getAttributeNames() {
        return getAttributeNames(PortletSession.PORTLET_SCOPE);
    }

    public Enumeration getAttributeNames(int scope) throws IllegalStateException {
        if (PortletSession.APPLICATION_SCOPE == scope) {
            Vector retval = new Vector();
            Enumeration en = session.getAttributeNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                if (!key.startsWith(Constants.SESSION_ATTRIBUTE_PREFIX)) {
                    retval.add(key);
                }
            }
            return Collections.enumeration(retval);
        } else {
            Vector retval = new Vector();
            Enumeration en = session.getAttributeNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                if (key.startsWith(Constants.SESSION_ATTRIBUTE_PREFIX + '.' + portlet.getId() + '?')) {
                    String prefix = Constants.SESSION_ATTRIBUTE_PREFIX + '.' + portlet.getId() + '?';
                    String paramName = key.substring(prefix.length());
                    retval.add(paramName);
                }
            }
            return Collections.enumeration(retval);
        }
    }

    public long getCreationTime() {
        return session.getCreationTime();
    }

    public String getId() {
        return session.getId();
    }

    public long getLastAccessedTime() {
        return session.getLastAccessedTime();
    }

    public int getMaxInactiveInterval() {
        return session.getMaxInactiveInterval();
    }

    public PortletContext getPortletContext() {
        return portletContext;
    }

    public void invalidate() {
        session.invalidate();
        renderRequest.request.setSession(null);
        renderRequest.portletSession = null;
    }

    public boolean isNew() {
        return session.isNew();
    }

    public void removeAttribute(String attr, int scope) throws IllegalArgumentException, IllegalStateException {
        if (null == attr) {
            throw new IllegalArgumentException("No null value allowed in PortletSession.removeAttribute(attr).");
        }
        if (scope == PortletSession.PORTLET_SCOPE) {
            session.removeAttribute(Constants.SESSION_ATTRIBUTE_PREFIX + '.' + portlet.getId() + '?' + attr);
        } else {
            session.removeAttribute(attr);
        }
    }

    public void removeAttribute(String attr) {
        removeAttribute(attr, PortletSession.PORTLET_SCOPE);
    }

    public void setAttribute(String attr, Object value, int scope) throws IllegalStateException, IllegalArgumentException {
        if (null == attr) {
            throw new IllegalArgumentException("No null value for key allowed in PortletSession.setAttribute(attr, value).");
        }
        if (scope == PortletSession.PORTLET_SCOPE) {
            session.setAttribute(Constants.SESSION_ATTRIBUTE_PREFIX + '.' + portlet.getId() + '?' + attr, value);
        } else {
            session.setAttribute(attr, value);
        }
    }

    public void setAttribute(String attr, Object value) {
        setAttribute(attr, value, PortletSession.PORTLET_SCOPE);
    }

    public void setMaxInactiveInterval(int interval) {
        session.setMaxInactiveInterval(interval);
    }

    public HttpSession getSession() {
        return session;
    }
}
