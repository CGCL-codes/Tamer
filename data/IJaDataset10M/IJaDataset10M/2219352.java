package com.techstar.dmis.dto;

import java.io.Serializable;
import com.techstar.framework.service.dto.DictionaryBaseDto;

/**
 * Domain classe for 低频减载定值通知明细
 * This classe is based on ValueObject Pattern
 * @author 
 * @date
 */
public class FsLfeqpfixinformdetailDto implements Serializable {

    public FsLfeqpfixinformdetailDto() {
    }

    private String fswitchno;

    private String flinename;

    private String fbusno;

    private int ffrequency;

    private int factiontime;

    private java.sql.Date fmodifydate;

    private String ffixtype;

    private String fremark;

    private String freceiver;

    private java.sql.Date freceivetime;

    private String fanswer;

    private java.sql.Date fanswertime;

    private String fixinformdetailid;

    private int version;

    private com.techstar.dmis.dto.FsLfparalistDto zfslfeqpfixinformdetail5;

    private com.techstar.dmis.dto.FsLfeqpfixinformDto zfslfeqpfixinformdetail1;

    private com.techstar.dmis.dto.StdLfroundDto zfslfeqpfixinformdetail2;

    /**
     * getters and setters
     */
    public void setFswitchno(String fswitchno) {
        this.fswitchno = fswitchno;
    }

    public String getFswitchno() {
        return fswitchno;
    }

    public void setFlinename(String flinename) {
        this.flinename = flinename;
    }

    public String getFlinename() {
        return flinename;
    }

    public void setFbusno(String fbusno) {
        this.fbusno = fbusno;
    }

    public String getFbusno() {
        return fbusno;
    }

    public void setFfrequency(int ffrequency) {
        this.ffrequency = ffrequency;
    }

    public int getFfrequency() {
        return ffrequency;
    }

    public void setFactiontime(int factiontime) {
        this.factiontime = factiontime;
    }

    public int getFactiontime() {
        return factiontime;
    }

    public void setFmodifydate(java.sql.Date fmodifydate) {
        this.fmodifydate = fmodifydate;
    }

    public java.sql.Date getFmodifydate() {
        return fmodifydate;
    }

    public void setFfixtype(String ffixtype) {
        this.ffixtype = ffixtype;
    }

    public String getFfixtype() {
        return ffixtype;
    }

    public void setFremark(String fremark) {
        this.fremark = fremark;
    }

    public String getFremark() {
        return fremark;
    }

    public void setFreceiver(String freceiver) {
        this.freceiver = freceiver;
    }

    public String getFreceiver() {
        return freceiver;
    }

    public void setFreceivetime(java.sql.Date freceivetime) {
        this.freceivetime = freceivetime;
    }

    public java.sql.Date getFreceivetime() {
        return freceivetime;
    }

    public void setFanswer(String fanswer) {
        this.fanswer = fanswer;
    }

    public String getFanswer() {
        return fanswer;
    }

    public void setFanswertime(java.sql.Date fanswertime) {
        this.fanswertime = fanswertime;
    }

    public java.sql.Date getFanswertime() {
        return fanswertime;
    }

    public void setFixinformdetailid(String fixinformdetailid) {
        this.fixinformdetailid = fixinformdetailid;
    }

    public String getFixinformdetailid() {
        return fixinformdetailid;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setZfslfeqpfixinformdetail5(com.techstar.dmis.dto.FsLfparalistDto zfslfeqpfixinformdetail5) {
        this.zfslfeqpfixinformdetail5 = zfslfeqpfixinformdetail5;
    }

    public com.techstar.dmis.dto.FsLfparalistDto getZfslfeqpfixinformdetail5() {
        return zfslfeqpfixinformdetail5;
    }

    public void setZfslfeqpfixinformdetail1(com.techstar.dmis.dto.FsLfeqpfixinformDto zfslfeqpfixinformdetail1) {
        this.zfslfeqpfixinformdetail1 = zfslfeqpfixinformdetail1;
    }

    public com.techstar.dmis.dto.FsLfeqpfixinformDto getZfslfeqpfixinformdetail1() {
        return zfslfeqpfixinformdetail1;
    }

    public void setZfslfeqpfixinformdetail2(com.techstar.dmis.dto.StdLfroundDto zfslfeqpfixinformdetail2) {
        this.zfslfeqpfixinformdetail2 = zfslfeqpfixinformdetail2;
    }

    public com.techstar.dmis.dto.StdLfroundDto getZfslfeqpfixinformdetail2() {
        return zfslfeqpfixinformdetail2;
    }
}
