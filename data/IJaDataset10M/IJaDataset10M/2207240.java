package org.apache.felix.upnp.basedriver.export;

import java.util.Vector;

public class RootDeviceExportingQueue {

    private Vector v;

    private boolean waiting;

    public RootDeviceExportingQueue() {
        v = new Vector();
        waiting = false;
    }

    public synchronized void addRootDevice(DeviceNode dev) {
        v.addElement(dev);
        if (waiting) notify();
    }

    public synchronized DeviceNode getRootDevice() {
        while (v.isEmpty()) {
            waiting = true;
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        DeviceNode dev = (DeviceNode) v.firstElement();
        v.remove(0);
        return dev;
    }

    /**
     * 
     * @param udn <code>String</code> rappresentaing the UDN of the device to remove 
     * @return a root <code>DeviceNode</code> that have UDN equals to the passed
     * 		 udn <code>String</code> or contain a device with the spcified UDN
     */
    public synchronized DeviceNode removeRootDevice(String udn) {
        for (int i = 0; i < v.size(); i++) {
            DeviceNode aux = (DeviceNode) v.elementAt(i);
            if (aux.contains(udn)) {
                v.remove(i);
                return aux;
            }
        }
        return null;
    }
}
