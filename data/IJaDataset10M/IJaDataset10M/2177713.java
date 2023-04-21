package com.volantis.shared.jsp;

import javax.servlet.jsp.PageContext;
import com.volantis.shared.servlet.ServletEnvironment;
import com.volantis.shared.servlet.ServletEnvironmentInteractionImpl;

/**
 * ServletEnvironmentInteractionImpl.
 * Data store for the servlet environment interaction.
 *
 * @author steve
 *
 */
public class JspEnvironmentInteractionImpl extends ServletEnvironmentInteractionImpl implements JspEnvironmentInteraction {

    /** The jsp page context */
    private PageContext context;

    /**
     * Create a ServletEnvironmentInteraction implementation
     */
    public JspEnvironmentInteractionImpl(ServletEnvironment servletEnvironment, PageContext context) {
        super(servletEnvironment, null, context.getServletConfig(), context.getRequest(), context.getResponse());
        this.context = context;
    }

    /**
     * Return the JSP page context
     * @return the page context
     */
    public PageContext getPageContext() {
        return context;
    }
}
