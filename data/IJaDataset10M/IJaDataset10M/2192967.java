package org.nakedobjects.system;

import org.nakedobjects.object.repository.NakedObjectsClient;
import org.nakedobjects.system.install.core.DefaultConfigurationLoader;
import org.nakedobjects.system.install.core.DefaultGuiViewer;
import org.nakedobjects.system.install.core.DefaultObjectLoader;
import org.nakedobjects.system.install.distribution.ClientConnection;

/**
 * Utility class to start a client, using the default configuration file: client.properties.
 */
public final class Client {

    public static void main(final String[] args) {
        NakedObjectsSystem e = new NakedObjectsSystem();
        DefaultConfigurationLoader configuration = new DefaultConfigurationLoader();
        configuration.addConfigurationFile("client.properties", false);
        e.setConfigurationLoader(configuration);
        e.setRepository(new NakedObjectsClient());
        e.setObjectLoader(new DefaultObjectLoader());
        ClientConnection connection = new ClientConnection();
        e.setObjectPersistor(connection);
        e.setSpecificationLoader(connection);
        DefaultGuiViewer viewer = new DefaultGuiViewer();
        e.setClient(viewer);
        e.init();
    }
}
