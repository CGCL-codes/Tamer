package com.etc.report.beans;

/**
 *
 * @author Administrator
 */
public class ReportYearlySalesProfitBeans {

    private static final long serialVersionUID = 2L;

    private String INVOICE_CODE;

    private String PROCODE;

    private String PRONAME;

    private String QTY;

    private String PRICE;

    private String SALE_AMOUNT;

    private String COST;

    private String COST_AMOUNT;

    private String MARGIN;

    private String MRG;

    private String CUSTOMER_CODE;

    private String CUSTOMER_NAME;

    private String TCODE;

    private String TNAME;

    private String TOTAL_QTY;

    private String TOTAL_SALES_AMOUNT;

    private String TOTAL_COST_AMOUNT;

    private String TOTAL_MARGIN;

    public ReportYearlySalesProfitBeans() {
    }

    public ReportYearlySalesProfitBeans(String IVCODE, String PROCODE, String PRONAME, String QTY, String PRICE, String SALE_AMOUNT, String COST, String COST_AMOUNT, String MARGIN, String MRG, String CUSTOMER_CODE, String CUSTOMER_NAME, String TCODE, String TNAME, String TOTAL_QTY, String TOTAL_SALES_AMOUNT, String TOTAL_COST_AMOUNT, String TOTAL_MARGIN) {
        this.INVOICE_CODE = IVCODE;
        this.PROCODE = PROCODE;
        this.PRONAME = PRONAME;
        this.QTY = QTY;
        this.PRICE = PRICE;
        this.SALE_AMOUNT = SALE_AMOUNT;
        this.COST = COST;
        this.COST_AMOUNT = COST_AMOUNT;
        this.MARGIN = MARGIN;
        this.MRG = MRG;
        this.CUSTOMER_CODE = CUSTOMER_CODE;
        this.CUSTOMER_NAME = CUSTOMER_NAME;
        this.TCODE = TCODE;
        this.TNAME = TNAME;
        this.TOTAL_QTY = TOTAL_QTY;
        this.TOTAL_SALES_AMOUNT = TOTAL_SALES_AMOUNT;
        this.TOTAL_COST_AMOUNT = TOTAL_COST_AMOUNT;
        this.TOTAL_MARGIN = TOTAL_MARGIN;
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

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getSALE_AMOUNT() {
        return SALE_AMOUNT;
    }

    public void setSALE_AMOUNT(String SALE_AMOUNT) {
        this.SALE_AMOUNT = SALE_AMOUNT;
    }

    public String getCOST() {
        return COST;
    }

    public void setCOST(String COST) {
        this.COST = COST;
    }

    public String getCOST_AMOUNT() {
        return COST_AMOUNT;
    }

    public void setCOST_AMOUNT(String COST_AMOUNT) {
        this.COST_AMOUNT = COST_AMOUNT;
    }

    public String getMARGIN() {
        return MARGIN;
    }

    public void setMARGIN(String MARGIN) {
        this.MARGIN = MARGIN;
    }

    public String getMRG() {
        return MRG;
    }

    public void setMRG(String MRG) {
        this.MRG = MRG;
    }

    public String getCUSTOMER_CODE() {
        return CUSTOMER_CODE;
    }

    public void setCUSTOMER_CODE(String CUSTOMER_CODE) {
        this.CUSTOMER_CODE = CUSTOMER_CODE;
    }

    public String getCUSTOMER_NAME() {
        return CUSTOMER_NAME;
    }

    public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
        this.CUSTOMER_NAME = CUSTOMER_NAME;
    }

    public String getTCODE() {
        return TCODE;
    }

    public void setTCODE(String TCODE) {
        this.TCODE = TCODE;
    }

    public String getTNAME() {
        return TNAME;
    }

    public void setTNAME(String TNAME) {
        this.TNAME = TNAME;
    }

    public String getTOTAL_QTY() {
        return TOTAL_QTY;
    }

    public void setTOTAL_QTY(String TOTAL_QTY) {
        this.TOTAL_QTY = TOTAL_QTY;
    }

    public String getTOTAL_SALES_AMOUNT() {
        return TOTAL_SALES_AMOUNT;
    }

    public void setTOTAL_SALES_AMOUNT(String TOTAL_SALES_AMOUNT) {
        this.TOTAL_SALES_AMOUNT = TOTAL_SALES_AMOUNT;
    }

    public String getTOTAL_COST_AMOUNT() {
        return TOTAL_COST_AMOUNT;
    }

    public void setTOTAL_COST_AMOUNT(String TOTAL_COST_AMOUNT) {
        this.TOTAL_COST_AMOUNT = TOTAL_COST_AMOUNT;
    }

    public String getTOTAL_MARGIN() {
        return TOTAL_MARGIN;
    }

    public void setTOTAL_MARGIN(String TOTAL_MARGIN) {
        this.TOTAL_MARGIN = TOTAL_MARGIN;
    }

    public String getINVOICE_CODE() {
        return INVOICE_CODE;
    }

    public void setINVOICE_CODE(String INVOICE_CODE) {
        this.INVOICE_CODE = INVOICE_CODE;
    }
}
