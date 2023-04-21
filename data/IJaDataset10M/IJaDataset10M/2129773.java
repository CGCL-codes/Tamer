package org.audiocomplex.bt;

import javax.bluetooth.*;
import java.util.Vector;
import org.audiocomplex.util.*;

public class BluetoothDiscovery implements DiscoveryListener {

    private BluetoothDiscoveryListener listener;

    private DiscoveryAgent agent;

    private Vector devices = new Vector();

    public BluetoothDiscovery(BluetoothDiscoveryListener listener) {
        this.listener = listener;
        try {
            agent = LocalDevice.getLocalDevice().getDiscoveryAgent();
        } catch (Exception e) {
            Logger.log(e);
        }
    }

    public boolean start() {
        try {
            agent.startInquiry(DiscoveryAgent.GIAC, this);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void cancel() {
        agent.cancelInquiry(this);
    }

    private void scanNextDevice() {
        if (devices.isEmpty()) {
            listener.discoveryCompleted();
            return;
        }
        RemoteDevice device = (RemoteDevice) devices.firstElement();
        devices.removeElementAt(0);
        try {
            UUID[] uuidSet = new UUID[] { new UUID("1101", true) };
            agent.searchServices(null, uuidSet, device, this);
        } catch (Exception e) {
            Logger.log(e);
            listener.discoveryCompleted();
        }
    }

    /*** DiscoveryListener ***/
    public void inquiryCompleted(int param) {
        listener.discoveryCompleted();
    }

    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass dc) {
        try {
            String name = remoteDevice.getFriendlyName(true);
            String url = "btspp://" + remoteDevice.getBluetoothAddress() + ":1";
            listener.serviceDiscovered(name, url);
        } catch (Exception e) {
        }
    }

    public void servicesDiscovered(int transID, ServiceRecord[] serviceRecord) {
        for (int i = 0; i < serviceRecord.length; i++) {
            ServiceRecord sr = serviceRecord[i];
            try {
                String name = sr.getHostDevice().getFriendlyName(true);
                String url = sr.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                Logger.log(url);
                listener.serviceDiscovered(name, url);
            } catch (Exception e) {
            }
        }
    }

    public void serviceSearchCompleted(int transID, int respCode) {
        scanNextDevice();
    }
}

;
