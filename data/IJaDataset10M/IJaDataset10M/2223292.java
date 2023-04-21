package br.ufrgs.inf.prav.interop.detectors;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Fernando Arena Varella
 * @version 1.0
 */
public class TVDeviceDetector {

    public static boolean isTV(HttpServletRequest request) throws Exception {
        String user_agent = request.getHeader("user-agent");
        if (user_agent != null) {
            if (user_agent.contains("Java/")) return true;
        }
        return false;
    }
}
