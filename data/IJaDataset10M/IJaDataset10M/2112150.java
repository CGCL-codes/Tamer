package com.creawor.hz_market.resource.groupjieru_resource;

import java.util.Date;

/**
 * GroupjieruResource entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class GroupjieruResource implements java.io.Serializable {

    private Integer id;

    private String groupCode;

    private String groupName;

    private String grouptype;

    private String company;

    private Date updatedDay;

    private Date insertDay;

    private String jierufs;

    private String shifouzy;

    private String guanglan;

    private String dianlan;

    /** default constructor */
    public GroupjieruResource() {
    }

    /** minimal constructor */
    public GroupjieruResource(String groupCode) {
        this.groupCode = groupCode;
    }

    /** full constructor */
    public GroupjieruResource(String groupCode, String groupName, String grouptype, String company, Date updatedDay, Date insertDay, String jierufs, String shifouzy, String guanglan, String dianlan) {
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.grouptype = grouptype;
        this.company = company;
        this.updatedDay = updatedDay;
        this.insertDay = insertDay;
        this.jierufs = jierufs;
        this.shifouzy = shifouzy;
        this.guanglan = guanglan;
        this.dianlan = dianlan;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupCode() {
        return this.groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGrouptype() {
        return this.grouptype;
    }

    public void setGrouptype(String grouptype) {
        this.grouptype = grouptype;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getUpdatedDay() {
        return this.updatedDay;
    }

    public void setUpdatedDay(Date updatedDay) {
        this.updatedDay = updatedDay;
    }

    public Date getInsertDay() {
        return this.insertDay;
    }

    public void setInsertDay(Date insertDay) {
        this.insertDay = insertDay;
    }

    public String getJierufs() {
        return this.jierufs;
    }

    public void setJierufs(String jierufs) {
        this.jierufs = jierufs;
    }

    public String getShifouzy() {
        return this.shifouzy;
    }

    public void setShifouzy(String shifouzy) {
        this.shifouzy = shifouzy;
    }

    public String getGuanglan() {
        return this.guanglan;
    }

    public void setGuanglan(String guanglan) {
        this.guanglan = guanglan;
    }

    public String getDianlan() {
        return this.dianlan;
    }

    public void setDianlan(String dianlan) {
        this.dianlan = dianlan;
    }
}
