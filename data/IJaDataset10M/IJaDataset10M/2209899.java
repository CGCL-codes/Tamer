package com.bradmcevoy.http;

import javax.servlet.http.HttpServletRequest;

public class MiltonUtils {

    public static String stripContext(HttpServletRequest req) {
        String s = req.getRequestURI();
        String contextPath = req.getContextPath();
        s = s.replaceFirst(contextPath, "");
        return s;
    }
}
