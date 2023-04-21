package org.mobicents.media.server.impl.resource.echo;

import org.mobicents.media.Component;
import org.mobicents.media.ComponentFactory;
import org.mobicents.media.server.spi.Endpoint;

/**
 *
 * @author kulikov
 */
public class EchoFactory implements ComponentFactory {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Component newInstance(Endpoint endpoint) {
        return new Echo(name);
    }
}
