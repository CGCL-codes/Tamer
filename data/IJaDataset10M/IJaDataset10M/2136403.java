package com.centraview.account.payment;

import java.io.Serializable;

public class PaymentPK implements Serializable {

    private int paymentID;

    private String dataSource;

    public int hashCode() {
        String aggregate = this.paymentID + this.dataSource;
        return (aggregate.hashCode());
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof PaymentPK)) return false; else if ((((PaymentPK) obj).getId() == this.paymentID) && ((PaymentPK) obj).getDataSource() == this.dataSource) return true; else return false;
    }

    public java.lang.String toString() {
        return ("PaymentPK: PaymentID: " + this.paymentID + ", dataSource: " + this.dataSource);
    }

    public PaymentPK(int pkId, String ds) {
        this.paymentID = pkId;
        this.dataSource = ds;
    }

    public int getId() {
        return this.paymentID;
    }

    public String getDataSource() {
        return this.dataSource;
    }

    public PaymentPK() {
    }
}
