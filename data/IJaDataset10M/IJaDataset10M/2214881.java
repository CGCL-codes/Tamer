package com.performance.model;

import java.io.Serializable;
import com.jxva.dao.annotation.Table;

/**
 * 
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2010-02-10 10:00:23 by Automatic Generate Toolkit
 */
@Table(name = "tbl_sms", increment = "smsid", primaryKeys = { "smsid" })
public class Sms implements Serializable {

    private static final long serialVersionUID = 1L;

    private java.lang.Integer smsid;

    private java.lang.String subject;

    private java.lang.String summary;

    private java.lang.String customize;

    private java.lang.String expenses;

    private java.lang.String region;

    private java.lang.String imgurl;

    public java.lang.Integer getSmsid() {
        return this.smsid;
    }

    public void setSmsid(java.lang.Integer smsid) {
        this.smsid = smsid;
    }

    public java.lang.String getSubject() {
        return this.subject;
    }

    public void setSubject(java.lang.String subject) {
        this.subject = subject;
    }

    public java.lang.String getSummary() {
        return this.summary;
    }

    public void setSummary(java.lang.String summary) {
        this.summary = summary;
    }

    public java.lang.String getCustomize() {
        return this.customize;
    }

    public void setCustomize(java.lang.String customize) {
        this.customize = customize;
    }

    public java.lang.String getExpenses() {
        return this.expenses;
    }

    public void setExpenses(java.lang.String expenses) {
        this.expenses = expenses;
    }

    public java.lang.String getRegion() {
        return this.region;
    }

    public void setRegion(java.lang.String region) {
        this.region = region;
    }

    public java.lang.String getImgurl() {
        return this.imgurl;
    }

    public void setImgurl(java.lang.String imgurl) {
        this.imgurl = imgurl;
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
        sb.append("smsid=").append(smsid).append(',');
        sb.append("subject=").append(subject).append(',');
        sb.append("summary=").append(summary).append(',');
        sb.append("customize=").append(customize).append(',');
        sb.append("expenses=").append(expenses).append(',');
        sb.append("region=").append(region).append(',');
        sb.append("imgurl=").append(imgurl).append(" ]");
        return sb.toString();
    }
}
