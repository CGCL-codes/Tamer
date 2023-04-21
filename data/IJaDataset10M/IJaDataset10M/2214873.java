package com.zhiyun.admin.vo;

import java.io.Serializable;

public class EbShip implements Serializable {

    private static final long serialVersionUID = 3713810764224610265L;

    private Long id;

    private String name;

    private Float charge;

    private String desc;

    private Integer freeTerm;

    private Float freePrice;

    private Integer Status;

    public EbShip() {
    }

    public EbShip(String name, Float charge, String desc, Integer freeTerm, Float freePrice, Integer status) {
        super();
        this.name = name;
        this.charge = charge;
        this.desc = desc;
        this.freeTerm = freeTerm;
        this.freePrice = freePrice;
        Status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getCharge() {
        return charge;
    }

    public void setCharge(Float charge) {
        this.charge = charge;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getFreeTerm() {
        return freeTerm;
    }

    public void setFreeTerm(Integer freeTerm) {
        this.freeTerm = freeTerm;
    }

    public Float getFreePrice() {
        return freePrice;
    }

    public void setFreePrice(Float freePrice) {
        this.freePrice = freePrice;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }
}
