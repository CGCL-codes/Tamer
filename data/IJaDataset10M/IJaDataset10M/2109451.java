package de.hsofttec.monitoring.host;

import org.apache.commons.configuration.ConfigurationException;
import de.hsofttec.monitoring.IMonitorContext;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">S.Homburg</a>
 * @version $Id: Host.java 2 2007-09-01 11:09:16Z shomburg $
 */
public class Host extends AbstractHost {

    public Host(IMonitorContext monitorContext, String configFileName) throws ConfigurationException {
        super(monitorContext, configFileName);
    }
}
