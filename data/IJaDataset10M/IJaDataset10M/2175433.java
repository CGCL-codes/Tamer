package org.chargecar.lcddisplay;

import edu.cmu.ri.createlab.device.CreateLabDeviceProxy;
import edu.cmu.ri.createlab.device.connectivity.BaseCreateLabDeviceConnectivityManager;
import org.apache.log4j.Logger;

/**
 * @author Chris Bartley (bartley@cmu.edu)
 */
final class LCDConnectivityManager extends BaseCreateLabDeviceConnectivityManager {

    private static final Logger LOG = Logger.getLogger(LCDConnectivityManager.class);

    protected CreateLabDeviceProxy scanForDeviceAndCreateProxy() {
        LOG.debug("LCDConnectivityManager.scanForDeviceAndCreateProxy()");
        if ((ChargeCarLCD.getAvailableSerialPorts() != null) && (!ChargeCarLCD.getAvailableSerialPorts().isEmpty())) {
            for (final String portName : ChargeCarLCD.getAvailableSerialPorts()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("LCDConnectivityManager.scanForDeviceAndCreateProxy(): checking serial port [" + portName + "]");
                }
                final CreateLabDeviceProxy proxy = LCDProxy.create(portName);
                if (proxy == null) {
                    LOG.debug("LCDConnectivityManager.scanForDeviceAndCreateProxy(): connection failed, maybe it's not the device we're looking for?");
                } else {
                    LOG.debug("LCDConnectivityManager.scanForDeviceAndCreateProxy(): connection established, returning proxy!");
                    ChargeCarLCD.removeAvailableSerialPort(portName);
                    return proxy;
                }
            }
        } else {
            LOG.debug("LCDConnectivityManager.scanForDeviceAndCreateProxy(): No available serial ports, returning null.");
        }
        return null;
    }
}
