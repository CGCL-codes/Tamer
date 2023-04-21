package com.angis.fx.data;

import java.io.Serializable;

public class DutyInfo implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2509757568406635963L;

    private String dusername;

    private String dutyno;

    private String startdutytime;

    private String enddutytime;

    private String dutystatus;

    private String mctjuuid;

    private String aftjuuid;

    private String mcareacode;

    private String afareacode;

    private String sxry;

    private String togetheruser;

    private String otheruser;

    private String othercount;

    private String cdrc;

    public DutyInfo() {
        super();
    }

    public DutyInfo(String dusername, String dutyno, String startdutytime, String enddutytime, String dutystatus, String mctjuuid, String aftjuuid, String mcareacode, String afareacode, String sxry, String togetheruser, String otheruser, String othercount, String cdrc) {
        super();
        this.dusername = dusername;
        this.dutyno = dutyno;
        this.startdutytime = startdutytime;
        this.enddutytime = enddutytime;
        this.dutystatus = dutystatus;
        this.mctjuuid = mctjuuid;
        this.aftjuuid = aftjuuid;
        this.mcareacode = mcareacode;
        this.afareacode = afareacode;
        this.sxry = sxry;
        this.togetheruser = togetheruser;
        this.otheruser = otheruser;
        this.othercount = othercount;
        this.cdrc = cdrc;
    }

    public String getDusername() {
        return dusername;
    }

    public void setDusername(String dusername) {
        this.dusername = dusername;
    }

    public String getDutyno() {
        return dutyno;
    }

    public void setDutyno(String dutyno) {
        this.dutyno = dutyno;
    }

    public String getStartdutytime() {
        return startdutytime;
    }

    public void setStartdutytime(String startdutytime) {
        this.startdutytime = startdutytime;
    }

    public String getEnddutytime() {
        return enddutytime;
    }

    public void setEnddutytime(String enddutytime) {
        this.enddutytime = enddutytime;
    }

    public String getDutystatus() {
        return dutystatus;
    }

    public void setDutystatus(String dutystatus) {
        this.dutystatus = dutystatus;
    }

    public String getMctjuuid() {
        return mctjuuid;
    }

    public void setMctjuuid(String mctjuuid) {
        this.mctjuuid = mctjuuid;
    }

    public String getAftjuuid() {
        return aftjuuid;
    }

    public void setAftjuuid(String aftjuuid) {
        this.aftjuuid = aftjuuid;
    }

    public String getMcareacode() {
        return mcareacode;
    }

    public void setMcareacode(String mcareacode) {
        this.mcareacode = mcareacode;
    }

    public String getAfareacode() {
        return afareacode;
    }

    public void setAfareacode(String afareacode) {
        this.afareacode = afareacode;
    }

    public String getSxry() {
        return sxry;
    }

    public void setSxry(String sxry) {
        this.sxry = sxry;
    }

    public String getTogetheruser() {
        return togetheruser;
    }

    public void setTogetheruser(String togetheruser) {
        this.togetheruser = togetheruser;
    }

    public String getOtheruser() {
        return otheruser;
    }

    public void setOtheruser(String otheruser) {
        this.otheruser = otheruser;
    }

    public String getOthercount() {
        return othercount;
    }

    public void setOthercount(String othercount) {
        this.othercount = othercount;
    }

    public String getCdrc() {
        return cdrc;
    }

    public void setCdrc(String cdrc) {
        this.cdrc = cdrc;
    }
}
