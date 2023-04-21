package com.avaje.ebeaninternal.server.net;

import java.net.URLConnection;
import java.util.*;

/**
 * A list of HttpCookies.
 */
public class HttpCookieList {

    private Hashtable<String, HttpCookie> cookieMap = new Hashtable<String, HttpCookie>();

    HttpCookieDateParser dateParser = new HttpCookieDateParser();

    /**
	 * Read the cookies from the urlConnection.
	 */
    public void getCookiesFromConnection(URLConnection urlconnection) {
        String headerKey;
        for (int i = 1; (headerKey = urlconnection.getHeaderFieldKey(i)) != null; i++) {
            if (headerKey.equalsIgnoreCase("set-cookie")) {
                String rawCookieHeader = urlconnection.getHeaderField(i);
                HttpCookie cookie = new HttpCookie(rawCookieHeader, dateParser);
                cookieMap.put(cookie.getName(), cookie);
            }
        }
    }

    /**
	 * Set the cookies to the urlConnection.
	 */
    public void setCookiesToConnection(URLConnection urlconnection) {
        if (cookieMap.size() < 1) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        Enumeration<HttpCookie> e = cookieMap.elements();
        while (e.hasMoreElements()) {
            HttpCookie cookie = (HttpCookie) e.nextElement();
            if (first) {
                first = false;
            } else {
                sb.append("; ");
            }
            sb.append(cookie.getNameValue());
        }
        urlconnection.setRequestProperty("Cookie", sb.toString());
    }
}
