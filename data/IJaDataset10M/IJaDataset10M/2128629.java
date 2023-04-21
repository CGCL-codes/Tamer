package net.sourceforge.cruisecontrol.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * Creates a link to this page excluding one request parameter.
 * By default the parameter excluded is log.
 * @author <a href="mailto:robertdw@users.sourceforge.net">Robert Watkins</a>
 */
public class LinkTag extends CruiseControlTagSupport {

    private String exclude = LOG_PARAMETER;

    public int doStartTag() throws JspException {
        getPageContext().setAttribute(getId(), createUrl(exclude));
        return Tag.SKIP_BODY;
    }

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }
}
