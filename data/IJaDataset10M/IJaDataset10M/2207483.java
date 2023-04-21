package org.ws4d.java.util;

/**
 * Whenever the DeviceStateChecker is used, there has to be a callback, which
 * has to implement this Interface.
 * 
 */
public interface IDeviceStateCheckerCallback {

    /**
	 * If probing the device was successful, the DeviceStateChecker calls
	 * handleDeviceStateUp.
	 * 
	 * @param xaddrs xaddress.
	 * @param endpointAddress endpoint.
	 * @param metadataVersion metadata version.
	 */
    void handleDeviceStateUp(String xaddrs, String endpointAddress, int metadataVersion);

    /**
	 * If probing the device wasn't successful, the DeviceStateChecker calls
	 * handleDeviceStateDown.
	 * 
	 * @param xaddrs xaddress.
	 */
    void handleDeviceStateDown(String xaddrs);
}
