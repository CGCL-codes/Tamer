package de.mnl.srcpd4cu.srcp;

/**
 * This interface defines additional methods for a device with an address.
 * 
 * @author Michael N. Lipp
 */
public interface AddressableDevice extends Device {

    /**
     * Return the address assigned to the device.
     * 
     * @return the address
     */
    long getAddress();
}
