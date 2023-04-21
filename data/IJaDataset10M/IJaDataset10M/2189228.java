package com.techstar.dmis.dto.transfer;

import java.io.Serializable;

/**
 * Domain classe for 遥测数据修改记录
 * This classe is based on ValueObject Pattern
 */
public class TransZdhRmrecDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public TransZdhRmrecDto() {
    }

    private String fstationname;

    private String switchno;

    private String changeexplanationo;

    private int modificationdatano;

    private String modificationreason;

    private java.sql.Date modificationdate;

    private String remarks;

    private String sys_fille;

    private String sys_filldept;

    private java.sql.Timestamp sys_filltime;

    private int sys_isvalid;

    private String sys_dataowner;

    private String recordno;

    private int version;

    private String trans_zzdhrmrec1;

    private String trans_zzdhrmrec4;

    private String trans_zzdhrmrec2;

    /**
     * getters and setters
     */
    public void setFstationname(String fstationname) {
        this.fstationname = fstationname;
    }

    public String getFstationname() {
        return fstationname;
    }

    public void setSwitchno(String switchno) {
        this.switchno = switchno;
    }

    public String getSwitchno() {
        return switchno;
    }

    public void setChangeexplanationo(String changeexplanationo) {
        this.changeexplanationo = changeexplanationo;
    }

    public String getChangeexplanationo() {
        return changeexplanationo;
    }

    public void setModificationdatano(int modificationdatano) {
        this.modificationdatano = modificationdatano;
    }

    public int getModificationdatano() {
        return modificationdatano;
    }

    public void setModificationreason(String modificationreason) {
        this.modificationreason = modificationreason;
    }

    public String getModificationreason() {
        return modificationreason;
    }

    public void setModificationdate(java.sql.Date modificationdate) {
        this.modificationdate = modificationdate;
    }

    public java.sql.Date getModificationdate() {
        return modificationdate;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
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

    public void setRecordno(String recordno) {
        this.recordno = recordno;
    }

    public String getRecordno() {
        return recordno;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setTrans_zzdhrmrec1(String trans_zzdhrmrec1) {
        this.trans_zzdhrmrec1 = trans_zzdhrmrec1;
    }

    public String getTrans_zzdhrmrec1() {
        return trans_zzdhrmrec1;
    }

    public void setTrans_zzdhrmrec4(String trans_zzdhrmrec4) {
        this.trans_zzdhrmrec4 = trans_zzdhrmrec4;
    }

    public String getTrans_zzdhrmrec4() {
        return trans_zzdhrmrec4;
    }

    public void setTrans_zzdhrmrec2(String trans_zzdhrmrec2) {
        this.trans_zzdhrmrec2 = trans_zzdhrmrec2;
    }

    public String getTrans_zzdhrmrec2() {
        return trans_zzdhrmrec2;
    }
}
