package kr.godsoft.egovframe.generatorwebapp.comtnhpcminfo.service;

/**
 * @Class Name : ComtnhpcminfoVO.java
 * @Description : Comtnhpcminfo VO class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public class ComtnhpcminfoVO extends ComtnhpcminfoDefaultVO {

    private static final long serialVersionUID = 1L;

    /** HPCM_ID */
    private String hpcmId;

    /** HPCM_SE_CODE */
    private String hpcmSeCode;

    /** HPCM_DFN */
    private String hpcmDfn;

    /** HPCM_DC */
    private String hpcmDc;

    /** FRST_REGIST_PNTTM */
    private String frstRegistPnttm;

    /** FRST_REGISTER_ID */
    private String frstRegisterId;

    /** LAST_UPDT_PNTTM */
    private String lastUpdtPnttm;

    /** LAST_UPDUSR_ID */
    private String lastUpdusrId;

    public String getHpcmId() {
        return this.hpcmId;
    }

    public void setHpcmId(String hpcmId) {
        this.hpcmId = hpcmId;
    }

    public String getHpcmSeCode() {
        return this.hpcmSeCode;
    }

    public void setHpcmSeCode(String hpcmSeCode) {
        this.hpcmSeCode = hpcmSeCode;
    }

    public String getHpcmDfn() {
        return this.hpcmDfn;
    }

    public void setHpcmDfn(String hpcmDfn) {
        this.hpcmDfn = hpcmDfn;
    }

    public String getHpcmDc() {
        return this.hpcmDc;
    }

    public void setHpcmDc(String hpcmDc) {
        this.hpcmDc = hpcmDc;
    }

    public String getFrstRegistPnttm() {
        return this.frstRegistPnttm;
    }

    public void setFrstRegistPnttm(String frstRegistPnttm) {
        this.frstRegistPnttm = frstRegistPnttm;
    }

    public String getFrstRegisterId() {
        return this.frstRegisterId;
    }

    public void setFrstRegisterId(String frstRegisterId) {
        this.frstRegisterId = frstRegisterId;
    }

    public String getLastUpdtPnttm() {
        return this.lastUpdtPnttm;
    }

    public void setLastUpdtPnttm(String lastUpdtPnttm) {
        this.lastUpdtPnttm = lastUpdtPnttm;
    }

    public String getLastUpdusrId() {
        return this.lastUpdusrId;
    }

    public void setLastUpdusrId(String lastUpdusrId) {
        this.lastUpdusrId = lastUpdusrId;
    }
}
