package org.tglman.jsdms;

import org.tglman.jsdms.network.NetworkFacade;
import org.tglman.jsdms.network.NetworkFacadeImpl;
import org.tglman.jsdms.network.engine.CompositeNetworkHandler;
import org.tglman.jsdms.network.engine.Engine;

public class JsdmsFacadeFactoryImpl extends JsdmsFacadeFactory {

    static {
        JsdmsFacadeFactory.instance = new JsdmsFacadeFactoryImpl();
    }

    private JsdmsFacade facade = null;

    public JsdmsFacadeFactoryImpl() {
        Engine engine = new Engine(new CompositeNetworkHandler(), 10);
        NetworkFacade networkFacade = new NetworkFacadeImpl(engine);
        facade = new JsdmsFacadeImpl(networkFacade);
    }

    @Override
    public JsdmsFacade getFacade() {
        return facade;
    }
}
