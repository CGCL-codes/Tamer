package kr.godsoft.egovframe.generatorwebapp.comtnscrap.service;

/**
 * @Class Name : ComtnscrapVO.java
 * @Description : Comtnscrap VO class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public class ComtnscrapVO extends ComtnscrapDefaultVO {

    private static final long serialVersionUID = 1L;

    /** SCRAP_ID */
    private String scrapId;

    /** NTT_ID */
    private String nttId;

    /** BBS_ID */
    private String bbsId;

    /** SCRAP_NM */
    private String scrapNm;

    /** USE_AT */
    private String useAt;

    /** FRST_REGIST_PNTTM */
    private String frstRegistPnttm;

    /** LAST_UPDT_PNTTM */
    private String lastUpdtPnttm;

    /** FRST_REGISTER_ID */
    private String frstRegisterId;

    /** LAST_UPDUSR_ID */
    private String lastUpdusrId;

    public String getScrapId() {
        return this.scrapId;
    }

    public void setScrapId(String scrapId) {
        this.scrapId = scrapId;
    }

    public String getNttId() {
        return this.nttId;
    }

    public void setNttId(String nttId) {
        this.nttId = nttId;
    }

    public String getBbsId() {
        return this.bbsId;
    }

    public void setBbsId(String bbsId) {
        this.bbsId = bbsId;
    }

    public String getScrapNm() {
        return this.scrapNm;
    }

    public void setScrapNm(String scrapNm) {
        this.scrapNm = scrapNm;
    }

    public String getUseAt() {
        return this.useAt;
    }

    public void setUseAt(String useAt) {
        this.useAt = useAt;
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
