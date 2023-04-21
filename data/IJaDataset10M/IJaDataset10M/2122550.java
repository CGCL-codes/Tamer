package com.techstar.dmis.entity;

import java.io.Serializable;

/**
 * Domain classe for 自动化缺陷分类2
 * This classe is based on ValueObject Pattern
 * @author 
 * @date
 */
public class StdZdhfaulttype implements Serializable {

    public StdZdhfaulttype() {
    }

    private String stdparentid;

    private String ffaulttype2;

    private String faulttypeid;

    private int version;

    private java.util.Collection fzdhfaultlist10;

    /**
     * getters and setters
     */
    public void setStdparentid(String stdparentid) {
        this.stdparentid = stdparentid;
    }

    public String getStdparentid() {
        return stdparentid;
    }

    public void setFfaulttype2(String ffaulttype2) {
        this.ffaulttype2 = ffaulttype2;
    }

    public String getFfaulttype2() {
        return ffaulttype2;
    }

    public void setFaulttypeid(String faulttypeid) {
        this.faulttypeid = faulttypeid;
    }

    public String getFaulttypeid() {
        return faulttypeid;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setFzdhfaultlist10(java.util.Collection fzdhfaultlist10) {
        this.fzdhfaultlist10 = fzdhfaultlist10;
    }

    public java.util.Collection getFzdhfaultlist10() {
        return fzdhfaultlist10;
    }
}
