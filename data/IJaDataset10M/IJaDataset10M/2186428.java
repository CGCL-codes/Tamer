package com.etc.report.beans;

import java.io.Serializable;

/**
 *
 * @author magicbank
 */
public class InvoiceBalanceBeans implements Serializable {

    private static final long serialVersionUID = 2L;

    private String PACODE;

    private String PANAME;

    private String TNAME;

    private String PNDATE;

    private String PNCODE;

    private String POCODE;

    private String SHIPDATE;

    private String PRICE;

    private String PROCODE;

    private String PRONAME;

    private String PNQTY;

    private String RVCODE;

    private String RVDATE;

    private String RVQTY;

    private String PNREM;

    private String POLID;

    private String MACODE;

    private String MANAME;

    private String UNIT;

    private String CURRENCY;

    private String AMOUNT;

    public InvoiceBalanceBeans() {
    }

    public InvoiceBalanceBeans(String PACODE, String PANAME, String TNAME, String PNDATE, String PNCODE, String POCODE, String SHIPDATE, String PRICE, String PROCODE, String PRONAME, String PNQTY, String RVCODE, String RVDATE, String RVQTY, String PNREM, String POLID, String MACODE, String MANAME, String UNIT, String CURRENCY, String AMOUNT) {
        this.PACODE = PACODE;
        this.PANAME = PANAME;
        this.TNAME = TNAME;
        this.PNDATE = PNDATE;
        this.PNCODE = PNCODE;
        this.POCODE = POCODE;
        this.SHIPDATE = SHIPDATE;
        this.PRICE = PRICE;
        this.PROCODE = PROCODE;
        this.PRONAME = PRONAME;
        this.PNQTY = PNQTY;
        this.RVCODE = RVCODE;
        this.RVDATE = RVDATE;
        this.RVQTY = RVQTY;
        this.PNREM = PNREM;
        this.POLID = POLID;
        this.MACODE = MACODE;
        this.MANAME = MANAME;
        this.UNIT = UNIT;
        this.CURRENCY = CURRENCY;
        this.AMOUNT = AMOUNT;
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
     * @return the PANAME
     */
    public String getPANAME() {
        return PANAME;
    }

    /**
     * @param PANAME the PANAME to set
     */
    public void setPANAME(String PANAME) {
        this.PANAME = PANAME;
    }

    /**
     * @return the TNAME
     */
    public String getTNAME() {
        return TNAME;
    }

    /**
     * @param TNAME the TNAME to set
     */
    public void setTNAME(String TNAME) {
        this.TNAME = TNAME;
    }

    /**
     * @return the PNDATE
     */
    public String getPNDATE() {
        return PNDATE;
    }

    /**
     * @param PNDATE the PNDATE to set
     */
    public void setPNDATE(String PNDATE) {
        this.PNDATE = PNDATE;
    }

    /**
     * @return the PNCODE
     */
    public String getPNCODE() {
        return PNCODE;
    }

    /**
     * @param PNCODE the PNCODE to set
     */
    public void setPNCODE(String PNCODE) {
        this.PNCODE = PNCODE;
    }

    /**
     * @return the POCODE
     */
    public String getPOCODE() {
        return POCODE;
    }

    /**
     * @param POCODE the POCODE to set
     */
    public void setPOCODE(String POCODE) {
        this.POCODE = POCODE;
    }

    /**
     * @return the SHIPDATE
     */
    public String getSHIPDATE() {
        return SHIPDATE;
    }

    /**
     * @param SHIPDATE the SHIPDATE to set
     */
    public void setSHIPDATE(String SHIPDATE) {
        this.SHIPDATE = SHIPDATE;
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
     * @return the PRONAME
     */
    public String getPRONAME() {
        return PRONAME;
    }

    /**
     * @param PRONAME the PRONAME to set
     */
    public void setPRONAME(String PRONAME) {
        this.PRONAME = PRONAME;
    }

    /**
     * @return the PNQTY
     */
    public String getPNQTY() {
        return PNQTY;
    }

    /**
     * @param PNQTY the PNQTY to set
     */
    public void setPNQTY(String PNQTY) {
        this.PNQTY = PNQTY;
    }

    /**
     * @return the RVCODE
     */
    public String getRVCODE() {
        return RVCODE;
    }

    /**
     * @param RVCODE the RVCODE to set
     */
    public void setRVCODE(String RVCODE) {
        this.RVCODE = RVCODE;
    }

    /**
     * @return the RVDATE
     */
    public String getRVDATE() {
        return RVDATE;
    }

    /**
     * @param RVDATE the RVDATE to set
     */
    public void setRVDATE(String RVDATE) {
        this.RVDATE = RVDATE;
    }

    /**
     * @return the RVQTY
     */
    public String getRVQTY() {
        return RVQTY;
    }

    /**
     * @param RVQTY the RVQTY to set
     */
    public void setRVQTY(String RVQTY) {
        this.RVQTY = RVQTY;
    }

    /**
     * @return the PNREM
     */
    public String getPNREM() {
        return PNREM;
    }

    /**
     * @param PNREM the PNREM to set
     */
    public void setPNREM(String PNREM) {
        this.PNREM = PNREM;
    }

    /**
     * @return the POLID
     */
    public String getPOLID() {
        return POLID;
    }

    /**
     * @param POLID the POLID to set
     */
    public void setPOLID(String POLID) {
        this.POLID = POLID;
    }

    /**
     * @return the MACODE
     */
    public String getMACODE() {
        return MACODE;
    }

    /**
     * @param MACODE the MACODE to set
     */
    public void setMACODE(String MACODE) {
        this.MACODE = MACODE;
    }

    /**
     * @return the MANAME
     */
    public String getMANAME() {
        return MANAME;
    }

    /**
     * @param MANAME the MANAME to set
     */
    public void setMANAME(String MANAME) {
        this.MANAME = MANAME;
    }

    /**
     * @return the UNIT
     */
    public String getUNIT() {
        return UNIT;
    }

    /**
     * @param UNIT the UNIT to set
     */
    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
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
     * @return the AMOUNT
     */
    public String getAMOUNT() {
        return AMOUNT;
    }

    /**
     * @param AMOUNT the AMOUNT to set
     */
    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }
}
