package com.cb.web.wasf.tag;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * @author CBO
 */
public class SubmitLinkTag extends BodyTagSupport implements TryCatchFinally {

    private String event;

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public int doStartTag() throws JspException {
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doAfterBody() throws JspException {
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<a ");
            if (id != null) {
                sb.append("id=\"").append(id).append("\" ");
            }
            sb.append("href=\"#\" onclick=\"");
            if (event != null) {
                sb.append("$('#").append(getParentForm().getId()).append("-event').val('").append(event).append("');");
                if (id != null) {
                    sb.append("$('#").append(getParentForm().getId()).append("-eventSource').val('").append(id).append("');");
                }
            }
            sb.append("$('#").append(getParentForm().getId()).append("').submit();return false;");
            sb.append("\">");
            pageContext.getOut().write(sb.toString());
            getBodyContent().writeOut(pageContext.getOut());
            pageContext.getOut().write("</a>");
        } catch (IOException ioe) {
            throw new JspException(ioe);
        }
        return EVAL_PAGE;
    }

    private FormTag getParentForm() {
        return (FormTag) findAncestorWithClass(this, FormTag.class);
    }

    @Override
    public void doCatch(Throwable throwable) throws Throwable {
        throw throwable;
    }

    @Override
    public void doFinally() {
        this.event = null;
        this.id = null;
    }
}
