package com.creawor.hz_market.resource.netbusiness_resource;

import java.util.Date;

/**
 * NetbusinessResource entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class NetbusinessResource implements java.io.Serializable {

    private Integer id;

    private String county;

    private String tonghUsers;

    private String huawuliangDay;

    private String huawuliangOne;

    private String shujuliuliang;

    private String duanxinliuliang;

    private Date insertDay;

    private Date updatedDay;

    /** default constructor */
    public NetbusinessResource() {
    }

    /** full constructor */
    public NetbusinessResource(String county, String tonghUsers, String huawuliangDay, String huawuliangOne, String duanxinliuliang, Date insertDay, Date updatedDay) {
        this.county = county;
        this.tonghUsers = tonghUsers;
        this.huawuliangDay = huawuliangDay;
        this.huawuliangOne = huawuliangOne;
        this.duanxinliuliang = duanxinliuliang;
        this.insertDay = insertDay;
        this.updatedDay = updatedDay;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCounty() {
        return this.county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTonghUsers() {
        return this.tonghUsers;
    }

    public void setTonghUsers(String tonghUsers) {
        this.tonghUsers = tonghUsers;
    }

    public String getHuawuliangDay() {
        return this.huawuliangDay;
    }

    public void setHuawuliangDay(String huawuliangDay) {
        this.huawuliangDay = huawuliangDay;
    }

    public String getHuawuliangOne() {
        return this.huawuliangOne;
    }

    public void setHuawuliangOne(String huawuliangOne) {
        this.huawuliangOne = huawuliangOne;
    }

    public String getDuanxinliuliang() {
        return this.duanxinliuliang;
    }

    public void setDuanxinliuliang(String duanxinliuliang) {
        this.duanxinliuliang = duanxinliuliang;
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

    public String getShujuliuliang() {
        return shujuliuliang;
    }

    public void setShujuliuliang(String shujuliuliang) {
        this.shujuliuliang = shujuliuliang;
    }
}
