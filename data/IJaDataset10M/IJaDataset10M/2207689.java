package com.icteam.fiji.model;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: s.ghezzi
 * Date: 10-giu-2008
 * Time: 10.17.55
 */
public class CronJob implements java.io.Serializable {

    private String NCronJob;

    private Date DStart;

    private Date DLast;

    private Date DNext;

    private String NType;

    public String getNType() {
        return NType;
    }

    public void setNType(String NType) {
        this.NType = NType;
    }

    public Date getDNext() {
        return DNext;
    }

    public void setDNext(Date DNext) {
        this.DNext = DNext;
    }

    public Date getDStart() {
        return DStart;
    }

    public void setDStart(Date DStart) {
        this.DStart = DStart;
    }

    public Date getDLast() {
        return DLast;
    }

    public void setDLast(Date DLast) {
        this.DLast = DLast;
    }

    public String getNCronJob() {
        return NCronJob;
    }

    public void setNCronJob(String NCronJob) {
        this.NCronJob = NCronJob;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof CronJob && super.equals(o);
    }
}
