package org.albianj.protocol.mgr;

import com.google.gson.Gson;

public class ClientIptable {

    private String appName;

    private String kernelId;

    private String ip;

    private String startTime;

    private EngineState state = EngineState.Normal;

    private String serialId;

    public ClientIptable() {
    }

    public ClientIptable(String json) {
        Gson gson = new Gson();
        ClientIptable iptable = gson.fromJson(json, ClientIptable.class);
        this.setAppName(iptable.getAppName());
        this.setIp(iptable.getAppName());
        this.setKernelId(iptable.getAppName());
        this.setSerialId(iptable.getSerialId());
        this.setStartTime(iptable.getStartTime());
        this.setState(iptable.getState());
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getKernelId() {
        return kernelId;
    }

    public void setKernelId(String kernelId) {
        this.kernelId = kernelId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public EngineState getState() {
        return this.state;
    }

    public void setState(EngineState state) {
        this.state = state;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    public String getSerialId() {
        return this.serialId;
    }

    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this, ClientIptable.class);
    }
}
