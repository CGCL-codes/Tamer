package com.noelios.restlet.util;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.restlet.data.Cookie;
import org.restlet.data.CookieSetting;
import org.restlet.util.DateUtils;
import com.noelios.restlet.http.HttpUtils;

/**
 * Cookie manipulation utilities.
 * 
 * @author Jerome Louvel (contact@noelios.com)
 */
public class CookieUtils {

    /**
     * Formats a list of cookies as an HTTP header.
     * 
     * @param cookies
     *            The list of cookies to format.
     * @return The HTTP header.
     */
    public static String format(List<Cookie> cookies) {
        StringBuilder sb = new StringBuilder();
        Cookie cookie;
        for (int i = 0; i < cookies.size(); i++) {
            cookie = cookies.get(i);
            if (i == 0) {
                if (cookie.getVersion() > 0) {
                    sb.append("$Version=\"").append(cookie.getVersion()).append("\"; ");
                }
            } else {
                sb.append("; ");
            }
            format(cookie, sb);
        }
        return sb.toString();
    }

    /**
     * Formats a cookie setting.
     * 
     * @param cookieSetting
     *            The cookie setting to format.
     * @return The formatted cookie setting.
     */
    public static String format(CookieSetting cookieSetting) {
        StringBuilder sb = new StringBuilder();
        try {
            format(cookieSetting, sb);
        } catch (IOException e) {
        }
        return sb.toString();
    }

    /**
     * Formats a cookie setting.
     * 
     * @param cookieSetting
     *            The cookie setting to format.
     * @param destination
     *            The appendable destination.
     */
    public static void format(CookieSetting cookieSetting, Appendable destination) throws IOException {
        String name = cookieSetting.getName();
        String value = cookieSetting.getValue();
        int version = cookieSetting.getVersion();
        if ((name == null) || (name.length() == 0)) {
            throw new IllegalArgumentException("Can't write cookie. Invalid name detected");
        } else {
            destination.append(name).append('=');
            if ((value != null) && (value.length() > 0)) {
                appendValue(value, version, destination);
            }
            if (version > 0) {
                destination.append("; Version=");
                appendValue(Integer.toString(version), version, destination);
            }
            String path = cookieSetting.getPath();
            if ((path != null) && (path.length() > 0)) {
                destination.append("; Path=");
                if (version == 0) {
                    destination.append(path);
                } else {
                    HttpUtils.appendQuote(path, destination);
                }
            }
            int maxAge = cookieSetting.getMaxAge();
            if (maxAge >= 0) {
                if (version == 0) {
                    long currentTime = System.currentTimeMillis();
                    long maxTime = ((long) maxAge * 1000L);
                    long expiresTime = currentTime + maxTime;
                    Date expires = new Date(expiresTime);
                    destination.append("; Expires=");
                    appendValue(DateUtils.format(expires, DateUtils.FORMAT_RFC_1036.get(0)), version, destination);
                } else {
                    destination.append("; Max-Age=");
                    appendValue(Integer.toString(cookieSetting.getMaxAge()), version, destination);
                }
            } else if ((maxAge == -1) && (version > 0)) {
                destination.append("; Discard");
            } else {
            }
            String domain = cookieSetting.getDomain();
            if ((domain != null) && (domain.length() > 0)) {
                destination.append("; Domain=");
                appendValue(domain.toLowerCase(), version, destination);
            }
            if (cookieSetting.isSecure()) {
                destination.append("; Secure");
            }
            if (version > 0) {
                String comment = cookieSetting.getComment();
                if ((comment != null) && (comment.length() > 0)) {
                    destination.append("; Comment=");
                    appendValue(comment, version, destination);
                }
            }
        }
    }

    /**
     * Formats a cookie.
     * 
     * @param cookie
     *            The cookie to format.
     * @return The formatted cookie.
     */
    public static String format(Cookie cookie) {
        StringBuilder sb = new StringBuilder();
        format(cookie, sb);
        return sb.toString();
    }

    /**
     * Formats a cookie setting.
     * 
     * @param cookie
     *            The cookie to format.
     * @param destination
     *            The appendable destination.
     */
    public static void format(Cookie cookie, Appendable destination) {
        String name = cookie.getName();
        String value = cookie.getValue();
        int version = cookie.getVersion();
        if ((name == null) || (name.length() == 0)) {
            throw new IllegalArgumentException("Can't write cookie. Invalid name detected");
        } else {
            try {
                appendValue(name, 0, destination).append('=');
                if ((value != null) && (value.length() > 0)) {
                    appendValue(value, version, destination);
                }
                if (version > 0) {
                    String path = cookie.getPath();
                    if ((path != null) && (path.length() > 0)) {
                        destination.append("; $Path=");
                        HttpUtils.appendQuote(path, destination);
                    }
                    String domain = cookie.getDomain();
                    if ((domain != null) && (domain.length() > 0)) {
                        destination.append("; $Domain=");
                        HttpUtils.appendQuote(domain, destination);
                    }
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * Appends a source string as an HTTP comment.
     * 
     * @param value
     *            The source string to format.
     * @param version
     *            The cookie version.
     * @param destination
     *            The appendable destination.
     * @throws IOException
     */
    private static Appendable appendValue(CharSequence value, int version, Appendable destination) throws IOException {
        if (version == 0) {
            destination.append(value.toString());
        } else {
            HttpUtils.appendQuote(value, destination);
        }
        return destination;
    }

    /**
     * Gets the cookies whose name is a key in the given map. If a matching
     * cookie is found, its value is put in the map.
     * 
     * @param source
     *            The source list of cookies.
     * @param destination
     *            The cookies map controlling the reading.
     */
    public static void getCookies(List<Cookie> source, Map<String, Cookie> destination) {
        Cookie cookie;
        for (Iterator<Cookie> iter = source.iterator(); iter.hasNext(); ) {
            cookie = iter.next();
            if (destination.containsKey(cookie.getName())) {
                destination.put(cookie.getName(), cookie);
            }
        }
    }
}
