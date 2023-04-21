package org.eaasyst.eaa.syst.data.persistent;

import java.text.DecimalFormat;
import java.util.Date;
import org.eaasyst.eaa.syst.EaasyStreet;
import org.eaasyst.eaa.syst.data.PersistentDataBean;
import org.eaasyst.eaa.syst.data.PersistentDataBeanBase;
import org.eaasyst.eaa.utils.DateUtils;

/**
 * <p>This entity class defines a <code>BatchJob</code> object.</p>
 *
 * @version 2.9.1
 * @author Saleem Salahuddin
 */
public class BatchJob extends PersistentDataBeanBase {

    private static final long serialVersionUID = 1;

    public static final String STATUS_RUNNING_VALUE = "Running";

    public static final String STATUS_COMPLETED_VALUE = "Completed";

    private String userId = null;

    private String jobStatus = null;

    private Date jobStartTime = null;

    private Date jobEndTime = null;

    private String jobLogFile = null;

    private String jobName = null;

    private Integer jobCount = null;

    private Float jobElapsedTime = null;

    private Integer fileId = null;

    /**
	 * <p>Updates this bean with the data from an equivalent bean.</p>
	 * 
	 * @param a previously populated instance of this class of entity bean
	 * @since Eaasy Street 2.0.1
	 */
    protected void updateBeanFields(PersistentDataBean bean) {
        BatchJob bj = (BatchJob) bean;
        userId = bj.getUserId();
        jobStatus = bj.getJobStatus();
        jobStartTime = bj.getJobStartTime();
        jobEndTime = bj.getJobEndTime();
        jobLogFile = bj.getJobLogFile();
        jobName = bj.getJobName();
        jobCount = bj.getJobCount();
        jobElapsedTime = bj.getJobElapsedTime();
        fileId = bj.getFileId();
    }

    /**
	 * <p>Returns a String containing the comma-separated values of
	 * the contents of this entity bean.</p>
	 * 
	 * @return a String containing the comma-separated values of
	 * this entity bean
	 * @since Eaasy Street 2.2
	 */
    protected String getDataAsCsv() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\"");
        buffer.append(userId);
        buffer.append("\",\"");
        buffer.append(jobStatus);
        buffer.append("\",\"");
        buffer.append(getJobStartTimeDisplay());
        buffer.append("\",\"");
        buffer.append(getJobEndTimeDisplay());
        buffer.append("\",\"");
        buffer.append(jobLogFile);
        buffer.append("\",\"");
        buffer.append(jobName);
        buffer.append("\",\"");
        buffer.append(jobCount);
        buffer.append("\",");
        buffer.append(jobElapsedTime);
        buffer.append("\",");
        buffer.append(fileId);
        return buffer.toString();
    }

    /**
	 * <p>Returns a String containing the comma-separated column 
	 * headings for the csv values of this entity bean.</p>
	 * 
	 * @return a String containing the comma-separated column 
	 * headings for the csv values of this entity bean
	 * @since Eaasy Street 2.2
	 */
    protected String getHeadingsAsCsv() {
        return "\"userId\",\"jobStatus\",\"JobStartTime\",\"JobEndTime\",\"jobLogFile\",\"jobName\",\"jobCount\",\"jobElapsedTime\",\"fileId\"";
    }

    /**
	 * Returns the html link to open the file attachment (if there) or the log file 
	 * @return String
	 */
    public String getLogFileLink() {
        String logFileLink = jobLogFile;
        if (fileId != null) {
            if (fileId.intValue() > 0) {
                String uri = EaasyStreet.getServletRequest().getRequestURI();
                uri = uri.substring(0, uri.lastIndexOf("/"));
                logFileLink = uri + "/fetch?id=" + fileId.toString();
            }
        }
        return "<a href=\"" + logFileLink + "\" target=\"_blank\">View Log</a>";
    }

    /**
	 * Returns the formatted date
	 * @return String
	 */
    public String getJobStartTimeDisplay() {
        return DateUtils.dateTimeToString(getJobStartTime());
    }

    /**
	 * Returns the formatted date
	 * @return String
	 */
    public String getJobEndTimeDisplay() {
        return DateUtils.dateTimeToString(getJobEndTime());
    }

    /**
	 * Returns the formatted date
	 * @return String
	 */
    public String getLastUpdateDisplay() {
        return DateUtils.dateTimeToString(getLastUpdate());
    }

    /**
	 * Returns the jobCount.
	 * @return Integer
	 */
    public Integer getJobCount() {
        return jobCount;
    }

    /**
	 * Returns the jobLogFile.
	 * @return String
	 */
    public String getJobLogFile() {
        return jobLogFile;
    }

    /**
	 * Returns the jobName.
	 * @return String
	 */
    public String getJobName() {
        return jobName;
    }

    /**
	 * Returns the jobStartTime.
	 * @return Date
	 */
    public Date getJobStartTime() {
        return jobStartTime;
    }

    /**
	 * Returns the jobStatus.
	 * @return String
	 */
    public String getJobStatus() {
        return jobStatus;
    }

    /**
	 * Returns the userId.
	 * @return String
	 */
    public String getUserId() {
        return userId;
    }

    /**
	 * Sets the jobCount.
	 * @param jobCount The jobCount to set
	 */
    public void setJobCount(Integer jobCount) {
        this.jobCount = jobCount;
    }

    /**
	 * Sets the jobLogFile.
	 * @param jobLogFile The jobLogFile to set
	 */
    public void setJobLogFile(String jobLogFile) {
        this.jobLogFile = jobLogFile;
    }

    /**
	 * Sets the jobName.
	 * @param jobName The jobName to set
	 */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
	 * Sets the jobStartTime.
	 * @param jobStartTime The jobStartTime to set
	 */
    public void setJobStartTime(Date jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    /**
	 * Sets the jobStatus.
	 * @param jobStatus The jobStatus to set
	 */
    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    /**
	 * Sets the userId.
	 * @param userId The userId to set
	 */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
	 * Returns the jobEndTime.
	 * @return Date
	 */
    public Date getJobEndTime() {
        return jobEndTime;
    }

    /**
	 * Sets the jobEndTime.
	 * @param jobEndTime The jobEndTime to set
	 */
    public void setJobEndTime(Date jobEndTime) {
        this.jobEndTime = jobEndTime;
    }

    /**
	 * Returns the jobElapsedTime.
	 * @return Float
	 */
    public Float getJobElapsedTime() {
        return jobElapsedTime;
    }

    /**
	 * Sets the jobElapsedTime.
	 * @param jobElapsedTime The jobElapsedTime to set
	 */
    public void setJobElapsedTime(Float jobElapsedTime) {
        this.jobElapsedTime = jobElapsedTime;
    }

    /**
	 * Returns the jobElapsedTimeDisplay
	 * formatted to be have 1 decimal places
	 * @return String
	 */
    public String getJobElapsedTimeDisplay() {
        DecimalFormat myFormatter = new DecimalFormat("#0.0");
        if (jobElapsedTime != null) {
            return myFormatter.format(jobElapsedTime);
        } else {
            return "";
        }
    }

    /**
	 * @return
	 */
    public Integer getFileId() {
        return fileId;
    }

    /**
	 * @param integer
	 */
    public void setFileId(Integer integer) {
        fileId = integer;
    }
}
