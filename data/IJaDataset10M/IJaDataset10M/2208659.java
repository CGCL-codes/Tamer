package org.jtools.server.impl;

import java.util.ArrayList;
import java.util.Collection;
import org.jtools.server.Component;
import org.jtools.server.Server;
import org.jtools.server.Service;
import org.jtools.server.config.ConfigurationHelper;
import org.jtools.server.config.ListenerConfiguration;

public class ServerImpl implements Server {

    private final Collection<Component> components = new ArrayList<Component>();

    private final ConfigurationHelper listenerConfigurationHelper;

    private final Collection<Service<?>> services = new ArrayList<Service<?>>();

    public ServerImpl() {
        this.listenerConfigurationHelper = new ConfigurationHelper();
    }

    public void startListener(ListenerConfiguration cfg) throws Exception {
        startComponent(cfg.create());
    }

    protected <C extends Component> C startComponent(C c) {
        c.start();
        components.add(c);
        return c;
    }

    public ConfigurationHelper getListenerConfigurationHelper() {
        return listenerConfigurationHelper;
    }

    @Override
    public void shutdown(int secs) {
        for (Component c : components) c.stop(secs);
    }

    public void addService(Service<?> service) {
        this.services.add(service);
    }

    @Override
    public void startServices() {
        for (Service<?> service : this.services) service.start();
    }
}
