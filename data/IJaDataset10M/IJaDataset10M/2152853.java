package org.koossery.adempiere.core.contract.dto.request;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;
import org.koossery.adempiere.core.contract.itf.request.IR_RequestUpdateDTO;

public class R_RequestUpdateDTO extends KTADempiereBaseDTO implements IR_RequestUpdateDTO {

    private static final long serialVersionUID = 1L;

    private String confidentialTypeEntry;

    private Timestamp endTime;

    private int m_ProductSpent_ID;

    private BigDecimal qtyInvoiced;

    private BigDecimal qtySpent;

    private int r_Request_ID;

    private int r_RequestUpdate_ID;

    private String result;

    private Timestamp startTime;

    private String isActive;

    public String getConfidentialTypeEntry() {
        return confidentialTypeEntry;
    }

    public void setConfidentialTypeEntry(String confidentialTypeEntry) {
        this.confidentialTypeEntry = confidentialTypeEntry;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public int getM_ProductSpent_ID() {
        return m_ProductSpent_ID;
    }

    public void setM_ProductSpent_ID(int m_ProductSpent_ID) {
        this.m_ProductSpent_ID = m_ProductSpent_ID;
    }

    public BigDecimal getQtyInvoiced() {
        return qtyInvoiced;
    }

    public void setQtyInvoiced(BigDecimal qtyInvoiced) {
        this.qtyInvoiced = qtyInvoiced;
    }

    public BigDecimal getQtySpent() {
        return qtySpent;
    }

    public void setQtySpent(BigDecimal qtySpent) {
        this.qtySpent = qtySpent;
    }

    public int getR_Request_ID() {
        return r_Request_ID;
    }

    public void setR_Request_ID(int r_Request_ID) {
        this.r_Request_ID = r_Request_ID;
    }

    public int getR_RequestUpdate_ID() {
        return r_RequestUpdate_ID;
    }

    public void setR_RequestUpdate_ID(int r_RequestUpdate_ID) {
        this.r_RequestUpdate_ID = r_RequestUpdate_ID;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
