package com.techstar.dmis.dto;

import java.io.Serializable;
import com.techstar.framework.service.dto.DictionaryBaseDto;

/**
 * Domain classe for 水电运行(计划放水)记录
 * This classe is based on ValueObject Pattern
 * @author 
 * @date
 */
public class DdWaterproofplanDto implements Serializable {

    public DdWaterproofplanDto() {
    }

    private String fapplyuser;

    private String fapplyunit;

    private java.sql.Timestamp fapplycnt;

    private int fwaterquantity;

    private String sys_fille;

    private String sys_filldept;

    private java.sql.Timestamp sys_filltime;

    private int sys_isvalid;

    private String sys_dataowner;

    private String fwriteinlog;

    private String fwriteinwglog;

    private String fid;

    private int version;

    private com.techstar.dmis.dto.DdClassondutylogDto zddwaterproofplan2;

    /**
     * getters and setters
     */
    public void setFapplyuser(String fapplyuser) {
        this.fapplyuser = fapplyuser;
    }

    public String getFapplyuser() {
        return fapplyuser;
    }

    public void setFapplyunit(String fapplyunit) {
        this.fapplyunit = fapplyunit;
    }

    public String getFapplyunit() {
        return fapplyunit;
    }

    public void setFapplycnt(java.sql.Timestamp fapplycnt) {
        this.fapplycnt = fapplycnt;
    }

    public java.sql.Timestamp getFapplycnt() {
        return fapplycnt;
    }

    public void setFwaterquantity(int fwaterquantity) {
        this.fwaterquantity = fwaterquantity;
    }

    public int getFwaterquantity() {
        return fwaterquantity;
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

    public void setFwriteinlog(String fwriteinlog) {
        this.fwriteinlog = fwriteinlog;
    }

    public String getFwriteinlog() {
        return fwriteinlog;
    }

    public void setFwriteinwglog(String fwriteinwglog) {
        this.fwriteinwglog = fwriteinwglog;
    }

    public String getFwriteinwglog() {
        return fwriteinwglog;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFid() {
        return fid;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setZddwaterproofplan2(com.techstar.dmis.dto.DdClassondutylogDto zddwaterproofplan2) {
        this.zddwaterproofplan2 = zddwaterproofplan2;
    }

    public com.techstar.dmis.dto.DdClassondutylogDto getZddwaterproofplan2() {
        return zddwaterproofplan2;
    }
}
