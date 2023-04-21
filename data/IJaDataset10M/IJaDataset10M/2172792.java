package com.techstar.dmis.dto.transfer;

import java.io.Serializable;

/**
 * Domain classe for 年低频减载方案的属性清单
 * This classe is based on ValueObject Pattern
 */
public class TransFsYearlfplanDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public TransFsYearlfplanDto() {
    }

    private int fannual;

    private String fprogramclass;

    private String funit;

    private int ffirstvalue;

    private int fsecondvalue;

    private int fthirdvalue;

    private int ffourthvalue;

    private int ffifthvalue;

    private int fsixthvalue;

    private int fseventhvalue;

    private int fspecialvalue;

    private String fprogramid;

    private int version;

    /**
     * getters and setters
     */
    public void setFannual(int fannual) {
        this.fannual = fannual;
    }

    public int getFannual() {
        return fannual;
    }

    public void setFprogramclass(String fprogramclass) {
        this.fprogramclass = fprogramclass;
    }

    public String getFprogramclass() {
        return fprogramclass;
    }

    public void setFunit(String funit) {
        this.funit = funit;
    }

    public String getFunit() {
        return funit;
    }

    public void setFfirstvalue(int ffirstvalue) {
        this.ffirstvalue = ffirstvalue;
    }

    public int getFfirstvalue() {
        return ffirstvalue;
    }

    public void setFsecondvalue(int fsecondvalue) {
        this.fsecondvalue = fsecondvalue;
    }

    public int getFsecondvalue() {
        return fsecondvalue;
    }

    public void setFthirdvalue(int fthirdvalue) {
        this.fthirdvalue = fthirdvalue;
    }

    public int getFthirdvalue() {
        return fthirdvalue;
    }

    public void setFfourthvalue(int ffourthvalue) {
        this.ffourthvalue = ffourthvalue;
    }

    public int getFfourthvalue() {
        return ffourthvalue;
    }

    public void setFfifthvalue(int ffifthvalue) {
        this.ffifthvalue = ffifthvalue;
    }

    public int getFfifthvalue() {
        return ffifthvalue;
    }

    public void setFsixthvalue(int fsixthvalue) {
        this.fsixthvalue = fsixthvalue;
    }

    public int getFsixthvalue() {
        return fsixthvalue;
    }

    public void setFseventhvalue(int fseventhvalue) {
        this.fseventhvalue = fseventhvalue;
    }

    public int getFseventhvalue() {
        return fseventhvalue;
    }

    public void setFspecialvalue(int fspecialvalue) {
        this.fspecialvalue = fspecialvalue;
    }

    public int getFspecialvalue() {
        return fspecialvalue;
    }

    public void setFprogramid(String fprogramid) {
        this.fprogramid = fprogramid;
    }

    public String getFprogramid() {
        return fprogramid;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }
}
