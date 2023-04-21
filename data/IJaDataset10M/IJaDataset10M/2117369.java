package com.creditors.vo;

public class ProcessStateVO {

    private long id;

    private long trxID;

    private long typeID;

    private String typeName;

    private long statusID;

    private String statusName;

    private String dbUser;

    private long userID;

    private java.sql.Timestamp modified;

    public void setId(long id) {
        this.id = id;
    }

    public void setTrxID(long trxID) {
        this.trxID = trxID;
    }

    public void setTypeID(long typeID) {
        this.typeID = typeID;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setStatusID(long statusID) {
        this.statusID = statusID;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setModified(java.sql.Timestamp modified) {
        this.modified = modified;
    }

    public long getId() {
        return id;
    }

    public long getTrxID() {
        return trxID;
    }

    public long getTypeID() {
        return typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public long getStatusID() {
        return statusID;
    }

    public String getStatusName() {
        return statusName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public long getUserID() {
        return userID;
    }

    public java.sql.Timestamp getModified() {
        return modified;
    }

    public String toString() {
        return id + ";" + trxID + ";" + typeID + ";" + typeName + ";" + statusID + ";" + statusName + ";" + dbUser + ";" + userID + ";" + modified;
    }

    public void init() {
        id = 0;
        trxID = 0;
        typeID = 0;
        typeName = "";
        statusID = 0;
        statusName = "";
        dbUser = "";
        userID = 0;
    }
}
