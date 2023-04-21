package org.chargecar.serial.streaming;

/**
 * @author Chris Bartley (bartley@cmu.edu)
 */
public interface StreamingSerialPortDeviceConnectionStateListener {

    void handleConnectionStateChange(final boolean isConnected);
}
