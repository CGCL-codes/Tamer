package com.babelstudio.cpasss.hibernate;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Product entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "product", catalog = "zp41e1_db")
public class Product implements java.io.Serializable {

    private Integer id;

    private String name;

    private Integer type;

    private Integer brand;

    private String esn;

    private String sn;

    private String model;

    private String memo;

    private Float cost;

    private Float sale;

    private Timestamp createtime;

    private String pinyin;

    /** default constructor */
    public Product() {
    }

    /** full constructor */
    public Product(String name, Integer type, Integer brand, String esn, String sn, String model, String memo, Float cost, Float sale, Timestamp createtime, String pinyin) {
        this.name = name;
        this.type = type;
        this.brand = brand;
        this.esn = esn;
        this.sn = sn;
        this.model = model;
        this.memo = memo;
        this.cost = cost;
        this.sale = sale;
        this.createtime = createtime;
        this.pinyin = pinyin;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name", length = 128)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "type")
    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "brand")
    public Integer getBrand() {
        return this.brand;
    }

    public void setBrand(Integer brand) {
        this.brand = brand;
    }

    @Column(name = "esn", length = 128)
    public String getEsn() {
        return this.esn;
    }

    public void setEsn(String esn) {
        this.esn = esn;
    }

    @Column(name = "sn", length = 128)
    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @Column(name = "model", length = 128)
    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "memo", length = 65535)
    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Column(name = "cost", precision = 12, scale = 0)
    public Float getCost() {
        return this.cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    @Column(name = "sale", precision = 12, scale = 0)
    public Float getSale() {
        return this.sale;
    }

    public void setSale(Float sale) {
        this.sale = sale;
    }

    @Column(name = "createtime", length = 19)
    public Timestamp getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    @Column(name = "pinyin", length = 128)
    public String getPinyin() {
        return this.pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
