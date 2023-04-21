package com.hyper9.vangaea.server;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import com.hyper9.vangaea.impls.beans.admin.AuthTokenBeanImpl;
import com.hyper9.vangaea.types.beans.admin.AuthTokenBean;

public class VangaeaUtil {

    /**
     * The system property prefix. This cannot be changed without also changed
     * the prefix for system properties in the properties file and the database.
     */
    private static String SYS_PROP_PREFIX = "com.hyper9.vangaea";

    /**
     * The name of the login context.
     */
    public static String LOGIN_CONTEXT_NAME = getSystemProperty("jaas.loginContext", "Vangaea");

    /**
     * The prefix for Cookie and POST parameters.
     */
    public static String PARAM_PREFIX = getSystemProperty("rest.params.prefix", "com.hyper9.vangaea.rest.params");

    /**
     * The name of the authentication token parameter.
     */
    public static String AUTH_TOKEN_PARAM_NAME = getSystemProperty("rest.params.keys.authToken", "authtoken");

    /**
     * The name of the user name parameter.
     */
    public static String USERNAME_PARAM_NAME = getSystemProperty("rest.params.keys.userName", "username");

    /**
     * The name of the password parameter.
     */
    public static String PASSWORD_PARAM_NAME = getSystemProperty("rest.params.keys.password", "password");

    /**
     * The name of the encryption/decryption key.
     */
    public static String ENCRYPTION_KEY_PARAM_NAME = getSystemProperty("rest.params.keys.encDecKey", "encdeckey");

    public static String getParamValue(HttpHeaders headers, String paramName) {
        paramName = PARAM_PREFIX + "." + paramName;
        if (headers.getCookies().containsKey(paramName)) {
            return headers.getCookies().get(paramName).getValue();
        } else {
            return null;
        }
    }

    public static String getParamValue(UriInfo uriInfo, String paramName) {
        if (uriInfo.getQueryParameters().containsKey(paramName)) {
            return uriInfo.getQueryParameters().getFirst(paramName);
        } else {
            return null;
        }
    }

    public static String getParamValue(HttpHeaders headers, UriInfo uriInfo, String paramName) {
        String val = getParamValue(headers, paramName);
        if (val != null) {
            return val;
        }
        return getParamValue(uriInfo, paramName);
    }

    public static String getParamValue(UriInfo uriInfo, HttpHeaders headers, String paramName) {
        String val = getParamValue(uriInfo, paramName);
        if (val != null) {
            return val;
        }
        return getParamValue(headers, paramName);
    }

    public static String getCookieValue(HttpHeaders headers, String paramName) {
        paramName = PARAM_PREFIX + "." + paramName;
        if (headers.getCookies().containsKey(paramName)) {
            return headers.getCookies().get(paramName).getValue();
        } else {
            return null;
        }
    }

    public static String getParamValue(ServletRequest request, String paramName) {
        String prefixPlusParamName = PARAM_PREFIX + "." + paramName;
        String paramVal = null;
        paramVal = request.getParameter(prefixPlusParamName);
        if (paramVal != null) {
            return paramVal;
        }
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(prefixPlusParamName)) {
                    paramVal = c.getValue();
                    break;
                }
            }
        }
        if (paramVal != null) {
            return paramVal;
        }
        paramVal = request.getParameter(paramName);
        return paramVal;
    }

    public static AuthTokenBean getAuthToken(ServletRequest request) {
        String acv = getParamValue(request, AUTH_TOKEN_PARAM_NAME);
        if (acv == null) {
            return null;
        }
        AuthTokenBean bean = AuthTokenBeanImpl.getByCookieValue(acv);
        return bean;
    }

    public static AuthTokenBean getAuthToken(HttpHeaders headers) {
        String acv = getCookieValue(headers, AUTH_TOKEN_PARAM_NAME);
        if (acv == null) {
            return null;
        }
        AuthTokenBean bean = AuthTokenBeanImpl.getByCookieValue(acv);
        return bean;
    }

    /**
     * Gets a system property value.
     * 
     * @param key The property's key.
     * @param defaultValue The default value to assign if the property's value
     *        is not present in the backing store.
     * @return A system property value.
     */
    public static String getSystemProperty(String key, String defaultValue) {
        key = SYS_PROP_PREFIX + "." + key;
        if (InitApplicationServlet.getProperties().containsKey(key)) {
            return InitApplicationServlet.getProperties().getProperty(key);
        }
        return defaultValue;
    }
}
