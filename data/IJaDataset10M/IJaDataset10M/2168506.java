package com.techstar.dmis.dto;

import java.io.Serializable;
import com.techstar.framework.service.dto.DictionaryBaseDto;

/**
 * Domain classe for 间隔表
 * This classe is based on ValueObject Pattern
 * @author 
 * @date
 */
public class MbayDto implements Serializable {

    public MbayDto() {
    }

    private String fname;

    private String ftype;

    private int fvoltage;

    private String fcontainer;

    private String fmainequipment;

    private String fid;

    private int version;

    private java.util.Collection fetsequipment2;

    private java.util.Collection fmbay2grouprole2;

    private java.util.Collection fmbayeqpment2;

    /**
     * getters and setters
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFname() {
        return fname;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFvoltage(int fvoltage) {
        this.fvoltage = fvoltage;
    }

    public int getFvoltage() {
        return fvoltage;
    }

    public void setFcontainer(String fcontainer) {
        this.fcontainer = fcontainer;
    }

    public String getFcontainer() {
        return fcontainer;
    }

    public void setFmainequipment(String fmainequipment) {
        this.fmainequipment = fmainequipment;
    }

    public String getFmainequipment() {
        return fmainequipment;
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

    public void setFetsequipment2(java.util.Collection fetsequipment2) {
        this.fetsequipment2 = fetsequipment2;
    }

    public java.util.Collection getFetsequipment2() {
        return fetsequipment2;
    }

    public void setFmbay2grouprole2(java.util.Collection fmbay2grouprole2) {
        this.fmbay2grouprole2 = fmbay2grouprole2;
    }

    public java.util.Collection getFmbay2grouprole2() {
        return fmbay2grouprole2;
    }

    public void setFmbayeqpment2(java.util.Collection fmbayeqpment2) {
        this.fmbayeqpment2 = fmbayeqpment2;
    }

    public java.util.Collection getFmbayeqpment2() {
        return fmbayeqpment2;
    }
}
