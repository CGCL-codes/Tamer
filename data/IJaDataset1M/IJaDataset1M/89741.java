package org.chargecar.lcddisplay;

import java.util.concurrent.Semaphore;
import edu.cmu.ri.createlab.device.connectivity.CreateLabDeviceConnectionEventListener;
import edu.cmu.ri.createlab.device.connectivity.CreateLabDeviceConnectionState;
import edu.cmu.ri.createlab.device.connectivity.CreateLabDeviceConnectivityManager;
import org.apache.log4j.Logger;

/**
 * @author Chris Bartley (bartley@cmu.edu)
 * @author Paul Dille (pdille@andrew.cmu.edu)
 */
final class LCDCreator {

    private static final Logger LOG = Logger.getLogger(LCDCreator.class);

    private final Semaphore connectionCompleteSemaphore = new Semaphore(1);

    private final CreateLabDeviceConnectivityManager connectivityManager = new LCDConnectivityManager();

    private LCD lcd = null;

    LCDCreator() {
        LOG.debug("LCDCreator.handleConnectionStateChange(): Connecting to LCD...this may take a few seconds...");
        connectivityManager.addConnectionEventListener(new CreateLabDeviceConnectionEventListener() {

            public void handleConnectionStateChange(final CreateLabDeviceConnectionState oldState, final CreateLabDeviceConnectionState newState, final String portName) {
                if (CreateLabDeviceConnectionState.CONNECTED.equals(newState)) {
                    LOG.debug("LCDCreator.handleConnectionStateChange(): Connected");
                    connectionCompleteSemaphore.release();
                    lcd = (LCDProxy) connectivityManager.getCreateLabDeviceProxy();
                } else if (CreateLabDeviceConnectionState.DISCONNECTED.equals(newState)) {
                    LOG.debug("LCDCreator.handleConnectionStateChange(): Disconnected");
                    lcd = null;
                } else if (CreateLabDeviceConnectionState.SCANNING.equals(newState)) {
                    LOG.debug("LCDCreator.handleConnectionStateChange(): Scanning...");
                } else {
                    LOG.error("LCDCreator.handleConnectionStateChange(): Unexpected CreateLabDeviceConnectionState [" + newState + "]");
                    lcd = null;
                }
            }
        });
        LOG.trace("LCDCreator.LCDCreator(): 1) acquiring connection lock");
        connectionCompleteSemaphore.acquireUninterruptibly();
        LOG.trace("LCDCreator.LCDCreator(): 2) connecting");
        connectivityManager.scanAndConnect();
        LOG.trace("LCDCreator.LCDCreator(): 3) waiting for connection to complete");
        connectionCompleteSemaphore.acquireUninterruptibly();
        LOG.trace("LCDCreator.LCDCreator(): 4) releasing lock");
        connectionCompleteSemaphore.release();
        LOG.trace("LCDCreator.LCDCreator(): 5) make sure we're actually connected");
        if (!CreateLabDeviceConnectionState.CONNECTED.equals(connectivityManager.getConnectionState())) {
            LOG.error("LCDCreator.LCDCreator(): Failed to connect to the LCD!  Aborting.");
            System.exit(1);
        }
        LOG.trace("LCDCreator.LCDCreator(): 6) All done!");
    }

    LCD getLCD() {
        return lcd;
    }
}
