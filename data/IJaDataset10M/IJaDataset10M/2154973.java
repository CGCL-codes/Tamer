package com.liusoft.dlog4j.upgrade;

import java.util.Date;

/**
 * �ɰ汾����ǩ
 * @author liudong
 */
public class BookMarkBean {

    protected int id;

    int type;

    int order;

    LogForm log;

    int userId;

    Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LogForm getLog() {
        return log;
    }

    public void setLog(LogForm log) {
        this.log = log;
    }
}
