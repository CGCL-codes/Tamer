package org.apache.felix.upnp.basedriver.importer.core.event.message;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.osgi.service.upnp.UPnPEventListener;

/**
 * This class rappresent a message that is equeued for the Suscriber.<br>
 * This is message is related to a registration of listener for a 
 * CyberLink Service during the registering of the UPnP Event Listener
 * 
 * @author <a href="mailto:dev@felix.apache.org">Felix Project Team</a>
*/
public class FirstMessage {

    private Service service;

    private UPnPEventListener listener;

    private String sid;

    private Device device;

    public FirstMessage(Service service, UPnPEventListener listener) {
        this.service = service;
        this.listener = listener;
        this.sid = "";
        this.device = service.getDevice();
    }

    public Service getService() {
        return service;
    }

    public UPnPEventListener getListener() {
        return listener;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSid() {
        return sid;
    }

    public Device getDevice() {
        return device;
    }

    public String getDeviceID() {
        return device.getUDN();
    }

    public String getServiceID() {
        return service.getServiceID();
    }
}
