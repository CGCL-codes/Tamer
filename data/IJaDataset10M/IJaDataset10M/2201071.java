package com.zhongkai.model.book;

/**
 * TXtXxts entity. @author MyEclipse Persistence Tools
 */
public class TXtXxts implements java.io.Serializable {

    private Integer xh;

    private String xxbt;

    private String xxnr;

    private String url;

    private String fjrDm;

    private String sjrDm;

    private String zt;

    /** default constructor */
    public TXtXxts() {
    }

    /** full constructor */
    public TXtXxts(String xxbt, String xxnr, String url, String fjrDm, String sjrDm, String zt) {
        this.xxbt = xxbt;
        this.xxnr = xxnr;
        this.url = url;
        this.fjrDm = fjrDm;
        this.sjrDm = sjrDm;
        this.zt = zt;
    }

    public Integer getXh() {
        return this.xh;
    }

    public void setXh(Integer xh) {
        this.xh = xh;
    }

    public String getXxbt() {
        return this.xxbt;
    }

    public void setXxbt(String xxbt) {
        this.xxbt = xxbt;
    }

    public String getXxnr() {
        return this.xxnr;
    }

    public void setXxnr(String xxnr) {
        this.xxnr = xxnr;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFjrDm() {
        return this.fjrDm;
    }

    public void setFjrDm(String fjrDm) {
        this.fjrDm = fjrDm;
    }

    public String getSjrDm() {
        return this.sjrDm;
    }

    public void setSjrDm(String sjrDm) {
        this.sjrDm = sjrDm;
    }

    public String getZt() {
        return this.zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }
}
