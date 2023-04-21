package com.xohm.utils;

import com.xohm.base.structs.LinkStatusInfo;

/**
 * <B>Singleton class implementing the utility interface for the WiMAX 
 * Connection manager.
 * </B><br><br>
 *
 * <font size=-1>Open source WiMAX connection manager<br>
 * � Copyright Sprint Nextel Corp. 2008</font><br><br>
 *
 * @author Sachin Kumar 
 */
public class CMUtils implements CMUtilInterface {

    /**
	 * Private Constructor.
	 */
    private CMUtils() {
    }

    /**
	 * This method creates a instance of CMUtils class, if not already present
	 * and returns the instance.
	 * @return CMUtilInterface - implemented interface
	 */
    public static CMUtilInterface getInstance() {
        CMUtilInterface instance = null;
        try {
            Class classObj = Class.forName(Properties.utilClass);
            instance = (CMUtilInterface) classObj.newInstance();
        } catch (Exception ex) {
            instance = new CMUtils();
        }
        return instance;
    }

    /**
	 * This method returns the number of signal strength bars for
	 * connected network using the specified link details and the
	 * RSSI and bar mapping from the configuration file.
	 * 
	 * @param linkStatusInfo LinkStatusInfo - link details
	 * @return int - number of bars
	 */
    public int getSignalStrength(LinkStatusInfo linkStatusInfo) {
        int strength = 0;
        int rssi = linkStatusInfo.getRssi();
        if (rssi >= Properties.minRSSIThreeBar) {
            strength = 3;
        } else if (rssi < Properties.minRSSIThreeBar && rssi >= Properties.minRSSITwoBar) {
            strength = 2;
        } else if (rssi < Properties.minRSSITwoBar && rssi >= Properties.minRSSIOneBar) {
            strength = 1;
        } else {
            strength = 0;
        }
        return strength;
    }
}
