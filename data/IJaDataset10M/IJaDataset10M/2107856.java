package com.lms.admin.orm;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by G.Vijayaraja
 * 
 * lms
 */
public class LeavePolicyOrm implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String id = null;

    private String desc = null;

    private int noOfDays;

    private String slabFlag = null;

    private String ratmId = null;

    private Date effectiveDate = null;

    private Date expiryDate = null;

    private String duration = null;

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(final String desc) {
        this.desc = desc;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(final String duration) {
        this.duration = duration;
    }

    public Date getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(final Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(final Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public int getNoOfDays() {
        return this.noOfDays;
    }

    public void setNoOfDays(final int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getRatmId() {
        return this.ratmId;
    }

    public void setRatmId(final String ratmId) {
        this.ratmId = ratmId;
    }

    public String getSlabFlag() {
        return this.slabFlag;
    }

    public void setSlabFlag(final String slabFlag) {
        this.slabFlag = slabFlag;
    }
}
