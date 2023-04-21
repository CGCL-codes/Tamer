package com.c2b2.open286.container.processing;

import com.c2b2.open286.container.ContainerContext;
import com.c2b2.open286.container.ContainerRequestData;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.WindowState;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class is the persistent representation of an address
 * <p>
 * Open 286 Render Request implements the java portlet api Render Request interface
 * Copyright 2008 C2B2 Consulting Limited. All rights reserved.
 * </p>
 */
public class Open286RenderRequest implements RenderRequest {

    public Open286RenderRequest(HttpServletRequest request, ContainerRequestData data, PortalContext pcontext, ContainerContext cc) {
        myRequest = request;
        myWindowID = request.getParameter(ProcessingConstants.WINDOW_ID_PARAMETER);
        myContext = pcontext;
        myContainerContext = cc;
        setAttribute(RenderRequest.LIFECYCLE_PHASE, RenderRequest.RENDER_PHASE);
    }

    public String getETag() {
        return myData.getETag();
    }

    public boolean isWindowStateAllowed(WindowState windowState) {
        boolean result = false;
        return result;
    }

    public boolean isPortletModeAllowed(PortletMode portletMode) {
        return false;
    }

    public PortletMode getPortletMode() {
        return new PortletMode(myData.getPortletMode());
    }

    public WindowState getWindowState() {
        return new WindowState(myData.getWindowState());
    }

    public PortletPreferences getPreferences() {
        return null;
    }

    public PortletSession getPortletSession() {
        return getPortletSession(true);
    }

    public PortletSession getPortletSession(boolean b) {
        if (mySession == null) {
            HttpSession servletSession = myRequest.getSession(b);
            if (servletSession != null) {
                mySession = new Open286Session(servletSession, myWindowID, myContainerContext);
            }
        }
        return mySession;
    }

    public String getProperty(String string) {
        return myRequest.getHeader(string);
    }

    public Enumeration getProperties(String string) {
        return myRequest.getHeaders(string);
    }

    public Enumeration getPropertyNames() {
        return myRequest.getHeaderNames();
    }

    public PortalContext getPortalContext() {
        return myContext;
    }

    public String getAuthType() {
        return myRequest.getAuthType();
    }

    public String getContextPath() {
        return myRequest.getContextPath();
    }

    public String getRemoteUser() {
        return myRequest.getRemoteUser();
    }

    public Principal getUserPrincipal() {
        return myRequest.getUserPrincipal();
    }

    public boolean isUserInRole(String string) {
        return myRequest.isUserInRole(string);
    }

    public Object getAttribute(String string) {
        return null;
    }

    public Enumeration getAttributeNames() {
        return null;
    }

    public String getParameter(String string) {
        return null;
    }

    public Enumeration getParameterNames() {
        return null;
    }

    public String[] getParameterValues(String string) {
        return new String[0];
    }

    public Map getParameterMap() {
        return null;
    }

    public boolean isSecure() {
        return myRequest.isSecure();
    }

    public void setAttribute(String string, Object object) {
    }

    public void removeAttribute(String string) {
    }

    public String getRequestedSessionId() {
        return myRequest.getRequestedSessionId();
    }

    public boolean isRequestedSessionIdValid() {
        return myRequest.isRequestedSessionIdValid();
    }

    public String getResponseContentType() {
    }

    public Enumeration getResponseContentTypes() {
        return null;
    }

    public Locale getLocale() {
        return myRequest.getLocale();
    }

    public Enumeration getLocales() {
        return myRequest.getLocales();
    }

    public String getScheme() {
        return myRequest.getScheme();
    }

    public String getServerName() {
        return myRequest.getServerName();
    }

    public int getServerPort() {
        return myRequest.getServerPort();
    }

    public String getWindowID() {
        return myData.getWindowID();
    }

    public Cookie[] getCookies() {
        return myRequest.getCookies();
    }

    public Map getPrivateParameterMap() {
        return myData.getParameters();
    }

    public Map getPublicParameterMap() {
        return myData.getPublicRenderParameters();
    }

    private HttpServletRequest myRequest;

    private PortalContext myContext;

    private ContainerContext myContainerContext;

    private Open286Session mySession;

    private String myWindowID;

    private ContainerRequestData myData;
}
