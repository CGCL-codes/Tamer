package org.weblayouttag.tag;

import org.weblayouttag.util.ParentFinder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author Andy Marek
 * @version May 23, 2005
 */
public class AbstractParentFinderTag extends BodyTagSupport {

    protected static final ParentFinder PARENT_FINDER = ParentFinder.getInstance();

    protected final Log log = LogFactory.getLog(this.getClass());

    public int doStartTag() throws JspException {
        PARENT_FINDER.registerTag(this.getPageContext(), this);
        return super.doStartTag();
    }

    public int doEndTag() throws JspException {
        PARENT_FINDER.deregisterTag(this.getPageContext());
        return super.doEndTag();
    }

    public PageContext getPageContext() {
        return this.pageContext;
    }
}
