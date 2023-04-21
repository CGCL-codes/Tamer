package com.angis.fx.data;

import java.io.Serializable;

public class CKTypeInfo implements Serializable {

    private static final long serialVersionUID = 4797875238755615020L;

    private String wid;

    private String conditionType;

    private String wgType;

    private String creationTime;

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getWgType() {
        return wgType;
    }

    public void setWgType(String wgType) {
        this.wgType = wgType;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
}
