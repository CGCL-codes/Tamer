package com.kaixinff.net;

import java.io.Serializable;

public class ProxyInfo implements Serializable {

    private static final long serialVersionUID = 5428791069128380573L;

    private String addr;

    private int port;

    private String realIP;

    private long lastUseTime;

    private int lastUseNum;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRealIP() {
        return realIP;
    }

    public void setRealIP(String realIP) {
        this.realIP = realIP;
    }

    public long getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(long lastUseTime) {
        this.lastUseTime = lastUseTime;
    }

    public int getLastUseNum() {
        return lastUseNum;
    }

    public void setLastUseNum(int lastUseNum) {
        this.lastUseNum = lastUseNum;
    }

    public boolean equals(ProxyInfo proxyInfo) {
        if (proxyInfo == null) {
            return false;
        }
        return this.getAddr().equals(proxyInfo.getAddr());
    }
}
