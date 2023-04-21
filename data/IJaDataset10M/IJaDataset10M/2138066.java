package com.skjolberg.ddr.wurfl;

import java.util.HashMap;
import java.util.Map;
import com.skjolberg.ddr.device.CapabilityDevice;

/**
 * 
 * Default implementation of a active wurfl device.
 * 
 * This instance is thread safe.
 * 
 * @author Thomas Rorvik Skjolberg
 *
 */
public class WurflCapabilityDevice extends CapabilityDevice implements WurflDevice {

    private static final long serialVersionUID = 1L;

    protected String userAgent;

    public static WurflCapabilityDevice newInstance() {
        return new WurflCapabilityDevice();
    }

    public static WurflCapabilityDevice newInstance(Map<String, String> internedStringMap) {
        WurflCapabilityDevice device = new WurflCapabilityDevice();
        device.setInternedStringMap(internedStringMap);
        return device;
    }

    public WurflCapabilityDevice() {
        this.localCapabilities = new HashMap<String, String>();
    }

    /**
	 * 
	 * Get the user-agent for this device.
	 * 
	 * @return the user-agent, or null if the device has no user-agent
	 */
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
