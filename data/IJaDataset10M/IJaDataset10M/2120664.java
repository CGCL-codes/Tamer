package x360mediaserver.upnpmediaserver.responder;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;

public class MyDeviceList extends DeviceList {

    public MyDevice getDevice(int n) {
        return (MyDevice) get(n);
    }
}
