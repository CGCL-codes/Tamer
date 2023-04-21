package org.tanso.fountain.router.synchronizeroutingtable;

import java.util.Timer;

/**
 * The class is used to start timer which schedule the UpdateRoutingFile to update routing file.
 * @author Song Huanhuan
 *
 */
public class SynchronizeRoutingTable {

    /**
	 * timer:Timer used to schedule
	 * seconds:param from user
	 */
    private final Timer timer = new Timer();

    private final int seconds;

    /**
     * constructor
     * @param seconds
     */
    public SynchronizeRoutingTable(int seconds) {
        this.seconds = seconds;
    }

    /**
     * this method is used to schedule UpdateRoutingFile.
     *
     */
    public void start() {
        UpdateRoutingFile updateRoutingFile = new UpdateRoutingFile();
        timer.schedule(updateRoutingFile, 1000, 1000 * seconds);
    }

    public void cancle() {
        timer.cancel();
    }
}
