package com.kni.etl.scheduler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import com.kni.etl.ETLJob;
import com.kni.etl.ETLJobStatus;
import com.kni.etl.ETLStatus;
import com.kni.etl.EngineConstants;
import com.kni.etl.Metadata;
import com.kni.etl.dbutils.ResourcePool;

public class MetadataScheduler extends Metadata {

    public MetadataScheduler(boolean pEnableEncryption, String pPassphrase) throws Exception {
        super(pEnableEncryption, pPassphrase);
    }

    public MetadataScheduler(boolean pEnableEncryption) throws Exception {
        super(pEnableEncryption);
    }

    /**
	 * Insert the method's description here. Creation date: (3/5/2002 3:18:54 PM)
	 * 
	 * @return jobscheduler.EtlJob
	 */
    public ETLJob getNextJobInQueue(Collection<String[]> pJobTypes, int pServerID) throws SQLException, java.lang.Exception {
        PreparedStatement clearJobLogStmt = null;
        PreparedStatement clearJobErrorStmt = null;
        PreparedStatement insJobLogHistStmt = null;
        PreparedStatement insJobErrorHistStmt = null;
        PreparedStatement getfinishedLoads = null;
        PreparedStatement setLoadEndDate = null;
        ResultSet rs = null;
        ResultSet m_rs = null;
        synchronized (this.oLock) {
            ETLStatus etlJobStatus = new ETLJobStatus();
            refreshMetadataConnection();
            getfinishedLoads = this.metadataConnection.prepareStatement("SELECT LOAD_ID FROM  " + tablePrefix + loadTableName() + " A WHERE EXISTS (SELECT 1 FROM  " + tablePrefix + "JOB_LOG B " + "WHERE STATUS_ID IN (?,?,?,?) AND A.START_JOB_ID = B.JOB_ID AND A.LOAD_ID = B.LOAD_ID)");
            getfinishedLoads.setInt(1, ETLJobStatus.FAILED);
            getfinishedLoads.setInt(2, ETLJobStatus.CANCELLED);
            getfinishedLoads.setInt(3, ETLJobStatus.SUCCESSFUL);
            getfinishedLoads.setInt(4, ETLJobStatus.SKIPPED);
            m_rs = getfinishedLoads.executeQuery();
            while (m_rs.next()) {
                if (setLoadEndDate == null) {
                    setLoadEndDate = this.metadataConnection.prepareStatement(" UPDATE  " + tablePrefix + loadTableName() + " SET END_DATE = " + currentTimeStampSyntax + " WHERE LOAD_ID = ?");
                }
                ResourcePool.releaseLoadLookups(m_rs.getInt(1));
                setLoadEndDate.setInt(1, m_rs.getInt(1));
                setLoadEndDate.executeUpdate();
                if (insJobErrorHistStmt == null) {
                    insJobErrorHistStmt = this.metadataConnection.prepareStatement("insert into  " + tablePrefix + "job_error_hist(dm_load_id,job_id,message,code,error_datetime) select dm_load_id,job_id,message,code,error_datetime from  " + tablePrefix + "job_error where dm_load_id in (select dm_load_id from  " + tablePrefix + "job_log where load_id = ?)");
                }
                insJobErrorHistStmt.setInt(1, m_rs.getInt(1));
                insJobErrorHistStmt.executeUpdate();
                if (clearJobErrorStmt == null) {
                    clearJobErrorStmt = this.metadataConnection.prepareStatement("delete from  " + tablePrefix + "job_error where dm_load_id in (select dm_load_id from  " + tablePrefix + "job_log where load_id = ?)");
                }
                clearJobErrorStmt.setInt(1, m_rs.getInt(1));
                clearJobErrorStmt.executeUpdate();
                String JOB_LOG_LAST_UPDATE_COL = "";
                if (columnExists("JOB_LOG", "LAST_UPDATE_DATE")) JOB_LOG_LAST_UPDATE_COL = ", LAST_UPDATE_DATE";
                String JOB_LOG_HIST_LAST_UPDATE_COL = "";
                if (columnExists("JOB_LOG_HIST", "LAST_UPDATE_DATE")) JOB_LOG_HIST_LAST_UPDATE_COL = ", LAST_UPDATE_DATE";
                if (insJobLogHistStmt == null) {
                    insJobLogHistStmt = this.metadataConnection.prepareStatement("insert into  " + tablePrefix + "job_log_hist(job_id,load_id,start_date,status_id,end_date,message,dm_load_id,retry_attempts,execution_date,stats,server_id" + JOB_LOG_LAST_UPDATE_COL + ") " + "select job_id,load_id,start_date,status_id,end_date,message,dm_load_id,retry_attempts,execution_date,stats,server_id" + JOB_LOG_HIST_LAST_UPDATE_COL + " from  " + tablePrefix + "job_log where load_id = ?");
                }
                insJobLogHistStmt.setInt(1, m_rs.getInt(1));
                insJobLogHistStmt.executeUpdate();
                if (clearJobLogStmt == null) {
                    clearJobLogStmt = this.metadataConnection.prepareStatement("delete from  " + tablePrefix + "job_log where load_id = ?");
                }
                clearJobLogStmt.setInt(1, m_rs.getInt(1));
                clearJobLogStmt.executeUpdate();
            }
            if (m_rs != null) {
                m_rs.close();
            }
            if (setLoadEndDate != null) {
                setLoadEndDate.close();
            }
            if (insJobErrorHistStmt != null) {
                insJobErrorHistStmt.close();
            }
            if (clearJobErrorStmt != null) {
                clearJobErrorStmt.close();
            }
            if (insJobLogHistStmt != null) {
                insJobLogHistStmt.close();
            }
            if (clearJobLogStmt != null) {
                clearJobLogStmt.close();
            }
            if (getfinishedLoads != null) {
                getfinishedLoads.close();
            }
            metadataConnection.commit();
            if (m_rs != null) {
                m_rs.close();
            }
            if (rs != null) {
                rs.close();
            }
            PreparedStatement dueJobs = this.metadataConnection.prepareStatement("SELECT A.JOB_ID, MONTH, MONTH_OF_YEAR, DAY, DAY_OF_WEEK, DAY_OF_MONTH, HOUR_OF_DAY,HOUR, NEXT_RUN_DATE, SCHEDULE_ID, PROJECT_ID, MINUTE, MINUTE_OF_HOUR FROM  " + tablePrefix + "JOB_SCHEDULE A, " + tablePrefix + "JOB B WHERE " + currentTimeStampSyntax + " >= NEXT_RUN_DATE AND A.JOB_ID = B.JOB_ID ORDER BY NEXT_RUN_DATE FOR UPDATE ");
            m_rs = dueJobs.executeQuery();
            String jobID = null;
            int month;
            int month_of_year;
            int day;
            int day_of_week;
            int day_of_month;
            int hour_of_day;
            int hour;
            int scheduleID;
            int projectID = -1;
            int minute;
            int minute_of_hour;
            java.util.Date nextRunDate;
            java.util.Date lastRunDate;
            PreparedStatement updJobSched = null;
            boolean scheduledJob = false;
            while (m_rs.next() && scheduledJob == false) {
                jobID = m_rs.getString(1);
                month = m_rs.getInt(2);
                if (m_rs.wasNull()) {
                    month = -1;
                }
                month_of_year = m_rs.getInt(3);
                if (m_rs.wasNull()) {
                    month_of_year = -1;
                }
                day = m_rs.getInt(4);
                if (m_rs.wasNull()) {
                    day = -1;
                }
                day_of_week = m_rs.getInt(5);
                if (m_rs.wasNull()) {
                    day_of_week = -1;
                }
                day_of_month = m_rs.getInt(6);
                if (m_rs.wasNull()) {
                    day_of_month = -1;
                }
                hour_of_day = m_rs.getInt(7);
                if (m_rs.wasNull()) {
                    hour_of_day = -1;
                }
                hour = m_rs.getInt(8);
                if (m_rs.wasNull()) {
                    hour = -1;
                }
                scheduleID = m_rs.getInt(10);
                projectID = m_rs.getInt(11);
                minute = m_rs.getInt(12);
                if (m_rs.wasNull()) {
                    minute = -1;
                }
                minute_of_hour = m_rs.getInt(13);
                if (m_rs.wasNull()) {
                    minute_of_hour = -1;
                }
                lastRunDate = m_rs.getTimestamp(9);
                nextRunDate = getNextDate(lastRunDate, month, month_of_year, day, day_of_week, day_of_month, hour, hour_of_day, minute, minute_of_hour);
                if (updJobSched == null) {
                    updJobSched = this.metadataConnection.prepareStatement(" UPDATE  " + tablePrefix + "JOB_SCHEDULE SET NEXT_RUN_DATE = ? WHERE SCHEDULE_ID = ? AND JOB_ID = ?");
                }
                if (nextRunDate == null || nextRunDate.equals(lastRunDate)) {
                    ResourcePool.LogMessage(this, ResourcePool.INFO_MESSAGE, "Disabling schedule [" + scheduleID + "] next run date is null or the same as the previous date");
                    updJobSched.setNull(1, java.sql.Types.TIMESTAMP);
                } else updJobSched.setTimestamp(1, new java.sql.Timestamp(nextRunDate.getTime()));
                updJobSched.setInt(2, scheduleID);
                updJobSched.setString(3, jobID);
                updJobSched.executeUpdate();
                scheduledJob = true;
            }
            if (m_rs != null) {
                m_rs.close();
            }
            if (updJobSched != null) {
                updJobSched.close();
            }
            if (dueJobs != null) {
                dueJobs.close();
            }
            if (scheduledJob) executeJob(projectID, jobID, false, false);
            PreparedStatement mReadyList = this.metadataConnection.prepareStatement("SELECT load_id, job_id, status_id FROM " + tablePrefix + "job_log AA WHERE STATUS_ID in (?, ?, ?) AND EXISTS " + " (SELECT 1 FROM  " + tablePrefix + "job_log a,  " + tablePrefix + "job_dependencie b " + " WHERE a.job_id = b.job_id " + " AND aa.load_id = a.load_id and aa.job_id = b.parent_job_id" + " GROUP BY load_id,parent_job_id " + " HAVING MAX (CASE WHEN continue_if_failed = 'Y' " + "   THEN (CASE status_id WHEN ? THEN 0 WHEN ? THEN 0 WHEN ? THEN 0 ELSE 1 END) " + "   ELSE (CASE status_id WHEN ? THEN  0 WHEN ? THEN 0 ELSE 1 END)" + " END) = 0) FOR UPDATE");
            mReadyList.setInt(1, ETLJobStatus.WAITING_FOR_CHILDREN);
            mReadyList.setInt(2, ETLJobStatus.WAITING_TO_SKIP);
            mReadyList.setInt(3, ETLJobStatus.WAITING_TO_PAUSE);
            mReadyList.setInt(4, (ETLJobStatus.PENDING_CLOSURE_SUCCESSFUL));
            mReadyList.setInt(5, (ETLJobStatus.PENDING_CLOSURE_FAILED));
            mReadyList.setInt(6, (ETLJobStatus.PENDING_CLOSURE_SKIP));
            mReadyList.setInt(7, (ETLJobStatus.PENDING_CLOSURE_SUCCESSFUL));
            mReadyList.setInt(8, (ETLJobStatus.PENDING_CLOSURE_SKIP));
            ResultSet rsReadyJobs = mReadyList.executeQuery();
            PreparedStatement updJobs = this.metadataConnection.prepareStatement(" UPDATE  " + tablePrefix + "job_log SET status_id = ?, message = ? where job_id = ? and load_id = ?");
            while (rsReadyJobs.next()) {
                int wait_status = rsReadyJobs.getInt("status_id");
                switch(wait_status) {
                    case ETLJobStatus.WAITING_FOR_CHILDREN:
                        updJobs.setInt(1, ETLJobStatus.READY_TO_RUN);
                        updJobs.setString(2, etlJobStatus.getStatusMessageForCode(ETLJobStatus.READY_TO_RUN));
                        break;
                    case ETLJobStatus.WAITING_TO_SKIP:
                        updJobs.setInt(1, ETLJobStatus.PENDING_CLOSURE_SKIP);
                        updJobs.setString(2, etlJobStatus.getStatusMessageForCode(ETLJobStatus.PENDING_CLOSURE_SKIP));
                        break;
                    case ETLJobStatus.WAITING_TO_PAUSE:
                        updJobs.setInt(1, ETLJobStatus.PAUSED);
                        updJobs.setString(2, etlJobStatus.getStatusMessageForCode(ETLJobStatus.PAUSED));
                        break;
                }
                updJobs.setString(3, rsReadyJobs.getString("job_id"));
                updJobs.setInt(4, rsReadyJobs.getInt("load_id"));
                updJobs.addBatch();
            }
            updJobs.executeBatch();
            if (mReadyList != null) mReadyList.close();
            if (rsReadyJobs != null) rsReadyJobs.close();
            if (updJobs != null) updJobs.close();
            PreparedStatement mRetryList = this.metadataConnection.prepareStatement("select a.job_id, " + " case when (coalesce(a.retry_attempts,0)) < b.retry_attempts then cast(? as int)  else  cast(? as int)  end as status_id, " + " case when (coalesce(a.retry_attempts,0)) < b.retry_attempts then cast(? as varchar(255)) else cast(? as varchar(255)) end as message, " + " case when (coalesce(a.retry_attempts,0)) < b.retry_attempts then (coalesce(a.retry_attempts,0))+1 else a.retry_attempts end as retry_attempts " + " from " + tablePrefix + "job_log a, " + tablePrefix + "job b " + " where a.job_id = b.job_id " + " and status_id = ? " + " and " + this.currentTimeStampSyntax + " > " + this.secondsBeforeRetry);
            mRetryList.setInt(1, ETLJobStatus.READY_TO_RUN);
            mRetryList.setInt(2, ETLJobStatus.PENDING_CLOSURE_FAILED);
            mRetryList.setString(3, etlJobStatus.getStatusMessageForCode(ETLJobStatus.READY_TO_RUN));
            mRetryList.setString(4, etlJobStatus.getStatusMessageForCode(ETLJobStatus.PENDING_CLOSURE_FAILED));
            mRetryList.setInt(5, ETLJobStatus.WAITING_TO_BE_RETRIED);
            ResultSet m_rsJobsToRetry = mRetryList.executeQuery();
            PreparedStatement updRetryJobs = this.metadataConnection.prepareStatement(" UPDATE  " + tablePrefix + "job_log " + " SET status_id = ?, message = ?,retry_attempts = ? where job_id = ?");
            while (m_rsJobsToRetry.next()) {
                updRetryJobs.setInt(1, m_rsJobsToRetry.getInt(2));
                updRetryJobs.setString(2, m_rsJobsToRetry.getString(3));
                updRetryJobs.setInt(3, m_rsJobsToRetry.getInt(4));
                updRetryJobs.setString(4, m_rsJobsToRetry.getString(1));
                updRetryJobs.addBatch();
            }
            updRetryJobs.executeBatch();
            if (mRetryList != null) {
                mRetryList.close();
            }
            if (m_rsJobsToRetry != null) {
                m_rsJobsToRetry.close();
            }
            PreparedStatement selFinishedJobs = this.metadataConnection.prepareStatement(" SELECT STATUS_ID, DM_LOAD_ID, LOAD_ID, JOB_ID FROM  " + tablePrefix + "JOB_LOG WHERE STATUS_ID IN (?,?,?, ?) FOR UPDATE");
            selFinishedJobs.setInt(1, ETLJobStatus.PENDING_CLOSURE_SUCCESSFUL);
            selFinishedJobs.setInt(2, ETLJobStatus.PENDING_CLOSURE_FAILED);
            selFinishedJobs.setInt(3, ETLJobStatus.PENDING_CLOSURE_SKIP);
            selFinishedJobs.setInt(4, ETLJobStatus.PENDING_CLOSURE_CANCELLED);
            m_rs = selFinishedJobs.executeQuery();
            PreparedStatement pStmt = null;
            PreparedStatement pCancelStmt = null;
            while (m_rs.next()) {
                int statusID = m_rs.getInt(1);
                int dmLoadID = m_rs.getInt(2);
                int LoadID = m_rs.getInt(3);
                String cJobID = m_rs.getString(4);
                switch(statusID) {
                    case ETLJobStatus.PENDING_CLOSURE_SUCCESSFUL:
                    case ETLJobStatus.PENDING_CLOSURE_FAILED:
                    case ETLJobStatus.PENDING_CLOSURE_SKIP:
                        if (pStmt == null) {
                            pStmt = metadataConnection.prepareStatement("UPDATE  " + tablePrefix + "JOB_LOG " + " SET STATUS_ID = (CASE STATUS_ID WHEN ? THEN ? WHEN ? THEN ? WHEN ? THEN ? END), " + "     MESSAGE = (CASE STATUS_ID WHEN ? THEN ? WHEN ? THEN ? WHEN ? THEN ? END) " + " WHERE DM_LOAD_ID = ? AND NOT EXISTS (SELECT 1 FROM  " + tablePrefix + "JOB_LOG B,  " + tablePrefix + "JOB_DEPENDENCIE C  " + " WHERE B.JOB_ID = C.PARENT_JOB_ID AND B.LOAD_ID = ? AND JOB_LOG.JOB_ID = C.JOB_ID AND B.STATUS_ID in (?, ?, ?))");
                        }
                        pStmt.setInt(1, (ETLJobStatus.PENDING_CLOSURE_FAILED));
                        pStmt.setInt(2, (ETLJobStatus.FAILED));
                        pStmt.setInt(3, (ETLJobStatus.PENDING_CLOSURE_SUCCESSFUL));
                        pStmt.setInt(4, (ETLJobStatus.SUCCESSFUL));
                        pStmt.setInt(5, (ETLJobStatus.PENDING_CLOSURE_SKIP));
                        pStmt.setInt(6, (ETLJobStatus.SKIPPED));
                        pStmt.setInt(7, (ETLJobStatus.PENDING_CLOSURE_FAILED));
                        pStmt.setString(8, etlJobStatus.getStatusMessageForCode(ETLJobStatus.FAILED));
                        pStmt.setInt(9, (ETLJobStatus.PENDING_CLOSURE_SUCCESSFUL));
                        pStmt.setString(10, etlJobStatus.getStatusMessageForCode(ETLJobStatus.SUCCESSFUL));
                        pStmt.setInt(11, (ETLJobStatus.PENDING_CLOSURE_SKIP));
                        pStmt.setString(12, etlJobStatus.getStatusMessageForCode(ETLJobStatus.SKIPPED));
                        pStmt.setInt(13, dmLoadID);
                        pStmt.setInt(14, LoadID);
                        pStmt.setInt(15, ETLJobStatus.WAITING_FOR_CHILDREN);
                        pStmt.setInt(16, ETLJobStatus.WAITING_TO_SKIP);
                        pStmt.setInt(17, ETLJobStatus.WAITING_TO_PAUSE);
                        pStmt.addBatch();
                        break;
                    case ETLJobStatus.PENDING_CLOSURE_CANCELLED:
                        if (pCancelStmt == null) {
                            pCancelStmt = metadataConnection.prepareStatement("UPDATE  " + tablePrefix + "JOB_LOG SET STATUS_ID = ?, MESSAGE = ? " + " WHERE DM_LOAD_ID = ? AND (EXISTS (SELECT 1 FROM " + tablePrefix + "LOAD WHERE LOAD_ID = ? AND START_JOB_ID <> ?) " + " OR NOT EXISTS (SELECT 1 FROM  " + tablePrefix + "LOAD B, " + tablePrefix + "JOB_LOG C " + " WHERE B.LOAD_ID = ? AND B.START_JOB_ID = ? " + " AND B.START_JOB_ID <> C.JOB_ID AND C.STATUS_ID in (?,?,?,?) ))");
                        }
                        pCancelStmt.setInt(1, (ETLJobStatus.CANCELLED));
                        pCancelStmt.setString(2, etlJobStatus.getStatusMessageForCode(ETLJobStatus.CANCELLED));
                        pCancelStmt.setInt(3, dmLoadID);
                        pCancelStmt.setInt(4, LoadID);
                        pCancelStmt.setString(5, cJobID);
                        pCancelStmt.setInt(6, LoadID);
                        pCancelStmt.setString(7, cJobID);
                        pCancelStmt.setInt(8, (ETLJobStatus.EXECUTING));
                        pCancelStmt.setInt(9, (ETLJobStatus.READY_TO_RUN));
                        pCancelStmt.setInt(10, (ETLJobStatus.ATTEMPT_PAUSE));
                        pCancelStmt.setInt(11, (ETLJobStatus.ATTEMPT_CANCEL));
                        pCancelStmt.addBatch();
                        break;
                }
            }
            if (pStmt != null) {
                pStmt.executeBatch();
                pStmt.close();
            }
            if (pCancelStmt != null) {
                pCancelStmt.executeBatch();
                pCancelStmt.close();
            }
            if (m_rs != null) {
                m_rs.close();
            }
            metadataConnection.commit();
            boolean ReturnNextJob = true;
            ETLJob job = null;
            if (ReturnNextJob == true) {
                StringBuffer jobTypesSQL = new StringBuffer();
                for (String[] jobType : pJobTypes) {
                    jobTypesSQL.append("(C.DESCRIPTION = '" + (String) jobType[0] + "' AND nvl(B.POOL,'" + EngineConstants.DEFAULT_POOL + "') = '" + jobType[1] + "') OR ");
                }
                PreparedStatement getNextJob = metadataConnection.prepareStatement(" SELECT A.JOB_ID,A.DM_LOAD_ID,A.LOAD_ID,A.RETRY_ATTEMPTS FROM  " + tablePrefix + "JOB_LOG A,  " + tablePrefix + "JOB B, " + tablePrefix + "JOB_TYPE C " + "  WHERE A.JOB_ID = B.JOB_ID AND B.JOB_TYPE_ID = C.JOB_TYPE_ID AND A.STATUS_ID IN (?) " + "  AND (" + jobTypesSQL + " B.JOB_TYPE_ID = 0) ORDER BY coalesce(B.PRIORITY,99999),START_DATE,A.LAST_UPDATE_DATE FOR UPDATE");
                getNextJob.setInt(1, ETLJobStatus.READY_TO_RUN);
                m_rs = getNextJob.executeQuery();
                String jobToHandle = null;
                PreparedStatement updJobLog = null;
                while (m_rs.next() && (job == null)) {
                    if (jobToHandle == null) {
                        jobToHandle = m_rs.getString(1);
                        if (updJobLog == null) {
                            updJobLog = metadataConnection.prepareStatement(" UPDATE  " + tablePrefix + "JOB_LOG SET STATUS_ID = ?, MESSAGE = ?, EXECUTION_DATE = " + currentTimeStampSyntax + ", SERVER_ID= ? WHERE DM_LOAD_ID = ?");
                        }
                        updJobLog.setInt(1, ETLJobStatus.EXECUTING);
                        updJobLog.setString(2, etlJobStatus.getStatusMessageForCode(ETLJobStatus.EXECUTING));
                        updJobLog.setInt(3, pServerID);
                        updJobLog.setInt(4, m_rs.getInt(2));
                        updJobLog.executeUpdate();
                        job = getJob(jobToHandle, m_rs.getInt(3), m_rs.getInt(2));
                        job.setRetryAttempts(m_rs.getInt(4));
                    }
                }
                if (getNextJob != null) {
                    getNextJob.close();
                }
                if (updJobLog != null) {
                    updJobLog.close();
                }
                if (m_rs != null) {
                    m_rs.close();
                }
            }
            metadataConnection.commit();
            if (m_rs != null) {
                m_rs.close();
            }
            if (updRetryJobs != null) {
                updRetryJobs.close();
            }
            if (updJobs != null) {
                updJobs.close();
            }
            if (selFinishedJobs != null) {
                selFinishedJobs.close();
            }
            if (rs != null) {
                rs.close();
            }
            return job;
        }
    }
}
