package com.hk.bean;

import com.hk.frame.dao.annotation.Table;

@Table(name = "labarootreplydel")
public class LabaRootReplyDel extends LabaRootReply {

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
