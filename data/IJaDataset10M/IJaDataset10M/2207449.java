package com.hk.bean;

import java.util.Date;
import com.hk.frame.dao.annotation.Table;

@Table(name = "resplaba", id = "labaid")
public class RespLaba {

    private long labaId;

    private int respcount;

    private Date uptime;

    private int hot;

    private Laba laba;

    public void setLaba(Laba laba) {
        this.laba = laba;
    }

    public Laba getLaba() {
        return laba;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public long getLabaId() {
        return labaId;
    }

    public void setLabaId(long labaId) {
        this.labaId = labaId;
    }

    public int getRespcount() {
        return respcount;
    }

    public void setRespcount(int respcount) {
        this.respcount = respcount;
    }

    public Date getUptime() {
        return uptime;
    }

    public void setUptime(Date uptime) {
        this.uptime = uptime;
    }
}
