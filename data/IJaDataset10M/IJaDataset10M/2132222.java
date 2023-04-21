package org.koossery.adempiere.core.contract.criteria.comptabilite.cout;

import java.sql.Date;
import java.math.*;

public class CostQueueCriteria {

    BigDecimal AccessLevel = BigDecimal.valueOf(3);

    public static final int Table_ID = 817;

    public static final String Table_Name = "M_CostQueue";

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

    public int c_AcctSchema_ID;

    public int getC_AcctSchema_ID() {
        return c_AcctSchema_ID;
    }

    public void setC_AcctSchema_ID(int C_AcctSchema_ID) {
        this.c_AcctSchema_ID = C_AcctSchema_ID;
    }

    public BigDecimal currentCostPrice;

    public BigDecimal getCurrentCostPrice() {
        return currentCostPrice;
    }

    public void setCurrentCostPrice(BigDecimal CurrentCostPrice) {
        this.currentCostPrice = CurrentCostPrice;
    }

    public BigDecimal currentQty;

    public BigDecimal getCurrentQty() {
        return currentQty;
    }

    public void setCurrentQty(BigDecimal CurrentQty) {
        this.currentQty = CurrentQty;
    }

    public int m_AttributeSetInstance_ID;

    public int getM_AttributeSetInstance_ID() {
        return m_AttributeSetInstance_ID;
    }

    public void setM_AttributeSetInstance_ID(int M_AttributeSetInstance_ID) {
        this.m_AttributeSetInstance_ID = M_AttributeSetInstance_ID;
    }

    public int m_CostElement_ID;

    public int getM_CostElement_ID() {
        return m_CostElement_ID;
    }

    public void setM_CostElement_ID(int M_CostElement_ID) {
        this.m_CostElement_ID = M_CostElement_ID;
    }

    public int m_CostQueue_ID;

    public int getM_CostQueue_ID() {
        return m_CostQueue_ID;
    }

    public void setM_CostQueue_ID(int M_CostQueue_ID) {
        this.m_CostQueue_ID = M_CostQueue_ID;
    }

    public int m_CostType_ID;

    public int getM_CostType_ID() {
        return m_CostType_ID;
    }

    public void setM_CostType_ID(int M_CostType_ID) {
        this.m_CostType_ID = M_CostType_ID;
    }

    public int m_Product_ID;

    public int getM_Product_ID() {
        return m_Product_ID;
    }

    public void setM_Product_ID(int M_Product_ID) {
        this.m_Product_ID = M_Product_ID;
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
        return getM_CostQueue_ID();
    }
}
