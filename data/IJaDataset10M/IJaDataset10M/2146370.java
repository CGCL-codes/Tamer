package org.brainypdm.dto.scheduler;

import java.util.Date;
import org.brainypdm.dto.BaseDto;

/**
 * @author <a href="mailto:thomas@brainypdm.org">Thomas Buffagni</a>
 */
public class JobStats extends BaseDto {

    private static final long serialVersionUID = 4928461441591452715L;

    private Long definitionId;

    private Long totRunning = new Long(0);

    private Long totTimes = new Long(0);

    private Long totStoppedBySys = new Long(0);

    private Long totStoppedByUser = new Long(0);

    private Long totFinished = new Long(0);

    private Long totFinishedWithErrors = new Long(0);

    private Long totFinishedWithWarning = new Long(0);

    private Long avgTime = new Long(0);

    private Long maxTime = new Long(0);

    private Long minTime = new Long(0);

    private Date lastCreation;

    private Date nextCreation;

    public void initialize(JobDefinition def) {
        this.definitionId = def.getId();
        totTimes = new Long(0);
        totStoppedBySys = new Long(0);
        totStoppedByUser = new Long(0);
        totFinished = new Long(0);
        totFinishedWithErrors = new Long(0);
        totFinishedWithWarning = new Long(0);
        avgTime = new Long(0);
        maxTime = new Long(0);
        minTime = new Long(0);
    }

    public Long getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(Long definitionId) {
        this.definitionId = definitionId;
    }

    public Long getTotTimes() {
        if (totTimes == null) {
            totTimes = new Long(0);
        }
        return totTimes;
    }

    public void setTotTimes(Long totTimes) {
        this.totTimes = totTimes;
    }

    public Long getTotStoppedBySys() {
        return totStoppedBySys;
    }

    public void setTotStoppedBySys(Long totStoppedBySys) {
        this.totStoppedBySys = totStoppedBySys;
    }

    public Long getTotStoppedByUser() {
        return totStoppedByUser;
    }

    public void setTotStoppedByUser(Long totStoppedByUser) {
        this.totStoppedByUser = totStoppedByUser;
    }

    public Long getTotFinished() {
        return totFinished;
    }

    public void setTotFinished(Long totFinished) {
        this.totFinished = totFinished;
    }

    public Long getTotFinishedWithErrors() {
        return totFinishedWithErrors;
    }

    public void setTotFinishedWithErrors(Long totFinishedWithErrors) {
        this.totFinishedWithErrors = totFinishedWithErrors;
    }

    public Long getTotFinishedWithWarning() {
        return totFinishedWithWarning;
    }

    public void setTotFinishedWithWarning(Long totFinishedWithWarning) {
        this.totFinishedWithWarning = totFinishedWithWarning;
    }

    public Long getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(Long avgTime) {
        this.avgTime = avgTime;
    }

    public Long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Long maxTime) {
        this.maxTime = maxTime;
    }

    public Long getMinTime() {
        return minTime;
    }

    public void setMinTime(Long minTime) {
        this.minTime = minTime;
    }

    public Date getLastCreation() {
        return lastCreation;
    }

    public void setLastCreation(Date lastCreation) {
        this.lastCreation = lastCreation;
    }

    public Date getNextCreation() {
        return nextCreation;
    }

    public void setNextCreation(Date nextCreation) {
        this.nextCreation = nextCreation;
    }

    public Long getTotRunning() {
        return totRunning;
    }

    public void setTotRunning(Long totRunning) {
        this.totRunning = totRunning;
    }
}
