package com.ohua.clustering.snapshot;

/**
 * The main loop of the snapshot algorithm.<br>
 * This class periodically forces each running node to take a snapshot of its running slave.
 * Furthermore it is responsible for coordination of the nodes during the snapshot period.
 * @author sertel
 * 
 */
public class MasterSnapshotManager implements Runnable {

    public void run() {
        throw new UnsupportedOperationException("MasterSnapshotManager.run(...) not yet implemented");
    }
}
