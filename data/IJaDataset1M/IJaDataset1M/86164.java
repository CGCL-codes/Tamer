package org.engine.network;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.engine.Universe;

/**
 * The Class ReturnNetworkThread.
 */
public class ReturnNetworkThread extends Thread {

    /** The uni. */
    private Universe uni;

    /** The millis. */
    private long millis;

    /** The still change. */
    boolean stillChange = true;

    /**
     * Instantiates a new return network thread.
     * 
     * @param uni
     *            the uni
     * @param millis
     *            the millis
     */
    public ReturnNetworkThread(Universe uni, long millis) {
        this.uni = uni;
        this.millis = millis;
    }

    @Override
    public void run() {
        uni.addBusyListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                stillChange = false;
            }
        });
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (stillChange) {
            uni.setBusy(false);
        }
    }
}
