package com.techstar.dmis.entity;

import java.io.Serializable;

/**
 * Domain classe for 限流电抗器
 * This classe is based on ValueObject Pattern
 * @author 
 * @date
 */
public class ParaParallReactor implements Serializable {

    public ParaParallReactor() {
    }

    private String fcoolmethod;

    private int fratedcurrent;

    private int fratedcapacity;

    private int freactorvalue;

    private int feqploss;

    private int ftotalweight;

    private String fmagnetizerstructure;

    private String fphaseno;

    private int funtankweight;

    private int foilweight;

    private String finsulatedielectric;

    private String fisthermosyphon;

    private String ftankstructure;

    private String ftankstrength;

    private String fspecdev;

    private String foilsealmethod;

    private int frtreactorvoldrop;

    private java.sql.Date ffixdate;

    private java.sql.Date fexamdate;

    private java.sql.Date fassaydate;

    private String feqpid;

    private int version;

    private com.techstar.dmis.entity.EtsEquipment zparaparallreactor2;

    /**
     * getters and setters
     */
    public void setFcoolmethod(String fcoolmethod) {
        this.fcoolmethod = fcoolmethod;
    }

    public String getFcoolmethod() {
        return fcoolmethod;
    }

    public void setFratedcurrent(int fratedcurrent) {
        this.fratedcurrent = fratedcurrent;
    }

    public int getFratedcurrent() {
        return fratedcurrent;
    }

    public void setFratedcapacity(int fratedcapacity) {
        this.fratedcapacity = fratedcapacity;
    }

    public int getFratedcapacity() {
        return fratedcapacity;
    }

    public void setFreactorvalue(int freactorvalue) {
        this.freactorvalue = freactorvalue;
    }

    public int getFreactorvalue() {
        return freactorvalue;
    }

    public void setFeqploss(int feqploss) {
        this.feqploss = feqploss;
    }

    public int getFeqploss() {
        return feqploss;
    }

    public void setFtotalweight(int ftotalweight) {
        this.ftotalweight = ftotalweight;
    }

    public int getFtotalweight() {
        return ftotalweight;
    }

    public void setFmagnetizerstructure(String fmagnetizerstructure) {
        this.fmagnetizerstructure = fmagnetizerstructure;
    }

    public String getFmagnetizerstructure() {
        return fmagnetizerstructure;
    }

    public void setFphaseno(String fphaseno) {
        this.fphaseno = fphaseno;
    }

    public String getFphaseno() {
        return fphaseno;
    }

    public void setFuntankweight(int funtankweight) {
        this.funtankweight = funtankweight;
    }

    public int getFuntankweight() {
        return funtankweight;
    }

    public void setFoilweight(int foilweight) {
        this.foilweight = foilweight;
    }

    public int getFoilweight() {
        return foilweight;
    }

    public void setFinsulatedielectric(String finsulatedielectric) {
        this.finsulatedielectric = finsulatedielectric;
    }

    public String getFinsulatedielectric() {
        return finsulatedielectric;
    }

    public void setFisthermosyphon(String fisthermosyphon) {
        this.fisthermosyphon = fisthermosyphon;
    }

    public String getFisthermosyphon() {
        return fisthermosyphon;
    }

    public void setFtankstructure(String ftankstructure) {
        this.ftankstructure = ftankstructure;
    }

    public String getFtankstructure() {
        return ftankstructure;
    }

    public void setFtankstrength(String ftankstrength) {
        this.ftankstrength = ftankstrength;
    }

    public String getFtankstrength() {
        return ftankstrength;
    }

    public void setFspecdev(String fspecdev) {
        this.fspecdev = fspecdev;
    }

    public String getFspecdev() {
        return fspecdev;
    }

    public void setFoilsealmethod(String foilsealmethod) {
        this.foilsealmethod = foilsealmethod;
    }

    public String getFoilsealmethod() {
        return foilsealmethod;
    }

    public void setFrtreactorvoldrop(int frtreactorvoldrop) {
        this.frtreactorvoldrop = frtreactorvoldrop;
    }

    public int getFrtreactorvoldrop() {
        return frtreactorvoldrop;
    }

    public void setFfixdate(java.sql.Date ffixdate) {
        this.ffixdate = ffixdate;
    }

    public java.sql.Date getFfixdate() {
        return ffixdate;
    }

    public void setFexamdate(java.sql.Date fexamdate) {
        this.fexamdate = fexamdate;
    }

    public java.sql.Date getFexamdate() {
        return fexamdate;
    }

    public void setFassaydate(java.sql.Date fassaydate) {
        this.fassaydate = fassaydate;
    }

    public java.sql.Date getFassaydate() {
        return fassaydate;
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

    public void setZparaparallreactor2(com.techstar.dmis.entity.EtsEquipment zparaparallreactor2) {
        this.zparaparallreactor2 = zparaparallreactor2;
    }

    public com.techstar.dmis.entity.EtsEquipment getZparaparallreactor2() {
        return zparaparallreactor2;
    }
}
