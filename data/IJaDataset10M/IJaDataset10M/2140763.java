package kr.godsoft.egovframe.generatorwebapp.comtnonlinepolliem.service;

/**
 * @Class Name : ComtnonlinepolliemVO.java
 * @Description : Comtnonlinepolliem VO class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public class ComtnonlinepolliemVO extends ComtnonlinepolliemDefaultVO {

    private static final long serialVersionUID = 1L;

    /** POLL_IEM_NM */
    private String pollIemNm;

    /** FRST_REGISTER_ID */
    private String frstRegisterId;

    /** FRST_REGIST_PNTTM */
    private String frstRegistPnttm;

    /** LAST_UPDUSR_ID */
    private String lastUpdusrId;

    /** LAST_UPDT_PNTTM */
    private String lastUpdtPnttm;

    /** POLL_IEM_ID */
    private String pollIemId;

    /** POLL_ID */
    private String pollId;

    public String getPollIemNm() {
        return this.pollIemNm;
    }

    public void setPollIemNm(String pollIemNm) {
        this.pollIemNm = pollIemNm;
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

    public String getPollIemId() {
        return this.pollIemId;
    }

    public void setPollIemId(String pollIemId) {
        this.pollIemId = pollIemId;
    }

    public String getPollId() {
        return this.pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }
}
