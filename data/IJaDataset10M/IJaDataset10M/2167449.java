package org.powerstone.web.paging;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.log4j.Logger;

public class CurrentPageNoTag extends BodyTagSupport {

    private Logger log = Logger.getLogger(getClass());

    public int doStartTag() throws JspException {
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        PageModel pm = (PageModel) pageContext.getRequest().getAttribute(PagingController.DEFAULT_PAGE_MODEL_NAME);
        int newPageNo = pm.computeNewPageNo();
        try {
            log.debug(newPageNo + "");
            super.pageContext.getOut().write(new Integer(newPageNo).toString());
        } catch (Exception ex) {
            log.error(ex);
        }
        return EVAL_PAGE;
    }
}
