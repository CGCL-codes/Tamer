package com.st.rrd.model;

public class FetchRequest {

    private ConsolFun consolFun;

    private long fetchStart;

    private long fetchEnd;

    private long resolution;

    private String filter;

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }

    public ConsolFun getConsolFun() {
        return consolFun;
    }

    public long getFetchStart() {
        return fetchStart;
    }

    public long getFetchEnd() {
        return fetchEnd;
    }

    public long getResolution() {
        return resolution;
    }

    public void setConsolFun(ConsolFun consolFun) {
        this.consolFun = consolFun;
    }

    public void setFetchStart(long fetchStart) {
        this.fetchStart = fetchStart;
    }

    public void setFetchEnd(long fetchEnd) {
        this.fetchEnd = fetchEnd;
    }

    public void setResolution(long resolution) {
        this.resolution = resolution;
    }
}
