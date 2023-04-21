package mn.more.wits.server.model;

import java.io.Serializable;

/**
 * @author <a href="mailto:mike.liu@aptechmongolia.edu.mn">Mike Liu</a>
 * @version $Revision: 119 $
 */
public class AccountHistory implements Serializable {

    private WitsUser user;

    private String sessionId;

    private long loginTs;

    private long logoutTs;

    private String clientIp;

    private String userAgent;

    private String logoutReason;

    public WitsUser getUser() {
        return user;
    }

    public void setUser(WitsUser user) {
        this.user = user;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getLoginTs() {
        return loginTs;
    }

    public void setLoginTs(long loginTs) {
        this.loginTs = loginTs;
    }

    public long getLogoutTs() {
        return logoutTs;
    }

    public void setLogoutTs(long logoutTs) {
        this.logoutTs = logoutTs;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getLogoutReason() {
        return logoutReason;
    }

    public void setLogoutReason(String logoutReason) {
        this.logoutReason = logoutReason;
    }
}
