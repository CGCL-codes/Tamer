package org.commsuite.jmx.impl;

import java.util.ArrayList;
import java.util.List;
import org.commsuite.devices.Device;
import org.commsuite.devices.DeviceManager;
import org.commsuite.jmx.IJMXDeviceManager;

/**
 * JMX class getting out devices
 * 
 * @since 1.0
 * @author Marek Musielak
 */
public class JMXDeviceManager implements IJMXDeviceManager {

    private DeviceManager deviceManager;

    public void setDeviceManager(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    /**
	 * Returns list containing all devices.toString()
	 * 
	 * @return list of devices strings
	 */
    public List<String> getDeviceList() {
        List<Device> list = deviceManager.getDevices();
        List<String> result = new ArrayList<String>();
        for (Device device : list) {
            result.add(device.toString());
        }
        return result;
    }
}
