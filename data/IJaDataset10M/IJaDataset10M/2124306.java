package com.performance.model;

import java.io.Serializable;
import com.jxva.dao.annotation.Table;

/**
 * 
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2010-02-10 10:00:20 by Automatic Generate Toolkit
 */
@Table(name = "tbl_advertise", increment = "advertiseId", primaryKeys = { "advertiseId" })
public class Advertise implements Serializable {

    private static final long serialVersionUID = 1L;

    private java.lang.Integer advertiseId;

    private java.lang.Short adType;

    private java.lang.String picUrl;

    private java.lang.String title;

    private java.lang.String description;

    private java.lang.String linkUrl;

    private java.lang.Integer isCommend;

    private java.lang.Integer sequenceNum;

    private java.lang.Short isSystem;

    public java.lang.Integer getAdvertiseId() {
        return this.advertiseId;
    }

    public void setAdvertiseId(java.lang.Integer advertiseId) {
        this.advertiseId = advertiseId;
    }

    public java.lang.Short getAdType() {
        return this.adType;
    }

    public void setAdType(java.lang.Short adType) {
        this.adType = adType;
    }

    public java.lang.String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(java.lang.String picUrl) {
        this.picUrl = picUrl;
    }

    public java.lang.String getTitle() {
        return this.title;
    }

    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    public java.lang.String getDescription() {
        return this.description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getLinkUrl() {
        return this.linkUrl;
    }

    public void setLinkUrl(java.lang.String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public java.lang.Integer getIsCommend() {
        return this.isCommend;
    }

    public void setIsCommend(java.lang.Integer isCommend) {
        this.isCommend = isCommend;
    }

    public java.lang.Integer getSequenceNum() {
        return this.sequenceNum;
    }

    public void setSequenceNum(java.lang.Integer sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    public java.lang.Short getIsSystem() {
        return this.isSystem;
    }

    public void setIsSystem(java.lang.Short isSystem) {
        this.isSystem = isSystem;
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        sb.append("advertiseId=").append(advertiseId).append(',');
        sb.append("adType=").append(adType).append(',');
        sb.append("picUrl=").append(picUrl).append(',');
        sb.append("title=").append(title).append(',');
        sb.append("description=").append(description).append(',');
        sb.append("linkUrl=").append(linkUrl).append(',');
        sb.append("isCommend=").append(isCommend).append(',');
        sb.append("sequenceNum=").append(sequenceNum).append(',');
        sb.append("isSystem=").append(isSystem).append(" ]");
        return sb.toString();
    }
}
