package org.bibop.xml.xforge.helpers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.ServletRequest;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.io.Serializable;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Locale;
import javax.servlet.ServletInputStream;

/**
 *
 * @author <a href="a.garoffolo@bibop.it">Alberto Garoffolo</a>
 * @version 1.0
 */
public class SerializableHttpServletRequest extends SerializableServletRequest implements HttpServletRequest {

    private SerializableServletInputStream sis;

    private String authType;

    private String method;

    private String pathInfo;

    private String pathTranslated;

    private String contextPath;

    private String queryString;

    private String remoteUser;

    private String requestSessionId;

    private String requestURI;

    private String servletPath;

    private String requestedSessionId;

    private boolean requestedSessionIdValid;

    private boolean requestedSessionIdFromCookie;

    private boolean requestedSessionIdFromURL;

    private boolean requestedSessionIdFromUrl;

    public SerializableHttpServletRequest(HttpServletRequest req) {
        super((ServletRequest) req);
        try {
            String type = null;
            String type1 = req.getHeader("Content-Type");
            String type2 = req.getContentType();
            if (type1 == null && type2 != null) {
                type = type2;
            } else if (type2 == null && type1 != null) {
                type = type1;
            } else if (type1 != null && type2 != null) {
                type = (type1.length() > type2.length() ? type1 : type2);
            }
            if (type == null || !type.toLowerCase().startsWith("multipart/form-data")) {
                this.sis = new SerializableServletInputStream(req.getInputStream(), 0);
            } else {
                this.sis = new SerializableServletInputStream(req.getInputStream(), req.getContentLength());
            }
        } catch (IOException ex) {
            this.sis = null;
        }
        this.authType = req.getAuthType();
        this.method = req.getMethod();
        this.pathInfo = req.getPathInfo();
        this.pathTranslated = req.getPathTranslated();
        try {
            this.contextPath = req.getContextPath();
        } catch (java.lang.NoSuchMethodError nsme) {
            this.contextPath = null;
        }
        this.queryString = req.getQueryString();
        this.remoteUser = req.getRemoteUser();
        this.requestedSessionId = req.getRequestedSessionId();
        this.requestURI = req.getRequestURI();
        this.servletPath = req.getServletPath();
        this.requestedSessionIdValid = req.isRequestedSessionIdValid();
        this.requestedSessionIdFromCookie = req.isRequestedSessionIdFromCookie();
        try {
            this.requestedSessionIdFromURL = req.isRequestedSessionIdFromURL();
        } catch (java.lang.NoSuchMethodError nsme) {
            this.contextPath = null;
        }
        this.requestedSessionIdFromUrl = req.isRequestedSessionIdFromUrl();
    }

    public String getAuthType() {
        return this.authType;
    }

    public Cookie[] getCookies() {
        return null;
    }

    public long getDateHeader(String p0) {
        return 0;
    }

    public String getHeader(String p0) {
        return null;
    }

    public Enumeration getHeaders(String p0) {
        return null;
    }

    public Enumeration getHeaderNames() {
        return null;
    }

    public int getIntHeader(String p0) {
        return -1;
    }

    public String getMethod() {
        return this.method;
    }

    public String getPathInfo() {
        return this.pathInfo;
    }

    public String getPathTranslated() {
        return this.pathTranslated;
    }

    public String getContextPath() {
        return this.contextPath;
    }

    public String getQueryString() {
        return this.queryString;
    }

    public String getRemoteUser() {
        return this.remoteUser;
    }

    public boolean isUserInRole(String p0) {
        return false;
    }

    public Principal getUserPrincipal() {
        return null;
    }

    public String getRequestedSessionId() {
        return this.requestedSessionId;
    }

    public String getRequestURI() {
        return this.requestURI;
    }

    public String getServletPath() {
        return this.servletPath;
    }

    public HttpSession getSession(boolean p0) {
        return null;
    }

    public HttpSession getSession() {
        return null;
    }

    public boolean isRequestedSessionIdValid() {
        return this.requestedSessionIdValid;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return this.requestedSessionIdFromCookie;
    }

    public boolean isRequestedSessionIdFromURL() {
        return this.requestedSessionIdFromURL;
    }

    public boolean isRequestedSessionIdFromUrl() {
        return this.requestedSessionIdFromUrl;
    }

    public ServletInputStream getInputStream() throws IOException {
        return this.sis;
    }
}
