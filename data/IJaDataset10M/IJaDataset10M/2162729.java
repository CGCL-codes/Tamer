package br.com.infoserver.jamtconnector.dataobjects;

import java.util.List;

public class Asset {

    private String hostname;

    private String ip;

    private String user;

    private String passwd;

    private String amtVersion;

    private String biosVendor;

    private String biosVersion;

    private String procVendor;

    private String procSpeed;

    private int ramBanks = 0;

    private List<RamInfo> ramInfoList;

    private List<DiskInfo> diskInfoList;

    private String hwModel;

    private String hwVendor;

    private String hwVersion;

    private String hwSerial;

    private String hwUUID;

    public String getAmtVersion() {
        return amtVersion;
    }

    public void setAmtVersion(String amtVersion) {
        this.amtVersion = amtVersion;
    }

    public String getBiosVendor() {
        return biosVendor;
    }

    public void setBiosVendor(String biosVendor) {
        this.biosVendor = biosVendor;
    }

    public String getBiosVersion() {
        return biosVersion;
    }

    public void setBiosVersion(String biosVersion) {
        this.biosVersion = biosVersion;
    }

    public String getProcVendor() {
        return procVendor;
    }

    public void setProcVendor(String procVendor) {
        this.procVendor = procVendor;
    }

    public String getProcSpeed() {
        return procSpeed;
    }

    public void setProcSpeed(String procSpeed) {
        this.procSpeed = procSpeed;
    }

    public int getRamBanks() {
        return ramBanks;
    }

    public void setRamBanks(int ramBanks) {
        this.ramBanks = ramBanks;
    }

    public List<RamInfo> getRamInfoList() {
        return ramInfoList;
    }

    public void setRamInfoList(List<RamInfo> ramInfoList) {
        this.ramInfoList = ramInfoList;
    }

    public List<DiskInfo> getDiskInfoList() {
        return diskInfoList;
    }

    public void setDiskInfoList(List<DiskInfo> diskInfoList) {
        this.diskInfoList = diskInfoList;
    }

    public String getHwModel() {
        return hwModel;
    }

    public void setHwModel(String hwModel) {
        this.hwModel = hwModel;
    }

    public String getHwVendor() {
        return hwVendor;
    }

    public void setHwVendor(String hwVendor) {
        this.hwVendor = hwVendor;
    }

    public String getHwVersion() {
        return hwVersion;
    }

    public void setHwVersion(String hwVersion) {
        this.hwVersion = hwVersion;
    }

    public String getHwSerial() {
        return hwSerial;
    }

    public void setHwSerial(String hwSerial) {
        this.hwSerial = hwSerial;
    }

    public String getHwUUID() {
        return hwUUID;
    }

    public void setHwUUID(String hwUUID) {
        this.hwUUID = hwUUID;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Asset(String hostname, String ip, String user, String passwd) {
        super();
        this.hostname = hostname;
        this.ip = ip;
        this.user = user;
        this.passwd = passwd;
    }
}
