package org.jaffa.qm.apis.data;

import javax.xml.bind.annotation.XmlEnum;
import org.jaffa.datatypes.DateTime;

/**
 * Represents a Queue in a queue-system.
 *
 * @author GautamJ
 */
public class QueueGraph {

    @XmlEnum(String.class)
    public enum Status {

        ACTIVE, INACTIVE
    }

    private QueueMetaData queueMetaData;

    private String type;

    private Long openCount;

    private Long successCount;

    private Long errorCount;

    private Long holdCount;

    private Long inProcessCount;

    private Long interruptedCount;

    private Long consumerCount;

    private Status status;

    private Boolean hasAdminAccess;

    private DateTime lastErroredOn;

    public QueueMetaData getQueueMetaData() {
        return queueMetaData;
    }

    public void setQueueMetaData(QueueMetaData queueMetaData) {
        this.queueMetaData = queueMetaData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getOpenCount() {
        return openCount;
    }

    public void setOpenCount(Long openCount) {
        this.openCount = openCount;
    }

    public Long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Long successCount) {
        this.successCount = successCount;
    }

    public Long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Long errorCount) {
        this.errorCount = errorCount;
    }

    public Long getHoldCount() {
        return holdCount;
    }

    public void setHoldCount(Long holdCount) {
        this.holdCount = holdCount;
    }

    public Long getInProcessCount() {
        return inProcessCount;
    }

    public void setInProcessCount(Long inProcessCount) {
        this.inProcessCount = inProcessCount;
    }

    public Long getInterruptedCount() {
        return interruptedCount;
    }

    public void setInterruptedCount(Long interruptedCount) {
        this.interruptedCount = interruptedCount;
    }

    public Long getConsumerCount() {
        return consumerCount;
    }

    public void setConsumerCount(Long consumerCount) {
        this.consumerCount = consumerCount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getHasAdminAccess() {
        return hasAdminAccess;
    }

    public void setHasAdminAccess(Boolean hasAdminAccess) {
        this.hasAdminAccess = hasAdminAccess;
    }

    public DateTime getLastErroredOn() {
        return lastErroredOn;
    }

    public void setLastErroredOn(DateTime lastErroredOn) {
        this.lastErroredOn = lastErroredOn;
    }
}
