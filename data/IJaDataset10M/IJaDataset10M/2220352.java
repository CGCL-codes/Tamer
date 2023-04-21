package com.hk.bean;

import com.hk.frame.dao.annotation.Table;

@Table(name = "labadel")
public class LabaDel extends Laba {

    public LabaDel() {
    }

    public LabaDel(Laba laba) {
        this.setLabaId(laba.getLabaId());
        this.setUserId(laba.getUserId());
        this.setCityId(laba.getCityId());
        this.setContent(laba.getContent());
        this.setCreateTime(laba.getCreateTime());
        this.setIp(laba.getIp());
        this.setReplyCount(laba.getReplyCount());
        this.setSendFrom(laba.getSendFrom());
        this.setRangeId(laba.getRangeId());
    }

    private long opuserId;

    private long optime;

    public long getOpuserId() {
        return opuserId;
    }

    public void setOpuserId(long opuserId) {
        this.opuserId = opuserId;
    }

    public long getOptime() {
        return optime;
    }

    public void setOptime(long optime) {
        this.optime = optime;
    }
}
