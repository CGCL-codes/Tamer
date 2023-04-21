package com.openthinks.woms.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;

/**
 * Style of products
 * 
 * @author Zhang Junlong
 * 
 */
@Entity
public class Style {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(length = 100)
    private String name;

    @OrderBy
    @Column(length = 20)
    private String code;

    @ManyToOne
    private Style parentStyle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Style getParentStyle() {
        return parentStyle;
    }

    public void setParentStyle(Style parentStyle) {
        this.parentStyle = parentStyle;
    }
}
