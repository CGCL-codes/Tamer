package org.koossery.adempiere.core.contract.dto.payment;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;
import org.koossery.adempiere.core.contract.itf.payment.IC_PaymentDTO;

public class C_PaymentDTO extends KTADempiereBaseDTO implements IC_PaymentDTO {

    private static final long serialVersionUID = 1L;

    private String a_City;

    private String a_Country;

    private String a_EMail;

    private String a_Ident_DL;

    private String a_Ident_SSN;

    private String a_Name;

    private String a_State;

    private String a_Street;

    private String a_Zip;

    private String accountNo;

    private int ad_OrgTrx_ID;

    private int c_Activity_ID;

    private int c_BankAccount_ID;

    private int c_BP_BankAccount_ID;

    private int c_BPartner_ID;

    private int c_Campaign_ID;

    private int c_Charge_ID;

    private int c_ConversionType_ID;

    private int c_Currency_ID;

    private int c_DocType_ID;

    private int c_Invoice_ID;

    private int c_Order_ID;

    private int c_Payment_ID;

    private int c_PaymentBatch_ID;

    private int c_Project_ID;

    private BigDecimal chargeAmt;

    private String checkNo;

    private int creditCardExpMM;

    private int creditCardExpYY;

    private String creditCardNumber;

    private String creditCardType;

    private String creditCardVV;

    private Timestamp dateAcct;

    private Timestamp dateTrx;

    private String description;

    private BigDecimal discountAmt;

    private String docAction;

    private String docStatus;

    private String documentNo;

    private String micr;

    private String oprocessing;

    private String orig_TrxID;

    private BigDecimal overUnderAmt;

    private BigDecimal payAmt;

    private String poNum;

    private String r_AuthCode;

    private String r_AuthCode_DC;

    private String r_AvsAddr;

    private String r_AvsZip;

    private String r_Info;

    private String r_PnRef;

    private String r_PnRef_DC;

    private String r_RespMsg;

    private String r_Result;

    private int ref_Payment_ID;

    private String routingNo;

    private String swipe;

    private BigDecimal taxAmt;

    private String tenderType;

    private String trxType;

    private int user1_ID;

    private int user2_ID;

    private String voiceAuthCode;

    private BigDecimal writeOffAmt;

    private String isAllocated;

    private String isApproved;

    private String isDelayedCapture;

    private String isOnline;

    private String isOverUnderPayment;

    private String isPosted;

    private String isPrepayment;

    private String isProcessed;

    private String isProcessing;

    private String isR_CVV2Match;

    private String isReceipt;

    private String isReconciled;

    private String isSelfService;

    private String isActive;

    public String getA_City() {
        return a_City;
    }

    public void setA_City(String a_City) {
        this.a_City = a_City;
    }

    public String getA_Country() {
        return a_Country;
    }

    public void setA_Country(String a_Country) {
        this.a_Country = a_Country;
    }

    public String getA_EMail() {
        return a_EMail;
    }

    public void setA_EMail(String a_EMail) {
        this.a_EMail = a_EMail;
    }

    public String getA_Ident_DL() {
        return a_Ident_DL;
    }

    public void setA_Ident_DL(String a_Ident_DL) {
        this.a_Ident_DL = a_Ident_DL;
    }

    public String getA_Ident_SSN() {
        return a_Ident_SSN;
    }

    public void setA_Ident_SSN(String a_Ident_SSN) {
        this.a_Ident_SSN = a_Ident_SSN;
    }

    public String getA_Name() {
        return a_Name;
    }

    public void setA_Name(String a_Name) {
        this.a_Name = a_Name;
    }

    public String getA_State() {
        return a_State;
    }

    public void setA_State(String a_State) {
        this.a_State = a_State;
    }

    public String getA_Street() {
        return a_Street;
    }

    public void setA_Street(String a_Street) {
        this.a_Street = a_Street;
    }

    public String getA_Zip() {
        return a_Zip;
    }

    public void setA_Zip(String a_Zip) {
        this.a_Zip = a_Zip;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public int getAd_OrgTrx_ID() {
        return ad_OrgTrx_ID;
    }

    public void setAd_OrgTrx_ID(int ad_OrgTrx_ID) {
        this.ad_OrgTrx_ID = ad_OrgTrx_ID;
    }

    public int getC_Activity_ID() {
        return c_Activity_ID;
    }

    public void setC_Activity_ID(int c_Activity_ID) {
        this.c_Activity_ID = c_Activity_ID;
    }

    public int getC_BankAccount_ID() {
        return c_BankAccount_ID;
    }

    public void setC_BankAccount_ID(int c_BankAccount_ID) {
        this.c_BankAccount_ID = c_BankAccount_ID;
    }

    public int getC_BP_BankAccount_ID() {
        return c_BP_BankAccount_ID;
    }

    public void setC_BP_BankAccount_ID(int c_BP_BankAccount_ID) {
        this.c_BP_BankAccount_ID = c_BP_BankAccount_ID;
    }

    public int getC_BPartner_ID() {
        return c_BPartner_ID;
    }

    public void setC_BPartner_ID(int c_BPartner_ID) {
        this.c_BPartner_ID = c_BPartner_ID;
    }

    public int getC_Campaign_ID() {
        return c_Campaign_ID;
    }

    public void setC_Campaign_ID(int c_Campaign_ID) {
        this.c_Campaign_ID = c_Campaign_ID;
    }

    public int getC_Charge_ID() {
        return c_Charge_ID;
    }

    public void setC_Charge_ID(int c_Charge_ID) {
        this.c_Charge_ID = c_Charge_ID;
    }

    public int getC_ConversionType_ID() {
        return c_ConversionType_ID;
    }

    public void setC_ConversionType_ID(int c_ConversionType_ID) {
        this.c_ConversionType_ID = c_ConversionType_ID;
    }

    public int getC_Currency_ID() {
        return c_Currency_ID;
    }

    public void setC_Currency_ID(int c_Currency_ID) {
        this.c_Currency_ID = c_Currency_ID;
    }

    public int getC_DocType_ID() {
        return c_DocType_ID;
    }

    public void setC_DocType_ID(int c_DocType_ID) {
        this.c_DocType_ID = c_DocType_ID;
    }

    public int getC_Invoice_ID() {
        return c_Invoice_ID;
    }

    public void setC_Invoice_ID(int c_Invoice_ID) {
        this.c_Invoice_ID = c_Invoice_ID;
    }

    public int getC_Order_ID() {
        return c_Order_ID;
    }

    public void setC_Order_ID(int c_Order_ID) {
        this.c_Order_ID = c_Order_ID;
    }

    public int getC_Payment_ID() {
        return c_Payment_ID;
    }

    public void setC_Payment_ID(int c_Payment_ID) {
        this.c_Payment_ID = c_Payment_ID;
    }

    public int getC_PaymentBatch_ID() {
        return c_PaymentBatch_ID;
    }

    public void setC_PaymentBatch_ID(int c_PaymentBatch_ID) {
        this.c_PaymentBatch_ID = c_PaymentBatch_ID;
    }

    public int getC_Project_ID() {
        return c_Project_ID;
    }

    public void setC_Project_ID(int c_Project_ID) {
        this.c_Project_ID = c_Project_ID;
    }

    public BigDecimal getChargeAmt() {
        return chargeAmt;
    }

    public void setChargeAmt(BigDecimal chargeAmt) {
        this.chargeAmt = chargeAmt;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public int getCreditCardExpMM() {
        return creditCardExpMM;
    }

    public void setCreditCardExpMM(int creditCardExpMM) {
        this.creditCardExpMM = creditCardExpMM;
    }

    public int getCreditCardExpYY() {
        return creditCardExpYY;
    }

    public void setCreditCardExpYY(int creditCardExpYY) {
        this.creditCardExpYY = creditCardExpYY;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }

    public String getCreditCardVV() {
        return creditCardVV;
    }

    public void setCreditCardVV(String creditCardVV) {
        this.creditCardVV = creditCardVV;
    }

    public Timestamp getDateAcct() {
        return dateAcct;
    }

    public void setDateAcct(Timestamp dateAcct) {
        this.dateAcct = dateAcct;
    }

    public Timestamp getDateTrx() {
        return dateTrx;
    }

    public void setDateTrx(Timestamp dateTrx) {
        this.dateTrx = dateTrx;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(BigDecimal discountAmt) {
        this.discountAmt = discountAmt;
    }

    public String getDocAction() {
        return docAction;
    }

    public void setDocAction(String docAction) {
        this.docAction = docAction;
    }

    public String getDocStatus() {
        return docStatus;
    }

    public void setDocStatus(String docStatus) {
        this.docStatus = docStatus;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getMicr() {
        return micr;
    }

    public void setMicr(String micr) {
        this.micr = micr;
    }

    public String getOprocessing() {
        return oprocessing;
    }

    public void setOprocessing(String oprocessing) {
        this.oprocessing = oprocessing;
    }

    public String getOrig_TrxID() {
        return orig_TrxID;
    }

    public void setOrig_TrxID(String orig_TrxID) {
        this.orig_TrxID = orig_TrxID;
    }

    public BigDecimal getOverUnderAmt() {
        return overUnderAmt;
    }

    public void setOverUnderAmt(BigDecimal overUnderAmt) {
        this.overUnderAmt = overUnderAmt;
    }

    public BigDecimal getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(BigDecimal payAmt) {
        this.payAmt = payAmt;
    }

    public String getPoNum() {
        return poNum;
    }

    public void setPoNum(String poNum) {
        this.poNum = poNum;
    }

    public String getR_AuthCode() {
        return r_AuthCode;
    }

    public void setR_AuthCode(String r_AuthCode) {
        this.r_AuthCode = r_AuthCode;
    }

    public String getR_AuthCode_DC() {
        return r_AuthCode_DC;
    }

    public void setR_AuthCode_DC(String r_AuthCode_DC) {
        this.r_AuthCode_DC = r_AuthCode_DC;
    }

    public String getR_AvsAddr() {
        return r_AvsAddr;
    }

    public void setR_AvsAddr(String r_AvsAddr) {
        this.r_AvsAddr = r_AvsAddr;
    }

    public String getR_AvsZip() {
        return r_AvsZip;
    }

    public void setR_AvsZip(String r_AvsZip) {
        this.r_AvsZip = r_AvsZip;
    }

    public String getR_Info() {
        return r_Info;
    }

    public void setR_Info(String r_Info) {
        this.r_Info = r_Info;
    }

    public String getR_PnRef() {
        return r_PnRef;
    }

    public void setR_PnRef(String r_PnRef) {
        this.r_PnRef = r_PnRef;
    }

    public String getR_PnRef_DC() {
        return r_PnRef_DC;
    }

    public void setR_PnRef_DC(String r_PnRef_DC) {
        this.r_PnRef_DC = r_PnRef_DC;
    }

    public String getR_RespMsg() {
        return r_RespMsg;
    }

    public void setR_RespMsg(String r_RespMsg) {
        this.r_RespMsg = r_RespMsg;
    }

    public String getR_Result() {
        return r_Result;
    }

    public void setR_Result(String r_Result) {
        this.r_Result = r_Result;
    }

    public int getRef_Payment_ID() {
        return ref_Payment_ID;
    }

    public void setRef_Payment_ID(int ref_Payment_ID) {
        this.ref_Payment_ID = ref_Payment_ID;
    }

    public String getRoutingNo() {
        return routingNo;
    }

    public void setRoutingNo(String routingNo) {
        this.routingNo = routingNo;
    }

    public String getSwipe() {
        return swipe;
    }

    public void setSwipe(String swipe) {
        this.swipe = swipe;
    }

    public BigDecimal getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
    }

    public String getTenderType() {
        return tenderType;
    }

    public void setTenderType(String tenderType) {
        this.tenderType = tenderType;
    }

    public String getTrxType() {
        return trxType;
    }

    public void setTrxType(String trxType) {
        this.trxType = trxType;
    }

    public int getUser1_ID() {
        return user1_ID;
    }

    public void setUser1_ID(int user1_ID) {
        this.user1_ID = user1_ID;
    }

    public int getUser2_ID() {
        return user2_ID;
    }

    public void setUser2_ID(int user2_ID) {
        this.user2_ID = user2_ID;
    }

    public String getVoiceAuthCode() {
        return voiceAuthCode;
    }

    public void setVoiceAuthCode(String voiceAuthCode) {
        this.voiceAuthCode = voiceAuthCode;
    }

    public BigDecimal getWriteOffAmt() {
        return writeOffAmt;
    }

    public void setWriteOffAmt(BigDecimal writeOffAmt) {
        this.writeOffAmt = writeOffAmt;
    }

    public String getIsAllocated() {
        return isAllocated;
    }

    public void setIsAllocated(String isAllocated) {
        this.isAllocated = isAllocated;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getIsDelayedCapture() {
        return isDelayedCapture;
    }

    public void setIsDelayedCapture(String isDelayedCapture) {
        this.isDelayedCapture = isDelayedCapture;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getIsOverUnderPayment() {
        return isOverUnderPayment;
    }

    public void setIsOverUnderPayment(String isOverUnderPayment) {
        this.isOverUnderPayment = isOverUnderPayment;
    }

    public String getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(String isPosted) {
        this.isPosted = isPosted;
    }

    public String getIsPrepayment() {
        return isPrepayment;
    }

    public void setIsPrepayment(String isPrepayment) {
        this.isPrepayment = isPrepayment;
    }

    public String getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(String isProcessed) {
        this.isProcessed = isProcessed;
    }

    public String getIsProcessing() {
        return isProcessing;
    }

    public void setIsProcessing(String isProcessing) {
        this.isProcessing = isProcessing;
    }

    public String getIsR_CVV2Match() {
        return isR_CVV2Match;
    }

    public void setIsR_CVV2Match(String isR_CVV2Match) {
        this.isR_CVV2Match = isR_CVV2Match;
    }

    public String getIsReceipt() {
        return isReceipt;
    }

    public void setIsReceipt(String isReceipt) {
        this.isReceipt = isReceipt;
    }

    public String getIsReconciled() {
        return isReconciled;
    }

    public void setIsReconciled(String isReconciled) {
        this.isReconciled = isReconciled;
    }

    public String getIsSelfService() {
        return isSelfService;
    }

    public void setIsSelfService(String isSelfService) {
        this.isSelfService = isSelfService;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
