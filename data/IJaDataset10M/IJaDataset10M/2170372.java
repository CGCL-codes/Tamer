package dnl.jexem.prcs;

/**
 * 
 * @author Daniel Orr
 *
 */
public class ProcessInfo {

    private int processID;

    private int parentProcessID;

    private String cmd;

    private double cpuUsagePercent;

    private long memUsage;

    private String startTime;

    private String userID;

    private String arguments;

    public ProcessInfo() {
    }

    public ProcessInfo(int processID, int parentID, String cmd) {
        super();
        this.processID = processID;
        this.parentProcessID = parentID;
        this.cmd = cmd;
    }

    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public int getParentProcessID() {
        return parentProcessID;
    }

    public void setParentProcessID(int parentProcessID) {
        this.parentProcessID = parentProcessID;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public double getCpuUsagePercent() {
        return cpuUsagePercent;
    }

    public void setCpuUsagePercent(double cpuUsage) {
        this.cpuUsagePercent = cpuUsage;
    }

    public long getMemUsage() {
        return memUsage;
    }

    public void setMemUsage(long memUsage) {
        this.memUsage = memUsage;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "ProcessInfo [userID=" + userID + ", processID=" + processID + ", parentProcessID=" + parentProcessID + ", cpuUsage=" + cpuUsagePercent + ", startTime=" + startTime + ", cmd=" + cmd + "]";
    }

    public String toStringNixStyle() {
        return userID + f(processID) + f(parentProcessID) + f(cpuUsagePercent) + f(startTime) + f(cmd) + f(arguments);
    }

    private String f(Object obj) {
        if (obj == null) {
            return "";
        }
        return " " + String.valueOf(obj);
    }
}
