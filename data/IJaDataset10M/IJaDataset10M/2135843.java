package com.mat.param;

public class ProcessStateParameter {

    private String id;

    private String trxID;

    private String typeID;

    private String typeName;

    private String statusID;

    private String statusName;

    private String dbUser;

    private String userID;

    private String modified;

    private String searchString;

    public void setId(String id) {
        this.id = id;
    }

    public void setTrxID(String trxID) {
        this.trxID = trxID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getId() {
        return id;
    }

    public String getTrxID() {
        return trxID;
    }

    public String getTypeID() {
        return typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getStatusID() {
        return statusID;
    }

    public String getStatusName() {
        return statusName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getUserID() {
        return userID;
    }

    public String getModified() {
        return modified;
    }

    public String getSearchString() {
        return searchString;
    }

    public String toString() {
        return id + ";" + trxID + ";" + typeID + ";" + typeName + ";" + statusID + ";" + statusName + ";" + dbUser + ";" + userID + ";" + modified + ";" + searchString;
    }

    public void init() {
    }
}
