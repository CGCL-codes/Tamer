package org.mobicents.media.server.impl.resource;

import org.mobicents.media.Component;
import org.mobicents.media.ComponentFactory;
import org.mobicents.media.server.spi.Endpoint;
import org.mobicents.media.server.spi.ResourceUnavailableException;

/**
 *
 * @author kulikov
 */
public class ProxySinkFactory implements ComponentFactory {

    private Proxy proxy;

    public ProxySinkFactory(Proxy proxy) {
        this.proxy = proxy;
    }

    public Component newInstance(Endpoint endpoint) throws ResourceUnavailableException {
        return proxy.getInput();
    }
}
