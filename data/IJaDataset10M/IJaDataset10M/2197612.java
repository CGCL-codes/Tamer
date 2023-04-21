package kr.godsoft.egovframe.generatorwebapp.comtnanswer.service;

/**
 * @Class Name : ComtnanswerVO.java
 * @Description : Comtnanswer VO class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public class ComtnanswerVO extends ComtnanswerDefaultVO {

    private static final long serialVersionUID = 1L;

    /** NTT_ID */
    private String nttId;

    /** BBS_ID */
    private String bbsId;

    /** WRTER_ID */
    private String wrterId;

    /** ANSWER */
    private String answer;

    /** USE_AT */
    private String useAt;

    /** WRTER_NM */
    private String wrterNm;

    /** FRST_REGIST_PNTTM */
    private String frstRegistPnttm;

    /** FRST_REGISTER_ID */
    private String frstRegisterId;

    /** LAST_UPDT_PNTTM */
    private String lastUpdtPnttm;

    /** LAST_UPDUSR_ID */
    private String lastUpdusrId;

    /** ANSWER_NO */
    private String answerNo;

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

    public String getWrterId() {
        return this.wrterId;
    }

    public void setWrterId(String wrterId) {
        this.wrterId = wrterId;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUseAt() {
        return this.useAt;
    }

    public void setUseAt(String useAt) {
        this.useAt = useAt;
    }

    public String getWrterNm() {
        return this.wrterNm;
    }

    public void setWrterNm(String wrterNm) {
        this.wrterNm = wrterNm;
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

    public String getAnswerNo() {
        return this.answerNo;
    }

    public void setAnswerNo(String answerNo) {
        this.answerNo = answerNo;
    }
}
