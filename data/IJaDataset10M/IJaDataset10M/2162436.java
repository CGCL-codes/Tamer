package org.dago.wecommand.wiimote;

import java.util.List;

/**
 * Listener for WiimoteDetector events.
 * 
 * @see WiimoteDetector
 */
public interface WiimoteDetection {

    /**
	 * Called to give the wiimote detection result
	 * @param macs the list of MAC address belonging to the visible Wiimotes
	 */
    void result(List<String> macs);

    /**
	 * Called when an error occurred during the detection
	 * @param error the error message
	 */
    void error(String error);
}
