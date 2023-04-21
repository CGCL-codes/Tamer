package org.ddth.mypluto.driver;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.portlet.WindowState;
import javax.servlet.ServletContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pluto.PortletContainer;
import org.apache.pluto.spi.optional.PortletRegistryService;

public class SupportedWindowStateServiceImpl implements SupportedWindowStateService {

    private static final Log LOG = LogFactory.getLog(SupportedWindowStateServiceImpl.class);

    /**
	 * Servlet context used to get a handle on the portlet container
	 */
    private ServletContext servletContext = null;

    /**
	 * PropertyConfigService is injected by Spring. We use it to obtain the
	 * window states that the portal supports. It is protected only so that the
	 * unit tests have access to the field.
	 */
    protected PropertyConfigService propertyService = null;

    /**
	 * PortletRegistry is obtained from the PortletContainer on this service's
	 * initialization. It is protected only so that the unit tests have access
	 * to the field.
	 * 
	 * Note that it is an optional container service, but this implmentation
	 * requires it.
	 */
    protected PortletRegistryService portletRegistry = null;

    /**
	 * Contains String objects of window states supported by the portal
	 * (obtained from PropertyConfigService). It is protected only so that the
	 * unit tests have access to the field.
	 */
    protected Set portalSupportedWindowStates = new HashSet(3);

    /**
	 * Window States that are specified in PLT.9
	 */
    protected static final Set JSR168_WINDOW_STATES;

    static {
        JSR168_WINDOW_STATES = new HashSet(3);
        JSR168_WINDOW_STATES.add(WindowState.MAXIMIZED);
        JSR168_WINDOW_STATES.add(WindowState.MINIMIZED);
        JSR168_WINDOW_STATES.add(WindowState.NORMAL);
    }

    private SupportedWindowStateServiceImpl() {
    }

    public SupportedWindowStateServiceImpl(PropertyConfigService propertyService) {
        this.propertyService = propertyService;
    }

    public boolean isWindowStateSupported(String portletId, String state) {
        if (JSR168_WINDOW_STATES.contains(state)) {
            return true;
        }
        return isWindowStateSupportedByPortal(state) && isWindowStateSupportedByPortlet(portletId, state);
    }

    public boolean isWindowStateSupportedByPortal(String state) {
        return portalSupportedWindowStates.contains(state);
    }

    public boolean isWindowStateSupportedByPortlet(String portletId, String state) {
        return true;
    }

    public void destroy() throws DriverConfigurationException {
        LOG.debug("Destroying SupportedWindowStateService... ");
        portletRegistry = null;
        propertyService = null;
        portalSupportedWindowStates = null;
        LOG.debug("SupportedWindowStateService destroyed.");
    }

    public void init(ServletContext ctx) throws DriverConfigurationException {
        LOG.debug("Initializing SupportedWindowStateService... ");
        servletContext = ctx;
        portalSupportedWindowStates = propertyService.getSupportedWindowStates();
        if (LOG.isDebugEnabled()) {
            StringBuffer msg = new StringBuffer();
            if (portalSupportedWindowStates != null) {
                msg.append("Portal supports [" + portalSupportedWindowStates.size() + "] window states.  ");
                for (Iterator i = portalSupportedWindowStates.iterator(); i.hasNext(); ) {
                    msg.append("[" + i.next() + "]");
                    if (i.hasNext()) {
                        msg.append(", ");
                    }
                }
                LOG.debug(msg.toString());
            }
        }
        if (portalSupportedWindowStates == null) {
            final String msg = "Portal supported window states is null!";
            LOG.error(msg);
            throw new DriverConfigurationException(msg);
        }
        LOG.debug("SupportedWindowStateService initialized.");
    }

    private PortletRegistryService getPortletRegistryService() {
        PortletContainer container = ((PortletContainer) servletContext.getAttribute(AttributeKeys.PORTLET_CONTAINER));
        if (container == null) {
            final String msg = "Unable to obtain an instance of the container.";
            LOG.fatal(msg);
            throw new NullPointerException(msg);
        }
        if (container.getOptionalContainerServices() == null || container.getOptionalContainerServices().getPortletRegistryService() == null) {
            final String msg = "Unable to obtain the portlet registry.  The supported window state " + "service cannot support custom window states.";
            LOG.info(msg);
            return null;
        }
        return container.getOptionalContainerServices().getPortletRegistryService();
    }
}
