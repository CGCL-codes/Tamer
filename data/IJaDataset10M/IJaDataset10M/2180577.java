package org.apache.pluto.driver.tags;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.pluto.PortletWindow;
import org.apache.pluto.driver.AttributeKeys;

/**
 * The portlet title tag is used to print the dynamic portlet title to the page.
 * @author <a href="mailto:ddewolf@apache.org">David H. DeWolf</a>
 * @author <a href="mailto:zheng@apache.org">ZHENG Zhong</a>
 * @version 1.0
 * @since Oct 4, 2004
 */
public class PortletTitleTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    /**
     * Method invoked when the start tag is encountered. This method retrieves
     * the portlet title and print it to the page.
     * 
	 * @see org.apache.pluto.services.PortalCallbackService#setTitle(HttpServletRequest, PortletWindow, String)
	 * @see org.apache.pluto.driver.services.container.PortalCallbackServiceImpl#setTitle(HttpServletRequest, PortletWindow, String)
	 * 
	 * @throws JspException
	 */
    public int doStartTag() throws JspException {
        PortletTag parentTag = (PortletTag) TagSupport.findAncestorWithClass(this, PortletTag.class);
        if (parentTag == null) {
            throw new JspException("Portlet title tag may only reside " + "within a pluto:portlet tag.");
        }
        try {
            pageContext.getOut().print(pageContext.getRequest().getAttribute(AttributeKeys.PORTLET_TITLE));
        } catch (IOException ex) {
            throw new JspException(ex);
        }
        return SKIP_BODY;
    }
}
