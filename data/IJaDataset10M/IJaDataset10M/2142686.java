package com.bugfree4j.app.security.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.bugfree4j.per.common.web.WebApplicationConstants;

/**
 * @author bugfree4j
 * @created 2005-3-3
 */
public class FilterUtils {

    private static Log log = LogFactory.getLog(FilterUtils.class);

    private static final String LOGON_PAGE = "Logon.jsp";

    private static final String LOGON_ACTION = "logon.do";

    private static final String AUTHORIZATION_FAILURE_PAGE = "HaveNoAuthorization.jsp";

    public static boolean isNotFilterResource(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String inLogon = (String) session.getAttribute(WebApplicationConstants.USER_INLOGON);
        String action = request.getServletPath();
        if (inLogon != null && inLogon.equals("true")) {
            return true;
        }
        if (action != null && (action.equals("/Logon.jsp") || action.equals("/logon.do"))) {
            return true;
        } else return false;
    }

    public static String getLogonPage(HttpServletRequest request) {
        return request.getContextPath() + "/" + LOGON_PAGE;
    }

    public static String getLogonAction(HttpServletRequest request) {
        return request.getContextPath() + "/" + LOGON_ACTION;
    }

    public static String getAuthFailure(HttpServletRequest request) {
        return request.getContextPath() + "/" + AUTHORIZATION_FAILURE_PAGE;
    }
}
