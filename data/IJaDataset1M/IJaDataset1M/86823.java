package org.cybergarage.controlpoint;

import org.cybergarage.upnp.*;

public class ServiceTable extends TableModel {

    public ServiceTable(Service dev) {
        super(dev.getServiceNode());
    }
}
