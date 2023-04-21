package com.techstar.dmis.entity;

import java.io.Serializable;

/**
 * Domain classe for 间隔标准停电范围
 * This classe is based on ValueObject Pattern
 * @author 
 * @date
 */
public class MSTDBayOutage implements Serializable {

    public MSTDBayOutage() {
    }

    private String foutagedescription;

    private String fworkplace;

    private String fid;

    private int version;

    private com.techstar.dmis.entity.Mbay zmstdbayoutage2;

    private com.techstar.dmis.entity.MBayGroup zmstdbayoutage3;

    /**
     * getters and setters
     */
    public void setFoutagedescription(String foutagedescription) {
        this.foutagedescription = foutagedescription;
    }

    public String getFoutagedescription() {
        return foutagedescription;
    }

    public void setFworkplace(String fworkplace) {
        this.fworkplace = fworkplace;
    }

    public String getFworkplace() {
        return fworkplace;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFid() {
        return fid;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setZmstdbayoutage2(com.techstar.dmis.entity.Mbay zmstdbayoutage2) {
        this.zmstdbayoutage2 = zmstdbayoutage2;
    }

    public com.techstar.dmis.entity.Mbay getZmstdbayoutage2() {
        return zmstdbayoutage2;
    }

    public void setZmstdbayoutage3(com.techstar.dmis.entity.MBayGroup zmstdbayoutage3) {
        this.zmstdbayoutage3 = zmstdbayoutage3;
    }

    public com.techstar.dmis.entity.MBayGroup getZmstdbayoutage3() {
        return zmstdbayoutage3;
    }
}
