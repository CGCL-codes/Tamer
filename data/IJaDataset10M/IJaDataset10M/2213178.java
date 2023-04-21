package com.lms.admin.orm;

import java.io.Serializable;

/**
 * Created by G.Vijayaraja lms
 */
public class SlabOrm implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String id = null;

    private String rateId = null;

    private String slabOneOpr = null;

    private String slabOne = null;

    private String slabOneTwo = null;

    private String slabTwoOpr = null;

    private String slabTwo = null;

    private String slabTwoTwo = null;

    private String addVal = null;

    private String addUnit = null;

    public String getAddUnit() {
        return this.addUnit;
    }

    public void setAddUnit(final String addUnit) {
        this.addUnit = addUnit;
    }

    public String getAddVal() {
        return this.addVal;
    }

    public void setAddVal(final String addVal) {
        this.addVal = addVal;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getRateId() {
        return this.rateId;
    }

    public void setRateId(final String rateId) {
        this.rateId = rateId;
    }

    public String getSlabOne() {
        return this.slabOne;
    }

    public void setSlabOne(final String slabOne) {
        this.slabOne = slabOne;
    }

    public String getSlabOneOpr() {
        return this.slabOneOpr;
    }

    public void setSlabOneOpr(final String slabOneOpr) {
        this.slabOneOpr = slabOneOpr;
    }

    public String getSlabOneTwo() {
        return this.slabOneTwo;
    }

    public void setSlabOneTwo(final String slabOneTwo) {
        this.slabOneTwo = slabOneTwo;
    }

    public String getSlabTwo() {
        return this.slabTwo;
    }

    public void setSlabTwo(final String slabTwo) {
        this.slabTwo = slabTwo;
    }

    public String getSlabTwoOpr() {
        return this.slabTwoOpr;
    }

    public void setSlabTwoOpr(final String slabTwoOpr) {
        this.slabTwoOpr = slabTwoOpr;
    }

    public String getSlabTwoTwo() {
        return this.slabTwoTwo;
    }

    public void setSlabTwoTwo(final String slabTwoTwo) {
        this.slabTwoTwo = slabTwoTwo;
    }
}
