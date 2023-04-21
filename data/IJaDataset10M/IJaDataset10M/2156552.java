package org.kablink.teaming.taglib;

import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.kablink.util.servlet.DynamicServletRequest;
import org.kablink.util.servlet.StringServletResponse;

public class ChartTag extends BodyTagSupport {

    private int count = 0;

    private int total = 100;

    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

    public int doAfterBody() {
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException {
        try {
            HttpServletRequest httpReq = (HttpServletRequest) pageContext.getRequest();
            HttpServletResponse httpRes = (HttpServletResponse) pageContext.getResponse();
            float p = (float) (this.count * 100) / this.total;
            ServletRequest req = null;
            req = new DynamicServletRequest(httpReq, new HashMap());
            req.setAttribute("percent", Math.round(p));
            req.setAttribute("count", count);
            String jsp = "/WEB-INF/jsp/tag_jsps/charts/chart.jsp";
            RequestDispatcher rd = httpReq.getRequestDispatcher(jsp);
            StringServletResponse res = new StringServletResponse(httpRes);
            rd.include(req, res);
            pageContext.getOut().print(res.getString());
        } catch (Exception e) {
            throw new JspTagException(e.getLocalizedMessage());
        } finally {
            this.count = 0;
            this.total = 100;
        }
        return EVAL_PAGE;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTotal(int total) {
        if (total == 0) {
            total = 100;
        }
        this.total = total;
    }
}
