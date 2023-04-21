package com.monad.homerun.pkg.upb;

import org.cdp1802.upb.UPBDeviceI;
import org.cdp1802.upb.UPBProductI;
import com.monad.homerun.object.Type;

/**
 * A wrapper class around HomeRun Type objects that exposes
 * semantics of a UPBProduct. 
 */
public class UpbProduct implements UPBProductI {

    Type deviceType = null;

    public UpbProduct(Type deviceType) {
        this.deviceType = deviceType;
    }

    /**
   * Get the product ID for this product.  Product IDs are
   * unique within a manufacturer ID.
   *
   * @return product ID 
   */
    public int getProductID() {
        return Integer.parseInt(deviceType.getProperty("product ID"));
    }

    /**
   * Get the name of this product
   *
   * @return name of this product
   */
    public String getProductName() {
        return deviceType.getProperty("product name");
    }

    /**
   * Get the kind of product
   * 
   * @return kind of this product
   */
    public int getProductKind() {
        return Integer.parseInt(deviceType.getProperty("product kind"));
    }

    /**
   * Get the manufacturer ID code for this product
   *
   * <P>Manufacturer IDs are unique
   *
   * @return manufacturer ID for this product
   */
    public int getManufacturerID() {
        return Integer.parseInt(deviceType.getProperty("manufacturer ID"));
    }

    /**
   * Is this product generally capable of being dimmed?  Not all 
   * products are and even for products that are dimming capable,
   * the dimming function can be turned off for select devices.
   *
   * <P>So to know if a particular device is dimmable, you need to 
   * check the UPBDevice.isDimmable() property
   *
   * @return true if products devices are capable of being dimmed
   */
    public boolean isDimmingCapable() {
        String canDim = deviceType.getProperty("dimmable");
        return canDim != null && "true".equals(canDim);
    }

    /**
     * Get number of transmit components for this products devices.
     *
     * @return transmit components for this product devices.
     */
    public int getTransmitComponentCount() {
        return Integer.parseInt(deviceType.getProperty("transmit components"));
    }

    /**
	 * Get number of receive components for this products devices.
	 *
	 * <P>NOTE: Often (though not always), the number of receive components
	 * tells you how many links the device can be attached to (a link
	 * being a receive component).  For most light and appliance
	 * modules, this is the case.
	 *
	 * @return number of receive components for this products devices
	 */
    public int getReceiveComponentCount() {
        return Integer.parseInt(deviceType.getProperty("receive components"));
    }

    /**
	 * Get the number of channels on this products devices.
	 *
	 * <P>In UPB terminology, a channel is load that can be controlled
	 * in a UPB device.  Most UPB devices have a single channel, but some
	 * specialty UPB devices can have >1
	 *
	 * @return number of channels for this products devices.
	 */
    public int getChannelCount() {
        int count = 1;
        String countStr = deviceType.getProperty("channels");
        if (countStr != null) {
            count = Integer.parseInt(countStr);
        }
        return count;
    }

    /**
	 * Get the primary channel number commands should apply to when
	 * no specific channel is specified.
	 *
	 * @return primary channel number (-1 for devices with NO channels)
	 */
    public int getPrimaryChannel() {
        int primary = 1;
        String primaryStr = deviceType.getProperty("primary channel");
        if (primaryStr != null) {
            primary = Integer.parseInt(primaryStr);
        }
        return primary;
    }

    /**
	 * Get the class used for instances of devices that use this product
	 *
	 * <P>The base class (UPBDevice) is fine for devices that can just
	 * be turned on and off.  However, more support is needed for dimming
	 * devices (UPBDimmerDevice), IO modules (UPBIODevice) and control
	 * panels (UPBControllerDevice).  All classes descend from UPBDevice.
	 *
	 * <P>This class is used when importing the UPStart file or doing device
	 * discovery to create appropriate devices instances for each device
	 *
	 * @return class to use for devices of this product
	 */
    public Class<? extends UPBDeviceI> getDeviceClass() {
        String classStr = deviceType.getProperty("class");
        Class theClass = null;
        try {
            theClass = Class.forName(classStr);
        } catch (ClassNotFoundException cnfe) {
            ;
        }
        return (Class<? extends UPBDeviceI>) theClass;
    }

    /**
	 * Most UPB devices can be asked to send their current state
	 * out.  However, some cannot (multi-button controllers is
	 * the only device like this I've come across).
	 *
	 * @return true if products devices can be queried for their current state
	 */
    public boolean isStatusQueryable() {
        String canQuery = deviceType.getProperty("queryable");
        return canQuery != null && "true".equals(canQuery);
    }
}
