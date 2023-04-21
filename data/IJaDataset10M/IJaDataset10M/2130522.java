package com.techstar.dmis.dto;

import java.io.Serializable;
import com.techstar.framework.service.dto.DictionaryBaseDto;

/**
 * Domain classe for 故障简报 This classe is based on ValueObject Pattern
 * 
 * @author
 * @date
 */
public class DdAccidentbriefDto implements Serializable {

    public DdAccidentbriefDto() {
    }

    private String faccidentname;

    private java.sql.Date faccidenttime;

    private String fdispatcher;

    private String freportedpeople;

    private java.sql.Date freportdate;

    private String freporter;

    private String fnotifiedunit;

    private String fnotifyoffice;

    private String freportleadership;

    private java.sql.Date frecoverytime;

    private String ffaultequipmemt;

    private String fdispatchno;

    private String fline;

    private int flesspower;

    private String fweather;

    private String fremark;

    private java.sql.Date ftopdispreporttime;

    private int fovercurrenttime;

    private int fiftimes;

    private int fothertimes;

    private String freporthandle;

    private String flineid;

    private String ffaultequipmemtid;

    private String accidentno;

    private String fstationname;

    private String fstationid;

    private String accidentreason;

    private String checker;

    private java.sql.Timestamp checktime;

    private String checkinfo;

    private String unsatisfyreason;

    private String fexcutestatus;

    private String sys_fille;

    private String sys_filldept;

    private java.sql.Timestamp sys_filltime;

    private int sys_isvalid;

    private String sys_dataowner;

    private String ffailurecauseid;

    private String fstatus;

    private String fvoltage;

    private String fimpact;

    private String fwhethercompleted;

    private String fcontrol;

    private String fgrounding;

    private String faccidentid;

    private int version;

    private java.util.Collection fbhactionrecord10;

    private java.util.Collection fddaccidentbriefprocess1;

    private DdAccidentbriefWFTaskDto ddAccidentbriefWFTaskDto;

    public DdAccidentbriefWFTaskDto getDdAccidentbriefWFTaskDto() {
        return ddAccidentbriefWFTaskDto;
    }

    public void setDdAccidentbriefWFTaskDto(DdAccidentbriefWFTaskDto ddAccidentbriefWFTaskDto) {
        this.ddAccidentbriefWFTaskDto = ddAccidentbriefWFTaskDto;
    }

    /**
	 * getters and setters
	 */
    public void setFaccidentname(String faccidentname) {
        this.faccidentname = faccidentname;
    }

    public String getFaccidentname() {
        return faccidentname;
    }

    public void setFaccidenttime(java.sql.Date faccidenttime) {
        this.faccidenttime = faccidenttime;
    }

    public java.sql.Date getFaccidenttime() {
        return faccidenttime;
    }

    public void setFdispatcher(String fdispatcher) {
        this.fdispatcher = fdispatcher;
    }

    public String getFdispatcher() {
        return fdispatcher;
    }

    public void setFreportedpeople(String freportedpeople) {
        this.freportedpeople = freportedpeople;
    }

    public String getFreportedpeople() {
        return freportedpeople;
    }

    public void setFreportdate(java.sql.Date freportdate) {
        this.freportdate = freportdate;
    }

    public java.sql.Date getFreportdate() {
        return freportdate;
    }

    public void setFreporter(String freporter) {
        this.freporter = freporter;
    }

    public String getFreporter() {
        return freporter;
    }

    public void setFnotifiedunit(String fnotifiedunit) {
        this.fnotifiedunit = fnotifiedunit;
    }

    public String getFnotifiedunit() {
        return fnotifiedunit;
    }

    public void setFnotifyoffice(String fnotifyoffice) {
        this.fnotifyoffice = fnotifyoffice;
    }

    public String getFnotifyoffice() {
        return fnotifyoffice;
    }

    public void setFreportleadership(String freportleadership) {
        this.freportleadership = freportleadership;
    }

    public String getFreportleadership() {
        return freportleadership;
    }

    public void setFrecoverytime(java.sql.Date frecoverytime) {
        this.frecoverytime = frecoverytime;
    }

    public java.sql.Date getFrecoverytime() {
        return frecoverytime;
    }

    public void setFfaultequipmemt(String ffaultequipmemt) {
        this.ffaultequipmemt = ffaultequipmemt;
    }

    public String getFfaultequipmemt() {
        return ffaultequipmemt;
    }

    public void setFdispatchno(String fdispatchno) {
        this.fdispatchno = fdispatchno;
    }

    public String getFdispatchno() {
        return fdispatchno;
    }

    public void setFline(String fline) {
        this.fline = fline;
    }

    public String getFline() {
        return fline;
    }

    public void setFlesspower(int flesspower) {
        this.flesspower = flesspower;
    }

    public int getFlesspower() {
        return flesspower;
    }

    public void setFweather(String fweather) {
        this.fweather = fweather;
    }

    public String getFweather() {
        return fweather;
    }

    public void setFremark(String fremark) {
        this.fremark = fremark;
    }

    public String getFremark() {
        return fremark;
    }

    public void setFtopdispreporttime(java.sql.Date ftopdispreporttime) {
        this.ftopdispreporttime = ftopdispreporttime;
    }

    public java.sql.Date getFtopdispreporttime() {
        return ftopdispreporttime;
    }

    public void setFovercurrenttime(int fovercurrenttime) {
        this.fovercurrenttime = fovercurrenttime;
    }

    public int getFovercurrenttime() {
        return fovercurrenttime;
    }

    public void setFiftimes(int fiftimes) {
        this.fiftimes = fiftimes;
    }

    public int getFiftimes() {
        return fiftimes;
    }

    public void setFothertimes(int fothertimes) {
        this.fothertimes = fothertimes;
    }

    public int getFothertimes() {
        return fothertimes;
    }

    public void setFreporthandle(String freporthandle) {
        this.freporthandle = freporthandle;
    }

    public String getFreporthandle() {
        return freporthandle;
    }

    public void setFlineid(String flineid) {
        this.flineid = flineid;
    }

    public String getFlineid() {
        return flineid;
    }

    public void setFfaultequipmemtid(String ffaultequipmemtid) {
        this.ffaultequipmemtid = ffaultequipmemtid;
    }

    public String getFfaultequipmemtid() {
        return ffaultequipmemtid;
    }

    public void setAccidentno(String accidentno) {
        this.accidentno = accidentno;
    }

    public String getAccidentno() {
        return accidentno;
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

    public void setAccidentreason(String accidentreason) {
        this.accidentreason = accidentreason;
    }

    public String getAccidentreason() {
        return accidentreason;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecktime(java.sql.Timestamp checktime) {
        this.checktime = checktime;
    }

    public java.sql.Timestamp getChecktime() {
        return checktime;
    }

    public void setCheckinfo(String checkinfo) {
        this.checkinfo = checkinfo;
    }

    public String getCheckinfo() {
        return checkinfo;
    }

    public void setUnsatisfyreason(String unsatisfyreason) {
        this.unsatisfyreason = unsatisfyreason;
    }

    public String getUnsatisfyreason() {
        return unsatisfyreason;
    }

    public void setFexcutestatus(String fexcutestatus) {
        this.fexcutestatus = fexcutestatus;
    }

    public String getFexcutestatus() {
        return fexcutestatus;
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

    public void setFfailurecauseid(String ffailurecauseid) {
        this.ffailurecauseid = ffailurecauseid;
    }

    public String getFfailurecauseid() {
        return ffailurecauseid;
    }

    public void setFstatus(String fstatus) {
        this.fstatus = fstatus;
    }

    public String getFstatus() {
        return fstatus;
    }

    public void setFvoltage(String fvoltage) {
        this.fvoltage = fvoltage;
    }

    public String getFvoltage() {
        return fvoltage;
    }

    public void setFimpact(String fimpact) {
        this.fimpact = fimpact;
    }

    public String getFimpact() {
        return fimpact;
    }

    public void setFwhethercompleted(String fwhethercompleted) {
        this.fwhethercompleted = fwhethercompleted;
    }

    public String getFwhethercompleted() {
        return fwhethercompleted;
    }

    public void setFcontrol(String fcontrol) {
        this.fcontrol = fcontrol;
    }

    public String getFcontrol() {
        return fcontrol;
    }

    public void setFgrounding(String fgrounding) {
        this.fgrounding = fgrounding;
    }

    public String getFgrounding() {
        return fgrounding;
    }

    public void setFaccidentid(String faccidentid) {
        this.faccidentid = faccidentid;
    }

    public String getFaccidentid() {
        return faccidentid;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setFbhactionrecord10(java.util.Collection fbhactionrecord10) {
        this.fbhactionrecord10 = fbhactionrecord10;
    }

    public java.util.Collection getFbhactionrecord10() {
        return fbhactionrecord10;
    }

    public void setFddaccidentbriefprocess1(java.util.Collection fddaccidentbriefprocess1) {
        this.fddaccidentbriefprocess1 = fddaccidentbriefprocess1;
    }

    public java.util.Collection getFddaccidentbriefprocess1() {
        return fddaccidentbriefprocess1;
    }
}
