package com.nccsjz.pojo;

public class BatExportFhtb {

    private String txndate;

    private String acccode;

    private float txnamt;

    private float aftamt;

    private String unitName;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BatExportFhtb() {
    }

    public String getTxndate() {
        return txndate;
    }

    public void setTxndate(String txndate) {
        this.txndate = txndate;
    }

    public String getAcccode() {
        return acccode;
    }

    public void setAcccode(String acccode) {
        this.acccode = acccode;
    }

    public float getTxnamt() {
        return txnamt;
    }

    public void setTxnamt(float txnamt) {
        this.txnamt = txnamt;
    }

    public float getAftamt() {
        return aftamt;
    }

    public void setAftamt(float aftamt) {
        this.aftamt = aftamt;
    }
}
