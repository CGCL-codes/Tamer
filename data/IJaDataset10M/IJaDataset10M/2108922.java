package org.apache.pluto.driver.services.impl.resource;

import java.io.InputStream;
import java.util.Set;
import javax.servlet.ServletContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pluto.driver.config.DriverConfigurationException;
import org.apache.pluto.driver.services.portal.PropertyConfigService;

/**
 * Default implementation of all of the portal Services.
 * Utilizes resource configuration from
 * <code>pluto-portal-driver-config.xml</code>
 *
 * @since Aug 10, 2005
 */
public class PropertyConfigServiceImpl implements PropertyConfigService {

    private static final Log LOG = LogFactory.getLog(PropertyConfigServiceImpl.class);

    private ResourceConfig config;

    public PropertyConfigServiceImpl() {
    }

    /**
     * Initialization Lifecycle Method
     * @param ctx
     */
    public void init(ServletContext ctx) {
        try {
            InputStream in = ctx.getResourceAsStream(ResourceConfigReader.CONFIG_FILE);
            config = ResourceConfigReader.getFactory().parse(in);
        } catch (Exception e) {
            LOG.error("Unable to parse resource config " + e.getMessage(), e);
            throw new DriverConfigurationException(e);
        }
    }

    /**
     * Shutdown the ResourceService.
     */
    public void destroy() {
        config = null;
    }

    public String getPortalName() {
        return config.getPortalName();
    }

    public String getPortalVersion() {
        return config.getPortalVersion();
    }

    public String getContainerName() {
        return config.getContainerName();
    }

    public Set getSupportedPortletModes() {
        return config.getSupportedPortletModes();
    }

    public Set getSupportedWindowStates() {
        return config.getSupportedWindowStates();
    }
}
