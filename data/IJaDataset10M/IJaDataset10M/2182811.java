package de.hsofttec.monitoring.host;

import org.apache.commons.configuration.Configuration;
import de.hsofttec.monitoring.IMonitorContext;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">shomburg</a>
 * @version $Id: HostContext.java 2 2007-09-01 11:09:16Z shomburg $
 */
public class HostContext implements IHostContext {

    private IMonitorContext _monitorContext;

    private Configuration _configuration;

    public HostContext(IMonitorContext monitorContext, Configuration configuration) {
        _monitorContext = monitorContext;
        _configuration = configuration;
    }

    /**
     * get the context of the monitor.
     *
     * @return context of the monitor
     */
    public IMonitorContext getMonitorContext() {
        return _monitorContext;
    }

    /**
     * get the configuration of this host.
     *
     * @return context of this host
     */
    public Configuration getConfiguration() {
        return _configuration;
    }

    /**
     * get the name of the host.
     *
     * @return the name of the host
     */
    public String getName() {
        return getConfiguration().getString("host");
    }
}
