package com.techstar.dmis.dto.transfer;

import java.io.Serializable;

/**
 * Domain classe for 继电保护装置台帐
 * This classe is based on ValueObject Pattern
 */
public class TransParaRelayProtecDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public TransParaRelayProtecDto() {
    }

    private String frununit;

    private int fstationhvol;

    private String fvoltage;

    private String fdispatchno;

    private String fprotectedequip;

    private String fdispatchname;

    private String fdispatchauthority;

    private String fequipclass;

    private String fdevicetype;

    private String faccesstype;

    private String fchannelmodel;

    private java.sql.Date fallverifydata;

    private java.sql.Date fpartverifydata;

    private java.sql.Date fnextverifydate;

    private String fnextverifytype;

    private String fisrunning;

    private String fsoftwareversion;

    private String fcheckcode;

    private String fmoniver;

    private String fmonivericode;

    private java.sql.Date fmonitime;

    private String fcpuonever;

    private String fcpuonevericode;

    private java.sql.Date fcpuonetime;

    private String fcputwover;

    private String fcputwovericode;

    private java.sql.Date fcputwotime;

    private String fcputhreever;

    private String fcputhreevericode;

    private java.sql.Date fcputhreetime;

    private String fcpufourver;

    private String fcpufourvericode;

    private java.sql.Date fcpufourtime;

    private String fmaterialcodify;

    private java.sql.Date fprefixveritime;

    private String fprefixveritype;

    private String fprefixveriunit;

    private int fprefixveriper;

    private String fremark;

    private String feqpid;

    private int version;

    private String trans_zpararelayprotec2;

    /**
     * getters and setters
     */
    public void setFrununit(String frununit) {
        this.frununit = frununit;
    }

    public String getFrununit() {
        return frununit;
    }

    public void setFstationhvol(int fstationhvol) {
        this.fstationhvol = fstationhvol;
    }

    public int getFstationhvol() {
        return fstationhvol;
    }

    public void setFvoltage(String fvoltage) {
        this.fvoltage = fvoltage;
    }

    public String getFvoltage() {
        return fvoltage;
    }

    public void setFdispatchno(String fdispatchno) {
        this.fdispatchno = fdispatchno;
    }

    public String getFdispatchno() {
        return fdispatchno;
    }

    public void setFprotectedequip(String fprotectedequip) {
        this.fprotectedequip = fprotectedequip;
    }

    public String getFprotectedequip() {
        return fprotectedequip;
    }

    public void setFdispatchname(String fdispatchname) {
        this.fdispatchname = fdispatchname;
    }

    public String getFdispatchname() {
        return fdispatchname;
    }

    public void setFdispatchauthority(String fdispatchauthority) {
        this.fdispatchauthority = fdispatchauthority;
    }

    public String getFdispatchauthority() {
        return fdispatchauthority;
    }

    public void setFequipclass(String fequipclass) {
        this.fequipclass = fequipclass;
    }

    public String getFequipclass() {
        return fequipclass;
    }

    public void setFdevicetype(String fdevicetype) {
        this.fdevicetype = fdevicetype;
    }

    public String getFdevicetype() {
        return fdevicetype;
    }

    public void setFaccesstype(String faccesstype) {
        this.faccesstype = faccesstype;
    }

    public String getFaccesstype() {
        return faccesstype;
    }

    public void setFchannelmodel(String fchannelmodel) {
        this.fchannelmodel = fchannelmodel;
    }

    public String getFchannelmodel() {
        return fchannelmodel;
    }

    public void setFallverifydata(java.sql.Date fallverifydata) {
        this.fallverifydata = fallverifydata;
    }

    public java.sql.Date getFallverifydata() {
        return fallverifydata;
    }

    public void setFpartverifydata(java.sql.Date fpartverifydata) {
        this.fpartverifydata = fpartverifydata;
    }

    public java.sql.Date getFpartverifydata() {
        return fpartverifydata;
    }

    public void setFnextverifydate(java.sql.Date fnextverifydate) {
        this.fnextverifydate = fnextverifydate;
    }

    public java.sql.Date getFnextverifydate() {
        return fnextverifydate;
    }

    public void setFnextverifytype(String fnextverifytype) {
        this.fnextverifytype = fnextverifytype;
    }

    public String getFnextverifytype() {
        return fnextverifytype;
    }

    public void setFisrunning(String fisrunning) {
        this.fisrunning = fisrunning;
    }

    public String getFisrunning() {
        return fisrunning;
    }

    public void setFsoftwareversion(String fsoftwareversion) {
        this.fsoftwareversion = fsoftwareversion;
    }

    public String getFsoftwareversion() {
        return fsoftwareversion;
    }

    public void setFcheckcode(String fcheckcode) {
        this.fcheckcode = fcheckcode;
    }

    public String getFcheckcode() {
        return fcheckcode;
    }

    public void setFmoniver(String fmoniver) {
        this.fmoniver = fmoniver;
    }

    public String getFmoniver() {
        return fmoniver;
    }

    public void setFmonivericode(String fmonivericode) {
        this.fmonivericode = fmonivericode;
    }

    public String getFmonivericode() {
        return fmonivericode;
    }

    public void setFmonitime(java.sql.Date fmonitime) {
        this.fmonitime = fmonitime;
    }

    public java.sql.Date getFmonitime() {
        return fmonitime;
    }

    public void setFcpuonever(String fcpuonever) {
        this.fcpuonever = fcpuonever;
    }

    public String getFcpuonever() {
        return fcpuonever;
    }

    public void setFcpuonevericode(String fcpuonevericode) {
        this.fcpuonevericode = fcpuonevericode;
    }

    public String getFcpuonevericode() {
        return fcpuonevericode;
    }

    public void setFcpuonetime(java.sql.Date fcpuonetime) {
        this.fcpuonetime = fcpuonetime;
    }

    public java.sql.Date getFcpuonetime() {
        return fcpuonetime;
    }

    public void setFcputwover(String fcputwover) {
        this.fcputwover = fcputwover;
    }

    public String getFcputwover() {
        return fcputwover;
    }

    public void setFcputwovericode(String fcputwovericode) {
        this.fcputwovericode = fcputwovericode;
    }

    public String getFcputwovericode() {
        return fcputwovericode;
    }

    public void setFcputwotime(java.sql.Date fcputwotime) {
        this.fcputwotime = fcputwotime;
    }

    public java.sql.Date getFcputwotime() {
        return fcputwotime;
    }

    public void setFcputhreever(String fcputhreever) {
        this.fcputhreever = fcputhreever;
    }

    public String getFcputhreever() {
        return fcputhreever;
    }

    public void setFcputhreevericode(String fcputhreevericode) {
        this.fcputhreevericode = fcputhreevericode;
    }

    public String getFcputhreevericode() {
        return fcputhreevericode;
    }

    public void setFcputhreetime(java.sql.Date fcputhreetime) {
        this.fcputhreetime = fcputhreetime;
    }

    public java.sql.Date getFcputhreetime() {
        return fcputhreetime;
    }

    public void setFcpufourver(String fcpufourver) {
        this.fcpufourver = fcpufourver;
    }

    public String getFcpufourver() {
        return fcpufourver;
    }

    public void setFcpufourvericode(String fcpufourvericode) {
        this.fcpufourvericode = fcpufourvericode;
    }

    public String getFcpufourvericode() {
        return fcpufourvericode;
    }

    public void setFcpufourtime(java.sql.Date fcpufourtime) {
        this.fcpufourtime = fcpufourtime;
    }

    public java.sql.Date getFcpufourtime() {
        return fcpufourtime;
    }

    public void setFmaterialcodify(String fmaterialcodify) {
        this.fmaterialcodify = fmaterialcodify;
    }

    public String getFmaterialcodify() {
        return fmaterialcodify;
    }

    public void setFprefixveritime(java.sql.Date fprefixveritime) {
        this.fprefixveritime = fprefixveritime;
    }

    public java.sql.Date getFprefixveritime() {
        return fprefixveritime;
    }

    public void setFprefixveritype(String fprefixveritype) {
        this.fprefixveritype = fprefixveritype;
    }

    public String getFprefixveritype() {
        return fprefixveritype;
    }

    public void setFprefixveriunit(String fprefixveriunit) {
        this.fprefixveriunit = fprefixveriunit;
    }

    public String getFprefixveriunit() {
        return fprefixveriunit;
    }

    public void setFprefixveriper(int fprefixveriper) {
        this.fprefixveriper = fprefixveriper;
    }

    public int getFprefixveriper() {
        return fprefixveriper;
    }

    public void setFremark(String fremark) {
        this.fremark = fremark;
    }

    public String getFremark() {
        return fremark;
    }

    public void setFeqpid(String feqpid) {
        this.feqpid = feqpid;
    }

    public String getFeqpid() {
        return feqpid;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setTrans_zpararelayprotec2(String trans_zpararelayprotec2) {
        this.trans_zpararelayprotec2 = trans_zpararelayprotec2;
    }

    public String getTrans_zpararelayprotec2() {
        return trans_zpararelayprotec2;
    }
}
