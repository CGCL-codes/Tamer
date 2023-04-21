package org.koossery.adempiere.core.contract.dto.payroll;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;

public class HR_AttributeDTO extends KTADempiereBaseDTO {

    private static final long serialVersionUID = 1L;

    private int ad_Rule_Engine_ID;

    private BigDecimal amount;

    private int c_BPartner_ID;

    private String columnType;

    private String description;

    private int hr_Attribute_Acct;

    private int hr_Attribute_ID;

    private int hr_Concept_ID;

    private int hr_Department_ID;

    private int hr_Employee_ID;

    private int hr_Job_ID;

    private int hr_Payroll_ID;

    private int maxValue;

    private int minValue;

    private BigDecimal qty;

    private Timestamp serviceDate;

    private String textMsg;

    private Timestamp validFrom;

    private Timestamp validTo;

    private String isPrinted;

    private String isActive;

    public int getAd_Rule_Engine_ID() {
        return ad_Rule_Engine_ID;
    }

    public void setAd_Rule_Engine_ID(int ad_Rule_Engine_ID) {
        this.ad_Rule_Engine_ID = ad_Rule_Engine_ID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getC_BPartner_ID() {
        return c_BPartner_ID;
    }

    public void setC_BPartner_ID(int c_BPartner_ID) {
        this.c_BPartner_ID = c_BPartner_ID;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHr_Attribute_Acct() {
        return hr_Attribute_Acct;
    }

    public void setHr_Attribute_Acct(int hr_Attribute_Acct) {
        this.hr_Attribute_Acct = hr_Attribute_Acct;
    }

    public int getHr_Attribute_ID() {
        return hr_Attribute_ID;
    }

    public void setHr_Attribute_ID(int hr_Attribute_ID) {
        this.hr_Attribute_ID = hr_Attribute_ID;
    }

    public int getHr_Concept_ID() {
        return hr_Concept_ID;
    }

    public void setHr_Concept_ID(int hr_Concept_ID) {
        this.hr_Concept_ID = hr_Concept_ID;
    }

    public int getHr_Department_ID() {
        return hr_Department_ID;
    }

    public void setHr_Department_ID(int hr_Department_ID) {
        this.hr_Department_ID = hr_Department_ID;
    }

    public int getHr_Employee_ID() {
        return hr_Employee_ID;
    }

    public void setHr_Employee_ID(int hr_Employee_ID) {
        this.hr_Employee_ID = hr_Employee_ID;
    }

    public int getHr_Job_ID() {
        return hr_Job_ID;
    }

    public void setHr_Job_ID(int hr_Job_ID) {
        this.hr_Job_ID = hr_Job_ID;
    }

    public int getHr_Payroll_ID() {
        return hr_Payroll_ID;
    }

    public void setHr_Payroll_ID(int hr_Payroll_ID) {
        this.hr_Payroll_ID = hr_Payroll_ID;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Timestamp getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Timestamp serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Timestamp getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Timestamp validFrom) {
        this.validFrom = validFrom;
    }

    public Timestamp getValidTo() {
        return validTo;
    }

    public void setValidTo(Timestamp validTo) {
        this.validTo = validTo;
    }

    public String getIsPrinted() {
        return isPrinted;
    }

    public void setIsPrinted(String isPrinted) {
        this.isPrinted = isPrinted;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }

    public String getTextMsg() {
        return textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }
}
