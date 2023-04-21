package com.tailrank.lbpool;

import java.util.*;

/**
 * Track the status of hosts and hold metadata about their status.
 */
public class LBHostStatus implements Comparable {

    public String hostname = null;

    /**
     * When true this host has been flagged as offline.
     */
    private boolean offline;

    private boolean offlineSlaveOverload = false;

    /**
     * When true this host is a master.
     */
    private boolean master;

    /**
     * When this machine is a slave record the current host that it considers a
     * master.
     */
    private String master_host = null;

    /**
     * Specific offline code as to why this host is marked offline.
     */
    public int offline_code;

    /**
     * If a host is offline we save the message here.
     */
    public String offline_message = null;

    /**
     * Load is the total number of seconds that all connections have been
     * executing valid SQL for.  
     */
    public int load = 0;

    /**
     * The mean load... computed as load / numConnections.
     */
    public long load_mean = 0;

    public int numConnections = 0;

    /**
     * Open connections for this individual host.
     */
    public List openConnections = Collections.synchronizedList(new ArrayList());

    public boolean isOffline() {
        return offline;
    }

    public boolean isMaster() {
        return master;
    }

    public boolean isOfflineSlaveOverload() {
        return offlineSlaveOverload;
    }

    public void setOffline(boolean v) {
        this.offline = v;
    }

    public void setMaster(boolean v) {
        this.master = v;
    }

    public void setMasterHost(String v) {
        this.master_host = v;
    }

    public void setOfflineSlaveOverload(boolean v) {
        this.offlineSlaveOverload = v;
    }

    public int compareTo(Object o1) {
        LBHostStatus h1 = (LBHostStatus) this;
        LBHostStatus h2 = (LBHostStatus) o1;
        if (h1.numConnections > h2.numConnections) return 1;
        if (h1.numConnections < h2.numConnections) return -1;
        return 0;
    }

    public String toString() {
        return hostname + " - offline: " + offline + ", load: " + load + ", numConnections: " + numConnections + ", offline_message: " + offline_message;
    }
}
