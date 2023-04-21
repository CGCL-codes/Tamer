package org.dspace.app.webui.jsptag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Tag for including a "sidebar" - a column on the right-hand side of the page.
 * Must be used within a dspace:layout tag.
 * 
 * @author Peter Breton
 * @version $Revision: 1189 $
 */
public class SidebarTag extends BodyTagSupport {

    public SidebarTag() {
        super();
    }

    public int doAfterBody() throws JspException {
        LayoutTag tag = (LayoutTag) TagSupport.findAncestorWithClass(this, LayoutTag.class);
        if (tag == null) {
            throw new JspException("Sidebar tag must be in an enclosing Layout tag");
        }
        tag.setSidebar(getBodyContent().getString());
        return SKIP_BODY;
    }
}
