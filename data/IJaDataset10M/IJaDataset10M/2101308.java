package com.hk.bean;

import com.hk.frame.dao.annotation.Table;

@Table(name = "cmpgroupquestion", id = "oid")
public class CmpGroupQuestion {

    private long oid;

    private long cmpgroupId;

    private String quest;

    private String answer;

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public long getCmpgroupId() {
        return cmpgroupId;
    }

    public void setCmpgroupId(long cmpgroupId) {
        this.cmpgroupId = cmpgroupId;
    }

    public String getQuest() {
        return quest;
    }

    public void setQuest(String quest) {
        this.quest = quest;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
