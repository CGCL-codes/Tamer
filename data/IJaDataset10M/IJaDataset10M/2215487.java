package binky.reportrunner.data;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity(name = "T_EVENT")
@NamedQueries({ @NamedQuery(name = "getFailedEvents", query = "from T_EVENT e where e.module = ? and timeStamp > ? and timeStamp < ? and e.success=false order by timeStamp desc"), @NamedQuery(name = "getLongestRunningEvents", query = "from T_EVENT e where e.module = ?  and timeStamp > ? and timeStamp < ? order by runTime desc"), @NamedQuery(name = "getSuccessEvents", query = "from T_EVENT e where e.module = ?  and timeStamp > ? and timeStamp < ? and e.success=true order by timeStamp desc"), @NamedQuery(name = "getOldEvents", query = "from T_EVENT e where e.timeStamp < ?") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RunnerHistoryEvent extends DatabaseObject<Long> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8583408359341993933L;

    public Long getId() {
        return eventId;
    }

    public RunnerHistoryEvent() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long eventId;

    private Date timeStamp;

    private boolean success;

    private long runTime;

    private String userName;

    private String module;

    private String arguments;

    private String method;

    private String errorText;

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getRunTime() {
        return runTime;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("Arguments=");
        ret.append(arguments);
        ret.append(" ");
        ret.append("Timestamp=");
        ret.append(timeStamp);
        ret.append(" ");
        ret.append("Elapsed Time=");
        ret.append(runTime);
        ret.append(" ");
        ret.append("Method=");
        ret.append(method);
        ret.append(" ");
        ret.append("User Name=");
        ret.append(userName);
        ret.append(" ");
        ret.append("Module=");
        ret.append(module);
        return ret.toString();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public RunnerHistoryEvent(Date timeStamp, boolean success, long runTime, String userName, String module, String arguments, String method, String errorText) {
        super();
        this.timeStamp = timeStamp;
        this.success = success;
        this.runTime = runTime;
        this.userName = userName;
        this.module = module;
        this.arguments = arguments;
        this.method = method;
        this.errorText = errorText;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((arguments == null) ? 0 : arguments.hashCode());
        result = prime * result + ((errorText == null) ? 0 : errorText.hashCode());
        result = prime * result + (int) (eventId ^ (eventId >>> 32));
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result = prime * result + ((module == null) ? 0 : module.hashCode());
        result = prime * result + (int) (runTime ^ (runTime >>> 32));
        result = prime * result + (success ? 1231 : 1237);
        result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RunnerHistoryEvent other = (RunnerHistoryEvent) obj;
        if (arguments == null) {
            if (other.arguments != null) return false;
        } else if (!arguments.equals(other.arguments)) return false;
        if (errorText == null) {
            if (other.errorText != null) return false;
        } else if (!errorText.equals(other.errorText)) return false;
        if (eventId != other.eventId) return false;
        if (method == null) {
            if (other.method != null) return false;
        } else if (!method.equals(other.method)) return false;
        if (module == null) {
            if (other.module != null) return false;
        } else if (!module.equals(other.module)) return false;
        if (runTime != other.runTime) return false;
        if (success != other.success) return false;
        if (timeStamp == null) {
            if (other.timeStamp != null) return false;
        } else if (!timeStamp.equals(other.timeStamp)) return false;
        if (userName == null) {
            if (other.userName != null) return false;
        } else if (!userName.equals(other.userName)) return false;
        return true;
    }
}
