package com.entelience.objects.mim;

import java.util.Date;

public class UserDetail implements java.io.Serializable {

    private Date date_hire;

    private Date date_end_contract;

    private int ad_logoncount;

    private Date ad_lastlogon;

    private Date ad_pwdlastset;

    private Date ad_whenchanged;

    private Date ad_whencreated;

    private int tss_logoncount;

    private Date tss_lastlogon;

    private Date tss_pwdexpires;

    private Date tss_pwdlastset;

    private Date tss_whenchanged;

    private Date tss_whencreated;

    public UserDetail() {
    }

    public void setDate_hire(Date date_hire) {
        this.date_hire = date_hire;
    }

    public Date getDate_hire() {
        return date_hire;
    }

    public void setDate_end_contract(Date date_end_contract) {
        this.date_end_contract = date_end_contract;
    }

    public Date getDate_end_contract() {
        return date_end_contract;
    }

    public void setAd_logoncount(int ad_logoncount) {
        this.ad_logoncount = ad_logoncount;
    }

    public int getAd_logoncount() {
        return ad_logoncount;
    }

    public void setAd_lastlogon(Date ad_lastlogon) {
        this.ad_lastlogon = ad_lastlogon;
    }

    public Date getAd_lastlogon() {
        return ad_lastlogon;
    }

    public void setAd_pwdlastset(Date ad_pwdlastset) {
        this.ad_pwdlastset = ad_pwdlastset;
    }

    public Date getAd_pwdlastset() {
        return ad_pwdlastset;
    }

    public void setAd_whenchanged(Date ad_whenchanged) {
        this.ad_whenchanged = ad_whenchanged;
    }

    public Date getAd_whenchanged() {
        return ad_whenchanged;
    }

    public void setAd_whencreated(Date ad_whencreated) {
        this.ad_whencreated = ad_whencreated;
    }

    public Date getAd_whencreated() {
        return ad_whencreated;
    }

    public void setTss_logoncount(int tss_logoncount) {
        this.tss_logoncount = tss_logoncount;
    }

    public int getTss_logoncount() {
        return tss_logoncount;
    }

    public void setTss_lastlogon(Date tss_lastlogon) {
        this.tss_lastlogon = tss_lastlogon;
    }

    public Date getTss_lastlogon() {
        return tss_lastlogon;
    }

    public void setTss_pwdexpires(Date tss_pwdexpires) {
        this.tss_pwdexpires = tss_pwdexpires;
    }

    public Date getTss_pwdexpires() {
        return tss_pwdexpires;
    }

    public void setTss_pwdlastset(Date tss_pwdlastset) {
        this.tss_pwdlastset = tss_pwdlastset;
    }

    public Date getTss_pwdlastset() {
        return tss_pwdlastset;
    }

    public void setTss_whenchanged(Date tss_whenchanged) {
        this.tss_whenchanged = tss_whenchanged;
    }

    public Date getTss_whenchanged() {
        return tss_whenchanged;
    }

    public void setTss_whencreated(Date tss_whencreated) {
        this.tss_whencreated = tss_whencreated;
    }

    public Date getTss_whencreated() {
        return tss_whencreated;
    }
}
