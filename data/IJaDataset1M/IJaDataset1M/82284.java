package com.sokolov.portlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.security.Principal;

/**
 * Wrapper for HttpRequest class.
 *
 * @author Sergei Sokolov
 * @version 1.0
 */
public class HttpRequestWrapper {

    HttpServletRequest httpServletRequest;

    public HttpRequestWrapper(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public String getParameter(String parameter) {
        return httpServletRequest.getParameter(parameter);
    }

    public Object getAttribute(String attribute) {
        return httpServletRequest.getAttribute(attribute);
    }

    public void setAttribute(String attribute, Object value) {
        httpServletRequest.setAttribute(attribute, value);
    }

    @Deprecated
    public String getAuthType() {
        return httpServletRequest.getAuthType();
    }

    @Deprecated
    public Cookie[] getCookies() {
        return httpServletRequest.getCookies();
    }

    @Deprecated
    long getDateHeader(String dateHeader) {
        return httpServletRequest.getDateHeader(dateHeader);
    }

    @Deprecated
    String getHeader(String header) {
        return httpServletRequest.getHeader(header);
    }

    @Deprecated
    Enumeration getHeaders(String s) {
        return httpServletRequest.getHeaders(s);
    }

    @Deprecated
    Enumeration getHeaderNames() {
        return httpServletRequest.getHeaderNames();
    }

    @Deprecated
    int getIntHeader(String s) {
        return httpServletRequest.getIntHeader(s);
    }

    @Deprecated
    String getMethod() {
        return httpServletRequest.getMethod();
    }

    @Deprecated
    String getPathInfo() {
        return httpServletRequest.getPathInfo();
    }

    @Deprecated
    String getPathTranslated() {
        return httpServletRequest.getPathTranslated();
    }

    @Deprecated
    String getContextPath() {
        return httpServletRequest.getContextPath();
    }

    @Deprecated
    String getQueryString() {
        return httpServletRequest.getQueryString();
    }

    @Deprecated
    String getRemoteUser() {
        return httpServletRequest.getRemoteUser();
    }

    @Deprecated
    boolean isUserInRole(String s) {
        return httpServletRequest.isUserInRole(s);
    }

    @Deprecated
    Principal getUserPrincipal() {
        return httpServletRequest.getUserPrincipal();
    }

    @Deprecated
    String getRequestedSessionId() {
        return httpServletRequest.getRequestedSessionId();
    }

    @Deprecated
    String getRequestURI() {
        return httpServletRequest.getRequestURI();
    }

    @Deprecated
    StringBuffer getRequestURL() {
        return httpServletRequest.getRequestURL();
    }

    @Deprecated
    String getServletPath() {
        return httpServletRequest.getServletPath();
    }

    @Deprecated
    HttpSession getSession(boolean b) {
        return httpServletRequest.getSession(b);
    }

    @Deprecated
    HttpSession getSession() {
        return httpServletRequest.getSession();
    }

    @Deprecated
    boolean isRequestedSessionIdValid() {
        return httpServletRequest.isRequestedSessionIdValid();
    }

    @Deprecated
    boolean isRequestedSessionIdFromCookie() {
        return httpServletRequest.isRequestedSessionIdFromCookie();
    }

    @Deprecated
    boolean isRequestedSessionIdFromURL() {
        return httpServletRequest.isRequestedSessionIdFromURL();
    }
}
