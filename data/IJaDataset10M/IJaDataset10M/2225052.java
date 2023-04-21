package com.wgo.bpot.common.transport.servicefacade;

public class SaveResult {

    private Object[] savedConcepts;

    private Long serverTime;

    public Object[] getSavedConcepts() {
        return savedConcepts;
    }

    public void setSavedConcepts(Object[] savedConcepts) {
        this.savedConcepts = savedConcepts;
    }

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }
}
