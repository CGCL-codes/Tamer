package org.jmage.util;

import javax.servlet.http.HttpServletRequest;

/**
 * UserAgentUtil detects Browser and OS
 */
public class UserAgentUtil {

    private static final String USER_AGENT = "user-agent";

    private static final String USER_AGENT_INICAPS = "User-Agent";

    private static final String WINDOWS = "windows";

    private static final String INTERNET_EXPLORER_5 = "msie 5.";

    private static final String INTERNET_EXPLORER_6 = "msie 6.";

    public boolean detectWindows(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        return (this.detectWindows(request.getHeader(USER_AGENT)) || (this.detectWindows(request.getHeader(USER_AGENT_INICAPS))));
    }

    public boolean detectWindows(String userAgent) {
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            return userAgent.indexOf(WINDOWS) > -1;
        } else {
            return false;
        }
    }

    public boolean detectInternetExplorer5Or6(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        return (this.detectInternetExplorer5Or6(request.getHeader(USER_AGENT))) || (this.detectInternetExplorer5Or6(request.getHeader(USER_AGENT_INICAPS)));
    }

    public boolean detectInternetExplorer5Or6(String userAgent) {
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            return (userAgent.indexOf(INTERNET_EXPLORER_6) > -1) || (userAgent.indexOf(INTERNET_EXPLORER_5) > -1);
        } else {
            return false;
        }
    }
}
