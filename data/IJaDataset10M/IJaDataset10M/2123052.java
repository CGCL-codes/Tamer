package hu.sztaki.lpds.statistics.portlet;

import hu.sztaki.lpds.statistics.db.DBBase;
import hu.sztaki.lpds.statistics.db.MetricInformationHarvester;
import hu.sztaki.lpds.statistics.db.StatisticLevel;
import hu.sztaki.lpds.statistics.db.StatistiticsInformationHarvester;
import hu.sztaki.lpds.statistics.db.DataHarvester;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Bean for Concrete Workflow
 * @author Alessandra
 */
public class WorkflowStatsBean {

    public String workflowId;

    public String workflowName;

    private DataHarvester tgworkflow;

    public java.util.List<java.lang.String> jobNames;

    Map<String, String> workflowInstances;

    private Map<String, List<hu.sztaki.lpds.statistics.db.MetricInformation>> ms = null;

    private MetricInformationHarvester mif;

    private hu.sztaki.lpds.statistics.db.MenuInformationHarvester mp;

    private StatistiticsInformationHarvester statsFac;

    public int numofWorkflowInstances;

    public StringBuilder cat1Stats;

    public StringBuilder cat2Stats;

    public StringBuilder cat3Stats;

    public StringBuilder cat4Stats;

    public StringBuilder cat5Stats;

    public StringBuilder cat6Stats;

    public String failrate;

    public String getFailrate() {
        return failrate;
    }

    public void setFailrate(String failrate) {
        this.failrate = failrate;
    }

    public String userId;

    public DataHarvester getTgworkflow() {
        return tgworkflow;
    }

    public void setTgworkflow(DataHarvester tgworkflow) {
        this.tgworkflow = tgworkflow;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public StringBuilder getCat1Stats() {
        return cat1Stats;
    }

    public void setCat1Stats(StringBuilder cat1Stats) {
        this.cat1Stats = cat1Stats;
    }

    public StringBuilder getCat2Stats() {
        return cat2Stats;
    }

    public void setCat2Stats(StringBuilder cat2Stats) {
        this.cat2Stats = cat2Stats;
    }

    public StringBuilder getCat3Stats() {
        return cat3Stats;
    }

    public void setCat3Stats(StringBuilder cat3Stats) {
        this.cat3Stats = cat3Stats;
    }

    public StringBuilder getCat4Stats() {
        return cat4Stats;
    }

    public void setCat4Stats(StringBuilder cat4Stats) {
        this.cat4Stats = cat4Stats;
    }

    public StringBuilder getCat5Stats() {
        return cat5Stats;
    }

    public void setCat5Stats(StringBuilder cat5Stats) {
        this.cat5Stats = cat5Stats;
    }

    public StringBuilder getCat6Stats() {
        return cat6Stats;
    }

    public void setCat6Stats(StringBuilder cat6Stats) {
        this.cat6Stats = cat6Stats;
    }

    public List<String> getJobNames() {
        return jobNames;
    }

    public void setJobNames(List<String> jobNames) {
        this.jobNames = jobNames;
    }

    public int getNumofWorkflowInstances() {
        return numofWorkflowInstances;
    }

    public void setNumofWorkflowInstances(int numofWorkflowInstances) {
        this.numofWorkflowInstances = numofWorkflowInstances;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public Map<String, String> getWorkflowInstances() {
        return workflowInstances;
    }

    public void setWorkflowInstances(Map<String, String> workflowInstances) {
        this.workflowInstances = workflowInstances;
    }

    public WorkflowStatsBean(String userId, String workflowId) throws ClassNotFoundException, SQLException {
        this.userId = userId;
        this.workflowId = workflowId;
        mif = new MetricInformationHarvester(new DBBase());
        mp = new hu.sztaki.lpds.statistics.db.MenuInformationHarvester(new DBBase());
        cat1Stats = new StringBuilder();
        cat2Stats = new StringBuilder();
        cat3Stats = new StringBuilder();
        cat4Stats = new StringBuilder();
        cat5Stats = new StringBuilder();
        cat6Stats = new StringBuilder();
        this.workflowName = mp.getWFIDs(this.userId).get(workflowId);
        workflowInstances = mp.getWRTIDs(workflowId);
        jobNames = mp.getAJobs(workflowId);
        tgworkflow = new DataHarvester(workflowId, "workflow");
        cat1Stats = tgworkflow.getCat1Stats();
        cat2Stats = tgworkflow.getCat2Stats();
        cat3Stats = tgworkflow.getCat3Stats();
        cat4Stats = tgworkflow.getCat4Stats();
        cat5Stats = tgworkflow.getCat5Stats();
        failrate = Double.toString(tgworkflow.getChartdata());
    }
}
