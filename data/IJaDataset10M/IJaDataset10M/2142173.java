package de.consolewars.api.data;

/**
 * 
 * @author cerpin (arrewk@gmail.com)
 */
public class AuthStatus {

    private String status;

    private String reason;

    public AuthStatus(String status, String reason) {
        this.status = status;
        this.reason = "";
    }

    public AuthStatus(String status) {
        this(status, "");
    }

    public AuthStatus() {
        this("", "");
    }

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String toString() {
        String sAuthstatus = "status: " + status;
        if (reason.length() > 0) {
            sAuthstatus += ", reason: " + reason;
        }
        return sAuthstatus;
    }
}
