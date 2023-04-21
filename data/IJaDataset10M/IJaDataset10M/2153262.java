package au.gov.naa.digipres.dpr.model.transferjob;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import au.gov.naa.digipres.dpr.model.job.ProcessingRecord;
import au.gov.naa.digipres.dpr.model.user.User;

/**
 * This class holds information regarding a transfer job that has stopped processing.
 * 
 * 
 */
@Entity
@Table(name = "qf_stop_processing_record")
public class QFStopProcessingRecord implements ProcessingRecord {

    private String id;

    private long version = -1;

    private QFTransferJobProcessingRecord qfTransferJobProcessingRecord;

    private Boolean rejectedInQF;

    private Date dateRejected;

    private User rejectedBy;

    private String rejectionReason;

    private Boolean restarted;

    private Date dateRestarted;

    private User restartedBy;

    private String restartComments;

    private String state = IN_PROGRESS;

    public static final String IN_PROGRESS = "In Progress";

    public static final String REJECTED = "Rejected";

    public static final String RESTARTED = "Restarted";

    public QFStopProcessingRecord() {
    }

    /**
	 */
    @Transient
    public int getProcessingOrder() {
        return 0;
    }

    /**
	 * TODO: Change the column name.
	 * 
	 * @return
	 */
    @Column(name = "restart_comments", columnDefinition = "text")
    public String getRestartComments() {
        return restartComments;
    }

    public void setRestartComments(String restartComments) {
        this.restartComments = restartComments;
    }

    /**
	 * @return
	 */
    @ManyToOne
    @JoinColumn(name = "qf_transfer_job_processing_record_id")
    public QFTransferJobProcessingRecord getQfTransferJobProcessingRecord() {
        return qfTransferJobProcessingRecord;
    }

    public void setQfTransferJobProcessingRecord(QFTransferJobProcessingRecord qfTransferJobProcessingRecord) {
        this.qfTransferJobProcessingRecord = qfTransferJobProcessingRecord;
    }

    /**
	 * @return
	 */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "qf_stop_processing_record_id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @return
	 */
    @Column(name = "rejected_in_qf")
    public Boolean getRejectedInQF() {
        return rejectedInQF;
    }

    public void setRejectedInQF(Boolean returnedToPF) {
        this.rejectedInQF = returnedToPF;
    }

    /**
	 * @return
	 */
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    /**
	 * @return Returns the state.
	 */
    @Column(name = "state")
    public String getState() {
        return state;
    }

    /**
	 * @param state
	 *            The state to set.
	 */
    public void setState(String state) {
        this.state = state;
    }

    /**
	 * @return Returns the restarted.
	 */
    @Column(name = "restarted")
    public Boolean getRestarted() {
        return restarted;
    }

    /**
	 * @param restarted
	 *            The restarted to set.
	 */
    public void setRestarted(Boolean restarted) {
        this.restarted = restarted;
    }

    /**
	 * @return Returns the dateRestarted.
	 */
    @Column(name = "date_restarted")
    public Date getDateRestarted() {
        return dateRestarted;
    }

    /**
	 * @param dateRestarted
	 *            The dateRestarted to set.
	 */
    public void setDateRestarted(Date dateRestarted) {
        this.dateRestarted = dateRestarted;
    }

    /**
	 * @return Returns the dateRejected.
	 */
    @Column(name = "date_returned")
    public Date getDateRejected() {
        return dateRejected;
    }

    /**
	 * @param dateRejected
	 *            The dateRejected to set.
	 */
    public void setDateRejected(Date dateReturned) {
        this.dateRejected = dateReturned;
    }

    /**
	 * @return Returns the restartedBy.
	 */
    @ManyToOne
    @JoinColumn(name = "restarted_by")
    public User getRestartedBy() {
        return restartedBy;
    }

    /**
	 * @param restartedBy
	 *            The restartedBy to set.
	 */
    public void setRestartedBy(User restartedBy) {
        this.restartedBy = restartedBy;
    }

    /**
	 * @return Returns the rejectedBy.
	 */
    @ManyToOne
    @JoinColumn(name = "returned_by")
    public User getRejectedBy() {
        return rejectedBy;
    }

    /**
	 * @param rejectedBy
	 *            The rejectedBy to set.
	 */
    public void setRejectedBy(User returnedBy) {
        this.rejectedBy = returnedBy;
    }

    /**
	 * @param currentUser
	 */
    public void reject(String reason, User currentUser) {
        setState(QFStopProcessingRecord.REJECTED);
        setRejectedInQF(Boolean.TRUE);
        setRejectedBy(currentUser);
        setRejectionReason(reason);
        setDateRejected(new Date());
        qfTransferJobProcessingRecord.getTransferJob().rejectedInQF();
        setRestarted(null);
        setRestartedBy(null);
        setDateRestarted(null);
        setRestartComments(null);
    }

    /**
	 * @param restartComments
	 * @param b
	 * @param currentUser
	 */
    public void restartInQF(String restartCommentsParm, User currentUser) {
        setRestartComments(restartCommentsParm);
        setState(QFStopProcessingRecord.RESTARTED);
        setRestarted(Boolean.TRUE);
        setRestartedBy(currentUser);
        setDateRestarted(new Date());
        qfTransferJobProcessingRecord.getTransferJob().restartInQF();
        setRejectedInQF(null);
        setRejectedBy(null);
        setDateRejected(null);
        setRejectionReason(null);
    }

    /**
	 * @return
	 */
    @Column(name = "rejection_reason", columnDefinition = "text")
    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
