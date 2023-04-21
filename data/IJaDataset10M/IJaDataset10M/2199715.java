package org.chargecar.honda;

import javax.swing.SwingUtilities;
import edu.cmu.ri.createlab.util.mvc.View;
import org.chargecar.serial.streaming.StreamingSerialPortDeviceConnectionStateListener;
import org.chargecar.serial.streaming.StreamingSerialPortDeviceReadingStateListener;

/**
 * @author Chris Bartley (bartley@cmu.edu)
 */
@SuppressWarnings({ "NoopMethodInAbstractClass" })
public abstract class StreamingSerialPortDeviceView<T> extends View<T> implements StreamingSerialPortDeviceConnectionStateListener, StreamingSerialPortDeviceReadingStateListener {

    public final void handleConnectionStateChange(final boolean isConnected) {
        if (SwingUtilities.isEventDispatchThread()) {
            handleConnectionStateChangeInGUIThread(isConnected);
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    handleConnectionStateChangeInGUIThread(isConnected);
                }
            });
        }
    }

    /**
    * Method for handling the reading state change, guaranteed to run in the GUI thread.  Does nothing by default.
    */
    protected void handleConnectionStateChangeInGUIThread(final boolean isConnected) {
    }

    public final void handleReadingStateChange(final boolean isReading) {
        if (SwingUtilities.isEventDispatchThread()) {
            handleReadingStateChangeInGUIThread(isReading);
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    handleReadingStateChangeInGUIThread(isReading);
                }
            });
        }
    }

    /**
    * Method for handling the reading state change, guaranteed to run in the GUI thread.  Does nothing by default.
    */
    protected void handleReadingStateChangeInGUIThread(final boolean isReading) {
    }
}
