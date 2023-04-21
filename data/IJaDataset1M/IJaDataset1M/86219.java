package com.jspx.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.util.Date;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User:chenYuan (mail:cayurain@21cn.com)
 * Date: 2007-3-12
 * Time: 10:07:36
 *
 */
public final class CookieUtil {

    private CookieUtil() {
    }

    public static boolean testCookie(HttpServletRequest request, HttpServletResponse response) {
        try {
            setCookie(response, "test", "jspx.net");
            String test = getCookieString(request, "test", "");
            if (StringUtil.isNULL(test)) return false;
            if (test.equals("test")) return true;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void cookieClear(HttpServletRequest request) {
        Cookie cookieList[] = request.getCookies();
        if (cookieList == null) return;
        for (Cookie aCookieList : cookieList) {
            aCookieList.setMaxAge(1);
        }
    }

    public static String getCookieString(HttpServletRequest request, String cookieName, String defaultValue) {
        Cookie cookieList[] = request.getCookies();
        if (cookieList == null || cookieName == null) return "";
        String result = null;
        for (Cookie aCookieList : cookieList) {
            if (aCookieList.getName().equalsIgnoreCase(cookieName)) result = aCookieList.getValue();
        }
        if (result == null) return defaultValue; else return result;
    }

    public static int getCookieInt(HttpServletRequest request, String cookieName, int defaultValue) {
        return StringUtil.toInt(getCookieString(request, cookieName, "0"), defaultValue);
    }

    public static Date getCookieDate(HttpServletRequest request, String cookieName) {
        String stmp = getCookieString(request, cookieName, "");
        if (StringUtil.isNULL(stmp)) return new Date();
        try {
            return StringUtil.toDate(stmp);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static boolean getCookieBoolean(HttpServletRequest request, String cookieName) {
        return StringUtil.toBoolean(getCookieString(request, cookieName, ""));
    }

    public static void setCookieBoolean(HttpServletResponse response, String cookieName, boolean cookieValue) {
        Cookie cookie = new Cookie(cookieName, BooleanUtil.toString(cookieValue));
        response.addCookie(cookie);
    }

    public static void setCookieDate(HttpServletResponse response, String cookieName, Date cookieValue) {
        Cookie cookie = new Cookie(cookieName, DateUtil.Format(DateUtil.FULL_ST_FORMAT, cookieValue));
        response.addCookie(cookie);
    }

    public static void setCookie(HttpServletResponse response, String cookieName, String cookieValue) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        response.addCookie(cookie);
    }

    public static void setCookie(HttpServletResponse response, String cookieName, int cookieValue) {
        Cookie cookie = new Cookie(cookieName, NumberUtil.toString(cookieValue));
        response.addCookie(cookie);
    }

    public static void setCookie(HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(cookieMaxage);
        response.addCookie(cookie);
    }

    public static void setCookie(HttpServletResponse response, String cookieName, int[] cookieValue) {
        for (int i = 0; i < cookieValue.length; i++) {
            Cookie cookie = new Cookie(cookieName + i, NumberUtil.toString(cookieValue[i]));
            response.addCookie(cookie);
        }
    }

    public static void setCookie(HttpServletResponse response, String cookieName, String[] cookieValue) {
        for (int i = 0; i < cookieValue.length; i++) {
            Cookie cookie = new Cookie(cookieName + i, cookieValue[i]);
            response.addCookie(cookie);
        }
    }

    public static String cookieToString(HttpServletRequest request) {
        Cookie cookieList[] = request.getCookies();
        StringBuffer sb = new StringBuffer();
        if (cookieList == null) return "";
        for (Cookie aCookieList : cookieList) {
            if (aCookieList == null) continue;
            sb.append(aCookieList.getName()).append("=").append(aCookieList.getValue()).append("<br/>");
        }
        return sb.toString();
    }
}
