package org.dhcp4java.server.config;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

/**
 * 
 * @author Stephan Hadinger
 * @version 0.70
 */
public class ServerConfigSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(ServerConfigSet.class.getName().toLowerCase());

    private AtomicReference<FrontendConfig> frontendConfig = new AtomicReference<FrontendConfig>();

    private AtomicReference<GlobalConfig> globalConfig = new AtomicReference<GlobalConfig>();

    private AtomicReference<TopologyConfig> topologyConfig = new AtomicReference<TopologyConfig>();

    /**
	 * @return Returns the frontendConfig.
	 */
    public FrontendConfig getFrontendConfig() {
        return frontendConfig.get();
    }

    /**
	 * @param frontendConfig The frontendConfig to set.
	 */
    public void setFrontendConfig(FrontendConfig frontendConfig) {
        this.frontendConfig.set(frontendConfig);
    }

    /**
	 * @return Returns the globalConfig.
	 */
    public GlobalConfig getGlobalConfig() {
        return globalConfig.get();
    }

    /**
	 * @param globalConfig The globalConfig to set.
	 */
    public void setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig.set(globalConfig);
    }

    /**
	 * @return Returns the topologyConfig.
	 */
    public TopologyConfig getTopologyConfig() {
        return topologyConfig.get();
    }

    /**
	 * @param topologyConfig The topologyConfig to set.
	 */
    public void setTopologyConfig(TopologyConfig topologyConfig) {
        this.topologyConfig.set(topologyConfig);
    }
}
