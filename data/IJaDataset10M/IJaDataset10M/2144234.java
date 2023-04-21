package com.etc.report.beans;

import java.io.Serializable;

/**
 *
 * @author magicbank
 */
public class VendorQuotationBeans implements Serializable {

    private static final long serialVersionUID = 2L;

    private String CODE;

    private String PROCODE;

    private String PACODE;

    private String MOQ;

    private String PPQ;

    private String PRICE;

    private String CURRENCY;

    private String DATE;

    private String LTIME;

    private String COUNTRY;

    public VendorQuotationBeans() {
    }

    public VendorQuotationBeans(String CODE, String PROCODE, String PACODE, String MOQ, String PPQ, String PRICE, String CURRENCY, String DATE, String LTIME, String COUNTRY) {
        this.CODE = CODE;
        this.PROCODE = PROCODE;
        this.PACODE = PACODE;
        this.MOQ = MOQ;
        this.PPQ = PPQ;
        this.PRICE = PRICE;
        this.CURRENCY = CURRENCY;
        this.DATE = DATE;
        this.LTIME = LTIME;
        this.COUNTRY = COUNTRY;
    }

    /**
     * @return the CODE
     */
    public String getCODE() {
        return CODE;
    }

    /**
     * @param CODE the CODE to set
     */
    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    /**
     * @return the PROCODE
     */
    public String getPROCODE() {
        return PROCODE;
    }

    /**
     * @param PROCODE the PROCODE to set
     */
    public void setPROCODE(String PROCODE) {
        this.PROCODE = PROCODE;
    }

    /**
     * @return the PACODE
     */
    public String getPACODE() {
        return PACODE;
    }

    /**
     * @param PACODE the PACODE to set
     */
    public void setPACODE(String PACODE) {
        this.PACODE = PACODE;
    }

    /**
     * @return the MOQ
     */
    public String getMOQ() {
        return MOQ;
    }

    /**
     * @param MOQ the MOQ to set
     */
    public void setMOQ(String MOQ) {
        this.MOQ = MOQ;
    }

    /**
     * @return the PPQ
     */
    public String getPPQ() {
        return PPQ;
    }

    /**
     * @param PPQ the PPQ to set
     */
    public void setPPQ(String PPQ) {
        this.PPQ = PPQ;
    }

    /**
     * @return the PRICE
     */
    public String getPRICE() {
        return PRICE;
    }

    /**
     * @param PRICE the PRICE to set
     */
    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    /**
     * @return the CURRENCY
     */
    public String getCURRENCY() {
        return CURRENCY;
    }

    /**
     * @param CURRENCY the CURRENCY to set
     */
    public void setCURRENCY(String CURRENCY) {
        this.CURRENCY = CURRENCY;
    }

    /**
     * @return the DATE
     */
    public String getDATE() {
        return DATE;
    }

    /**
     * @param DATE the DATE to set
     */
    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    /**
     * @return the LTIME
     */
    public String getLTIME() {
        return LTIME;
    }

    /**
     * @param LTIME the LTIME to set
     */
    public void setLTIME(String LTIME) {
        this.LTIME = LTIME;
    }

    /**
     * @return the COUNTRY
     */
    public String getCOUNTRY() {
        return COUNTRY;
    }

    /**
     * @param COUNTRY the COUNTRY to set
     */
    public void setCOUNTRY(String COUNTRY) {
        this.COUNTRY = COUNTRY;
    }
}
