package org.xmlvm.ios;

import java.util.*;
import org.xmlvm.XMLVMSkeletonOnly;

@XMLVMSkeletonOnly
public interface EAAccessoryDelegate {

    /**
	 * - (void)accessoryDidDisconnect:(EAAccessory *)accessory ;
	 */
    public abstract void accessoryDidDisconnect(EAAccessory accessory);
}
