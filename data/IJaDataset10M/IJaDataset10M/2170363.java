package com.laoer.bbscs.bean;

import java.util.*;

public class Elite {

    private Long id;

    private long parentID;

    private List parentIDs;

    private String eliteName;

    private String createUser;

    private long eliteTime;

    private int orders;

    private long boardID;

    public Elite() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setParentID(long parentID) {
        this.parentID = parentID;
    }

    public void setParentIDs(List parentIDs) {
        this.parentIDs = parentIDs;
    }

    public void setEliteName(String eliteName) {
        this.eliteName = eliteName;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public void setEliteTime(long eliteTime) {
        this.eliteTime = eliteTime;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public void setBoardID(long boardID) {
        this.boardID = boardID;
    }

    public Long getId() {
        return id;
    }

    public long getParentID() {
        return parentID;
    }

    public List getParentIDs() {
        return parentIDs;
    }

    public String getEliteName() {
        return eliteName;
    }

    public String getCreateUser() {
        return createUser;
    }

    public long getEliteTime() {
        return eliteTime;
    }

    public int getOrders() {
        return orders;
    }

    public long getBoardID() {
        return boardID;
    }
}
