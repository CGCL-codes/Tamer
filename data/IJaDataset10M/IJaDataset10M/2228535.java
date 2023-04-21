package com.schinzer.fin.basic;

public class RefundPayment {

    private Refund refund;

    private RefundableTransaction transaction;

    private float refundAmount;

    private String status;

    public RefundPayment(Refund ref, RefundableTransaction txn) {
        refund = ref;
        transaction = txn;
    }

    public float getAggregatedAmount() {
        return 0;
    }

    /**
	 * @return the refundAmount
	 */
    public float getRefundAmount() {
        return refundAmount;
    }

    /**
	 * @param refundAmount the refundAmount to set
	 */
    public void setRefundAmount(float refundAmount) {
        this.refundAmount = refundAmount;
    }

    /**
	 * @return the status
	 */
    public String getStatus() {
        return status;
    }

    /**
	 * @param status the status to set
	 */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
	 * @return the refund
	 */
    public Refund getRefund() {
        return refund;
    }

    /**
	 * @param transaction the transaction to set
	 */
    public void setTransaction(RefundableTransaction transaction) {
        this.transaction = transaction;
    }

    /**
	 * @param refund the refund to set
	 */
    public void setRefund(Refund refund) {
        this.refund = refund;
    }

    /**
	 * @return the transaction
	 */
    public RefundableTransaction getTransaction() {
        return transaction;
    }
}
