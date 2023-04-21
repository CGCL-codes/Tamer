package com.volantis.styling.device;

/**
 * An enumeration of the possible outlooks on the information provided about
 * the device.
 */
public class DeviceOutlook {

    /**
     * The device information is reasonably accurate but there are gaps.
     */
    public static final DeviceOutlook REALISTIC = new DeviceOutlook("REALISTIC");

    /**
     * The device information is perfect.
     */
    public static final DeviceOutlook OPTIMISTIC = new DeviceOutlook("OPTIMISTIC");

    /**
     * The name.
     */
    private final String name;

    /**
     * Initialise.
     *
     * @param name The name.
     */
    private DeviceOutlook(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
