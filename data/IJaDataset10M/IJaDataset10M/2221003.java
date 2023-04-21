package com.techstar.dmis.entity;

import java.io.Serializable;

/**
 * Domain classe for 自投规程
 * This classe is based on ValueObject Pattern
 * @author 
 * @date
 */
public class BhAsregulate implements Serializable {

    public BhAsregulate() {
    }

    private String fstationname;

    private java.sql.Date fwritedate;

    private String funitname;

    private String dispatchcenteropinion;

    private String runmodedeptopinion;

    private String protectdeptopinion;

    private String protectresponsor;

    private String excutetime;

    private String ondutyname;

    private byte[] asregulateoriginal;

    private byte[] runmodedigram;

    private String remarks;

    private String asregulateno;

    private String fiscancel;

    private String sys_fille;

    private String sys_filldept;

    private java.sql.Timestamp sys_filltime;

    private int sys_isvalid;

    private String sys_dataowner;

    private String asregulateid;

    private int version;

    private java.util.Collection fbhasregulatedetail3;

    private com.techstar.dmis.entity.StdStation zbhasregulate2;

    /**
     * getters and setters
     */
    public void setFstationname(String fstationname) {
        this.fstationname = fstationname;
    }

    public String getFstationname() {
        return fstationname;
    }

    public void setFwritedate(java.sql.Date fwritedate) {
        this.fwritedate = fwritedate;
    }

    public java.sql.Date getFwritedate() {
        return fwritedate;
    }

    public void setFunitname(String funitname) {
        this.funitname = funitname;
    }

    public String getFunitname() {
        return funitname;
    }

    public void setDispatchcenteropinion(String dispatchcenteropinion) {
        this.dispatchcenteropinion = dispatchcenteropinion;
    }

    public String getDispatchcenteropinion() {
        return dispatchcenteropinion;
    }

    public void setRunmodedeptopinion(String runmodedeptopinion) {
        this.runmodedeptopinion = runmodedeptopinion;
    }

    public String getRunmodedeptopinion() {
        return runmodedeptopinion;
    }

    public void setProtectdeptopinion(String protectdeptopinion) {
        this.protectdeptopinion = protectdeptopinion;
    }

    public String getProtectdeptopinion() {
        return protectdeptopinion;
    }

    public void setProtectresponsor(String protectresponsor) {
        this.protectresponsor = protectresponsor;
    }

    public String getProtectresponsor() {
        return protectresponsor;
    }

    public void setExcutetime(String excutetime) {
        this.excutetime = excutetime;
    }

    public String getExcutetime() {
        return excutetime;
    }

    public void setOndutyname(String ondutyname) {
        this.ondutyname = ondutyname;
    }

    public String getOndutyname() {
        return ondutyname;
    }

    public void setAsregulateoriginal(byte[] asregulateoriginal) {
        this.asregulateoriginal = asregulateoriginal;
    }

    public byte[] getAsregulateoriginal() {
        return asregulateoriginal;
    }

    public void setRunmodedigram(byte[] runmodedigram) {
        this.runmodedigram = runmodedigram;
    }

    public byte[] getRunmodedigram() {
        return runmodedigram;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setAsregulateno(String asregulateno) {
        this.asregulateno = asregulateno;
    }

    public String getAsregulateno() {
        return asregulateno;
    }

    public void setFiscancel(String fiscancel) {
        this.fiscancel = fiscancel;
    }

    public String getFiscancel() {
        return fiscancel;
    }

    public void setSys_fille(String sys_fille) {
        this.sys_fille = sys_fille;
    }

    public String getSys_fille() {
        return sys_fille;
    }

    public void setSys_filldept(String sys_filldept) {
        this.sys_filldept = sys_filldept;
    }

    public String getSys_filldept() {
        return sys_filldept;
    }

    public void setSys_filltime(java.sql.Timestamp sys_filltime) {
        this.sys_filltime = sys_filltime;
    }

    public java.sql.Timestamp getSys_filltime() {
        return sys_filltime;
    }

    public void setSys_isvalid(int sys_isvalid) {
        this.sys_isvalid = sys_isvalid;
    }

    public int getSys_isvalid() {
        return sys_isvalid;
    }

    public void setSys_dataowner(String sys_dataowner) {
        this.sys_dataowner = sys_dataowner;
    }

    public String getSys_dataowner() {
        return sys_dataowner;
    }

    public void setAsregulateid(String asregulateid) {
        this.asregulateid = asregulateid;
    }

    public String getAsregulateid() {
        return asregulateid;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setFbhasregulatedetail3(java.util.Collection fbhasregulatedetail3) {
        this.fbhasregulatedetail3 = fbhasregulatedetail3;
    }

    public java.util.Collection getFbhasregulatedetail3() {
        return fbhasregulatedetail3;
    }

    public void setZbhasregulate2(com.techstar.dmis.entity.StdStation zbhasregulate2) {
        this.zbhasregulate2 = zbhasregulate2;
    }

    public com.techstar.dmis.entity.StdStation getZbhasregulate2() {
        return zbhasregulate2;
    }
}
