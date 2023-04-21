package com.sefer.dragonfly.client.core.domain;

import java.io.Serializable;

public class LogData {

    private Serializable obj;

    private String cacheId;

    private int timeout;

    public String getCacheId() {
        return cacheId;
    }

    public void setCacheId(String cacheId) {
        this.cacheId = cacheId;
    }

    public Serializable getObj() {
        return obj;
    }

    public void setObj(Serializable obj) {
        this.obj = obj;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public LogData(Serializable obj, String cacheId, int timeout) {
        super();
        this.obj = obj;
        this.cacheId = cacheId;
        this.timeout = timeout;
    }
}
