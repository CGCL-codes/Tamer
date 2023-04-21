package com.evs.objava29.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;

public class LoopTag implements IterationTag {

    private PageContext pageContext;

    private Tag parentTag;

    private Integer count = 0;

    public int doAfterBody() throws JspException {
        if (--count > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return EVAL_BODY_AGAIN;
        } else {
            return SKIP_BODY;
        }
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    @Override
    public int doStartTag() throws JspException {
        if (getCount() > 0) {
            return EVAL_BODY_AGAIN;
        } else {
            return SKIP_BODY;
        }
    }

    public Tag getParent() {
        return null;
    }

    public void release() {
    }

    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    public void setParent(Tag parentTag) {
        this.parentTag = parentTag;
    }

    /**
	 * @param count
	 *            the count to set
	 */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
	 * @return the count
	 */
    public Integer getCount() {
        return count;
    }
}
