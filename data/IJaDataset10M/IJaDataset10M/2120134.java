package org.koossery.adempiere.core.contract.dto.invoice;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;
import org.koossery.adempiere.core.contract.itf.invoice.IC_InvoiceDTO;

public class C_InvoiceDTO extends KTADempiereBaseDTO implements IC_InvoiceDTO {

    private static final long serialVersionUID = 1L;

    private int ad_OrgTrx_ID;

    private int ad_User_ID;

    private int c_Activity_ID;

    private int c_BPartner_ID;

    private int c_BPartner_Location_ID;

    private int c_Campaign_ID;

    private int c_CashLine_ID;

    private int c_Charge_ID;

    private int c_ConversionType_ID;

    private int c_Currency_ID;

    private int c_DocType_ID;

    private int c_DocTypeTarget_ID;

    private int c_DunningLevel_ID;

    private int c_Invoice_ID;

    private int c_Order_ID;

    private int c_Payment_ID;

    private int c_PaymentTerm_ID;

    private int c_Project_ID;

    private BigDecimal chargeAmt;

    private String copyFrom;

    private String createFrom;

    private Timestamp dateAcct;

    private Timestamp dateInvoiced;

    private Timestamp dateOrdered;

    private Timestamp datePrinted;

    private String description;

    private String docAction;

    private String docStatus;

    private String documentNo;

    private Timestamp dunningGrace;

    private String generateTo;

    private BigDecimal grandTotal;

    private String invoiceCollectionType;

    private int m_PriceList_ID;

    private int m_RMA_ID;

    private String paymentRule;

    private String poReference;

    private int ref_Invoice_ID;

    private int salesRep_ID;

    private BigDecimal totalLines;

    private int user1_ID;

    private int user2_ID;

    private String isApproved;

    private String isDiscountPrinted;

    private String isInDispute;

    private String isPaid;

    private String isPayScheduleValid;

    private String isPosted;

    private String isPrinted;

    private String isProcessed;

    private String isProcessing;

    private String isSelfService;

    private String isSendEMail;

    private String isSOTrx;

    private String isTaxIncluded;

    private String isTransferred;

    private String isActive;

    public int getAd_OrgTrx_ID() {
        return ad_OrgTrx_ID;
    }

    public void setAd_OrgTrx_ID(int ad_OrgTrx_ID) {
        this.ad_OrgTrx_ID = ad_OrgTrx_ID;
    }

    public int getAd_User_ID() {
        return ad_User_ID;
    }

    public void setAd_User_ID(int ad_User_ID) {
        this.ad_User_ID = ad_User_ID;
    }

    public int getC_Activity_ID() {
        return c_Activity_ID;
    }

    public void setC_Activity_ID(int c_Activity_ID) {
        this.c_Activity_ID = c_Activity_ID;
    }

    public int getC_BPartner_ID() {
        return c_BPartner_ID;
    }

    public void setC_BPartner_ID(int c_BPartner_ID) {
        this.c_BPartner_ID = c_BPartner_ID;
    }

    public int getC_BPartner_Location_ID() {
        return c_BPartner_Location_ID;
    }

    public void setC_BPartner_Location_ID(int c_BPartner_Location_ID) {
        this.c_BPartner_Location_ID = c_BPartner_Location_ID;
    }

    public int getC_Campaign_ID() {
        return c_Campaign_ID;
    }

    public void setC_Campaign_ID(int c_Campaign_ID) {
        this.c_Campaign_ID = c_Campaign_ID;
    }

    public int getC_CashLine_ID() {
        return c_CashLine_ID;
    }

    public void setC_CashLine_ID(int c_CashLine_ID) {
        this.c_CashLine_ID = c_CashLine_ID;
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

    public int getC_DocTypeTarget_ID() {
        return c_DocTypeTarget_ID;
    }

    public void setC_DocTypeTarget_ID(int c_DocTypeTarget_ID) {
        this.c_DocTypeTarget_ID = c_DocTypeTarget_ID;
    }

    public int getC_DunningLevel_ID() {
        return c_DunningLevel_ID;
    }

    public void setC_DunningLevel_ID(int c_DunningLevel_ID) {
        this.c_DunningLevel_ID = c_DunningLevel_ID;
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

    public int getC_PaymentTerm_ID() {
        return c_PaymentTerm_ID;
    }

    public void setC_PaymentTerm_ID(int c_PaymentTerm_ID) {
        this.c_PaymentTerm_ID = c_PaymentTerm_ID;
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

    public String getCopyFrom() {
        return copyFrom;
    }

    public void setCopyFrom(String copyFrom) {
        this.copyFrom = copyFrom;
    }

    public String getCreateFrom() {
        return createFrom;
    }

    public void setCreateFrom(String createFrom) {
        this.createFrom = createFrom;
    }

    public Timestamp getDateAcct() {
        return dateAcct;
    }

    public void setDateAcct(Timestamp dateAcct) {
        this.dateAcct = dateAcct;
    }

    public Timestamp getDateInvoiced() {
        return dateInvoiced;
    }

    public void setDateInvoiced(Timestamp dateInvoiced) {
        this.dateInvoiced = dateInvoiced;
    }

    public Timestamp getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(Timestamp dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public Timestamp getDatePrinted() {
        return datePrinted;
    }

    public void setDatePrinted(Timestamp datePrinted) {
        this.datePrinted = datePrinted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Timestamp getDunningGrace() {
        return dunningGrace;
    }

    public void setDunningGrace(Timestamp dunningGrace) {
        this.dunningGrace = dunningGrace;
    }

    public String getGenerateTo() {
        return generateTo;
    }

    public void setGenerateTo(String generateTo) {
        this.generateTo = generateTo;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getInvoiceCollectionType() {
        return invoiceCollectionType;
    }

    public void setInvoiceCollectionType(String invoiceCollectionType) {
        this.invoiceCollectionType = invoiceCollectionType;
    }

    public int getM_PriceList_ID() {
        return m_PriceList_ID;
    }

    public void setM_PriceList_ID(int m_PriceList_ID) {
        this.m_PriceList_ID = m_PriceList_ID;
    }

    public int getM_RMA_ID() {
        return m_RMA_ID;
    }

    public void setM_RMA_ID(int m_RMA_ID) {
        this.m_RMA_ID = m_RMA_ID;
    }

    public String getPaymentRule() {
        return paymentRule;
    }

    public void setPaymentRule(String paymentRule) {
        this.paymentRule = paymentRule;
    }

    public String getPoReference() {
        return poReference;
    }

    public void setPoReference(String poReference) {
        this.poReference = poReference;
    }

    public int getRef_Invoice_ID() {
        return ref_Invoice_ID;
    }

    public void setRef_Invoice_ID(int ref_Invoice_ID) {
        this.ref_Invoice_ID = ref_Invoice_ID;
    }

    public int getSalesRep_ID() {
        return salesRep_ID;
    }

    public void setSalesRep_ID(int salesRep_ID) {
        this.salesRep_ID = salesRep_ID;
    }

    public BigDecimal getTotalLines() {
        return totalLines;
    }

    public void setTotalLines(BigDecimal totalLines) {
        this.totalLines = totalLines;
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

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getIsDiscountPrinted() {
        return isDiscountPrinted;
    }

    public void setIsDiscountPrinted(String isDiscountPrinted) {
        this.isDiscountPrinted = isDiscountPrinted;
    }

    public String getIsInDispute() {
        return isInDispute;
    }

    public void setIsInDispute(String isInDispute) {
        this.isInDispute = isInDispute;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public String getIsPayScheduleValid() {
        return isPayScheduleValid;
    }

    public void setIsPayScheduleValid(String isPayScheduleValid) {
        this.isPayScheduleValid = isPayScheduleValid;
    }

    public String getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(String isPosted) {
        this.isPosted = isPosted;
    }

    public String getIsPrinted() {
        return isPrinted;
    }

    public void setIsPrinted(String isPrinted) {
        this.isPrinted = isPrinted;
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

    public String getIsSelfService() {
        return isSelfService;
    }

    public void setIsSelfService(String isSelfService) {
        this.isSelfService = isSelfService;
    }

    public String getIsSendEMail() {
        return isSendEMail;
    }

    public void setIsSendEMail(String isSendEMail) {
        this.isSendEMail = isSendEMail;
    }

    public String getIsSOTrx() {
        return isSOTrx;
    }

    public void setIsSOTrx(String isSOTrx) {
        this.isSOTrx = isSOTrx;
    }

    public String getIsTaxIncluded() {
        return isTaxIncluded;
    }

    public void setIsTaxIncluded(String isTaxIncluded) {
        this.isTaxIncluded = isTaxIncluded;
    }

    public String getIsTransferred() {
        return isTransferred;
    }

    public void setIsTransferred(String isTransferred) {
        this.isTransferred = isTransferred;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
