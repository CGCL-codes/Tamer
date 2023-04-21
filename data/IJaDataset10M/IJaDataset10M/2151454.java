package com.zhongkai.tools;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OpCookies {

    public OpCookies() {
    }

    /**
 * 
 * @param CookiesName
 * @param CookiesValue
 * @param MaxAge
 * @param response
 * @return
 */
    public static String setCookies(String CookiesName, String CookiesValue, int MaxAge, HttpServletResponse response) {
        return setCookies(CookiesName, CookiesValue, MaxAge, "/", response);
    }

    /**
  *
  * @param CookiesName CookiesName
  * @param CookiesValue CookiesValue
  * @param MaxAge MaxAge
  * @param path Path
  * @param response HttpSerlvetResponse
  * @return CookiesValue
  */
    public static String setCookies(String CookiesName, String CookiesValue, int MaxAge, String path, HttpServletResponse response) {
        return setCookies(CookiesName, CookiesValue, MaxAge, path, "", response);
    }

    /**
 * 
 * @param CookiesName
 * @param CookiesValue
 * @param MaxAge
 * @param path
 * @param domain
 * @param response
 * @return
 */
    public static String setCookies(String CookiesName, String CookiesValue, int MaxAge, String path, String domain, HttpServletResponse response) {
        if (CookiesName == null) return null;
        if (MaxAge <= 0) MaxAge = 3600;
        if (path == null || path.length() == 0) path = "/";
        if (response == null) return null;
        Cookie nameCookie = new Cookie(CookiesName, CookiesValue);
        nameCookie.setMaxAge(MaxAge);
        nameCookie.setPath(path);
        nameCookie.setDomain(domain);
        nameCookie.setSecure(false);
        response.addCookie(nameCookie);
        return CookiesValue;
    }

    /**
 * 
 * @param CookiesName
 * @param path
 * @param response
 */
    public static void Del_Cookies(String CookiesName, String path, HttpServletResponse response) {
        if (CookiesName == null) return;
        if (path == null && path.length() < 0) path = "/";
        if (response == null) return;
        Cookie nameCookie = new Cookie(CookiesName, "");
        nameCookie.setMaxAge(0);
        nameCookie.setPath(path);
        response.addCookie(nameCookie);
    }

    /**
 * 
 * @param CookieName
 * @param request
 * @return
 */
    public static String getCookies(String CookieName, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String CookiesValue = null;
        String cookieName = null;
        Cookie cookie = null;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                cookieName = cookie.getName();
                if (cookieName.equals(CookieName)) {
                    CookiesValue = cookie.getValue();
                }
            }
        }
        return CookiesValue;
    }
}
