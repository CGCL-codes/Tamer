package com.techstar.dmis.dto.transfer;

import java.io.Serializable;

/**
 * Domain classe for 自动化设备缺陷
 * This classe is based on ValueObject Pattern
 */
public class TransZdhFaultlistDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public TransZdhFaultlistDto() {
    }

    private String fsystemname;

    private String funitname;

    private String fsysclass;

    private String fequipclass;

    private String ffaulttypeid2;

    private String ffaultcontent;

    private int fcontinuetime;

    private String freason;

    private String fdiscover;

    private java.sql.Date foccurredtime;

    private java.sql.Date frestoretime;

    private String frepairunit;

    private String feqpfaultpart;

    private String fstaticreason;

    private String fremarks;

    private String rfiller;

    private java.sql.Date rfilltime;

    private String feqpid;

    private String fstatus;

    private String stationno;

    private String ffaulttypeid1;

    private String sys_fille;

    private String sys_filldept;

    private java.sql.Timestamp sys_filltime;

    private int sys_isvalid;

    private String sys_dataowner;

    private String ffaultgrade;

    private String ffaulttypename1;

    private String ffaulttypename2;

    private String ffaultno;

    private int version;

    private String trans_zzdhfaultlist7;

    /**
     * getters and setters
     */
    public void setFsystemname(String fsystemname) {
        this.fsystemname = fsystemname;
    }

    public String getFsystemname() {
        return fsystemname;
    }

    public void setFunitname(String funitname) {
        this.funitname = funitname;
    }

    public String getFunitname() {
        return funitname;
    }

    public void setFsysclass(String fsysclass) {
        this.fsysclass = fsysclass;
    }

    public String getFsysclass() {
        return fsysclass;
    }

    public void setFequipclass(String fequipclass) {
        this.fequipclass = fequipclass;
    }

    public String getFequipclass() {
        return fequipclass;
    }

    public void setFfaulttypeid2(String ffaulttypeid2) {
        this.ffaulttypeid2 = ffaulttypeid2;
    }

    public String getFfaulttypeid2() {
        return ffaulttypeid2;
    }

    public void setFfaultcontent(String ffaultcontent) {
        this.ffaultcontent = ffaultcontent;
    }

    public String getFfaultcontent() {
        return ffaultcontent;
    }

    public void setFcontinuetime(int fcontinuetime) {
        this.fcontinuetime = fcontinuetime;
    }

    public int getFcontinuetime() {
        return fcontinuetime;
    }

    public void setFreason(String freason) {
        this.freason = freason;
    }

    public String getFreason() {
        return freason;
    }

    public void setFdiscover(String fdiscover) {
        this.fdiscover = fdiscover;
    }

    public String getFdiscover() {
        return fdiscover;
    }

    public void setFoccurredtime(java.sql.Date foccurredtime) {
        this.foccurredtime = foccurredtime;
    }

    public java.sql.Date getFoccurredtime() {
        return foccurredtime;
    }

    public void setFrestoretime(java.sql.Date frestoretime) {
        this.frestoretime = frestoretime;
    }

    public java.sql.Date getFrestoretime() {
        return frestoretime;
    }

    public void setFrepairunit(String frepairunit) {
        this.frepairunit = frepairunit;
    }

    public String getFrepairunit() {
        return frepairunit;
    }

    public void setFeqpfaultpart(String feqpfaultpart) {
        this.feqpfaultpart = feqpfaultpart;
    }

    public String getFeqpfaultpart() {
        return feqpfaultpart;
    }

    public void setFstaticreason(String fstaticreason) {
        this.fstaticreason = fstaticreason;
    }

    public String getFstaticreason() {
        return fstaticreason;
    }

    public void setFremarks(String fremarks) {
        this.fremarks = fremarks;
    }

    public String getFremarks() {
        return fremarks;
    }

    public void setRfiller(String rfiller) {
        this.rfiller = rfiller;
    }

    public String getRfiller() {
        return rfiller;
    }

    public void setRfilltime(java.sql.Date rfilltime) {
        this.rfilltime = rfilltime;
    }

    public java.sql.Date getRfilltime() {
        return rfilltime;
    }

    public void setFeqpid(String feqpid) {
        this.feqpid = feqpid;
    }

    public String getFeqpid() {
        return feqpid;
    }

    public void setFstatus(String fstatus) {
        this.fstatus = fstatus;
    }

    public String getFstatus() {
        return fstatus;
    }

    public void setStationno(String stationno) {
        this.stationno = stationno;
    }

    public String getStationno() {
        return stationno;
    }

    public void setFfaulttypeid1(String ffaulttypeid1) {
        this.ffaulttypeid1 = ffaulttypeid1;
    }

    public String getFfaulttypeid1() {
        return ffaulttypeid1;
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

    public void setFfaultgrade(String ffaultgrade) {
        this.ffaultgrade = ffaultgrade;
    }

    public String getFfaultgrade() {
        return ffaultgrade;
    }

    public void setFfaulttypename1(String ffaulttypename1) {
        this.ffaulttypename1 = ffaulttypename1;
    }

    public String getFfaulttypename1() {
        return ffaulttypename1;
    }

    public void setFfaulttypename2(String ffaulttypename2) {
        this.ffaulttypename2 = ffaulttypename2;
    }

    public String getFfaulttypename2() {
        return ffaulttypename2;
    }

    public void setFfaultno(String ffaultno) {
        this.ffaultno = ffaultno;
    }

    public String getFfaultno() {
        return ffaultno;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setTrans_zzdhfaultlist7(String trans_zzdhfaultlist7) {
        this.trans_zzdhfaultlist7 = trans_zzdhfaultlist7;
    }

    public String getTrans_zzdhfaultlist7() {
        return trans_zzdhfaultlist7;
    }
}
