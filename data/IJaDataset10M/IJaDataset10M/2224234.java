package net.emotivecloud.scheduler.hadoop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import net.emotivecloud.scheduler.hadoop.Slave;

public class Cluster {

    private String clusterName;

    private String jobTrackerId;

    private String jobTrackerIp;

    private String nameNodeId;

    private String nameNodeIp;

    private HashMap slaves;

    private int cardinality;

    private int counter;

    private int defaultMemory;

    private int defaultCPU;

    private String defaultIP;

    private int maxSlaves;

    private int replicationFactor;

    public Cluster(int replicationFactor) {
        this.setCardinality(0);
        this.setCounter(0);
        this.setDefaultCPU(100);
        this.setDefaultIP("0.0.0.0");
        this.setDefaultMemory(512);
        this.slaves = new HashMap();
        this.setReplicationFactor(replicationFactor);
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getJobTrackerId() {
        return jobTrackerId;
    }

    public void setJobTrackerId(String jobTrackerId) {
        this.jobTrackerId = jobTrackerId;
    }

    public String getJobTrackerIp() {
        return jobTrackerIp;
    }

    public void setJobTrackerIp(String jobTrackerIp) {
        this.jobTrackerIp = jobTrackerIp;
    }

    public String getNameNodeId() {
        return nameNodeId;
    }

    public void setNameNodeId(String nameNodeId) {
        this.nameNodeId = nameNodeId;
    }

    public String getNameNodeIp() {
        return nameNodeIp;
    }

    public void setNameNodeIp(String nameNodeIp) {
        this.nameNodeIp = nameNodeIp;
    }

    public HashMap getSlaves() {
        return slaves;
    }

    public void addSlave(Slave slave) {
        this.slaves.put(slave.getId(), slave);
    }

    public void deleteSlave(Slave slave) {
        this.slaves.remove(slave.getId());
    }

    public void setCardinality(int cardinality) {
        this.cardinality = cardinality;
    }

    public int getCardinality() {
        return cardinality;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public int getDefaultMemory() {
        return defaultMemory;
    }

    public void setDefaultMemory(int defaultMemory) {
        this.defaultMemory = defaultMemory;
    }

    public int getDefaultCPU() {
        return defaultCPU;
    }

    public void setDefaultCPU(int defaultCPU) {
        this.defaultCPU = defaultCPU;
    }

    public String getDefaultIP() {
        return defaultIP;
    }

    public void setDefaultIP(String defaultIP) {
        this.defaultIP = defaultIP;
    }

    public int getMaxSlaves() {
        return maxSlaves;
    }

    public void setMaxSlaves(int maxSlaves) {
        this.maxSlaves = maxSlaves;
    }

    public int getReplicationFactor() {
        return replicationFactor;
    }

    public void setReplicationFactor(int replicationFactor) {
        this.replicationFactor = replicationFactor;
    }
}
