package com.sl.eventlog.web.action.setting;

import com.opensymphony.xwork2.ActionSupport;
import com.sl.eventlog.service.server.SeverCondition;

public class ServerAction extends ActionSupport {

    private int maxHeapMemory;

    private int usedHeapMemory;

    private int unUsedHeapMemory;

    private int maxMemory;

    private int processNum;

    private String hostName;

    private String ipAddress;

    private String operSystem;

    private String operSystemVersion;

    private String operSystemTime;

    @SuppressWarnings("static-access")
    public String getServerMsg() {
        SeverCondition condition = new SeverCondition();
        maxHeapMemory = condition.getHeapMaxMemory();
        usedHeapMemory = condition.getHeapUsedMemory();
        unUsedHeapMemory = condition.getHeapUnUsedMemory();
        maxMemory = condition.getMaxMemory();
        processNum = condition.getProcessNum();
        hostName = condition.getLocalHostName();
        ipAddress = condition.getLocalHostIp();
        operSystem = condition.getOperSystem();
        operSystemVersion = condition.getOperSystemVersion();
        operSystemTime = condition.getCurrentTime();
        return this.SUCCESS;
    }

    public int getMaxHeapMemory() {
        return maxHeapMemory;
    }

    public int getUsedHeapMemory() {
        return usedHeapMemory;
    }

    public int getUnUsedHeapMemory() {
        return unUsedHeapMemory;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

    public int getProcessNum() {
        return processNum;
    }

    public String getHostName() {
        return hostName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getOperSystem() {
        return operSystem;
    }

    public String getOperSystemVersion() {
        return operSystemVersion;
    }

    public String getOperSystemTime() {
        return operSystemTime;
    }
}
