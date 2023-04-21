package org.apache.pluto.driver;

import java.io.IOException;
import java.util.Enumeration;
import javax.portlet.PortletException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pluto.PortletContainer;
import org.apache.pluto.PortletContainerException;
import org.apache.pluto.core.PortalSessionServiceImpl;
import org.apache.pluto.driver.core.PortalRequestContext;
import org.apache.pluto.driver.core.PortletWindowImpl;
import org.apache.pluto.driver.services.portal.PageConfig;
import org.apache.pluto.driver.services.portal.PortletWindowConfig;
import org.apache.pluto.driver.url.PortalURL;
import org.apache.pluto.internal.impl.PortalSessionImpl;
import org.apache.pluto.om.portlet.PortletDefinition;
import cn.vlabs.duckling.vwb.VWBContext;
import cn.vlabs.duckling.vwb.services.resource.Resource;
import cn.vlabs.duckling.vwb.ui.base.BaseFunctions;
import cn.vlabs.duckling.vwb.ui.command.PortalCommand;
import cn.vlabs.duckling.vwb.ui.map.GlobalMap;

/**
 * The controller servlet used to drive the Portal Driver. All requests mapped
 * to this servlet will be processed as Portal Requests.
 *
 * @version 1.0
 * @since Sep 22, 2004
 */
public class PortalDriver {

    /**
	 * Brief Intro Here
	 */
    private static final long serialVersionUID = 1L;

    /** Internal Logger. */
    private static final Log LOG = LogFactory.getLog(PortalDriver.class);

    /** The Portal Driver sServlet Context */
    private ServletContext servletContext = null;

    private BaseFunctions functions;

    public static final String DEFAULT_PAGE_URI = "/WEB-INF/themes/pluto-default-theme.jsp";

    public static final String DEFAULT_PAGE_DIR = "/WEB-INF/themes/";

    /** The portlet container to which we will forward all portlet requests. */
    protected PortletContainer container = null;

    /** Character encoding and content type of the response */
    private String contentType = "";

    public String getServletInfo() {
        return "Pluto Portal Driver Servlet";
    }

    /**
     * Initialize the Portal Driver. This method retrieves the portlet container
     * instance from the servlet context scope.
     * @see PortletContainer
     */
    public void init(ServletContext servletContext) {
        this.servletContext = servletContext;
        container = (PortletContainer) servletContext.getAttribute(AttributeKeys.PORTLET_CONTAINER);
        contentType = "text/html;";
        functions = new BaseFunctions(servletContext);
    }

    /**
     * Handle all requests. All POST requests are passed to this method.
     * @param request  the incoming HttpServletRequest.
     * @param response  the incoming HttpServletResponse.
     * @throws ServletException  if an internal error occurs.
     * @throws IOException  if an error occurs writing to the response.
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start of PortalDriverServlet.doGet() to process portlet request . . .");
        }
        PortalRequestContext portalRequestContext = new PortalRequestContext(servletContext, request, response);
        PortalURL portalURL = null;
        try {
            portalURL = portalRequestContext.getRequestedPortalURL();
        } catch (Exception ex) {
            String msg = "Cannot handle request for portal URL. Problem: " + ex.getMessage();
            LOG.error(msg, ex);
            throw new ServletException(msg, ex);
        }
        Resource resource = functions.getSavedViewPort(request);
        VWBContext context = VWBContext.createContext(request, PortalCommand.VIEW, resource);
        if (!context.hasAccess(response)) {
            return;
        }
        PortalSessionImpl psession = (PortalSessionImpl) PortalSessionServiceImpl.getPortalSession(request);
        psession.setVO(context.getVO());
        GlobalMap.saveToRequest(request);
        String actionWindowId = portalURL.getActionWindow();
        String resourceWindowId = portalURL.getResourceWindow();
        PortletWindowConfig actionWindowConfig = null;
        PortletWindowConfig resourceWindowConfig = null;
        if (resourceWindowId != null) {
            resourceWindowConfig = PortletWindowConfig.fromId(resourceWindowId);
        } else if (actionWindowId != null) {
            actionWindowConfig = PortletWindowConfig.fromId(actionWindowId);
        }
        if (resourceWindowConfig == null) {
            if (contentType != "") {
                response.setContentType(contentType);
            }
        }
        if (actionWindowConfig != null) {
            PortletWindowImpl portletWindow = new PortletWindowImpl(container, actionWindowConfig, portalURL);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Processing action request for window: " + portletWindow.getId().getStringId());
            }
            try {
                container.doAction(portletWindow, request, response);
            } catch (PortletContainerException ex) {
                LOG.error(ex.getMessage(), ex);
                throw new ServletException(ex);
            } catch (PortletException ex) {
                LOG.error(ex.getMessage(), ex);
                throw new ServletException(ex);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Action request processed.\n\n");
            }
        } else if (resourceWindowConfig != null) {
            try {
                if (request.getParameterNames().hasMoreElements()) setPublicRenderParameter(request, portalURL, portalURL.getResourceWindow());
            } catch (PortletContainerException e) {
                LOG.error(e);
                throw new ServletException(e);
            }
            PortletWindowImpl portletWindow = new PortletWindowImpl(container, resourceWindowConfig, portalURL);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Processing resource Serving request for window: " + portletWindow.getId().getStringId());
            }
            try {
                container.doServeResource(portletWindow, request, response);
            } catch (PortletContainerException ex) {
                LOG.error(ex.getMessage(), ex);
                throw new ServletException(ex);
            } catch (PortletException ex) {
                LOG.error(ex.getMessage(), ex);
                throw new ServletException(ex);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Resource serving request processed.\n\n");
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Processing render request.");
            }
            PageConfig pageConfig = portalURL.getPageConfig(servletContext);
            if (pageConfig == null) {
                String renderPath = (portalURL == null ? "" : portalURL.getRenderPath());
                String msg = "PageConfig for render path [" + renderPath + "] could not be found.";
                LOG.error(msg);
                throw new ServletException(msg);
            }
            request.setAttribute(AttributeKeys.CURRENT_PAGE, pageConfig);
            String uri = (pageConfig.getUri() != null) ? pageConfig.getUri() : DEFAULT_PAGE_URI;
            if (LOG.isDebugEnabled()) {
                LOG.debug("Dispatching to: " + uri);
            }
            functions.layout(request, response, context);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Render request processed.\n\n");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void setPublicRenderParameter(HttpServletRequest request, PortalURL portalURL, String portletID) throws ServletException, PortletContainerException {
        String applicationId = PortletWindowConfig.parseContextPath(portletID);
        String applicationName = applicationId;
        if (applicationName.length() > 0) {
            applicationName = applicationName.substring(1);
        }
        String portletName = PortletWindowConfig.parsePortletName(portletID);
        PortletDefinition portletDD = container.getOptionalContainerServices().getPortletRegistryService().getPortlet(applicationName, portletName);
        Enumeration<String> parameterNames = request.getParameterNames();
        if (parameterNames != null) {
            while (parameterNames.hasMoreElements()) {
                String parameterName = parameterNames.nextElement();
                if (portletDD.getSupportedPublicRenderParameters() != null) {
                    if (portletDD.getSupportedPublicRenderParameters().contains(parameterName)) {
                        String value = request.getParameter(parameterName);
                        portalURL.addPublicParameterActionResourceParameter(parameterName, value);
                    }
                }
            }
        }
    }
}
