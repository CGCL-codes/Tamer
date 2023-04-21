package com.liferay.util.portlet;

import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;

/**
 * <a href="PortletRequestWrapper.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class PortletRequestWrapper implements PortletRequest {

    public PortletRequestWrapper(PortletRequest req) {
        _req = req;
    }

    public boolean isWindowStateAllowed(WindowState state) {
        return _req.isWindowStateAllowed(state);
    }

    public boolean isPortletModeAllowed(PortletMode mode) {
        return _req.isPortletModeAllowed(mode);
    }

    public PortletMode getPortletMode() {
        return _req.getPortletMode();
    }

    public WindowState getWindowState() {
        return _req.getWindowState();
    }

    public PortletPreferences getPreferences() {
        return _req.getPreferences();
    }

    public PortletSession getPortletSession() {
        return _req.getPortletSession();
    }

    public PortletSession getPortletSession(boolean create) {
        return _req.getPortletSession(create);
    }

    public String getProperty(String name) {
        return _req.getProperty(name);
    }

    public Enumeration getProperties(String name) {
        return _req.getProperties(name);
    }

    public Enumeration getPropertyNames() {
        return _req.getPropertyNames();
    }

    public PortalContext getPortalContext() {
        return _req.getPortalContext();
    }

    public String getAuthType() {
        return _req.getAuthType();
    }

    public String getContextPath() {
        return _req.getContextPath();
    }

    public String getRemoteUser() {
        return _req.getRemoteUser();
    }

    public Principal getUserPrincipal() {
        return _req.getUserPrincipal();
    }

    public boolean isUserInRole(String role) {
        return _req.isUserInRole(role);
    }

    public Object getAttribute(String name) {
        return _req.getAttribute(name);
    }

    public Enumeration getAttributeNames() {
        return _req.getAttributeNames();
    }

    public String getParameter(String name) {
        return _req.getParameter(name);
    }

    public Enumeration getParameterNames() {
        return _req.getParameterNames();
    }

    public String[] getParameterValues(String name) {
        return _req.getParameterValues(name);
    }

    public Map getParameterMap() {
        return _req.getParameterMap();
    }

    public boolean isSecure() {
        return _req.isSecure();
    }

    public void setAttribute(String name, Object obj) {
        _req.setAttribute(name, obj);
    }

    public void removeAttribute(String name) {
        _req.removeAttribute(name);
    }

    public String getRequestedSessionId() {
        return _req.getRequestedSessionId();
    }

    public boolean isRequestedSessionIdValid() {
        return _req.isRequestedSessionIdValid();
    }

    public String getResponseContentType() {
        return _req.getResponseContentType();
    }

    public Enumeration getResponseContentTypes() {
        return _req.getResponseContentTypes();
    }

    public Locale getLocale() {
        return _req.getLocale();
    }

    public Enumeration getLocales() {
        return _req.getLocales();
    }

    public String getScheme() {
        return _req.getScheme();
    }

    public String getServerName() {
        return _req.getServerName();
    }

    public int getServerPort() {
        return _req.getServerPort();
    }

    public PortletRequest getPortletRequest() {
        return _req;
    }

    public void setPortletRequest(PortletRequest req) {
        _req = req;
    }

    private PortletRequest _req;
}
