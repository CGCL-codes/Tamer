package com.icteam.fiji.command.profiler;

import java.io.Serializable;

public class ClientProfilingStat implements Serializable {

    private static final long serialVersionUID = 1L;

    private String operationTag;

    private Long startTime;

    private Long stopTime;

    public ClientProfilingStat() {
        super();
    }

    public String getOperationTag() {
        return operationTag;
    }

    public void setOperationTag(String operationTag) {
        this.operationTag = operationTag;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getStopTime() {
        return stopTime;
    }

    public void setStopTime(Long stopTime) {
        this.stopTime = stopTime;
    }
}
