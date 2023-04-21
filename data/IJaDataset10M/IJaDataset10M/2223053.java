package org.nakedobjects.ejb;

import org.apache.log4j.Logger;
import org.nakedobjects.object.context.ThreadContext;
import org.nakedobjects.object.security.PasswordFileAuthenticator;
import org.nakedobjects.object.transaction.TransactionPeerFactory;
import org.nakedobjects.system.NakedObjectsSystem;
import org.nakedobjects.utility.configuration.ResourceConfigurationLoader;

public class Server {

    private static final Logger LOG = Logger.getLogger(Server.class);

    private static Server instance;

    private NakedObjectsSystem system;

    public static NakedObjectsSystem getSystem() {
        if (instance == null) {
            synchronized (Server.class) {
                if (instance == null) {
                    Server instance = new Server();
                    instance.init();
                    Server.instance = instance;
                }
            }
        }
        return instance.system;
    }

    private void init() {
        LOG.info("creating naked objects server");
        system = new NakedObjectsSystem();
        system.addAuthenticator(new PasswordFileAuthenticator());
        system.addReflectivePeer(new TransactionPeerFactory());
        system.setInstallConfiguration(new ResourceConfigurationLoader());
        system.setContext(ThreadContext.createInstance());
        system.setPersistorPerContext(true);
        system.disableSplash(true);
        system.init();
        LOG.info("server started");
    }
}
