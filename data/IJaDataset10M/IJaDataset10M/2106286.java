package org.ashkelon.taglibs;

import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Sep 4, 2005
 * Time: 2:33:00 PM
 */
public class IncludeTag extends TagSupport {

    private String _page;

    private boolean _dynamic;

    public int doStartTag() throws JspException {
        if (include()) {
            try {
                pageContext.include(_page);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Tag.SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    private boolean include() {
        boolean staticContext = ((Boolean) pageContext.getServletContext().getAttribute("static-context")).booleanValue();
        boolean exclude = _dynamic && staticContext;
        return !exclude;
    }

    public boolean isDynamic() {
        return _dynamic;
    }

    public void setDynamic(boolean dynamic) {
        _dynamic = dynamic;
    }

    public String getPage() {
        return _page;
    }

    public void setPage(String page) {
        _page = page;
    }
}
