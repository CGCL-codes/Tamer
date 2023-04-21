package kr.godsoft.egovframe.generatorwebapp.comtntrsmrcvmntrng.service;

/**
 * @Class Name : ComtntrsmrcvmntrngVO.java
 * @Description : Comtntrsmrcvmntrng VO class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public class ComtntrsmrcvmntrngVO extends ComtntrsmrcvmntrngDefaultVO {

    private static final long serialVersionUID = 1L;

    /** CNTC_ID */
    private String cntcId;

    /** TEST_CLASS_NM */
    private String testClassNm;

    /** MNGR_NM */
    private String mngrNm;

    /** MNGR_EMAIL_ADRES */
    private String mngrEmailAdres;

    /** MNTRNG_STTUS */
    private String mntrngSttus;

    /** CREAT_DT */
    private String creatDt;

    /** FRST_REGISTER_ID */
    private String frstRegisterId;

    /** FRST_REGIST_PNTTM */
    private String frstRegistPnttm;

    /** LAST_UPDUSR_ID */
    private String lastUpdusrId;

    /** LAST_UPDT_PNTTM */
    private String lastUpdtPnttm;

    public String getCntcId() {
        return this.cntcId;
    }

    public void setCntcId(String cntcId) {
        this.cntcId = cntcId;
    }

    public String getTestClassNm() {
        return this.testClassNm;
    }

    public void setTestClassNm(String testClassNm) {
        this.testClassNm = testClassNm;
    }

    public String getMngrNm() {
        return this.mngrNm;
    }

    public void setMngrNm(String mngrNm) {
        this.mngrNm = mngrNm;
    }

    public String getMngrEmailAdres() {
        return this.mngrEmailAdres;
    }

    public void setMngrEmailAdres(String mngrEmailAdres) {
        this.mngrEmailAdres = mngrEmailAdres;
    }

    public String getMntrngSttus() {
        return this.mntrngSttus;
    }

    public void setMntrngSttus(String mntrngSttus) {
        this.mntrngSttus = mntrngSttus;
    }

    public String getCreatDt() {
        return this.creatDt;
    }

    public void setCreatDt(String creatDt) {
        this.creatDt = creatDt;
    }

    public String getFrstRegisterId() {
        return this.frstRegisterId;
    }

    public void setFrstRegisterId(String frstRegisterId) {
        this.frstRegisterId = frstRegisterId;
    }

    public String getFrstRegistPnttm() {
        return this.frstRegistPnttm;
    }

    public void setFrstRegistPnttm(String frstRegistPnttm) {
        this.frstRegistPnttm = frstRegistPnttm;
    }

    public String getLastUpdusrId() {
        return this.lastUpdusrId;
    }

    public void setLastUpdusrId(String lastUpdusrId) {
        this.lastUpdusrId = lastUpdusrId;
    }

    public String getLastUpdtPnttm() {
        return this.lastUpdtPnttm;
    }

    public void setLastUpdtPnttm(String lastUpdtPnttm) {
        this.lastUpdtPnttm = lastUpdtPnttm;
    }
}
