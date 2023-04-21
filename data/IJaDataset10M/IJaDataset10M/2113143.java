package hu.sztaki.lpds.wfs.service.angie;

import hu.sztaki.lpds.information.local.PropertyLoader;
import hu.sztaki.lpds.wfs.com.JobStatusBean;
import hu.sztaki.lpds.wfs.utils.Status;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author krisztian
 */
public class StatusHandlerService extends Thread {

    private PreparedStatement jobStatusUpdate, jobStatusInsert, jobOutputs, jobOutputsDelete;

    private PreparedStatement workflowStatusUpdate, workflowStatusInsert;

    private BlockingQueue<JobStatusBean> statuses = new LinkedBlockingQueue<JobStatusBean>();

    private boolean saveVisualizerData = false;

    private Connection conu = null;

    private Connection coni = null;

    private Connection conwi = null;

    private Connection conwu = null;

    private Connection conju = null;

    private Connection conv = null;

    public StatusHandlerService() {
        try {
            saveVisualizerData = "true".equals(PropertyLoader.getInstance().getProperty("guse.wfs.system.savevisualizerdata"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPersistItem(JobStatusBean pValue) throws Exception {
        statuses.put(pValue);
    }

    public int getPoolSize() {
        return statuses.size();
    }

    private void saveJobStatus(String dbWorkflowID, String dbJobID, JobStatusBean tmp) throws Exception {
        Connection conn = Base.getConnection();
        if (Status.isFinished(tmp.getStatus())) {
            jobOutputsDelete = conn.prepareStatement("DELETE FROM job_outputs WHERE wrtid=? and id_ajob=? and pid=?");
            jobOutputsDelete.setString(1, tmp.getWrtID());
            jobOutputsDelete.setString(2, dbJobID);
            jobOutputsDelete.setLong(3, tmp.getPID());
            jobOutputsDelete.executeUpdate();
            jobOutputsDelete.close();
            jobOutputs = conn.prepareStatement("INSERT INTO job_outputs VALUES(?,?,?,?,?)");
            jobOutputs.setString(1, tmp.getWrtID());
            jobOutputs.setString(2, dbJobID);
            jobOutputs.setLong(3, tmp.getPID());
            Enumeration<String> enmOut = tmp.getOutputs().keys();
            String outkey;
            while (enmOut.hasMoreElements()) {
                outkey = enmOut.nextElement();
                jobOutputs.setString(4, outkey);
                jobOutputs.setLong(5, tmp.getOutputCount(outkey));
                jobOutputs.executeUpdate();
            }
            jobOutputs.close();
        }
        jobStatusUpdate = conn.prepareStatement("UPDATE job_status SET status=?, resource=? WHERE id_workflow=? and id_ajob=? and wrtid=? and pid=?");
        jobStatusUpdate.setString(1, "" + tmp.getStatus());
        jobStatusUpdate.setString(2, "" + tmp.getResource());
        jobStatusUpdate.setString(3, dbWorkflowID);
        jobStatusUpdate.setString(4, dbJobID);
        jobStatusUpdate.setString(5, tmp.getWrtID());
        jobStatusUpdate.setLong(6, tmp.getPID());
        long updateCount = jobStatusUpdate.executeUpdate();
        jobStatusUpdate.close();
        if (updateCount == 0) {
            jobStatusInsert = conn.prepareStatement("INSERT INTO job_status VALUES(?,?,?,?,?,?)");
            jobStatusInsert.setString(1, dbWorkflowID);
            jobStatusInsert.setString(2, dbJobID);
            jobStatusInsert.setString(3, tmp.getWrtID());
            jobStatusInsert.setLong(4, tmp.getPID());
            jobStatusInsert.setString(5, "" + tmp.getStatus());
            jobStatusInsert.setString(6, "" + tmp.getResource());
            jobStatusInsert.executeUpdate();
            jobStatusInsert.close();
        }
        conn.close();
    }

    private void saveWorkflowStatus(String dbWorkflowID, JobStatusBean tmp) throws Exception {
        Connection conn = Base.getConnection();
        workflowStatusUpdate = conn.prepareStatement("UPDATE workflow_prop SET value=? WHERE id_workflow=? and wrtid=? and name='status'");
        if ("6".equals(tmp.getWorkflowStatus()) || "7".equals(tmp.getWorkflowStatus()) || "15".equals(tmp.getWorkflowStatus())) {
            workflowStatusUpdate.setString(1, tmp.getWorkflowStatus());
            workflowStatusUpdate.setString(2, dbWorkflowID);
            workflowStatusUpdate.setString(3, tmp.getWrtID());
            long cnt = workflowStatusUpdate.executeUpdate();
            workflowStatusUpdate.close();
            if (cnt == 0) {
                workflowStatusInsert = conn.prepareStatement("INSERT INTO workflow_prop VALUES(?,?,'status',?)");
                workflowStatusInsert.setString(1, dbWorkflowID);
                workflowStatusInsert.setString(2, tmp.getWrtID());
                workflowStatusInsert.setString(3, tmp.getWorkflowStatus());
                workflowStatusInsert.executeUpdate();
                workflowStatusInsert.close();
            }
            if (("6".equals(tmp.getWorkflowStatus())) || ("15".equals(tmp.getWorkflowStatus()))) {
                PreparedStatement pso = conn.prepareStatement("DELETE FROM job_outputs WHERE wrtid=?");
                pso.setString(1, tmp.getWrtID());
                pso.executeUpdate();
                pso.close();
                conn.close();
            }
        }
        conn.close();
    }

    private void saveStat(JobStatusBean tmp, String workflowID) throws Exception {
        if (tmp.getStatus() != Status.INIT) {
            Connection conn = Base.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO stat_running(portalURL,userID,wfID,wrtID,jobName,pid,jobStatus,wfStatus,resource,seq) VALUES(?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, tmp.getPortalID());
            ps.setString(2, tmp.getUserID());
            ps.setString(3, workflowID);
            ps.setString(4, tmp.getWrtID());
            ps.setString(5, tmp.getJobID());
            ps.setString(6, "" + tmp.getPID());
            ps.setString(7, "" + tmp.getStatus());
            ps.setString(8, tmp.getWorkflowStatus());
            ps.setString(9, "" + tmp.getResource());
            ps.setString(10, "" + tmp.getTim());
            ps.executeUpdate();
            ps.close();
            conn.close();
        }
    }

    @Override
    public void run() {
        String dbWorkflowID, dbJobID;
        JobStatusBean tmp;
        while (true) {
            try {
                tmp = statuses.take();
                dbWorkflowID = getDBWorkflowID(tmp.getPortalID(), tmp.getUserID(), tmp.getWorkflowID());
                dbJobID = getDBJobID(tmp.getPortalID(), tmp.getUserID(), tmp.getWorkflowID(), tmp.getJobID());
                saveJobStatus(dbWorkflowID, dbJobID, tmp);
                saveWorkflowStatus(dbWorkflowID, tmp);
                if (saveVisualizerData) saveStat(tmp, dbWorkflowID);
                if ((tmp.getStatus() == 6) || (tmp.getStatus() == 7) || (tmp.getStatus() == 15) || (tmp.getStatus() == 17) || (tmp.getStatus() == 21) || (tmp.getStatus() == 22) || (tmp.getStatus() == 25)) {
                    Base.getI().delete(tmp);
                    Base.getI().deleteJobDescriptionFromCache(tmp.getPortalID(), tmp.getUserID(), tmp.getWorkflowID(), tmp.getWrtID());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getDBWorkflowID(String pPortalID, String pUserID, String pWorkflowName) {
        String res = "0";
        try {
            res = Base.getI().getWorkflowIdFromJobIdCache(pPortalID, pUserID, pWorkflowName);
        } catch (NullPointerException e) {
            Connection con = null;
            PreparedStatement ps = null;
            try {
                con = Base.getConnection();
                ps = con.prepareStatement("SELECT b.id FROM workflow as b, aworkflow as c WHERE c.id_portal=? and c.id_user=? and c.id=b.id_aworkflow and b.name=?");
                ps.setString(1, pPortalID);
                ps.setString(2, pUserID);
                ps.setString(3, pWorkflowName);
                ResultSet rst = ps.executeQuery();
                if (rst.next()) {
                    res = rst.getString(1);
                    Base.getI().addWorkflowIdToJobIdCache(pPortalID, pUserID, pWorkflowName, res);
                }
                rst.close();
            } catch (Exception e0) {
                e0.printStackTrace();
            } finally {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return res;
    }

    private String getDBJobID(String pPortalID, String pUserID, String pWorkflowName, String pJobName) {
        String res = "";
        try {
            res = Base.getI().getJobIdFromJobIdCache(pPortalID, pUserID, pWorkflowName, pJobName);
        } catch (NullPointerException e) {
            Connection con = null;
            PreparedStatement ps = null;
            try {
                con = Base.getConnection();
                ps = con.prepareStatement("SELECT a.id FROM ajob as a, workflow as b, aworkflow as c WHERE c.id_portal=? and c.id_user=? and c.id=b.id_aworkflow and b.name=? and a.id_aworkflow=c.id and a.name=?");
                ps.setString(1, pPortalID);
                ps.setString(2, pUserID);
                ps.setString(3, pWorkflowName);
                ps.setString(4, pJobName);
                ResultSet rst = ps.executeQuery();
                if (rst.next()) {
                    res = rst.getString(1);
                    Base.getI().addJobIdToJobIdCache(pPortalID, pUserID, pWorkflowName, pJobName, res);
                }
                rst.close();
            } catch (Exception e0) {
                e0.printStackTrace();
            } finally {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return res;
    }
}
