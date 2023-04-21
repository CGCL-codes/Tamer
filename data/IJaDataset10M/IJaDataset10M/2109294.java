package vlan.webgame.manage.entity;

import java.io.Serializable;

public class AdminLogin implements Serializable {

    private static final long serialVersionUID = 4787270360232862720L;

    private String loginId;

    private String name;

    private java.sql.Timestamp loginTime;

    private String remoteInfo;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.sql.Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(java.sql.Timestamp loginTime) {
        this.loginTime = loginTime;
    }

    public String getRemoteInfo() {
        return remoteInfo;
    }

    public void setRemoteInfo(String remoteInfo) {
        this.remoteInfo = remoteInfo;
    }

    @Override
    public String toString() {
        return "AdminLogin [loginId:" + loginId + ",name:" + name + ",loginTime:" + loginTime + ",remoteInfo:" + remoteInfo + "]";
    }
}
