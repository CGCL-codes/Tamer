package org.jtools.server;

import org.jtools.server.config.ConfigurationHelper;
import org.jtools.server.config.ListenerConfiguration;

public interface Server {

    void addService(Service<?> service);

    void startServices();

    void startListener(ListenerConfiguration cfg) throws Exception;

    ConfigurationHelper getListenerConfigurationHelper();

    void shutdown(int secs);
}
