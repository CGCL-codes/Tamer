package com.liferay.taglib.ui;

import com.liferay.portal.kernel.servlet.StringServletResponse;
import com.liferay.taglib.util.IncludeTag;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

/**
 * <a href="MyPlacesTag.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class MyPlacesTag extends IncludeTag {

    public static void doTag(ServletContext ctx, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        doTag(_PAGE, ctx, req, res);
    }

    public static void doTag(String page, ServletContext ctx, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        RequestDispatcher rd = ctx.getRequestDispatcher(page);
        rd.include(req, res);
    }

    public int doEndTag() throws JspException {
        try {
            ServletContext ctx = getServletContext();
            HttpServletRequest req = getServletRequest();
            StringServletResponse res = getServletResponse();
            doTag(ctx, req, res);
            pageContext.getOut().print(res.getString());
            return EVAL_PAGE;
        } catch (Exception e) {
            throw new JspException(e);
        }
    }

    protected String getDefaultPage() {
        return _PAGE;
    }

    private static final String _PAGE = "/html/taglib/ui/my_places/page.jsp";
}
