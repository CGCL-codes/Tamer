package net.ocdstudio.modans.core.devices.simple;

import net.ocdstudio.modans.core.communication.IProtocol;
import net.ocdstudio.modans.core.devices.IDeviceProperties;
import net.ocdstudio.modans.core.devices.IDevice.DeviceTypes;
import net.ocdstudio.modans.core.devices.wireless.IWirelessPoint;

public class SimpleDeviceProperties implements IDeviceProperties {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5871943829790306294L;

    private String dName;

    private DeviceTypes dType;

    private IWirelessPoint dPoint = null;

    private IProtocol dProt = null;

    public SimpleDeviceProperties(String name) {
        this.dName = name;
    }

    public String getDeviceName() {
        return (dName);
    }

    public DeviceTypes getDeviceType() {
        return (dType);
    }

    public IWirelessPoint getWirelessPoint() {
        return (dPoint);
    }

    public void setDeviceName(String n) {
        this.dName = n;
    }

    public void setDeviceType(DeviceTypes type) {
        this.dType = type;
    }

    public void setDeviceWirelessPoint(IWirelessPoint p) {
        this.dPoint = p;
    }

    public IProtocol getProtocol() {
        return (dProt);
    }

    public void setProtocol(IProtocol prot) {
        this.dProt = prot;
    }
}
