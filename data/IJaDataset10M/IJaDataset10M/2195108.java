package net.woodstock.rockapi.jsp.taglib.db.util;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import net.woodstock.rockapi.utils.StringUtils;

public abstract class DBTagUtils {

    private DBTagUtils() {
    }

    public static void setAttribute(JspContext jspContext, String name, Object attribute, String scope) throws JspException {
        if (!StringUtils.isEmpty(scope)) {
            if (scope.equalsIgnoreCase("APPLICATION")) {
                DBTagUtils.setAttribute(jspContext, name, attribute, PageContext.APPLICATION_SCOPE);
            } else if (scope.equalsIgnoreCase("PAGE")) {
                DBTagUtils.setAttribute(jspContext, name, attribute, PageContext.PAGE_SCOPE);
            } else if (scope.equalsIgnoreCase("REQUEST")) {
                DBTagUtils.setAttribute(jspContext, name, attribute, PageContext.REQUEST_SCOPE);
            } else if (scope.equalsIgnoreCase("SESSION")) {
                DBTagUtils.setAttribute(jspContext, name, attribute, PageContext.SESSION_SCOPE);
            } else {
                throw new JspException("Invalid scope: '" + scope + "'");
            }
        } else {
            DBTagUtils.setAttribute(jspContext, name, attribute, PageContext.PAGE_SCOPE);
        }
    }

    private static void setAttribute(JspContext jspContext, String name, Object attribute, int scope) {
        jspContext.setAttribute(name, attribute, scope);
    }
}
