package org.appspy.server.bo.scheduler;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
@Entity
@Table(name = "SCH_JOB_EXEC")
public class JobExecution {

    @Id
    @GeneratedValue
    @Column(name = "jobExecId")
    protected Long mId;

    @ManyToOne
    @JoinColumn(name = "jobSchId")
    protected JobSchedule mJobSchedule;

    @Basic
    @Column(name = "jobExecStart")
    protected Timestamp mStartTime;

    @Basic
    @Column(name = "jobExecEnd")
    protected Timestamp mEndTime;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "mJobExecution")
    @Cascade({ org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    protected Set<JobExecutionParam> mParams = new HashSet<JobExecutionParam>();

    @Basic
    @Column(name = "jobException", length = 255)
    protected String mExceptionMessage;

    /**
     * @return the id
     */
    public Long getId() {
        return mId;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        mId = id;
    }

    /**
     * @return the jobSchedule
     */
    public JobSchedule getJobSchedule() {
        return mJobSchedule;
    }

    /**
     * @param jobSchedule the jobSchedule to set
     */
    public void setJobSchedule(JobSchedule jobSchedule) {
        mJobSchedule = jobSchedule;
    }

    /**
     * @return the startTime
     */
    public Timestamp getStartTime() {
        return mStartTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Timestamp startTime) {
        mStartTime = startTime;
    }

    /**
     * @return the endTime
     */
    public Timestamp getEndTime() {
        return mEndTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Timestamp endTime) {
        mEndTime = endTime;
    }

    /**
     * @return the params
     */
    public Set<JobExecutionParam> getParams() {
        return mParams;
    }

    /**
     * @param params the params to set
     */
    public void setParams(Set<JobExecutionParam> params) {
        mParams = params;
    }

    /**
     * @return the exceptionMessage
     */
    public String getExceptionMessage() {
        return mExceptionMessage;
    }

    /**
     * @param exceptionMessage the exceptionMessage to set
     */
    public void setExceptionMessage(String exceptionMessage) {
        mExceptionMessage = exceptionMessage;
    }
}
