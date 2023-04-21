package com.empower.model;

import java.util.Date;

/**
 * @author Alok Ranjan
 *
 */
public class MoneyTransferModel {

    private Date transferDate;

    private float transferAmt;

    private String fromTransactionWith;

    private String fromAccountId;

    private Integer fromAccountTypeId;

    private String toTransactionWith;

    private String toAccountId;

    private Integer toAccountTypeId;

    private String memo;

    /**
	 * 
	 */
    public MoneyTransferModel() {
    }

    /**
	 * @return the transferDate
	 */
    public Date getTransferDate() {
        return transferDate;
    }

    /**
	 * @param transferDate the transferDate to set
	 */
    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    /**
	 * @return the transferAmt
	 */
    public float getTransferAmt() {
        return transferAmt;
    }

    /**
	 * @param transferAmt the transferAmt to set
	 */
    public void setTransferAmt(float transferAmt) {
        this.transferAmt = transferAmt;
    }

    /**
	 * @return the fromAccountId
	 */
    public String getFromAccountId() {
        return fromAccountId;
    }

    /**
	 * @param fromAccountId the fromAccountId to set
	 */
    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    /**
	 * @return the fromAccountTypeId
	 */
    public Integer getFromAccountTypeId() {
        return fromAccountTypeId;
    }

    /**
	 * @param fromAccountTypeId the fromAccountTypeId to set
	 */
    public void setFromAccountTypeId(Integer fromAccountTypeId) {
        this.fromAccountTypeId = fromAccountTypeId;
    }

    /**
	 * @return the toAccountId
	 */
    public String getToAccountId() {
        return toAccountId;
    }

    /**
	 * @param toAccountId the toAccountId to set
	 */
    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    /**
	 * @return the toAccountTypeId
	 */
    public Integer getToAccountTypeId() {
        return toAccountTypeId;
    }

    /**
	 * @param toAccountTypeId the toAccountTypeId to set
	 */
    public void setToAccountTypeId(Integer toAccountTypeId) {
        this.toAccountTypeId = toAccountTypeId;
    }

    /**
	 * @return the memo
	 */
    public String getMemo() {
        return memo;
    }

    /**
	 * @param memo the memo to set
	 */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
	 * @return the fromTransactionWith
	 */
    public String getFromTransactionWith() {
        return fromTransactionWith;
    }

    /**
	 * @param fromTransactionWith the fromTransactionWith to set
	 */
    public void setFromTransactionWith(String fromTransactionWith) {
        this.fromTransactionWith = fromTransactionWith;
    }

    /**
	 * @return the toTransactionWith
	 */
    public String getToTransactionWith() {
        return toTransactionWith;
    }

    /**
	 * @param toTransactionWith the toTransactionWith to set
	 */
    public void setToTransactionWith(String toTransactionWith) {
        this.toTransactionWith = toTransactionWith;
    }
}
