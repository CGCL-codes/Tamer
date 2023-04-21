package org.sqlanyware.taglib;

import java.io.IOException;
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public abstract class Condition extends BodyTagSupport {

    protected abstract boolean init() throws Exception;

    public int doStartTag() throws JspException {
        try {
            boolean b = init();
            if (false == b) {
                return SKIP_BODY;
            } else {
                return EVAL_BODY_BUFFERED;
            }
        } catch (Exception e) {
            throw new JspException(e);
        }
    }

    public int doEndTag() throws JspException {
        return super.doEndTag();
    }

    public int doAfterBody() throws JspException {
        try {
            BodyContent body = getBodyContent();
            body.writeOut(getPreviousOut());
            bodyContent.clearBody();
            return SKIP_BODY;
        } catch (IOException e) {
            throw new JspException(e);
        }
    }

    protected Iterator m_oIter;
}
