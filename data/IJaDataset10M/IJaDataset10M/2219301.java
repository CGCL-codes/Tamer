package org.koossery.adempiere.core.contract.dto.comptabilite;

import java.sql.Date;
import java.math.*;

public class PeriodControlDTO {

    BigDecimal AccessLevel = BigDecimal.valueOf(2);

    public static final int Table_ID = 229;

    public static final String Table_Name = "C_PeriodControl";

    public static int getTable_ID() {
        return Table_ID;
    }

    public static String getTable_Name() {
        return Table_Name;
    }

    public int ad_Client_ID;

    public int getAd_Client_ID() {
        return ad_Client_ID;
    }

    public void setAd_Client_ID(int ad_Client_ID) {
        this.ad_Client_ID = ad_Client_ID;
    }

    public int ad_Org_ID;

    public int getAd_Org_ID() {
        return ad_Org_ID;
    }

    public void setAd_Org_ID(int ad_Org_ID) {
        this.ad_Org_ID = ad_Org_ID;
    }

    public String isactive;

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public int c_Period_ID;

    public int getC_Period_ID() {
        return c_Period_ID;
    }

    public void setC_Period_ID(int C_Period_ID) {
        this.c_Period_ID = C_Period_ID;
    }

    public int c_PeriodControl_ID;

    public int getC_PeriodControl_ID() {
        return c_PeriodControl_ID;
    }

    public void setC_PeriodControl_ID(int C_PeriodControl_ID) {
        this.c_PeriodControl_ID = C_PeriodControl_ID;
    }

    public String docBaseType;

    public String getDocBaseType() {
        return docBaseType;
    }

    public void setDocBaseType(String DocBaseType) {
        this.docBaseType = DocBaseType;
    }

    public String periodAction;

    public String getPeriodAction() {
        return periodAction;
    }

    public void setPeriodAction(String PeriodAction) {
        this.periodAction = PeriodAction;
    }

    public String periodStatus;

    public String getPeriodStatus() {
        return periodStatus;
    }

    public void setPeriodStatus(String PeriodStatus) {
        this.periodStatus = PeriodStatus;
    }

    public String isProcessing;

    public String getIsProcessing() {
        return isProcessing;
    }

    public void setIsProcessing(String isProcessing) {
        this.isProcessing = isProcessing;
    }

    public Date created;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int createdby;

    public int getCreatedby() {
        return createdby;
    }

    public void setCreatedby(int createdby) {
        this.createdby = createdby;
    }

    public Date updated;

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public int updatedby;

    public int getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(int updatedby) {
        this.updatedby = updatedby;
    }

    public int getId() {
        return getC_PeriodControl_ID();
    }
}
