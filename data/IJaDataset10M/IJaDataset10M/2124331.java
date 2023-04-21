package mpc.pong;

import java.util.*;
import javax.bluetooth.*;

class ServiceDiscoveryUtils implements DiscoveryListener {

    private static ServiceDiscoveryUtils instance;

    public static synchronized ServiceDiscoveryUtils getInstance() {
        if (instance == null) instance = new ServiceDiscoveryUtils();
        return instance;
    }

    private int[] attrSet = { 0x0003, 0x0100 };

    private boolean waitV;

    private Vector list;

    protected ServiceDiscoveryUtils() {
        list = new Vector();
    }

    private synchronized void waitPrepare() {
        waitV = true;
        notifyAll();
    }

    private synchronized void waitBlock() {
        while (waitV) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    private synchronized void waitDone() {
        waitV = false;
        notifyAll();
    }

    public void deviceDiscovered(RemoteDevice device, DeviceClass devClass) {
        list.addElement(device);
    }

    public void inquiryCompleted(int arg0) {
        waitDone();
    }

    public void serviceSearchCompleted(int arg0, int arg1) {
        waitDone();
    }

    public void servicesDiscovered(int arg0, ServiceRecord[] srvc) {
        list.addElement(srvc);
    }

    synchronized RemoteDevice[] getNearbyDevices() {
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent discoveryAgent = localDevice.getDiscoveryAgent();
            waitPrepare();
            discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);
            waitBlock();
            return discoveryAgent.retrieveDevices(DiscoveryAgent.CACHED);
        } catch (Exception e) {
            return null;
        }
    }

    synchronized String getServiceUrl(UUID uuid) {
        try {
            RemoteDevice[] devices = getNearbyDevices();
            if (devices == null) return null;
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent discoveryAgent = localDevice.getDiscoveryAgent();
            UUID[] uuids = new UUID[1];
            uuids[0] = uuid;
            for (int i = 0; i < devices.length; i++) {
                list.removeAllElements();
                waitPrepare();
                discoveryAgent.searchServices(attrSet, uuids, devices[i], this);
                waitBlock();
                for (int j = 0; j < list.size(); j++) {
                    ServiceRecord[] r = (ServiceRecord[]) list.elementAt(j);
                    for (int k = 0; k < r.length; k++) {
                        return r[k].getConnectionURL(0, false);
                    }
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
