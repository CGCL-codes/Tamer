package com.etc.report.beans;

/**
 *
 * @author horizonx
 */
public class StockBalanceBeans {

    private static final long serialVersionUID = 2L;

    private String PACODE;

    private String PANAME;

    private String TYPE;

    private String PROCODE;

    private String PRONAME;

    private String UNIT;

    private String POCODE;

    private String PNCODE;

    private String QTY;

    private String COST;

    private String AMOUNT;

    private String PRICE;

    public StockBalanceBeans() {
    }

    public StockBalanceBeans(String PACODE, String PANAME, String TYPE, String PROCODE, String PRONAME, String UNIT, String POCODE, String PNCODE, String QTY, String COST, String AMOUNT, String PRICE) {
        this.PACODE = PACODE;
        this.PANAME = PANAME;
        this.TYPE = TYPE;
        this.PROCODE = PROCODE;
        this.PRONAME = PRONAME;
        this.UNIT = UNIT;
        this.POCODE = POCODE;
        this.PNCODE = PNCODE;
        this.QTY = QTY;
        this.COST = COST;
        this.AMOUNT = AMOUNT;
        this.PRICE = PRICE;
    }

    public String getPACODE() {
        return PACODE;
    }

    public void setPACODE(String PACODE) {
        this.PACODE = PACODE;
    }

    public String getPANAME() {
        return PANAME;
    }

    public void setPANAME(String PANAME) {
        this.PANAME = PANAME;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getPROCODE() {
        return PROCODE;
    }

    public void setPROCODE(String PROCODE) {
        this.PROCODE = PROCODE;
    }

    public String getPRONAME() {
        return PRONAME;
    }

    public void setPRONAME(String PRONAME) {
        this.PRONAME = PRONAME;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getPOCODE() {
        return POCODE;
    }

    public void setPOCODE(String POCODE) {
        this.POCODE = POCODE;
    }

    public String getPNCODE() {
        return PNCODE;
    }

    public void setPNCODE(String PNCODE) {
        this.PNCODE = PNCODE;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getCOST() {
        return COST;
    }

    public void setCOST(String COST) {
        this.COST = COST;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }
}
