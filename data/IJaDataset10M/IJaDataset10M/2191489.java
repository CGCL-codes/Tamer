package cn.jsprun.vo.logs;

import java.io.Serializable;

public class BanlogVO implements Serializable {

    private static final long serialVersionUID = 1863206700045345442L;

    private String firstName;

    private String secondName;

    private String ipAddress;

    private String usergroup;

    private String operateDate;

    private String operate;

    private String oldUsergroup;

    private String newUsergroup;

    private String periodDate;

    private String reason;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getNewUsergroup() {
        return newUsergroup;
    }

    public void setNewUsergroup(String newUsergroup) {
        this.newUsergroup = newUsergroup;
    }

    public String getOldUsergroup() {
        return oldUsergroup;
    }

    public void setOldUsergroup(String oldUsergroup) {
        this.oldUsergroup = oldUsergroup;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(String operateDate) {
        this.operateDate = operateDate;
    }

    public String getPeriodDate() {
        return periodDate;
    }

    public void setPeriodDate(String periodDate) {
        this.periodDate = periodDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getUsergroup() {
        return usergroup;
    }

    public void setUsergroup(String usergroup) {
        this.usergroup = usergroup;
    }
}
