package com.techstar.dmis.entity;

import java.io.Serializable;

/**
 * Domain classe for 班组表
 * This classe is based on ValueObject Pattern
 * @author 
 * @date
 */
public class DdGroup implements Serializable {

    public DdGroup() {
    }

    private String fgroupname;

    private int roundno;

    private String id;

    private int version;

    private java.util.Collection fddgroupmember2;

    /**
     * getters and setters
     */
    public void setFgroupname(String fgroupname) {
        this.fgroupname = fgroupname;
    }

    public String getFgroupname() {
        return fgroupname;
    }

    public void setRoundno(int roundno) {
        this.roundno = roundno;
    }

    public int getRoundno() {
        return roundno;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setFddgroupmember2(java.util.Collection fddgroupmember2) {
        this.fddgroupmember2 = fddgroupmember2;
    }

    public java.util.Collection getFddgroupmember2() {
        return fddgroupmember2;
    }
}
