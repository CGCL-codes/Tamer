package kr.godsoft.egovframe.generatorwebapp.comtnntfcinfo.service;

/**
 * @Class Name : ComtnntfcinfoVO.java
 * @Description : Comtnntfcinfo VO class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public class ComtnntfcinfoVO extends ComtnntfcinfoDefaultVO {

    private static final long serialVersionUID = 1L;

    /** NTCN_NO */
    private String ntcnNo;

    /** NTCN_SJ */
    private String ntcnSj;

    /** NTCN_CN */
    private String ntcnCn;

    /** NTCN_TM */
    private String ntcnTm;

    /** BH_NTCN_INTRVL */
    private String bhNtcnIntrvl;

    /** FRST_REGIST_PNTTM */
    private String frstRegistPnttm;

    /** LAST_UPDT_PNTTM */
    private String lastUpdtPnttm;

    /** FRST_REGISTER_ID */
    private String frstRegisterId;

    /** LAST_UPDUSR_ID */
    private String lastUpdusrId;

    public String getNtcnNo() {
        return this.ntcnNo;
    }

    public void setNtcnNo(String ntcnNo) {
        this.ntcnNo = ntcnNo;
    }

    public String getNtcnSj() {
        return this.ntcnSj;
    }

    public void setNtcnSj(String ntcnSj) {
        this.ntcnSj = ntcnSj;
    }

    public String getNtcnCn() {
        return this.ntcnCn;
    }

    public void setNtcnCn(String ntcnCn) {
        this.ntcnCn = ntcnCn;
    }

    public String getNtcnTm() {
        return this.ntcnTm;
    }

    public void setNtcnTm(String ntcnTm) {
        this.ntcnTm = ntcnTm;
    }

    public String getBhNtcnIntrvl() {
        return this.bhNtcnIntrvl;
    }

    public void setBhNtcnIntrvl(String bhNtcnIntrvl) {
        this.bhNtcnIntrvl = bhNtcnIntrvl;
    }

    public String getFrstRegistPnttm() {
        return this.frstRegistPnttm;
    }

    public void setFrstRegistPnttm(String frstRegistPnttm) {
        this.frstRegistPnttm = frstRegistPnttm;
    }

    public String getLastUpdtPnttm() {
        return this.lastUpdtPnttm;
    }

    public void setLastUpdtPnttm(String lastUpdtPnttm) {
        this.lastUpdtPnttm = lastUpdtPnttm;
    }

    public String getFrstRegisterId() {
        return this.frstRegisterId;
    }

    public void setFrstRegisterId(String frstRegisterId) {
        this.frstRegisterId = frstRegisterId;
    }

    public String getLastUpdusrId() {
        return this.lastUpdusrId;
    }

    public void setLastUpdusrId(String lastUpdusrId) {
        this.lastUpdusrId = lastUpdusrId;
    }
}
