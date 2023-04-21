package com.core.pay.dta;

public class TA827 extends BaseRecord {

    private char[] referenceNr = new char[16];

    private char[] debitAccount = new char[24];

    private char[] amount;

    private char[] orderer1 = new char[24];

    private char[] orderer2 = new char[24];

    private char[] orderer3 = new char[24];

    private char[] orderer4 = new char[24];

    private char[] endBenef1 = new char[30];

    private char[] endBenef2 = new char[24];

    private char[] endBenef3 = new char[24];

    private char[] endBenef4 = new char[24];

    private char[] endBenef5 = new char[24];

    private char[] beneficiary1 = new char[30];

    private char[] beneficiary2 = new char[24];

    private char[] beneficiary3 = new char[24];

    private char[] beneficiary4 = new char[24];

    private char[] beneficiary5 = new char[24];

    private char[] paymentReason1 = new char[28];

    private char[] paymentReason2 = new char[28];

    private char[] paymentReason3 = new char[28];

    private char[] paymentReason4 = new char[28];

    public TA827() {
        super("827");
    }

    public char[] getAmount() {
        return this.padNumber(21, amount);
    }

    public void setAmount(char[] amount) {
        this.amount = amount;
    }

    public char[] getBeneficiary1() {
        return beneficiary1;
    }

    public void setBeneficiary1(char[] beneficiary1) {
        this.beneficiary1 = beneficiary1;
    }

    public char[] getBeneficiary2() {
        return beneficiary2;
    }

    public void setBeneficiary2(char[] beneficiary2) {
        this.beneficiary2 = beneficiary2;
    }

    public char[] getBeneficiary3() {
        return beneficiary3;
    }

    public void setBeneficiary3(char[] beneficiary3) {
        this.beneficiary3 = beneficiary3;
    }

    public char[] getBeneficiary4() {
        return beneficiary4;
    }

    public void setBeneficiary4(char[] beneficiary4) {
        this.beneficiary4 = beneficiary4;
    }

    public char[] getBeneficiary5() {
        return beneficiary5;
    }

    public void setBeneficiary5(char[] beneficiary5) {
        this.beneficiary5 = beneficiary5;
    }

    public char[] getEndBenef1() {
        return endBenef1;
    }

    public void setEndBenef1(char[] endBenef1) {
        this.endBenef1 = endBenef1;
    }

    public char[] getEndBenef2() {
        return endBenef2;
    }

    public void setEndBenef2(char[] endBenef2) {
        this.endBenef2 = endBenef2;
    }

    public char[] getEndBenef3() {
        return endBenef3;
    }

    public void setEndBenef3(char[] endBenef3) {
        this.endBenef3 = endBenef3;
    }

    public char[] getEndBenef4() {
        return endBenef4;
    }

    public void setEndBenef4(char[] endBenef4) {
        this.endBenef4 = endBenef4;
    }

    public char[] getEndBenef5() {
        return endBenef5;
    }

    public void setEndBenef5(char[] endBenef5) {
        this.endBenef5 = endBenef5;
    }

    public char[] getOrderer1() {
        return orderer1;
    }

    public void setOrderer1(char[] orderer1) {
        this.orderer1 = orderer1;
    }

    public char[] getOrderer2() {
        return orderer2;
    }

    public void setOrderer2(char[] orderer2) {
        this.orderer2 = orderer2;
    }

    public char[] getOrderer3() {
        return orderer3;
    }

    public void setOrderer3(char[] orderer3) {
        this.orderer3 = orderer3;
    }

    public char[] getOrderer4() {
        return orderer4;
    }

    public void setOrderer4(char[] orderer4) {
        this.orderer4 = orderer4;
    }

    public char[] getPaymentReason1() {
        return paymentReason1;
    }

    public void setPaymentReason1(char[] paymentReason1) {
        this.paymentReason1 = paymentReason1;
    }

    public char[] getPaymentReason2() {
        return paymentReason2;
    }

    public void setPaymentReason2(char[] paymentReason2) {
        this.paymentReason2 = paymentReason2;
    }

    public char[] getPaymentReason3() {
        return paymentReason3;
    }

    public void setPaymentReason3(char[] paymentReason3) {
        this.paymentReason3 = paymentReason3;
    }

    public char[] getPaymentReason4() {
        return paymentReason4;
    }

    public void setPaymentReason4(char[] paymentReason4) {
        this.paymentReason4 = paymentReason4;
    }

    private String getBeneficiary() {
        return toString(beneficiary1) + toString(beneficiary2) + toString(beneficiary3) + toString(beneficiary4) + toString(beneficiary5);
    }

    public char[] getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(char[] debitAccount) {
        this.debitAccount = debitAccount;
    }

    private String getEndBenef() {
        return toString(endBenef1) + toString(endBenef2) + toString(endBenef3) + toString(endBenef4) + toString(endBenef5);
    }

    private String getOrderer() {
        return toString(orderer1) + toString(orderer2) + toString(orderer3) + toString(orderer4);
    }

    private String getPaymentReason() {
        return toString(paymentReason1) + toString(paymentReason2) + toString(paymentReason3) + toString(paymentReason4);
    }

    public char[] getReferenceNr() {
        return referenceNr;
    }

    public void setReferenceNr(char[] referenceNr) {
        this.referenceNr = referenceNr;
    }

    public String toString() {
        return new String(segment01) + super.toString() + toString(referenceNr) + toString(debitAccount) + toString(amount) + new String(reserve14) + crlf + new String(segment02) + getOrderer() + new String(reserve30) + crlf + new String(segment03) + getBeneficiary() + crlf + new String(segment04) + getPaymentReason() + new String(reserve14) + crlf + new String(segment05) + getEndBenef();
    }

    public String toStringBank() {
        return new String(segment01) + super.toString() + toString(referenceNr) + toString(debitAccount) + toString(amount) + new String(reserve14) + crlf + new String(segment02) + getOrderer() + new String(reserve30) + crlf + new String(segment03) + getBeneficiary() + crlf + new String(segment04) + getPaymentReason() + new String(reserve14);
    }
}
