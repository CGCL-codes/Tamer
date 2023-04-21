package net.sourceforge.obexftpfrontend.model;

import java.io.Serializable;

/**
 * This is an event object that is sent to the ConfigurationListener objects
 * when the configuration changes.
 * @author Daniel F. Martins
 */
public class ConfigurationEvent implements Serializable {

    /** Configuration object related to the event. */
    private Configuration config;

    /**
     * Create a new instance of ConfigurationEvent.
     * @param config Configuration object.
     */
    public ConfigurationEvent(Configuration config) {
        super();
        this.config = config;
    }

    /**
     * Get the new Configuration object.
     * @return New Configuration object.
     */
    public Configuration getConfiguration() {
        return config;
    }
}
