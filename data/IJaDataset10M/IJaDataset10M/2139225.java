package org.jpos.voldemort;

import voldemort.server.VoldemortConfig;
import voldemort.server.VoldemortServer;
import voldemort.server.StoreRepository;
import voldemort.store.StorageEngine;
import org.jpos.q2.QBeanSupport;
import org.jpos.util.NameRegistrar;
import org.jpos.util.LogEvent;
import org.jpos.util.Logger;
import org.jpos.core.ConfigurationException;
import java.util.Iterator;

/**
 * VolcermortServerdaptor
 * @author Alejandro Revilla
 */
public class VoldemortServerAdaptor extends QBeanSupport {

    VoldemortServer server = null;

    public VoldemortServerAdaptor() {
        super();
    }

    public void initService() throws ConfigurationException {
        VoldemortConfig config = VoldemortConfig.loadFromVoldemortHome(cfg.get("config"));
        server = new VoldemortServer(config);
    }

    public void startService() throws ConfigurationException {
        server.start();
        NameRegistrar.register(getName(), server);
    }

    protected void stopService() throws Exception {
        try {
            server.stop();
        } catch (Exception e) {
            getLog().error(e.getMessage());
        }
        NameRegistrar.unregister(getName());
    }
}
