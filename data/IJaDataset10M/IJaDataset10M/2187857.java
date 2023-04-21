package com.extentech.luminet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHandler extends java.util.Hashtable {

    Cookie cookies[];

    HttpServletResponse response;

    public CookieHandler(HttpServletRequest req, HttpServletResponse res) {
        response = res;
        if ((cookies = req.getCookies()) != null) {
            for (int i = 0; i < cookies.length; i++) {
                try {
                    String cName = cookies[i].getName();
                    String cVal = cookies[i].getValue();
                    this.put(cName, cVal);
                } catch (Exception e) {
                    System.err.println("CookieHandler init exception: " + e);
                }
            }
        }
    }

    /** set a cookie value
     */
    public void setVal(String name, String val, int age) {
        Cookie ck = new Cookie(name, val);
        ck.setMaxAge(age);
        response.addCookie(ck);
        this.put(name, val);
    }

    /** set a cookie value
    */
    public void setVal(String name, String val) {
        response.addCookie(new Cookie(name, val));
        this.put(name, val);
    }

    /** get a cookie value
    */
    public String getVal(String name) {
        String retval = null;
        if (this.get(name) != null) {
            retval = (String) this.get(name);
        }
        return retval;
    }
}
