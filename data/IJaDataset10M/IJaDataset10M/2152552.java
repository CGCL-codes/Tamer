package net.joindesk.util;

import javax.servlet.http.HttpSession;

public class ValidateCodeHelp {

    public static boolean check(HttpSession session, String code) {
        String codeInSession = (String) session.getAttribute(ValidateCodeServlet.SessionAttributeName);
        if (codeInSession == null) return false;
        if (codeInSession.equals(code)) return true;
        return false;
    }
}
