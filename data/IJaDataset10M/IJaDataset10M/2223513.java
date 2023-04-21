package org.beblue.jna.usb;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

/**
 * This class represents the native structure <code>struct usb_dev_handle</code>
 * .
 * 
 * @author Mario Boikov
 * 
 */
public class usb_dev_handle extends PointerType {

    /**
     * Instantiates a new usb_dev_handle.
     */
    public usb_dev_handle() {
        super();
    }

    /**
     * Instantiates a new usb_dev_handle.
     *
     * @param address the address
     */
    public usb_dev_handle(Pointer address) {
        super(address);
    }
}
