package org.opengoss.snmphibernate.api;

public class SnmpTarget implements ISnmpTarget {

    private int port = 161;

    private String ip;

    private int version = V2C;

    private String community = "public";

    private String writeCommunity = "private";

    private String deviceType;

    private String deviceVersion;

    private String vendor;

    public SnmpTarget(String ip) {
        this.ip = ip;
    }

    public SnmpTarget(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getWriteCommunity() {
        return writeCommunity;
    }

    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }
}
