package kr.godsoft.egovframe.generatorwebapp.comtnproxyinfo.service;

/**
 * @Class Name : ComtnproxyinfoVO.java
 * @Description : Comtnproxyinfo VO class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public class ComtnproxyinfoVO extends ComtnproxyinfoDefaultVO {

    private static final long serialVersionUID = 1L;

    /** PROXY_ID */
    private String proxyId;

    /** PROXY_NM */
    private String proxyNm;

    /** PROXY_IP */
    private String proxyIp;

    /** PROXY_PORT */
    private String proxyPort;

    /** TRGET_SVC_NM */
    private String trgetSvcNm;

    /** SVC_DC */
    private String svcDc;

    /** SVC_IP */
    private String svcIp;

    /** SVC_PORT */
    private String svcPort;

    /** SVC_STTUS */
    private String svcSttus;

    /** FRST_REGISTER_ID */
    private String frstRegisterId;

    /** FRST_REGIST_PNTTM */
    private String frstRegistPnttm;

    /** LAST_UPDUSR_ID */
    private String lastUpdusrId;

    /** LAST_UPDT_PNTTM */
    private String lastUpdtPnttm;

    public String getProxyId() {
        return this.proxyId;
    }

    public void setProxyId(String proxyId) {
        this.proxyId = proxyId;
    }

    public String getProxyNm() {
        return this.proxyNm;
    }

    public void setProxyNm(String proxyNm) {
        this.proxyNm = proxyNm;
    }

    public String getProxyIp() {
        return this.proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    public String getProxyPort() {
        return this.proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getTrgetSvcNm() {
        return this.trgetSvcNm;
    }

    public void setTrgetSvcNm(String trgetSvcNm) {
        this.trgetSvcNm = trgetSvcNm;
    }

    public String getSvcDc() {
        return this.svcDc;
    }

    public void setSvcDc(String svcDc) {
        this.svcDc = svcDc;
    }

    public String getSvcIp() {
        return this.svcIp;
    }

    public void setSvcIp(String svcIp) {
        this.svcIp = svcIp;
    }

    public String getSvcPort() {
        return this.svcPort;
    }

    public void setSvcPort(String svcPort) {
        this.svcPort = svcPort;
    }

    public String getSvcSttus() {
        return this.svcSttus;
    }

    public void setSvcSttus(String svcSttus) {
        this.svcSttus = svcSttus;
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
