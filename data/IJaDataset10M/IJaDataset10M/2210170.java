package de.unistuttgart.iaaskpimonitoring.esper.eventmanagement;

import java.util.Set;

public class Event {

    protected String name;

    protected long timeStamp;

    protected long processInstanceID;

    protected Set<Long> correlationSet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getProcessInstanceID() {
        return processInstanceID;
    }

    public void setProcessInstanceID(long processInstanceID) {
        this.processInstanceID = processInstanceID;
    }
}
