package com.sitescape.team.portal.liferay;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.web.bind.RequestUtils;
import com.sitescape.team.portal.AbstractPortalLogin;
import com.sitescape.team.servlet.portal.PortalLoginException;

public class LiferayLogin extends AbstractPortalLogin {

    private static final String SESSION_COOKIE_NAME = "JSESSIONID";

    private static final String ID_COOKIE_NAME = "ID";

    private static final String PASSWORD_COOKIE_NAME = "PASSWORD";

    private static final String LOGIN_PATH = "/c/portal/login";

    private static final String LOGOUT_PATH = "/c/portal/logout";

    private static final String PASSWORD_PATTERN = "name=\".*_password";

    @Override
    protected Cookie[] logIntoPortal(HttpServletRequest request, HttpServletResponse response, HttpClient httpClient, String username, String password, boolean remember) throws Exception {
        GetMethod getMethod = new GetMethod(LOGIN_PATH);
        String body = null;
        int statusCode;
        try {
            statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                logger.warn("Failed to log into portal as " + username + " - " + getMethod.getStatusLine().toString());
                throw new PortalLoginException();
            }
            body = getMethod.getResponseBodyAsString();
        } finally {
            getMethod.releaseConnection();
        }
        String passwordFieldName = getPasswordFieldName(body);
        if (passwordFieldName == null) {
            logger.warn("Failed to log into portal as " + username + " - Cannot obtain password field name");
            throw new PortalLoginException();
        }
        PostMethod postMethod = new PostMethod(LOGIN_PATH);
        String location = null;
        try {
            postMethod.addParameter("cmd", "already-registered");
            if (remember) postMethod.addParameter("rememberMe", "on");
            postMethod.addParameter("login", username);
            postMethod.addParameter(passwordFieldName, password);
            statusCode = httpClient.executeMethod(postMethod);
            if (statusCode != HttpStatus.SC_MOVED_TEMPORARILY) {
                logger.warn("Failed to log into portal as " + username + " - Unexpected status: " + postMethod.getStatusLine().toString());
                throw new PortalLoginException();
            }
            Header locationHeader = postMethod.getResponseHeader("Location");
            if (locationHeader != null) location = locationHeader.getValue();
        } finally {
            postMethod.releaseConnection();
        }
        if (location == null) {
            logger.warn("Failed to log into portal as " + username + " - Location header is missing");
            throw new PortalLoginException();
        }
        getMethod = new GetMethod(location);
        try {
            statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                logger.warn("Failed to log into portal as " + username + " - " + getMethod.getStatusLine().toString());
                throw new PortalLoginException();
            }
        } finally {
            getMethod.releaseConnection();
        }
        return httpClient.getState().getCookies();
    }

    @Override
    protected Cookie[] logOutFromPortal(HttpServletRequest request, HttpServletResponse response, HttpClient httpClient) throws Exception {
        GetMethod getMethod = new GetMethod(LOGOUT_PATH);
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                logger.warn("Failed to log out of portal - " + getMethod.getStatusLine().toString());
                throw new PortalLoginException();
            }
        } finally {
            getMethod.releaseConnection();
        }
        return httpClient.getState().getCookies();
    }

    @Override
    protected Cookie[] touchIntoPortal(HttpServletRequest request, HttpServletResponse response, HttpClient httpClient) throws Exception {
        GetMethod getMethod = new GetMethod(LOGIN_PATH);
        int statusCode;
        try {
            statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                logger.warn("Failed to touch into portal - " + getMethod.getStatusLine().toString());
                throw new PortalLoginException();
            }
        } finally {
            getMethod.releaseConnection();
        }
        return httpClient.getState().getCookies();
    }

    protected String getCookiePath() {
        return "/";
    }

    private String getPasswordFieldName(String body) {
        String passwordFieldName = null;
        if (body != null) {
            Pattern p = Pattern.compile(PASSWORD_PATTERN);
            Matcher m = p.matcher(body);
            boolean b = m.find();
            if (b) {
                passwordFieldName = body.substring(m.start() + 6, m.end());
            }
        }
        return passwordFieldName;
    }
}
