package com.google.code.sapien.util;

import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import org.apache.struts2.views.util.UrlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * Dispatcher Result that supports themeing.
 * @author Adam
 * @version $Id: ThemeDispatcherResult.java 20 2009-05-03 19:37:57Z a.ruggles $
 * 
 * Created on Mar 31, 2009 at 8:59:49 PM 
 */
public class ThemeDispatcherResult extends StrutsResultSupport {

    /**
	 * Serial Version UID.
	 */
    private static final long serialVersionUID = -8405583761802614417L;

    /**
     * The <code>Logger</code> is used by the application to generate a log messages.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ThemeDispatcherResult.class);

    /**
     * Default constructor.
     */
    public ThemeDispatcherResult() {
        super();
    }

    /**
     * Constructs a Theme Dispatcher with a location.
     * @param location The location to go to after execution.
     */
    public ThemeDispatcherResult(final String location) {
        super(location);
    }

    /**
     * {@inheritDoc}
     * @see org.apache.struts2.dispatcher.StrutsResultSupport#doExecute(
     * java.lang.String, com.opensymphony.xwork2.ActionInvocation)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doExecute(final String finalLocation, final ActionInvocation invocation) throws Exception {
        LOG.debug("Forwarding to location [{}]", finalLocation);
        final PageContext pageContext = ServletActionContext.getPageContext();
        if (pageContext == null) {
            final HttpServletRequest request = ServletActionContext.getRequest();
            final HttpServletResponse response = ServletActionContext.getResponse();
            final RequestDispatcher dispatcher = request.getRequestDispatcher(finalLocation);
            if (invocation != null && finalLocation != null && finalLocation.length() > 0 && finalLocation.indexOf('?') > 0) {
                final String queryString = finalLocation.substring(finalLocation.indexOf('?') + 1);
                final Map parameters = (Map) invocation.getInvocationContext().getContextMap().get("parameters");
                final Map queryParams = UrlHelper.parseQueryString(queryString, true);
                if (queryParams != null && !queryParams.isEmpty()) {
                    parameters.putAll(queryParams);
                }
            }
            if (dispatcher == null) {
                response.sendError(404, "result '" + finalLocation + "' not found");
                return;
            }
            if (!response.isCommitted() && (request.getAttribute("javax.servlet.include.servlet_path") == null)) {
                request.setAttribute("struts.view_uri", finalLocation);
                request.setAttribute("struts.request_uri", request.getRequestURI());
                dispatcher.forward(request, response);
            } else {
                dispatcher.include(request, response);
            }
        } else {
            pageContext.include(finalLocation);
        }
    }
}
