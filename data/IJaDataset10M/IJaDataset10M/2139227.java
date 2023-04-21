package com.creawor.hz_market.resource.cheliang_resource;

import java.util.Date;

/**
 * CheliangResource entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class CheliangResource implements java.io.Serializable {

    private Integer id;

    private String county;

    private Integer selfNum;

    private Integer zuyongNum;

    private Date insertDay;

    private Date updatedDay;

    public CheliangResource() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public Integer getSelfNum() {
        return selfNum;
    }

    public void setSelfNum(Integer selfNum) {
        this.selfNum = selfNum;
    }

    public Integer getZuyongNum() {
        return zuyongNum;
    }

    public void setZuyongNum(Integer zuyongNum) {
        this.zuyongNum = zuyongNum;
    }

    public Date getInsertDay() {
        return insertDay;
    }

    public void setInsertDay(Date insertDay) {
        this.insertDay = insertDay;
    }

    public Date getUpdatedDay() {
        return updatedDay;
    }

    public void setUpdatedDay(Date updatedDay) {
        this.updatedDay = updatedDay;
    }
}
