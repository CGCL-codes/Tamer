package org.jage.test.mock;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jage.config.IConfiguration;
import org.jage.core.plugin.PluginComponent;

/**
 * @author Monika Nawrot, Jakub Rodzen
 * 
 * Mock Configuration Repository
 */
public class SimpleConfigurationRepository implements PluginComponent {

    public SimpleConfigurationRepository() {
        Logger.getLogger(SimpleConfigurationRepository.class.getName()).log(Level.INFO, "SimpleConfigurationRepository up!");
    }

    public IConfiguration getConfiguration(String id) {
        if (id.equals("RMIConnector")) return new RMIConnectorConfiguration();
        if (id.equals("RMIConnector2")) return new RMIConnector2Configuration();
        if (id.equals("SocketConnector")) return new SocketConnectorConfiguration();
        return null;
    }
}
