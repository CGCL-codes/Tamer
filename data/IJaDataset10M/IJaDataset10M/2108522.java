package org.exolab.jms.server.net;

import org.exolab.jms.config.Configuration;
import org.exolab.jms.config.types.SchemeType;

/**
 * Configuration for the HTTP connector.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.2 $ $Date: 2005/05/07 14:17:22 $
 */
class HTTPConnectorCfg extends AbstractHTTPConnectorCfg {

    /**
     * Construct a new <code>TCPConnectorCfg</code>.
     *
     * @param config the configuration to use
     */
    public HTTPConnectorCfg(Configuration config) {
        super(SchemeType.HTTP, config, config.getHttpConfiguration());
    }
}
