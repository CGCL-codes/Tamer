package com.zhiyun.estore.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@SuppressWarnings("serial")
@Entity
@Table(name = "eb_city")
public class EbCity implements Serializable {

    private Long id;

    private EbArea area;

    private String city_name;

    private String desc;

    private Integer status;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "area_id")
    public EbArea getArea() {
        return area;
    }

    public void setArea(EbArea area) {
        this.area = area;
    }

    @Column(name = "city_name", nullable = true, length = 50)
    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
