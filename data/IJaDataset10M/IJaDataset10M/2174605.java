package com.techstar.dmis.dto;

import java.io.Serializable;
import com.techstar.framework.service.dto.DictionaryBaseDto;

/**
 * Domain classe for 保护动作记录
 * This classe is based on ValueObject Pattern
 * @author 
 * @date
 */
public class BhActionrecordDto implements Serializable {

    public BhActionrecordDto() {
    }

    private String eqpid;

    private String fstationname;

    private String fstationid;

    private String dispatchno;

    private java.sql.Date actiontime;

    private int actionevaluatetimes;

    private int faultrecordertimes;

    private int faultrecordergoodtimes;

    private String faulteqp;

    private byte[] actiondescription;

    private int actionkeeptimes;

    private int faultkeeptimes;

    private String switchmodel;

    private String remarks;

    private String sys_fille;

    private String sys_filldept;

    private java.sql.Timestamp sys_filltime;

    private int sys_isvalid;

    private String sys_dataowner;

    private String actionevaluate;

    private String reclosuremode;

    private String isreclosure;

    private String linereclosure;

    private String protectionrecordid;

    private int version;

    private com.techstar.dmis.dto.StdBhtresponseDto zbhactionrecord3;

    private com.techstar.dmis.dto.StdPreasontypeDto zbhactionrecord4;

    private com.techstar.dmis.dto.StdBheqpfaultDto zbhactionrecord6;

    private com.techstar.dmis.dto.DdAccidentbriefDto zbhactionrecord10;

    /**
     * getters and setters
     */
    public void setEqpid(String eqpid) {
        this.eqpid = eqpid;
    }

    public String getEqpid() {
        return eqpid;
    }

    public void setFstationname(String fstationname) {
        this.fstationname = fstationname;
    }

    public String getFstationname() {
        return fstationname;
    }

    public void setFstationid(String fstationid) {
        this.fstationid = fstationid;
    }

    public String getFstationid() {
        return fstationid;
    }

    public void setDispatchno(String dispatchno) {
        this.dispatchno = dispatchno;
    }

    public String getDispatchno() {
        return dispatchno;
    }

    public void setActiontime(java.sql.Date actiontime) {
        this.actiontime = actiontime;
    }

    public java.sql.Date getActiontime() {
        return actiontime;
    }

    public void setActionevaluatetimes(int actionevaluatetimes) {
        this.actionevaluatetimes = actionevaluatetimes;
    }

    public int getActionevaluatetimes() {
        return actionevaluatetimes;
    }

    public void setFaultrecordertimes(int faultrecordertimes) {
        this.faultrecordertimes = faultrecordertimes;
    }

    public int getFaultrecordertimes() {
        return faultrecordertimes;
    }

    public void setFaultrecordergoodtimes(int faultrecordergoodtimes) {
        this.faultrecordergoodtimes = faultrecordergoodtimes;
    }

    public int getFaultrecordergoodtimes() {
        return faultrecordergoodtimes;
    }

    public void setFaulteqp(String faulteqp) {
        this.faulteqp = faulteqp;
    }

    public String getFaulteqp() {
        return faulteqp;
    }

    public void setActiondescription(byte[] actiondescription) {
        this.actiondescription = actiondescription;
    }

    public byte[] getActiondescription() {
        return actiondescription;
    }

    public void setActionkeeptimes(int actionkeeptimes) {
        this.actionkeeptimes = actionkeeptimes;
    }

    public int getActionkeeptimes() {
        return actionkeeptimes;
    }

    public void setFaultkeeptimes(int faultkeeptimes) {
        this.faultkeeptimes = faultkeeptimes;
    }

    public int getFaultkeeptimes() {
        return faultkeeptimes;
    }

    public void setSwitchmodel(String switchmodel) {
        this.switchmodel = switchmodel;
    }

    public String getSwitchmodel() {
        return switchmodel;
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

    public void setActionevaluate(String actionevaluate) {
        this.actionevaluate = actionevaluate;
    }

    public String getActionevaluate() {
        return actionevaluate;
    }

    public void setReclosuremode(String reclosuremode) {
        this.reclosuremode = reclosuremode;
    }

    public String getReclosuremode() {
        return reclosuremode;
    }

    public void setIsreclosure(String isreclosure) {
        this.isreclosure = isreclosure;
    }

    public String getIsreclosure() {
        return isreclosure;
    }

    public void setLinereclosure(String linereclosure) {
        this.linereclosure = linereclosure;
    }

    public String getLinereclosure() {
        return linereclosure;
    }

    public void setProtectionrecordid(String protectionrecordid) {
        this.protectionrecordid = protectionrecordid;
    }

    public String getProtectionrecordid() {
        return protectionrecordid;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setZbhactionrecord3(com.techstar.dmis.dto.StdBhtresponseDto zbhactionrecord3) {
        this.zbhactionrecord3 = zbhactionrecord3;
    }

    public com.techstar.dmis.dto.StdBhtresponseDto getZbhactionrecord3() {
        return zbhactionrecord3;
    }

    public void setZbhactionrecord4(com.techstar.dmis.dto.StdPreasontypeDto zbhactionrecord4) {
        this.zbhactionrecord4 = zbhactionrecord4;
    }

    public com.techstar.dmis.dto.StdPreasontypeDto getZbhactionrecord4() {
        return zbhactionrecord4;
    }

    public void setZbhactionrecord6(com.techstar.dmis.dto.StdBheqpfaultDto zbhactionrecord6) {
        this.zbhactionrecord6 = zbhactionrecord6;
    }

    public com.techstar.dmis.dto.StdBheqpfaultDto getZbhactionrecord6() {
        return zbhactionrecord6;
    }

    public void setZbhactionrecord10(com.techstar.dmis.dto.DdAccidentbriefDto zbhactionrecord10) {
        this.zbhactionrecord10 = zbhactionrecord10;
    }

    public com.techstar.dmis.dto.DdAccidentbriefDto getZbhactionrecord10() {
        return zbhactionrecord10;
    }
}
