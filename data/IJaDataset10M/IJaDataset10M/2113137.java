package hu.sztaki.lpds.pgportal.portlets.pgrade;

import hu.sztaki.lpds.pgportal.services.pgrade.*;
import hu.sztaki.lpds.pgportal.services.utils.*;
import java.io.File;

public class PGradeBean {

    private static String serverURLForJNLP = null;

    private static String serverURLForApplet = null;

    private static String workflowTracesDir = null;

    private String message = null;

    private SZGWorkflow[] wfList = null;

    private SZGAppletSizes appletSizes = null;

    private String username = null;

    private File logFile = null;

    private SZGJob logJob = null;

    private SZGWorkflow logWorkflow = null;

    private short logType;

    private String grid = null;

    private SZGJob jobToVisualize = null;

    private String jspToGo = null;

    private boolean showeWFList = false;

    private PGradePortlet pgradePortlet = null;

    public PGradeBean(PGradePortlet pgradePortlet) {
        this.pgradePortlet = pgradePortlet;
    }

    public PGradeBean() {
    }

    public void setServerURLs(String urlForJNLP, String urlForApplet) {
        PGradeBean.serverURLForJNLP = urlForJNLP;
        PGradeBean.serverURLForApplet = urlForApplet;
    }

    public String getServerUrlForApplet() {
        return PGradeBean.serverURLForApplet;
    }

    public String getServerUrlForJNLP() {
        return PGradeBean.serverURLForJNLP;
    }

    public void setWorkflowTracesDir(String value) {
        PGradeBean.workflowTracesDir = value;
    }

    public String getWorkflowTracesDir() {
        return PGradeBean.workflowTracesDir;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isDemoAccount() {
        return false;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setWorkflowList(SZGWorkflow[] wfList) {
        this.wfList = wfList;
    }

    public SZGWorkflow[] getWorkflowList() {
        SZGWorkflow[] retValue = new SZGWorkflow[0];
        try {
            if (this.wfList.length != 0) retValue = this.wfList;
        } catch (NullPointerException e) {
        }
        return retValue;
    }

    public void setAppletSizes(SZGAppletSizes appletSizes) {
        this.appletSizes = appletSizes;
    }

    /**
	 * @return null: if sizes is null or empty
	 */
    public SZGAppletSizes getAppletSizes() {
        return this.appletSizes;
    }

    protected void setLogFile(File lf, short logType, SZGJob logJob) {
        this.logFile = lf;
        this.logType = logType;
        this.logJob = logJob;
    }

    protected void setLogFileWorkflow(File lf, short logType, SZGWorkflow logWf) {
        this.logFile = lf;
        this.logType = logType;
        this.logWorkflow = logWf;
    }

    public String[] getLogLines() {
        String[] retValue = LogFileUtil.readInLogFile(this.logFile);
        if (retValue == null) {
            String[] other = { "Log file not exists (maybe deleted by the runtime system)!" };
            retValue = other;
        }
        return retValue;
    }

    public short getLogType() {
        return this.logType;
    }

    public SZGJob getLogSZGJob() {
        return this.logJob;
    }

    public SZGWorkflow getLogSZGWorkflow() {
        return this.logWorkflow;
    }

    public void setWorkflow(SZGWorkflow wf) {
        this.pgradePortlet.setCurrentWorkflow(this.getUsername(), wf);
        if (wf != null) System.out.println("PGradeBean.setWorkflow(" + wf.getId() + ")");
    }

    public SZGWorkflow getWorkflow() {
        return pgradePortlet == null ? null : this.pgradePortlet.getCurrentWorkflow(username);
    }

    public void setJobToVisualize(SZGJob job) {
        System.out.println("PGradeBean.setJobToVisualize(" + job.getId() + ")");
        this.jobToVisualize = job;
    }

    public SZGJob getJobToVisualize() {
        return this.jobToVisualize;
    }

    public void setJSPToGo(String jspName) {
        this.jspToGo = jspName;
    }

    public String getJSPToGo() {
        return this.jspToGo;
    }

    public void setGrid(String grid) {
        this.grid = grid;
    }

    public String getGrid() {
        return this.grid;
    }

    public boolean isShoweWFList() {
        return showeWFList;
    }

    public void setShoweWFList(boolean showeWFList) {
        this.showeWFList = showeWFList;
    }

    public java.util.List<String> getPSStatistics() {
        return LogFileUtil.getPSStatistics(this.getWorkflow().getUserId().toString(), this.getWorkflow().getId().toString());
    }

    public boolean isPSStatisticsExists() {
        return this.getWorkflow() == null ? false : this.getWorkflow().hasPSStatisticfile();
    }
}
