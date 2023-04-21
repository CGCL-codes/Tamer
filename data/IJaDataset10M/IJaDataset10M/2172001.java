package com.sitescape.team.module.workflow.support;

import java.io.Serializable;
import java.util.Map;

public class WorkflowStatus implements Serializable {

    private static final int serialVersionUUID = 1;

    private String message;

    private int retrySeconds = 300;

    private Map params;

    public WorkflowStatus(Map params, int retrySeconds) {
        this.params = params;
        this.retrySeconds = retrySeconds;
    }

    public String getErrorMessage() {
        return message;
    }

    public void setErrorMessage(String message) {
        this.message = message;
    }

    public int getRetrySeconds() {
        return retrySeconds;
    }

    public void setRetrySeconds(int seconds) {
        this.retrySeconds = seconds;
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }
}
