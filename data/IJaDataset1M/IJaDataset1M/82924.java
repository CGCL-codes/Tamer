package com.netflexitysolutions.amazonws.ec2;

public class BlockDeviceMapping {

    private String deviceName;

    private String virtualName;

    public BlockDeviceMapping() {
    }

    /**
	 * @param deviceName
	 * @param virtualName
	 */
    public BlockDeviceMapping(String deviceName, String virtualName) {
        super();
        this.deviceName = deviceName;
        this.virtualName = virtualName;
    }

    /**
	 * @return the deviceName
	 */
    public String getDeviceName() {
        return deviceName;
    }

    /**
	 * @param deviceName the deviceName to set
	 */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
	 * @return the virtualName
	 */
    public String getVirtualName() {
        return virtualName;
    }

    /**
	 * @param virtualName the virtualName to set
	 */
    public void setVirtualName(String virtualName) {
        this.virtualName = virtualName;
    }
}
