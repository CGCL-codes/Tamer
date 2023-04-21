package me.buick.util.snmp.core.pojo;

import java.io.Serializable;

/**
 * Version 1.0
 * 
 * @author buick
 *
 */
public class CpuOverviewInfoPojo implements Serializable {

    private static final long serialVersionUID = 7523828585804055563L;

    private long cpuRawUser;

    private long cpuRawNice;

    private long cpuRawSystem;

    private long cpuRawIdle;

    private long cpuRawWait;

    private long cpuRawKernel;

    private long cpuRawInterrupt;

    private double cpuLoad;

    public long getCpuRawUser() {
        return cpuRawUser;
    }

    public void setCpuRawUser(long cpuRawUser) {
        this.cpuRawUser = cpuRawUser;
    }

    public long getCpuRawNice() {
        return cpuRawNice;
    }

    public void setCpuRawNice(long cpuRawNice) {
        this.cpuRawNice = cpuRawNice;
    }

    public long getCpuRawSystem() {
        return cpuRawSystem;
    }

    public void setCpuRawSystem(long cpuRawSystem) {
        this.cpuRawSystem = cpuRawSystem;
    }

    public long getCpuRawIdle() {
        return cpuRawIdle;
    }

    public void setCpuRawIdle(long cpuRawIdle) {
        this.cpuRawIdle = cpuRawIdle;
    }

    public long getCpuRawWait() {
        return cpuRawWait;
    }

    public void setCpuRawWait(long cpuRawWait) {
        this.cpuRawWait = cpuRawWait;
    }

    public long getCpuRawKernel() {
        return cpuRawKernel;
    }

    public void setCpuRawKernel(long cpuRawKernel) {
        this.cpuRawKernel = cpuRawKernel;
    }

    public long getCpuRawInterrupt() {
        return cpuRawInterrupt;
    }

    public void setCpuRawInterrupt(long cpuRawInterrupt) {
        this.cpuRawInterrupt = cpuRawInterrupt;
    }

    public void setCpuLoad(double cpuLoad) {
        this.cpuLoad = cpuLoad;
    }

    public double getCpuLoad() {
        return this.cpuLoad;
    }
}
