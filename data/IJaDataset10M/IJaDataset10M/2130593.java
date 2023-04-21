package com.hk.bean;

import com.hk.frame.dao.annotation.Table;

@Table(name = "cmpunionopuser")
public class CmpUnionOpUser {

    private long uid;

    private long userId;

    private User user;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
