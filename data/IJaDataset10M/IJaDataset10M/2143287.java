package org.riverock.webmill.container.tags;

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * * Supporting class for the <CODE>renderURL</CODE> tag.
 * * Creates a url that points to the current Portlet and triggers an render request
 * * with the supplied parameters.
 * *
 */
public class RenderURLTag extends BasicURLTag {

    public int doStartTag() throws JspException {
        if (var != null) {
            pageContext.removeAttribute(var, PageContext.PAGE_SCOPE);
        }
        RenderResponse renderResponse = (RenderResponse) pageContext.getRequest().getAttribute("javax.portlet.response");
        if (renderResponse != null) {
            setUrl(renderResponse.createRenderURL());
            if (portletMode != null) {
                try {
                    PortletMode mode = new PortletMode(portletMode);
                    url.setPortletMode(mode);
                } catch (PortletModeException e) {
                    throw new JspException(e);
                }
            }
            if (windowState != null) {
                try {
                    WindowState state = new WindowState(windowState);
                    url.setWindowState(state);
                } catch (WindowStateException e) {
                    throw new JspException(e);
                }
            }
            if (secure != null) {
                try {
                    url.setSecure(getSecureBoolean());
                } catch (PortletSecurityException e) {
                    throw new JspException(e);
                }
            }
        }
        return EVAL_PAGE;
    }
}
