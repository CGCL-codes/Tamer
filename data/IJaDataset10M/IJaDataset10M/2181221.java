package com.koossery.adempiere.fe.beans.util;

import java.sql.Timestamp;

public class BPartnerBean {

    private static final long serialVersionUID = 1L;

    private long acqusitionCost;

    private long actualLifeTimeValue;

    private String ad_Language;

    private String ad_OrgBP_ID;

    private int bpartner_Parent_ID;

    private int c_BP_Group_ID;

    private int c_BPartner_ID;

    private int c_Dunning_ID;

    private int c_Greeting_ID;

    private int c_InvoiceSchedule_ID;

    private int c_PaymentTerm_ID;

    private int c_taxgroup_id;

    private String deliveryRule;

    private String deliveryViaRule;

    private String description;

    private int documentCopies;

    private Timestamp dunningGrace;

    private String duNS;

    private Timestamp firstSale;

    private long flatDiscount;

    private String freightCostRule;

    private int invoice_PrintFormat_ID;

    private String invoiceRule;

    private String isActive;

    private String isCustomer;

    private String isDiscountPrinted;

    private String isEmployee;

    private String isOneTime;

    private String isProspect;

    private String isSalesRep;

    private String isSummary;

    private String isTaxExempt;

    private String isVendor;

    private int m_DiscountSchema_ID;

    private int m_PriceList_ID;

    private String naICS;

    private String name;

    private String name2;

    private int numberEmployees;

    private String paymentRule;

    private String paymentRulePO;

    private int po_DiscountSchema_ID;

    private int po_PaymentTerm_ID;

    private int po_PriceList_ID;

    private String poReference;

    private long potentialLifeTimeValue;

    private String rating;

    private String referenceNo;

    private int salesRep_ID;

    private int salesVolume;

    private String sendEMail;

    private int shareOfCustomer;

    private int shelfLifeMinPct;

    private long so_CreditLimit;

    private long so_CreditUsed;

    private String so_Description;

    private String soCreditStatus;

    private String taxID;

    private long totalOpenBalance;

    private String urL;

    private String value;

    private String nomGroup;

    public String getNomGroup() {
        return nomGroup;
    }

    public void setNomGroup(String nomGroup) {
        this.nomGroup = nomGroup;
    }

    public long getAcqusitionCost() {
        return acqusitionCost;
    }

    public void setAcqusitionCost(Object obj) {
        this.acqusitionCost = obj == null ? -1 : new Long(obj.toString());
    }

    public long getActualLifeTimeValue() {
        return actualLifeTimeValue;
    }

    public void setActualLifeTimeValue(Object obj) {
        this.actualLifeTimeValue = obj == null ? -1 : new Long(obj.toString());
    }

    public String getAd_Language() {
        return ad_Language;
    }

    public void setAd_Language(String ad_Language) {
        this.ad_Language = ad_Language;
    }

    public int getBpartner_Parent_ID() {
        return bpartner_Parent_ID;
    }

    public void setBpartner_Parent_ID(Object obj) {
        this.bpartner_Parent_ID = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public int getC_BP_Group_ID() {
        return c_BP_Group_ID;
    }

    public void setC_BP_Group_ID(Object obj) {
        c_BP_Group_ID = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public int getC_BPartner_ID() {
        return c_BPartner_ID;
    }

    public void setC_BPartner_ID(Object obj) {
        c_BPartner_ID = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public int getC_Dunning_ID() {
        return c_Dunning_ID;
    }

    public void setC_Dunning_ID(Object obj) {
        c_Dunning_ID = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public int getC_Greeting_ID() {
        return c_Greeting_ID;
    }

    public void setC_Greeting_ID(Object obj) {
        c_Greeting_ID = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public int getC_InvoiceSchedule_ID() {
        return c_InvoiceSchedule_ID;
    }

    public void setC_InvoiceSchedule_ID(Object obj) {
        c_InvoiceSchedule_ID = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public int getC_PaymentTerm_ID() {
        return c_PaymentTerm_ID;
    }

    public void setC_PaymentTerm_ID(Object obj) {
        c_PaymentTerm_ID = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public int getC_taxgroup_id() {
        return c_taxgroup_id;
    }

    public void setC_taxgroup_id(Object obj) {
        this.c_taxgroup_id = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public String getDeliveryRule() {
        return deliveryRule;
    }

    public void setDeliveryRule(String deliveryRule) {
        this.deliveryRule = deliveryRule;
    }

    public String getDeliveryViaRule() {
        return deliveryViaRule;
    }

    public void setDeliveryViaRule(String deliveryViaRule) {
        this.deliveryViaRule = deliveryViaRule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDocumentCopies() {
        return documentCopies;
    }

    public void setDocumentCopies(Object obj) {
        this.documentCopies = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public Timestamp getDunningGrace() {
        return dunningGrace;
    }

    public void setDunningGrace(Timestamp dunningGrace) {
        this.dunningGrace = dunningGrace;
    }

    public String getDuNS() {
        return duNS;
    }

    public void setDuNS(String duNS) {
        this.duNS = duNS;
    }

    public Timestamp getFirstSale() {
        return firstSale;
    }

    public void setFirstSale(Timestamp firstSale) {
        this.firstSale = firstSale;
    }

    public long getFlatDiscount() {
        return flatDiscount;
    }

    public void setFlatDiscount(Object obj) {
        this.flatDiscount = obj == null ? -1 : new Long(obj.toString());
    }

    public String getFreightCostRule() {
        return freightCostRule;
    }

    public void setFreightCostRule(String freightCostRule) {
        this.freightCostRule = freightCostRule;
    }

    public int getInvoice_PrintFormat_ID() {
        return invoice_PrintFormat_ID;
    }

    public void setInvoice_PrintFormat_ID(Object obj) {
        this.invoice_PrintFormat_ID = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public String getInvoiceRule() {
        return invoiceRule;
    }

    public void setInvoiceRule(String invoiceRule) {
        this.invoiceRule = invoiceRule;
    }

    public String getIsCustomer() {
        return isCustomer;
    }

    public void setIsCustomer(String isCustomer) {
        this.isCustomer = isCustomer;
    }

    public String getIsDiscountPrinted() {
        return isDiscountPrinted;
    }

    public void setIsDiscountPrinted(String isDiscountPrinted) {
        this.isDiscountPrinted = isDiscountPrinted;
    }

    public String getIsEmployee() {
        return isEmployee;
    }

    public void setIsEmployee(String isEmployee) {
        this.isEmployee = isEmployee;
    }

    public String getIsOneTime() {
        return isOneTime;
    }

    public void setIsOneTime(String isOneTime) {
        this.isOneTime = isOneTime;
    }

    public String getIsProspect() {
        return isProspect;
    }

    public void setIsProspect(String isProspect) {
        this.isProspect = isProspect;
    }

    public String getIsSalesRep() {
        return isSalesRep;
    }

    public void setIsSalesRep(String isSalesRep) {
        this.isSalesRep = isSalesRep;
    }

    public String getIsSummary() {
        return isSummary;
    }

    public void setIsSummary(String isSummary) {
        this.isSummary = isSummary;
    }

    public String getIsTaxExempt() {
        return isTaxExempt;
    }

    public void setIsTaxExempt(String isTaxExempt) {
        this.isTaxExempt = isTaxExempt;
    }

    public String getIsVendor() {
        return isVendor;
    }

    public void setIsVendor(String isVendor) {
        this.isVendor = isVendor;
    }

    public int getM_DiscountSchema_ID() {
        return m_DiscountSchema_ID;
    }

    public void setM_DiscountSchema_ID(Object obj) {
        m_DiscountSchema_ID = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public int getM_PriceList_ID() {
        return m_PriceList_ID;
    }

    public void setM_PriceList_ID(Object obj) {
        m_PriceList_ID = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public String getNaICS() {
        return naICS;
    }

    public void setNaICS(String naICS) {
        this.naICS = naICS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public int getNumberEmployees() {
        return numberEmployees;
    }

    public void setNumberEmployees(Object obj) {
        this.numberEmployees = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public String getPaymentRule() {
        return paymentRule;
    }

    public void setPaymentRule(String paymentRule) {
        this.paymentRule = paymentRule;
    }

    public String getPaymentRulePO() {
        return paymentRulePO;
    }

    public void setPaymentRulePO(String paymentRulePO) {
        this.paymentRulePO = paymentRulePO;
    }

    public int getPo_DiscountSchema_ID() {
        return po_DiscountSchema_ID;
    }

    public void setPo_DiscountSchema_ID(Object obj) {
        this.po_DiscountSchema_ID = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public int getPo_PaymentTerm_ID() {
        return po_PaymentTerm_ID;
    }

    public void setPo_PaymentTerm_ID(Object obj) {
        this.po_PaymentTerm_ID = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public int getPo_PriceList_ID() {
        return po_PriceList_ID;
    }

    public void setPo_PriceList_ID(Object obj) {
        this.po_PriceList_ID = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public long getPotentialLifeTimeValue() {
        return potentialLifeTimeValue;
    }

    public void setPotentialLifeTimeValue(Object obj) {
        this.potentialLifeTimeValue = obj == null ? -1 : new Long(obj.toString());
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public int getSalesRep_ID() {
        return salesRep_ID;
    }

    public void setSalesRep_ID(Object obj) {
        this.salesRep_ID = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(Object obj) {
        this.salesVolume = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public String getSendEMail() {
        return sendEMail;
    }

    public void setSendEMail(String sendEMail) {
        this.sendEMail = sendEMail;
    }

    public int getShareOfCustomer() {
        return shareOfCustomer;
    }

    public void setShareOfCustomer(Object obj) {
        this.shareOfCustomer = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public int getShelfLifeMinPct() {
        return shelfLifeMinPct;
    }

    public void setShelfLifeMinPct(Object obj) {
        this.shelfLifeMinPct = obj == null ? -1 : Integer.parseInt(obj.toString());
    }

    public long getSo_CreditLimit() {
        return so_CreditLimit;
    }

    public void setSo_CreditLimit(Object obj) {
        this.so_CreditLimit = obj == null ? -1 : new Long(obj.toString());
    }

    public long getSo_CreditUsed() {
        return so_CreditUsed;
    }

    public void setSo_CreditUsed(Object obj) {
        this.so_CreditUsed = obj == null ? -1 : new Long(obj.toString());
    }

    public String getSo_Description() {
        return so_Description;
    }

    public void setSo_Description(String so_Description) {
        this.so_Description = so_Description;
    }

    public String getSoCreditStatus() {
        return soCreditStatus;
    }

    public void setSoCreditStatus(String soCreditStatus) {
        this.soCreditStatus = soCreditStatus;
    }

    public String getTaxID() {
        return taxID;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    public String getUrL() {
        return urL;
    }

    public void setUrL(String urL) {
        this.urL = urL;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAd_OrgBP_ID() {
        return ad_OrgBP_ID;
    }

    public void setAd_OrgBP_ID(String ad_OrgBP_ID) {
        this.ad_OrgBP_ID = ad_OrgBP_ID;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getPoReference() {
        return poReference;
    }

    public void setPoReference(String poReference) {
        this.poReference = poReference;
    }

    public long getTotalOpenBalance() {
        return totalOpenBalance;
    }

    public void setTotalOpenBalance(Object obj) {
        this.totalOpenBalance = obj == null ? -1 : new Long(obj.toString());
    }

    public void setAcqusitionCost(long acqusitionCost) {
        this.acqusitionCost = acqusitionCost;
    }

    public void setActualLifeTimeValue(long actualLifeTimeValue) {
        this.actualLifeTimeValue = actualLifeTimeValue;
    }

    public void setBpartner_Parent_ID(int bpartner_Parent_ID) {
        this.bpartner_Parent_ID = bpartner_Parent_ID;
    }

    public void setC_BP_Group_ID(int group_ID) {
        c_BP_Group_ID = group_ID;
    }

    public void setC_BPartner_ID(int partner_ID) {
        c_BPartner_ID = partner_ID;
    }

    public void setC_Dunning_ID(int dunning_ID) {
        c_Dunning_ID = dunning_ID;
    }

    public void setC_Greeting_ID(int greeting_ID) {
        c_Greeting_ID = greeting_ID;
    }

    public void setC_InvoiceSchedule_ID(int invoiceSchedule_ID) {
        c_InvoiceSchedule_ID = invoiceSchedule_ID;
    }

    public void setC_PaymentTerm_ID(int paymentTerm_ID) {
        c_PaymentTerm_ID = paymentTerm_ID;
    }

    public void setC_taxgroup_id(int c_taxgroup_id) {
        this.c_taxgroup_id = c_taxgroup_id;
    }

    public void setDocumentCopies(int documentCopies) {
        this.documentCopies = documentCopies;
    }

    public void setFlatDiscount(long flatDiscount) {
        this.flatDiscount = flatDiscount;
    }

    public void setInvoice_PrintFormat_ID(int invoice_PrintFormat_ID) {
        this.invoice_PrintFormat_ID = invoice_PrintFormat_ID;
    }

    public void setM_DiscountSchema_ID(int discountSchema_ID) {
        m_DiscountSchema_ID = discountSchema_ID;
    }

    public void setM_PriceList_ID(int priceList_ID) {
        m_PriceList_ID = priceList_ID;
    }

    public void setNumberEmployees(int numberEmployees) {
        this.numberEmployees = numberEmployees;
    }

    public void setPo_DiscountSchema_ID(int po_DiscountSchema_ID) {
        this.po_DiscountSchema_ID = po_DiscountSchema_ID;
    }

    public void setPo_PaymentTerm_ID(int po_PaymentTerm_ID) {
        this.po_PaymentTerm_ID = po_PaymentTerm_ID;
    }

    public void setPo_PriceList_ID(int po_PriceList_ID) {
        this.po_PriceList_ID = po_PriceList_ID;
    }

    public void setPotentialLifeTimeValue(long potentialLifeTimeValue) {
        this.potentialLifeTimeValue = potentialLifeTimeValue;
    }

    public void setSalesRep_ID(int salesRep_ID) {
        this.salesRep_ID = salesRep_ID;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    public void setShareOfCustomer(int shareOfCustomer) {
        this.shareOfCustomer = shareOfCustomer;
    }

    public void setShelfLifeMinPct(int shelfLifeMinPct) {
        this.shelfLifeMinPct = shelfLifeMinPct;
    }

    public void setSo_CreditLimit(long so_CreditLimit) {
        this.so_CreditLimit = so_CreditLimit;
    }

    public void setSo_CreditUsed(long so_CreditUsed) {
        this.so_CreditUsed = so_CreditUsed;
    }

    public void setTotalOpenBalance(long totalOpenBalance) {
        this.totalOpenBalance = totalOpenBalance;
    }
}
