package ar.com.coonocer.framework.gui.taglibs;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

public class BaseTag implements BodyTag {

    protected PageContext pageContext;

    protected Tag parent;

    protected BodyContent bodyContent;

    public BaseTag() {
        super();
    }

    public int doStartTag() throws javax.servlet.jsp.JspTagException {
        return SKIP_BODY;
    }

    public int doEndTag() throws javax.servlet.jsp.JspTagException {
        try {
            pageContext.getOut().write("doEndTag() method must be overwriten for class: " + this.getClass().getName());
        } catch (java.io.IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
        return EVAL_PAGE;
    }

    public void release() {
    }

    public void setPageContext(final javax.servlet.jsp.PageContext pageContext) {
        this.pageContext = pageContext;
    }

    public void setParent(final javax.servlet.jsp.tagext.Tag parent) {
        this.parent = parent;
    }

    public javax.servlet.jsp.tagext.Tag getParent() {
        return parent;
    }

    public void setBodyContent(BodyContent bodyContent) {
        this.bodyContent = bodyContent;
    }

    public void doInitBody() throws JspException {
    }

    public int doAfterBody() throws JspException {
        return SKIP_BODY;
    }
}
