package com.zhiyun.admin.vo;

import java.util.Date;

public class EbItem implements java.io.Serializable {

    private static final long serialVersionUID = -11551275362469162L;

    private String id;

    private EbShop ebShop;

    private EbCatagory catagory;

    private String name;

    private String serial;

    private String spec;

    private EbBrand brand;

    private Float weight;

    private String desc;

    private Float price;

    private Float priceDisc;

    private Integer points;

    private Long enterCount;

    private Long transactionCount;

    private Long exitCount;

    private Date createTime;

    private Date updateTime;

    private Integer delFlag;

    /** default constructor */
    public EbItem() {
    }

    /** full constructor */
    public EbItem(EbShop ebShop, EbCatagory catagory, String name, String serial, String spec, EbBrand brand, Float weight, String desc, Float price, Float priceDisc, Integer points, Long enterCount, Long transactionCount, Long exitCount, Date createTime, Date updateTime, Integer delFlag) {
        super();
        this.ebShop = ebShop;
        this.catagory = catagory;
        this.name = name;
        this.serial = serial;
        this.spec = spec;
        this.brand = brand;
        this.weight = weight;
        this.desc = desc;
        this.price = price;
        this.priceDisc = priceDisc;
        this.points = points;
        this.enterCount = enterCount;
        this.transactionCount = transactionCount;
        this.exitCount = exitCount;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.delFlag = delFlag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EbShop getEbShop() {
        return ebShop;
    }

    public void setEbShop(EbShop ebShop) {
        this.ebShop = ebShop;
    }

    public EbCatagory getCatagory() {
        return catagory;
    }

    public void setCatagory(EbCatagory catagory) {
        this.catagory = catagory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public EbBrand getBrand() {
        return brand;
    }

    public void setBrand(EbBrand brand) {
        this.brand = brand;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getPriceDisc() {
        return priceDisc;
    }

    public void setPriceDisc(Float priceDisc) {
        this.priceDisc = priceDisc;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Long getEnterCount() {
        return enterCount;
    }

    public void setEnterCount(Long enterCount) {
        this.enterCount = enterCount;
    }

    public Long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Long transactionCount) {
        this.transactionCount = transactionCount;
    }

    public Long getExitCount() {
        return exitCount;
    }

    public void setExitCount(Long exitCount) {
        this.exitCount = exitCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
