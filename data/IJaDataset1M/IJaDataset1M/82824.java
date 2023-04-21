package ci.monitor.status;

import java.util.Date;

public class StatusResult {

    StatusCode code = StatusCode.UNKNOWN;

    Date lastBuildAttempted = null;

    Date lastBuildPassed = null;

    public StatusResult() {
    }

    public StatusCode getCode() {
        return code;
    }

    public void setCode(StatusCode code) {
        this.code = code;
    }

    public Date getLastBuildAttempted() {
        return lastBuildAttempted;
    }

    public void setLastBuildAttempted(Date lastBuildAttempted) {
        this.lastBuildAttempted = lastBuildAttempted;
    }

    public Date getLastBuildPassed() {
        return lastBuildPassed;
    }

    public void setLastBuildPassed(Date lastBuildPassed) {
        this.lastBuildPassed = lastBuildPassed;
    }

    public String toString() {
        return "StatusResult[ code='" + code.name() + "', lastBuildAttempted='" + lastBuildAttempted + "', lastBuildPassed='" + lastBuildPassed + "']";
    }
}
