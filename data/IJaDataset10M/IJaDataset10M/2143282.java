package com.jcorporate.expresso.core.controller;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Extended the Struts' <code>RequestUtils</code> to add additional
 * utility methods.
 * @author Max Cooper, Steve Ditlinger,Prakash Malani eBuilt Inc.
 */
public class SecureRequestUtils {

    private static final String HTTP = "http";

    private static final String HTTPS = "https";

    private static final String STD_HTTP_PORT = "80";

    private static final String STD_HTTPS_PORT = "443";

    private static final String STOWED_REQUEST_ATTRIBS = "ssl.redirect.attrib.stowed";

    /**
     * Builds the protocol, server name, and port portion of the new URL
     * @param request The current request
     * @param desiredScheme  The scheme (http or https) to be used in the new URL
     * @param desiredPort The port number to be used in th enew URL
     * @return The new URL as a StringBuffer
     * @keep
     */
    private static StringBuffer startNewUrlString(HttpServletRequest request, String desiredScheme, String desiredPort) {
        StringBuffer url = new StringBuffer(128);
        String serverName = request.getServerName();
        url.append(desiredScheme).append("://").append(serverName);
        if ((HTTP.equals(desiredScheme) && !STD_HTTP_PORT.equals(desiredPort)) || (HTTPS.equals(desiredScheme) && !STD_HTTPS_PORT.equals(desiredPort))) {
            url.append(":").append(desiredPort);
        }
        return url;
    }

    /**
     * Creates query String from request body parameters
     * @param aRequest The current request
     * @return The created query string (with no leading "?")
     */
    public static String getRequestParameters(HttpServletRequest aRequest) {
        Map m = aRequest.getParameterMap();
        return createQueryStringFromMap(m, "&").toString();
    }

    /**
     * Builds a query string from a given map of parameters
     * @param m A map of parameters
     * @param ampersand String to use for ampersands (e.g. "&" or "&amp;" )
     * @return query string (with no leading "?")
     */
    public static StringBuffer createQueryStringFromMap(Map m, String ampersand) {
        StringBuffer aReturn = new StringBuffer(128);
        Set aEntryS = m.entrySet();
        Iterator aEntryI = aEntryS.iterator();
        while (aEntryI.hasNext()) {
            Map.Entry aEntry = (Map.Entry) aEntryI.next();
            Object value = aEntry.getValue();
            String[] aValues = new String[1];
            if (value == null) {
                aValues[0] = "";
            } else if (value instanceof List) {
                List aList = (List) value;
                aValues = (String[]) aList.toArray(new String[aList.size()]);
            } else if (value instanceof String) {
                aValues[0] = (String) value;
            } else {
                aValues = (String[]) value;
            }
            for (int i = 0; i < aValues.length; i++) {
                append(aEntry.getKey(), aValues[i], aReturn, ampersand);
            }
        }
        return aReturn;
    }

    /**
     * Appends new key and value pair to query string
     * @param key parameter name
     * @param value value of parameter
     * @param queryString existing query string
     * @param ampersand string to use for ampersand (e.g. "&" or "&amp;")
     * @return query string (with no leading "?")
     */
    private static StringBuffer append(Object key, Object value, StringBuffer queryString, String ampersand) {
        if (queryString.length() > 0) {
            queryString.append(ampersand);
        }
        queryString.append(URLEncoder.encode(key.toString()));
        queryString.append("=");
        queryString.append(URLEncoder.encode(value.toString()));
        return queryString;
    }

    /**
     * Stores request attributes in session
     * @param aRequest The current request
     * @return true, if the attributes were stowed in the session,
     * false otherwise
     * @keep
     */
    public static boolean stowRequestAttributes(HttpServletRequest aRequest) {
        if (aRequest.getSession().getAttribute(STOWED_REQUEST_ATTRIBS) != null) {
            return false;
        }
        Enumeration enum_ = aRequest.getAttributeNames();
        Map map = new HashMap();
        while (enum_.hasMoreElements()) {
            String name = (String) enum_.nextElement();
            map.put(name, aRequest.getAttribute(name));
        }
        aRequest.getSession().setAttribute(STOWED_REQUEST_ATTRIBS, map);
        return true;
    }

    /**
     * Reclaims request attributes from session to request
     * @param aRequest The current request
     * @param doRemove True, if the attributes should be removed after being reclaimed,
     * false otherwise
     * @keep
     */
    public static void reclaimRequestAttributes(HttpServletRequest aRequest, boolean doRemove) {
        Map map = (Map) aRequest.getSession().getAttribute(STOWED_REQUEST_ATTRIBS);
        if (map == null) {
            return;
        }
        Iterator itr = map.keySet().iterator();
        while (itr.hasNext()) {
            String name = (String) itr.next();
            aRequest.setAttribute(name, map.get(name));
        }
        if (doRemove) {
            aRequest.getSession().removeAttribute(STOWED_REQUEST_ATTRIBS);
        }
    }

    /**
     * Creates a redirect URL string if the current request should be redirected
     * @param request current servlet request
     * @param httpPort the http port used by the web application
     * @param httpsPort the https port used by the web application
     * @param isSecure True if the current request should be transmitted via SSL
     * @return the URL to redirect to
     */
    public static String getRedirectString(HttpServletRequest request, String httpPort, String httpsPort, boolean isSecure) {
        String desiredScheme = isSecure ? HTTPS : HTTP;
        String usingScheme = request.getScheme();
        String desiredPort = isSecure ? httpsPort : httpPort;
        String usingPort = String.valueOf(request.getServerPort());
        String urlString = null;
        if (!desiredScheme.equals(usingScheme) || !desiredPort.equals(usingPort)) {
            urlString = buildNewUrlString(request, desiredScheme, usingScheme, desiredPort, usingPort);
            if (!SecureRequestUtils.stowRequestAttributes(request)) {
                SecureRequestUtils.reclaimRequestAttributes(request, false);
            }
        } else {
            SecureRequestUtils.reclaimRequestAttributes(request, true);
        }
        return urlString;
    }

    /**
     * Builds the URL that we will redirect to
     * @param request The current request
     * @param desiredScheme The protocol (http or https) we wish to use in new URL
     * @param usingScheme The scheme we used in the current request
     * @param desiredPort The port number we wish to use in new URL
     * @param usingPort The port number we used in the current request
     * @return the URL we will redirect to, as a String
     * @keep
     */
    private static String buildNewUrlString(HttpServletRequest request, String desiredScheme, String usingScheme, String desiredPort, String usingPort) {
        StringBuffer url = startNewUrlString(request, desiredScheme, desiredPort);
        url.append(request.getRequestURI());
        return addQueryString(request, url);
    }

    /**
     * Adds the query string, if any, to the given URL.  The query string
     * is either taken from the existing query string or
     * generated from the posting request body parameters.
     * @param request The current request
     * @param url The existing URL we will add the query string to
     * @return The URL with query string
     * @keep
     */
    private static String addQueryString(HttpServletRequest request, StringBuffer url) {
        String queryString = request.getQueryString();
        if (queryString != null && queryString.length() != 0) {
            url.append("?" + queryString);
        } else {
            queryString = SecureRequestUtils.getRequestParameters(request);
            if (queryString != null && queryString.length() != 0) {
                url.append("?" + queryString);
            }
        }
        return url.toString();
    }
}
