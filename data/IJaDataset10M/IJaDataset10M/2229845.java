package com.xtreme.cis.model;

import java.util.Date;

/**
 * FreezeBilling generated by hbm2java
 */
public class FreezeBilling implements java.io.Serializable {

    private int id;

    private Department department;

    private Date effDt;

    private char active;

    private Date createdDt;

    private String updatedBy;

    public FreezeBilling() {
    }

    public FreezeBilling(int id, Department department, Date effDt, char active, Date createdDt, String updatedBy) {
        this.id = id;
        this.department = department;
        this.effDt = effDt;
        this.active = active;
        this.createdDt = createdDt;
        this.updatedBy = updatedBy;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Date getEffDt() {
        return this.effDt;
    }

    public void setEffDt(Date effDt) {
        this.effDt = effDt;
    }

    public char getActive() {
        return this.active;
    }

    public void setActive(char active) {
        this.active = active;
    }

    public Date getCreatedDt() {
        return this.createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
