package net.naijatek.myalumni.modules.common.domain;

import java.util.Date;
import net.naijatek.myalumni.modules.common.helper.ReasonCodes;

/**
 * This class holds user login history information
 */
public class LoginHistoryVO extends MyAlumniBaseVO {

    private String loginHistoryId;

    private Date requestTime;

    private String userName;

    private String sourceIP;

    private String loginStatus;

    private String userAgent;

    private String reasonCode;

    private String reasonCodeDesc;

    public String getReasonCodeDesc() {
        reasonCodeDesc = ReasonCodes.getDescription(this.reasonCode);
        return reasonCodeDesc;
    }

    public void setReasonCodeDesc(String reasonCodeDesc) {
        this.reasonCodeDesc = reasonCodeDesc;
    }

    public LoginHistoryVO() {
    }

    public String getLoginHistoryId() {
        return loginHistoryId;
    }

    public void setLoginHistoryId(String accessHistoryId) {
        this.loginHistoryId = accessHistoryId;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonCode() {
        return reasonCode;
    }
}
