package com.openjmsadapter.configuration.database;

/**
 *
 * @author hari
 */
public class JmsDestinationParameters {

    private long id;

    private String destinationName;

    private String destinationType;

    private String operationType;

    private long lastSequenceSent;

    private long lastSequenceReceived;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

    public long getLastSequenceReceived() {
        return lastSequenceReceived;
    }

    public void setLastSequenceReceived(long lastSequenceReceived) {
        this.lastSequenceReceived = lastSequenceReceived;
    }

    public long getLastSequenceSent() {
        return lastSequenceSent;
    }

    public void setLastSequenceSent(long lastSequenceSent) {
        this.lastSequenceSent = lastSequenceSent;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
