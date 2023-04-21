package net.redlightning.dht;

import java.io.File;

public class DHTConfiguration {

    private boolean routerBootstrap = true;

    private boolean persistingID = true;

    private boolean multiHoming = true;

    private File nodeCachePath = new File("/sdcard/redtorrent.dht");

    private int listeningPort = 6881;

    /**
	 * @param routerBootstrap the routerBootstrap to set
	 */
    public synchronized void setRouterBootstrap(final boolean routerBootstrap) {
        this.routerBootstrap = routerBootstrap;
    }

    /**
	 * @param persistingID the persistingID to set
	 */
    public synchronized void setPersistingID(final boolean persistingID) {
        this.persistingID = persistingID;
    }

    /**
	 * @param multiHoming the multiHoming to set
	 */
    public synchronized void setMultiHoming(final boolean multiHoming) {
        this.multiHoming = multiHoming;
    }

    /**
	 * @param nodeCachePath the nodeCachePath to set
	 */
    public synchronized void setNodeCachePath(final File nodeCachePath) {
        this.nodeCachePath = nodeCachePath;
    }

    /**
	 * @param listeningPort the listeningPort to set
	 */
    public synchronized void setListeningPort(final int listeningPort) {
        this.listeningPort = listeningPort;
    }

    public boolean isRouterBootstrap() {
        return routerBootstrap;
    }

    public boolean isPersistingID() {
        return persistingID;
    }

    public File getNodeCachePath() {
        return nodeCachePath;
    }

    public int getListeningPort() {
        return listeningPort;
    }

    public boolean isMultiHoming() {
        return multiHoming;
    }
}
