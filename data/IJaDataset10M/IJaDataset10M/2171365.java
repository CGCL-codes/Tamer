package com.etc.report.beans;

import java.io.Serializable;

/**
 *
 * @author magicbank
 */
public class PurchaseRequestBeans implements Serializable {

    private static final long serialVersionUID = 2L;

    private String WORK;

    private String PROCODE;

    private String PRONAME;

    private String UNIT;

    private String QTY;

    private String REMARK;

    public PurchaseRequestBeans() {
    }

    public PurchaseRequestBeans(String WORK, String PROCODE, String PRONAME, String UNIT, String QTY, String REMARK) {
        this.WORK = WORK;
        this.PROCODE = PROCODE;
        this.PRONAME = PRONAME;
        this.UNIT = UNIT;
        this.QTY = QTY;
        this.REMARK = REMARK;
    }

    /**
     * @return the WORK
     */
    public String getWORK() {
        return WORK;
    }

    /**
     * @param WORK the WORK to set
     */
    public void setWORK(String WORK) {
        this.WORK = WORK;
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
     * @return the QTY
     */
    public String getQTY() {
        return QTY;
    }

    /**
     * @param QTY the QTY to set
     */
    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    /**
     * @return the REMARK
     */
    public String getREMARK() {
        return REMARK;
    }

    /**
     * @param REMARK the REMARK to set
     */
    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }
}
