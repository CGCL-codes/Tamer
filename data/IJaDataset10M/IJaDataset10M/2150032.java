package com.angis.fx.data;

import java.io.Serializable;
import java.util.Date;

public class ContextInfo implements Serializable {

    private static final long serialVersionUID = 6532714613822673373L;

    private int loginType;

    private int loginFlag;

    private String deviceid;

    private String deviceno;

    private String dpLevel;

    private String memberInfos;

    private Date loginTime;

    private OrganizationInfo organizationInfo;

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public int getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(int loginFlag) {
        this.loginFlag = loginFlag;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDeviceno() {
        return deviceno;
    }

    public void setDeviceno(String deviceno) {
        this.deviceno = deviceno;
    }

    public String getDpLevel() {
        return dpLevel;
    }

    public void setDpLevel(String dpLevel) {
        this.dpLevel = dpLevel;
    }

    public String getMemberInfos() {
        return memberInfos;
    }

    public void setMemberInfos(String memberInfos) {
        this.memberInfos = memberInfos;
    }

    public OrganizationInfo getOrganizationInfo() {
        return organizationInfo;
    }

    public void setOrganizationInfo(OrganizationInfo organizationInfo) {
        this.organizationInfo = organizationInfo;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}
