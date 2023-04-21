package com.googlecode.webduff.util;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

public final class ServletUtilities {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ServletUtilities.class);

    @SuppressWarnings("unchecked")
    public static void logRequest(HttpServletRequest aRequest) {
        Enumeration<String> theNames = aRequest.getHeaderNames();
        while (theNames.hasMoreElements()) {
            String aName = theNames.nextElement();
            log.trace(aName + " = " + aRequest.getHeader(aName));
        }
    }
}
