package org.archive.crawler.admin.ui;

import javax.servlet.http.Cookie;

/**
 * Utility methods for accessing cookies.
 * Used by the JSP UI pages.
 * @author stack
 * @version $Date: 2004-10-21 01:34:37 +0000 (Thu, 21 Oct 2004) $, $Revision: 2705 $
 */
public class CookieUtils {

    public static String getCookieValue(Cookie[] cookies, String cookieName, String defaultValue) {
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookieName.equals(cookie.getName())) {
                    return (cookie.getValue());
                }
            }
        }
        return (defaultValue);
    }
}
