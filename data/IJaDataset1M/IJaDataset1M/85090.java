package edu.uiuc.ncsa.security.util.configuration;

import edu.uiuc.ncsa.security.core.configuration.OldConfiguration;
import edu.uiuc.ncsa.security.core.configuration.OldConfigurationDepot;

/**
 * Top-level factory that retrieves a {@link edu.uiuc.ncsa.security.core.configuration.OldConfigurationDepot}.
 * <p/>
 * Only one configuration is active at a time.
 * <p>Created by Jeff Gaynor<br>
 * on Feb 23, 2011 at  1:45:09 PM
 */
public class ConfigurationDepotFactory {

    public ConfigurationDepotFactory() {
    }

    public ConfigurationDepotFactory(OldConfigurationDepot configurationDepot) {
        this.configurationDepot = configurationDepot;
    }

    public OldConfigurationDepot getConfigurationDepot() {
        return configurationDepot;
    }

    public boolean isConfigured() {
        return this.configurationDepot != null;
    }

    public void setConfiguration(OldConfigurationDepot configurationDepot) {
        if (isConfigured()) {
            throw new IllegalArgumentException("Error: The configuration store for this factory has been set.");
        }
        this.configurationDepot = configurationDepot;
    }

    OldConfigurationDepot configurationDepot;

    /**
     * The currently active configuration.
     *
     * @return
     */
    protected OldConfiguration getConfiguration() {
        return getConfigurationDepot().getCurrentConfiguration();
    }
}
