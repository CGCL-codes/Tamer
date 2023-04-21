package net.sourceforge.cath.context;

import org.aspectj.lang.Signature;

public class LoggingUnit {

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getStartCpuTime() {
        return startCpuTime;
    }

    public void setStartCpuTime(long startCpuTime) {
        this.startCpuTime = startCpuTime;
    }

    public long getEndCpuTime() {
        return endCpuTime;
    }

    public void setEndCpuTime(long endCpuTime) {
        this.endCpuTime = endCpuTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    long startTime;

    long endTime;

    long startCpuTime;

    long endCpuTime;

    String transactionId;

    String localTransactionId;

    public String getLocalTransactionId() {
        return localTransactionId;
    }

    public void setLocalTransactionId(String localTransactionId) {
        this.localTransactionId = localTransactionId;
    }

    Signature signature;

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    String note = "";

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
