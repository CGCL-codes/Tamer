package com.guanghua.brick.html.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CoreElseTag extends BodyTagSupport {

    private static Log logger = LogFactory.getLog(CoreElseIfTag.class);

    private String id = null;

    public int doStartTag() throws JspException {
        Boolean t = (Boolean) pageContext.getRequest().getAttribute(this.id);
        if (t == null) {
            logger.warn("else control must have the same id's if control");
            return BodyTagSupport.SKIP_BODY;
        } else if (t) {
            return BodyTagSupport.SKIP_BODY;
        } else {
            return BodyTagSupport.EVAL_BODY_INCLUDE;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
