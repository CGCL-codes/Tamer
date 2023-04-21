package com.creawor.hz_market.resource.group_resource;

import java.util.Date;

/**
 * GroupResource entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class GroupResource implements java.io.Serializable {

    private Integer id;

    private String type;

    private Integer num;

    private Integer kejieru;

    private Integer bukejieru;

    private Double jierubili;

    private Date insertDay;

    private Date updatedDay;

    /** default constructor */
    public GroupResource() {
    }

    /** full constructor */
    public GroupResource(String type, Integer num, Integer kejieru, Integer bukejieru, Double jierubili, Date insertDay, Date updatedDay) {
        this.type = type;
        this.num = num;
        this.kejieru = kejieru;
        this.bukejieru = bukejieru;
        this.jierubili = jierubili;
        this.insertDay = insertDay;
        this.updatedDay = updatedDay;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNum() {
        return this.num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getKejieru() {
        return this.kejieru;
    }

    public void setKejieru(Integer kejieru) {
        this.kejieru = kejieru;
    }

    public Integer getBukejieru() {
        return this.bukejieru;
    }

    public void setBukejieru(Integer bukejieru) {
        this.bukejieru = bukejieru;
    }

    public Double getJierubili() {
        return this.jierubili;
    }

    public void setJierubili(Double jierubili) {
        this.jierubili = jierubili;
    }

    public Date getInsertDay() {
        return this.insertDay;
    }

    public void setInsertDay(Date insertDay) {
        this.insertDay = insertDay;
    }

    public Date getUpdatedDay() {
        return this.updatedDay;
    }

    public void setUpdatedDay(Date updatedDay) {
        this.updatedDay = updatedDay;
    }
}
