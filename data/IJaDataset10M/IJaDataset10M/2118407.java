package com.techstar.dmis.dto.transfer;

import java.io.Serializable;

/**
 * Domain classe for 汽轮机及其调节系统参数
 * This classe is based on ValueObject Pattern
 */
public class TransParaTurBineAdjusterDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public TransParaTurBineAdjusterDto() {
    }

    private String fpowerplan;

    private String fpowerunitno;

    private String fpowerunitmode;

    private int fpowerunitcapa;

    private String fboilertype;

    private String fhasdcs;

    private String fdcsfactory;

    private String fturbinefactory;

    private String fturbinetype;

    private int fturbinerotorspeed;

    private int fhvreakconttime;

    private int freheatreakconttime;

    private int flvreakconttime;

    private int fhvpowerratio;

    private int fmvpowerratio;

    private int flvpowerratio;

    private int fplantmaxoutpower;

    private int fplantidlepower;

    private int freakconttime;

    private int ftakereakpress;

    private int fheattakereakpress;

    private int fturbinefixpress;

    private String feqpid;

    private int version;

    private String trans_zparaturbineadjuster2;

    /**
     * getters and setters
     */
    public void setFpowerplan(String fpowerplan) {
        this.fpowerplan = fpowerplan;
    }

    public String getFpowerplan() {
        return fpowerplan;
    }

    public void setFpowerunitno(String fpowerunitno) {
        this.fpowerunitno = fpowerunitno;
    }

    public String getFpowerunitno() {
        return fpowerunitno;
    }

    public void setFpowerunitmode(String fpowerunitmode) {
        this.fpowerunitmode = fpowerunitmode;
    }

    public String getFpowerunitmode() {
        return fpowerunitmode;
    }

    public void setFpowerunitcapa(int fpowerunitcapa) {
        this.fpowerunitcapa = fpowerunitcapa;
    }

    public int getFpowerunitcapa() {
        return fpowerunitcapa;
    }

    public void setFboilertype(String fboilertype) {
        this.fboilertype = fboilertype;
    }

    public String getFboilertype() {
        return fboilertype;
    }

    public void setFhasdcs(String fhasdcs) {
        this.fhasdcs = fhasdcs;
    }

    public String getFhasdcs() {
        return fhasdcs;
    }

    public void setFdcsfactory(String fdcsfactory) {
        this.fdcsfactory = fdcsfactory;
    }

    public String getFdcsfactory() {
        return fdcsfactory;
    }

    public void setFturbinefactory(String fturbinefactory) {
        this.fturbinefactory = fturbinefactory;
    }

    public String getFturbinefactory() {
        return fturbinefactory;
    }

    public void setFturbinetype(String fturbinetype) {
        this.fturbinetype = fturbinetype;
    }

    public String getFturbinetype() {
        return fturbinetype;
    }

    public void setFturbinerotorspeed(int fturbinerotorspeed) {
        this.fturbinerotorspeed = fturbinerotorspeed;
    }

    public int getFturbinerotorspeed() {
        return fturbinerotorspeed;
    }

    public void setFhvreakconttime(int fhvreakconttime) {
        this.fhvreakconttime = fhvreakconttime;
    }

    public int getFhvreakconttime() {
        return fhvreakconttime;
    }

    public void setFreheatreakconttime(int freheatreakconttime) {
        this.freheatreakconttime = freheatreakconttime;
    }

    public int getFreheatreakconttime() {
        return freheatreakconttime;
    }

    public void setFlvreakconttime(int flvreakconttime) {
        this.flvreakconttime = flvreakconttime;
    }

    public int getFlvreakconttime() {
        return flvreakconttime;
    }

    public void setFhvpowerratio(int fhvpowerratio) {
        this.fhvpowerratio = fhvpowerratio;
    }

    public int getFhvpowerratio() {
        return fhvpowerratio;
    }

    public void setFmvpowerratio(int fmvpowerratio) {
        this.fmvpowerratio = fmvpowerratio;
    }

    public int getFmvpowerratio() {
        return fmvpowerratio;
    }

    public void setFlvpowerratio(int flvpowerratio) {
        this.flvpowerratio = flvpowerratio;
    }

    public int getFlvpowerratio() {
        return flvpowerratio;
    }

    public void setFplantmaxoutpower(int fplantmaxoutpower) {
        this.fplantmaxoutpower = fplantmaxoutpower;
    }

    public int getFplantmaxoutpower() {
        return fplantmaxoutpower;
    }

    public void setFplantidlepower(int fplantidlepower) {
        this.fplantidlepower = fplantidlepower;
    }

    public int getFplantidlepower() {
        return fplantidlepower;
    }

    public void setFreakconttime(int freakconttime) {
        this.freakconttime = freakconttime;
    }

    public int getFreakconttime() {
        return freakconttime;
    }

    public void setFtakereakpress(int ftakereakpress) {
        this.ftakereakpress = ftakereakpress;
    }

    public int getFtakereakpress() {
        return ftakereakpress;
    }

    public void setFheattakereakpress(int fheattakereakpress) {
        this.fheattakereakpress = fheattakereakpress;
    }

    public int getFheattakereakpress() {
        return fheattakereakpress;
    }

    public void setFturbinefixpress(int fturbinefixpress) {
        this.fturbinefixpress = fturbinefixpress;
    }

    public int getFturbinefixpress() {
        return fturbinefixpress;
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

    public void setTrans_zparaturbineadjuster2(String trans_zparaturbineadjuster2) {
        this.trans_zparaturbineadjuster2 = trans_zparaturbineadjuster2;
    }

    public String getTrans_zparaturbineadjuster2() {
        return trans_zparaturbineadjuster2;
    }
}
