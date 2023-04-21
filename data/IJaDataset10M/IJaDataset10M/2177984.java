package hambo.app.util;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.*;
import hambo.config.Config;
import hambo.config.ConfigManager;
import hambo.svc.log.*;
import hambo.user.User;
import hambo.user.UserManager;
import hambo.util.JCrypt;

/**
 * Utility class that has methods for handling cookies.
 *
 */
public class CookieUtil {

    private static final boolean DEBUG = false;

    /** The default maximum age is set to 60 days (in seconds). */
    public static final int DEFAULT_AGE = (int) (60 * 24 * 60 * 60);

    /** The age to set when you want the cookie killed. */
    public static final int DIE = 0;

    /** The name of the session cookie */
    public static String SESSION_COOKIE_NAME = "JSESSIONID";

    /** The name of the auto-login cookie */
    public static String AUTOLOGIN_COOKIE_NAME = "hambo-login";

    /** The name of the country cookie */
    public static String COUNTRY_COOKIE_NAME = "hambo-country";

    /** The path that is set in the country and auto-login cookies. */
    private static String COOKIE_PATH = "/";

    /** Cookie value delimiter */
    private static final String DELIMITER = ":";

    /** A log instance used for debugging. */
    private static Log log = new Log() {

        public void println(int level, String msg) {
        }

        public void println(int level, String msg, Throwable exception) {
        }

        public boolean isEnabled(int level) {
            return false;
        }
    };

    /**
     * Find a cookie in the HTTP request object.
     * @param request the HTTP request.
     * @param name    the name of the cookie
     */
    public static Cookie findCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) for (int i = 0; i < cookies.length; i++) if (cookies[i].getName().equalsIgnoreCase(name)) return cookies[i];
        return null;
    }

    /**
     * Sets a cookie in the HTTP response object.
     * @param response the HTTP response to add the cookie to.
     * @param cookie   the cookie to add.
     */
    public static void setCookie(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }

    /**
     * Sets a cookie in the response with the specified name, value and age.
     * @param response the HTTP response to add the cookie to.
     * @param name  the name of the cookie.
     * @param value the value of the cookie.
     * @param age   the maximum age of the cookie specified in seconds.
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int age) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(age);
        setCookie(response, cookie);
    }

    /**
     * Deletes a cookie in the response with the specified name.
     * @param response the HTTP response to delete the cookie from.
     * @param name     the name of the cookie.
     */
    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(DIE);
        setCookie(response, cookie);
    }

    /**
     * Get the expiry date in &quot;cookie format&quot;.
     * @param age the maximum age of the cookie specified in seconds.
     */
    public static String getExpiryDate(int age) {
        TimeZone zone = TimeZone.getTimeZone("GMT");
        Locale loc = Locale.US;
        Calendar cal = new GregorianCalendar(zone, loc);
        cal.setTime(new Date(System.currentTimeMillis() + (age * 1000)));
        String datePattern = "EEE, dd MMM yyyyy HH:mm:ss z";
        SimpleDateFormat df = new SimpleDateFormat(datePattern, loc);
        df.setTimeZone(zone);
        return df.format(cal.getTime());
    }

    /**
     * Checks if the client device supports cookies <i>and</i> if the device has
     * cookies <i>activated</i>.
     */
    public static boolean hasCookiesActivated(HttpServletRequest request) {
        return findCookie(request, SESSION_COOKIE_NAME) != null;
    }

    /**
     * Set a Hambo country cookie in the HTTP response object.
     * @param response the HTTP response to add the country cookie to.
     * @param country  the country.
     */
    public static void setCountryCookie(HttpServletResponse response, String country) {
        if (country == null || country.equals("")) return;
        Cookie cookie = new Cookie(COUNTRY_COOKIE_NAME, country);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(DEFAULT_AGE);
        setCookie(response, cookie);
    }

    /**
     * Tries to find a Hambo auto-login cookie in the HTTP request object.
     * @param response the HTTP request to search for the cookie in.
     * @return the username found in the auto-login cookie. Null if no 
     *         cookie was found.
     */
    public static String getUserIdFromAutoLoginCookie(HttpServletRequest request) {
        Cookie foundKey = findCookie(request, AUTOLOGIN_COOKIE_NAME);
        String userId = null;
        String cipher = null;
        boolean valid = false;
        log.println(Log.DEBUG3, "Trying to find auto-login cookie: " + foundKey);
        if (foundKey != null) {
            userId = getUserIdFromKey(foundKey.getValue());
            cipher = getCipherFromKey(foundKey.getValue());
            if (userId != null && cipher != null) if (validate(userId, cipher)) valid = true;
        }
        return valid ? userId : null;
    }

    /**
     * Sets a Hambo auto-login cookie in the HTTP response object.
     * @param response the HTTP response to add the cookie to.
     * @param userId   the user's username.
     * @param password the user's password.
     */
    public static void setAutoLoginCookie(HttpServletResponse response, String userId, String password) {
        String value = userId + DELIMITER + JCrypt.crypt("SC", password);
        Cookie cookie = new Cookie(AUTOLOGIN_COOKIE_NAME, value);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(DEFAULT_AGE);
        setCookie(response, cookie);
    }

    /**
     * Deletes a Hambo auto-login cookie from the HTTP response object.
     * @param response the HTTP response to delete the cookie from.
     */
    public static void deleteAutoLoginCookie(HttpServletResponse response) {
        deleteCookie(response, AUTOLOGIN_COOKIE_NAME);
    }

    /**
     * Validate a username/cipher pair.
     */
    private static boolean validate(String userId, String key) {
        boolean isValid = false;
        try {
            UserManager userManager = UserManager.getUserManager();
            User user = userManager.findUser(userId);
            String encryptedPassword = user.getAccountInfo().getPassword();
            isValid = key.equals(JCrypt.crypt("SC", encryptedPassword));
        } catch (NullPointerException e) {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isValid;
    }

    private static String getUserIdFromKey(String cookieValue) {
        String userId = null;
        int index = cookieValue.indexOf(DELIMITER);
        if (index > -1) userId = cookieValue.substring(0, index);
        return userId;
    }

    private static String getCipherFromKey(String cookieValue) {
        String cipher = null;
        int index = cookieValue.indexOf(DELIMITER);
        if (index > -1 && index + 1 < cookieValue.length()) cipher = cookieValue.substring(index + 1);
        return cipher;
    }

    static {
        log = LogServiceManager.getLog("CookieUtil");
        try {
            Config config = ConfigManager.getConfig("cookie");
            String temp = config.getProperty("session_name");
            if (temp != null) SESSION_COOKIE_NAME = temp;
            temp = config.getProperty("autologin_name");
            if (temp != null) AUTOLOGIN_COOKIE_NAME = temp;
            temp = config.getProperty("country_name");
            if (temp != null) COUNTRY_COOKIE_NAME = temp;
            temp = ConfigManager.getConfig("server").getProperty("contextpath");
            if (temp != null) {
                temp = temp.trim();
                if (!temp.endsWith("/")) temp = temp + '/';
                COOKIE_PATH = temp;
            }
        } catch (Exception ex) {
            log.println(Log.ERROR, "hambo.app.util.CookeiUtil: could " + "not get context path from configuration file.", ex);
        }
    }
}
