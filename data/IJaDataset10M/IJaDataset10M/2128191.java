package com.koossery.adempiere.fe.beans.bank;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;

public class C_BankStatementLineBean extends KTADempiereBaseDTO {

    private static final long serialVersionUID = 1L;

    private int c_BankStatement_ID;

    private int c_BankStatementLine_ID;

    private int c_BPartner_ID;

    private int c_Charge_ID;

    private int c_Currency_ID;

    private int c_Invoice_ID;

    private int c_Payment_ID;

    private BigDecimal chargeAmt;

    private String createPayment;

    private Timestamp dateAcct;

    private String description;

    private BigDecimal eftAmt;

    private String eftCheckNo;

    private String eftCurrency;

    private String eftMemo;

    private String eftPayee;

    private String eftPayeeAccount;

    private String eftReference;

    private Timestamp eftStatementLineDate;

    private String eftTrxID;

    private String eftTrxType;

    private Timestamp eftValutaDate;

    private BigDecimal interestAmt;

    private int line;

    private String matchStatement;

    private String memo;

    private String referenceNo;

    private Timestamp statementLineDate;

    private BigDecimal stmtAmt;

    private BigDecimal trxAmt;

    private Timestamp valutaDate;

    private String isManual;

    private String isProcessed;

    private String isReversal;

    private String isActive;

    public int getC_BankStatement_ID() {
        return c_BankStatement_ID;
    }

    public void setC_BankStatement_ID(int c_BankStatement_ID) {
        this.c_BankStatement_ID = c_BankStatement_ID;
    }

    public int getC_BankStatementLine_ID() {
        return c_BankStatementLine_ID;
    }

    public void setC_BankStatementLine_ID(int c_BankStatementLine_ID) {
        this.c_BankStatementLine_ID = c_BankStatementLine_ID;
    }

    public int getC_BPartner_ID() {
        return c_BPartner_ID;
    }

    public void setC_BPartner_ID(int c_BPartner_ID) {
        this.c_BPartner_ID = c_BPartner_ID;
    }

    public int getC_Charge_ID() {
        return c_Charge_ID;
    }

    public void setC_Charge_ID(int c_Charge_ID) {
        this.c_Charge_ID = c_Charge_ID;
    }

    public int getC_Currency_ID() {
        return c_Currency_ID;
    }

    public void setC_Currency_ID(int c_Currency_ID) {
        this.c_Currency_ID = c_Currency_ID;
    }

    public int getC_Invoice_ID() {
        return c_Invoice_ID;
    }

    public void setC_Invoice_ID(int c_Invoice_ID) {
        this.c_Invoice_ID = c_Invoice_ID;
    }

    public int getC_Payment_ID() {
        return c_Payment_ID;
    }

    public void setC_Payment_ID(int c_Payment_ID) {
        this.c_Payment_ID = c_Payment_ID;
    }

    public BigDecimal getChargeAmt() {
        return chargeAmt;
    }

    public void setChargeAmt(BigDecimal chargeAmt) {
        this.chargeAmt = chargeAmt;
    }

    public String getCreatePayment() {
        return createPayment;
    }

    public void setCreatePayment(String createPayment) {
        this.createPayment = createPayment;
    }

    public Timestamp getDateAcct() {
        return dateAcct;
    }

    public void setDateAcct(Timestamp dateAcct) {
        this.dateAcct = dateAcct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getEftAmt() {
        return eftAmt;
    }

    public void setEftAmt(BigDecimal eftAmt) {
        this.eftAmt = eftAmt;
    }

    public String getEftCheckNo() {
        return eftCheckNo;
    }

    public void setEftCheckNo(String eftCheckNo) {
        this.eftCheckNo = eftCheckNo;
    }

    public String getEftCurrency() {
        return eftCurrency;
    }

    public void setEftCurrency(String eftCurrency) {
        this.eftCurrency = eftCurrency;
    }

    public String getEftMemo() {
        return eftMemo;
    }

    public void setEftMemo(String eftMemo) {
        this.eftMemo = eftMemo;
    }

    public String getEftPayee() {
        return eftPayee;
    }

    public void setEftPayee(String eftPayee) {
        this.eftPayee = eftPayee;
    }

    public String getEftPayeeAccount() {
        return eftPayeeAccount;
    }

    public void setEftPayeeAccount(String eftPayeeAccount) {
        this.eftPayeeAccount = eftPayeeAccount;
    }

    public String getEftReference() {
        return eftReference;
    }

    public void setEftReference(String eftReference) {
        this.eftReference = eftReference;
    }

    public Timestamp getEftStatementLineDate() {
        return eftStatementLineDate;
    }

    public void setEftStatementLineDate(Timestamp eftStatementLineDate) {
        this.eftStatementLineDate = eftStatementLineDate;
    }

    public String getEftTrxID() {
        return eftTrxID;
    }

    public void setEftTrxID(String eftTrxID) {
        this.eftTrxID = eftTrxID;
    }

    public String getEftTrxType() {
        return eftTrxType;
    }

    public void setEftTrxType(String eftTrxType) {
        this.eftTrxType = eftTrxType;
    }

    public Timestamp getEftValutaDate() {
        return eftValutaDate;
    }

    public void setEftValutaDate(Timestamp eftValutaDate) {
        this.eftValutaDate = eftValutaDate;
    }

    public BigDecimal getInterestAmt() {
        return interestAmt;
    }

    public void setInterestAmt(BigDecimal interestAmt) {
        this.interestAmt = interestAmt;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getMatchStatement() {
        return matchStatement;
    }

    public void setMatchStatement(String matchStatement) {
        this.matchStatement = matchStatement;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public Timestamp getStatementLineDate() {
        return statementLineDate;
    }

    public void setStatementLineDate(Timestamp statementLineDate) {
        this.statementLineDate = statementLineDate;
    }

    public BigDecimal getStmtAmt() {
        return stmtAmt;
    }

    public void setStmtAmt(BigDecimal stmtAmt) {
        this.stmtAmt = stmtAmt;
    }

    public BigDecimal getTrxAmt() {
        return trxAmt;
    }

    public void setTrxAmt(BigDecimal trxAmt) {
        this.trxAmt = trxAmt;
    }

    public Timestamp getValutaDate() {
        return valutaDate;
    }

    public void setValutaDate(Timestamp valutaDate) {
        this.valutaDate = valutaDate;
    }

    public String getIsManual() {
        return isManual;
    }

    public void setIsManual(String isManual) {
        this.isManual = isManual;
    }

    public String getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(String isProcessed) {
        this.isProcessed = isProcessed;
    }

    public String getIsReversal() {
        return isReversal;
    }

    public void setIsReversal(String isReversal) {
        this.isReversal = isReversal;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
