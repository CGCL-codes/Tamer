package com.blindeye.model.blindeyes;

import static org.jboss.seam.ScopeType.EVENT;
import static org.jboss.seam.ScopeType.SESSION;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Role;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("job")
@Scope(SESSION)
@Role(name = "tempJob", scope = EVENT)
@Table(name = "jobs")
public class Job implements Serializable {

    private Integer id;

    private int version;

    /** Name can be any arbitrary label, ect... nmapScan, nmapXmlParse, admin*/
    private String name = "";

    /** Type of job must correspond with availabe types: plugin, javascript, linuxShellScript*/
    private String type = "";

    /** Member of these specific groups*/
    private List<ExecutionGroup> executionGroups = new ArrayList<ExecutionGroup>();

    /** new, executing, failed, completed */
    private String status = "new";

    /** Default to 0 length for the output */
    private int outputLength = 0;

    /** Default to plain text type */
    private String outputMimeType = "text/plain";

    /** Resulting output after the job has finished*/
    private byte[] output = new byte[] {};

    /** Resulting error after the job has finished or failed*/
    private byte[] error = new byte[] {};

    private List<Parameter> parameters = new ArrayList<Parameter>();

    /** Dictates when the job is actually loaded into the queue to be processed */
    private Date jobStartDateTime = new Date();

    /** Contains the interval in which to reoccur the job
	 */
    private TimeInterval timeInterval = new TimeInterval();

    /** Contains the interval in which to reoccur the job
	 */
    private TimeInterval timeOut = new TimeInterval();

    /** Time in ms that the job was first created */
    private Date dateCreated;

    /** Time in ms that the job was last started */
    private Date dateStarted;

    /** Time in ms that the job was last completed*/
    private Date dateCompleted;

    /** Indicates the number of times the job should be retried in the event of a failure or timeout */
    private int retriesOnFailure = 0;

    private String active = "False";

    private int numberOfCompletedExecutions = 0;

    private int numberOfFailedExecutions = 0;

    @Transient
    private String timeTaken;

    @Transient
    private static long MILLIS_IN_MONTH = DateUtils.MILLIS_PER_DAY * 30;

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(length = 50)
    @Length(max = 50)
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    public List<ExecutionGroup> getExecutionGroups() {
        return executionGroups;
    }

    public void setExecutionGroups(List<ExecutionGroup> executionGroups) {
        this.executionGroups = executionGroups;
    }

    @Column(length = 50)
    @Length(max = 50)
    @NotNull
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(length = 10)
    @Length(max = 10)
    @NotNull
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Lob
    @Column(length = 2147483647)
    public byte[] getError() {
        return error;
    }

    public void setError(byte[] error) {
        this.error = error;
    }

    @Lob
    @Column(length = 2147483647)
    public byte[] getOutput() {
        return output;
    }

    public void setOutput(byte[] output) {
        this.output = output;
    }

    @NotNull
    public int getOutputLength() {
        return outputLength;
    }

    public void setOutputLength(int outputLength) {
        this.outputLength = outputLength;
    }

    @Column(length = 20)
    @Length(max = 20)
    @NotNull
    public String getOutputMimeType() {
        return outputMimeType;
    }

    public void setOutputMimeType(String outputMimeType) {
        this.outputMimeType = outputMimeType;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getJobStartDateTime() {
        return jobStartDateTime;
    }

    public void setJobStartDateTime(Date jobStartDateTime) {
        this.jobStartDateTime = jobStartDateTime;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    public TimeInterval getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(TimeInterval timeOut) {
        this.timeOut = timeOut;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    @Version
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Integer getNumberOfCompletedExecutions() {
        return numberOfCompletedExecutions;
    }

    public void setNumberOfCompletedExecutions(Integer numberOfCompletedExecutions) {
        this.numberOfCompletedExecutions = numberOfCompletedExecutions;
    }

    public Integer getNumberOfFailedExecutions() {
        return numberOfFailedExecutions;
    }

    public void setNumberOfFailedExecutions(Integer numberOfFailedExecutions) {
        this.numberOfFailedExecutions = numberOfFailedExecutions;
    }

    @Transient
    public String getTimeTaken() {
        if (dateStarted == null || dateCompleted == null) {
            return "";
        }
        long startTime = dateStarted.getTime();
        long endTime = dateCompleted.getTime();
        long elapsedTime = endTime - startTime;
        return formatTime(elapsedTime);
    }

    @Transient
    public String formatTime(long ms) {
        long x = 0;
        int months = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        ;
        while ((x = (ms - MILLIS_IN_MONTH)) > 0) {
            ms -= MILLIS_IN_MONTH;
            ++months;
        }
        while ((x = (ms - DateUtils.MILLIS_PER_DAY)) > 0) {
            ms -= DateUtils.MILLIS_PER_DAY;
            ++days;
        }
        while ((x = (ms - DateUtils.MILLIS_PER_HOUR)) > 0) {
            ms -= DateUtils.MILLIS_PER_HOUR;
            ++hours;
        }
        while ((x = (ms - DateUtils.MILLIS_PER_MINUTE)) > 0) {
            ms -= DateUtils.MILLIS_PER_MINUTE;
            ++minutes;
        }
        while ((x = (ms - DateUtils.MILLIS_PER_SECOND)) > 0) {
            ms -= DateUtils.MILLIS_PER_SECOND;
            ++seconds;
        }
        return formatUnit(months) + "M " + formatUnit(days) + "d " + formatUnit(hours) + "h " + formatUnit(minutes) + "m " + formatUnit(seconds) + "s";
    }

    @Transient
    public String formatUnit(int unit) {
        String unitString = String.valueOf(unit);
        if (unitString.length() == 2) {
            return unitString;
        } else {
            return (unitString = " " + unitString);
        }
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Job other = (Job) obj;
        if (id != other.id) return false;
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append("name: " + name).append(", type: " + type).append(", id: " + id).append(", status: " + status).append("]");
        return sb.toString();
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public int getRetriesOnFailure() {
        return retriesOnFailure;
    }

    public void setRetriesOnFailure(int retriesOnFailure) {
        this.retriesOnFailure = retriesOnFailure;
    }
}
