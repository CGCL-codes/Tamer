package com.comm.user.model;

import java.math.BigDecimal;

public class User {

    private String chk;

    private BigDecimal seq;

    private String daneoNm;

    private String yeongmunSeolmyeong;

    private String hanjaSeolmyeong;

    private String chulcheo;

    private String bunya;

    public BigDecimal getSeq() {
        return seq;
    }

    public void setSeq(BigDecimal seq) {
        this.seq = seq;
    }

    public String getDaneoNm() {
        return daneoNm;
    }

    public void setDaneoNm(String daneoNm) {
        this.daneoNm = daneoNm;
    }

    public String getYeongmunSeolmyeong() {
        return yeongmunSeolmyeong;
    }

    public void setYeongmunSeolmyeong(String yeongmunSeolmyeong) {
        this.yeongmunSeolmyeong = yeongmunSeolmyeong;
    }

    public String getHanjaSeolmyeong() {
        return hanjaSeolmyeong;
    }

    public void setHanjaSeolmyeong(String hanjaSeolmyeong) {
        this.hanjaSeolmyeong = hanjaSeolmyeong;
    }

    public String getChulcheo() {
        return chulcheo;
    }

    public void setChulcheo(String chulcheo) {
        this.chulcheo = chulcheo;
    }

    public String getBunya() {
        return bunya;
    }

    public void setBunya(String bunya) {
        this.bunya = bunya;
    }

    public String getChk() {
        return chk;
    }

    public void setChk(String chk) {
        this.chk = chk;
    }
}
