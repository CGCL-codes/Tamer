package org.skirmishgame.financewizard.bo;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class Transaction implements Serializable {

    private static final long serialVersionUID = 482938745015351867L;

    private double value;

    private int sourceMoneyStoreId;

    private int targetMoneyStoreId;

    private GregorianCalendar transactDate;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getSourceMoneyStoreId() {
        return sourceMoneyStoreId;
    }

    public void setSourceMoneyStoreId(int sourceMoneyStoreId) {
        this.sourceMoneyStoreId = sourceMoneyStoreId;
    }

    public int getTargetMoneyStoreId() {
        return targetMoneyStoreId;
    }

    public void setTargetMoneyStoreId(int targetMoneyStoreId) {
        this.targetMoneyStoreId = targetMoneyStoreId;
    }

    public GregorianCalendar getTransactDate() {
        return transactDate;
    }

    public void setTransactDate(GregorianCalendar transactDate) {
        this.transactDate = transactDate;
    }
}
