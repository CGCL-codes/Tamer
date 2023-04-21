package org.jnav.location;

import java.util.Enumeration;
import java.util.Vector;
import org.jnav.location.bluetooth.BluetoothLocationController;
import org.jnav.location.bluetooth.BluetoothLocationDevice;
import org.jnav.location.local.LocalLocationController;
import org.jnav.location.local.LocalLocationDevice;

/**
 *
 * @author Matthias Lohr <mail@matthias-lohr.net>
 */
public class LocationController implements PositionListener {

    private Vector positionListeners;

    public LocationController() {
        this.positionListeners = new Vector();
    }

    public void addPositionListener(PositionListener positionListener) {
        if (this.equals(positionListener)) {
            return;
        }
        if (positionListeners.contains(positionListener)) {
            return;
        }
        positionListeners.addElement(positionListener);
    }

    public LocationDevice getDevice(String identifier) {
        if (identifier.equals("internal")) {
            return new LocalLocationDevice();
        } else if (identifier.length() == 12) {
            return new BluetoothLocationDevice(identifier);
        }
        return null;
    }

    public boolean removePositionListener(PositionListener positionListener) {
        if (this.positionListeners.contains(positionListener)) {
            this.positionListeners.removeElement(positionListener);
            return true;
        } else {
            return false;
        }
    }

    public void startLocation(String deviceIdentifier) {
        LocationDevice device = this.getDevice(deviceIdentifier);
        if (device != null) {
            device.startLocation(this);
        }
    }

    public void stopLocation() {
    }

    public void startScan(LocationDeviceListener locationDeviceListener) {
        if (System.getProperty("microedition.location.version") != null) {
            LocalLocationController localController = new LocalLocationController();
            localController.startScan(locationDeviceListener);
        }
        try {
            BluetoothLocationController bluetoothController = new BluetoothLocationController();
            bluetoothController.startScan(locationDeviceListener);
        } catch (NoClassDefFoundError e) {
        }
    }

    public void updatePosition(GeoPosition position) {
        Enumeration locEnum = this.positionListeners.elements();
        PositionListener listener;
        while (locEnum.hasMoreElements()) {
            listener = (PositionListener) locEnum.nextElement();
            listener.updatePosition(position);
        }
    }
}
