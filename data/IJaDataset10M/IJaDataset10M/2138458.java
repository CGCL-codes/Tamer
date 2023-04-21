package com.feyaSoft.plugin.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * @author fzhuang
 * 
 * @Created at Nov 21, 2006
 * @Updated at Nov 21, 2006
 * 
 * Mobile.java is used to
 */
public class Mobile {

    public static boolean isMobile(HttpServletRequest request) {
        String userAccept = request.getHeader("accept");
        if (userAccept.matches(".*?text/vnd\\.wap\\.wml.*?")) {
            return true;
        } else if (userAccept.matches(".*?text/html.*?")) {
            return false;
        }
        String userAgent = request.getHeader("user-agent");
        String mobileRegExp = "";
        Pattern pattern = Pattern.compile(mobileRegExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userAgent);
        if (matcher.find()) {
            return false;
        }
        return true;
    }
}
