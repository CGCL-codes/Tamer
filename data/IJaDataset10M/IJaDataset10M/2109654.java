package com.koossery.adempiere.fe.beans.accounting.cost;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @version 1.0
 * @created 25-ao�t-2008 15:11:08
 */
public class CostBean {

    /**
	 * numero  et du nom de la table correspondant à la DTO
	 */
    BigDecimal AccessLevel = BigDecimal.valueOf(3);

    /**
	 * declaration de l'attribut ad_client_id, son Get et  son Set
	 */
    public int ad_Client_ID;

    /**
	 * declaration de l'attribut ad_org_id, son Get et  son Set
	 */
    public int ad_Org_ID;

    /**
	 * declaration de l'attribut c_AcctSchema_ID , son Get et  son Set
	 */
    public int c_AcctSchema_ID;

    /**
	 * declaration de l'attribut costingMethod , son Get et  son Set
	 */
    public String costingMethod;

    /**
	 * declaration de l'attribut created, son Get et  son Set
	 */
    public Timestamp created;

    /**
	 * declaration de l'attribut createdby, son Get et  son Set
	 */
    public int createdby;

    /**
	 * declaration de l'attribut cumulatedAmt , son Get et  son Set
	 */
    public BigDecimal cumulatedAmt;

    /**
	 * declaration de l'attribut cumulatedQty , son Get et  son Set
	 */
    public BigDecimal cumulatedQty;

    /**
	 * declaration de l'attribut currentCostPrice , son Get et  son Set
	 */
    public BigDecimal currentCostPrice;

    /**
	 * declaration de l'attribut currentQty , son Get et  son Set
	 */
    public BigDecimal currentQty;

    /**
	 * declaration de l'attribut description , son Get et  son Set
	 */
    public String description;

    /**
	 * declaration de l'attribut futureCostPrice , son Get et  son Set
	 */
    public BigDecimal futureCostPrice;

    /**
	 * declaration de l'attribut isactive, son Get et  son Set
	 */
    public String isactive;

    /**
	 * declaration de l'attribut isProcessed , son Get et  son Set
	 */
    public String isProcessed;

    /**
	 * declaration de l'attribut m_AttributeSetInstance_ID , son Get et  son Set
	 */
    public int m_AttributeSetInstance_ID;

    /**
	 * declaration de l'attribut m_CostElement_ID , son Get et  son Set
	 */
    public int m_CostElement_ID;

    /**
	 * declaration de l'attribut m_CostType_ID , son Get et  son Set
	 */
    public int m_CostType_ID;

    /**
	 * declaration de l'attribut m_Product_ID , son Get et  son Set
	 */
    public int m_Product_ID;

    /**
	 * declaration de l'attribut percent , son Get et  son Set
	 */
    public int percent;

    public static final int Table_ID = 771;

    public static final String Table_Name = "M_Cost";

    /**
	 * declaration de l'attribut updated, son Get et  son Set
	 */
    public Timestamp updated;

    /**
	 * declaration de l'attribut updatedby, son Get et  son Set
	 */
    public int updatedby;

    public CostBean() {
    }

    public void finalize() throws Throwable {
    }

    public BigDecimal getAccessLevel() {
        return AccessLevel;
    }

    public void setAccessLevel(BigDecimal accessLevel) {
        AccessLevel = accessLevel;
    }

    public int getAd_Client_ID() {
        return ad_Client_ID;
    }

    public void setAd_Client_ID(int ad_Client_ID) {
        this.ad_Client_ID = ad_Client_ID;
    }

    public int getAd_Org_ID() {
        return ad_Org_ID;
    }

    public void setAd_Org_ID(int ad_Org_ID) {
        this.ad_Org_ID = ad_Org_ID;
    }

    public int getC_AcctSchema_ID() {
        return c_AcctSchema_ID;
    }

    public void setC_AcctSchema_ID(int acctSchema_ID) {
        c_AcctSchema_ID = acctSchema_ID;
    }

    public String getCostingMethod() {
        return costingMethod;
    }

    public void setCostingMethod(String costingMethod) {
        this.costingMethod = costingMethod;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public int getCreatedby() {
        return createdby;
    }

    public void setCreatedby(int createdby) {
        this.createdby = createdby;
    }

    public BigDecimal getCumulatedAmt() {
        return cumulatedAmt;
    }

    public void setCumulatedAmt(BigDecimal cumulatedAmt) {
        this.cumulatedAmt = cumulatedAmt;
    }

    public BigDecimal getCumulatedQty() {
        return cumulatedQty;
    }

    public void setCumulatedQty(BigDecimal cumulatedQty) {
        this.cumulatedQty = cumulatedQty;
    }

    public BigDecimal getCurrentCostPrice() {
        return currentCostPrice;
    }

    public void setCurrentCostPrice(BigDecimal currentCostPrice) {
        this.currentCostPrice = currentCostPrice;
    }

    public BigDecimal getCurrentQty() {
        return currentQty;
    }

    public void setCurrentQty(BigDecimal currentQty) {
        this.currentQty = currentQty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getFutureCostPrice() {
        return futureCostPrice;
    }

    public void setFutureCostPrice(BigDecimal futureCostPrice) {
        this.futureCostPrice = futureCostPrice;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(String isProcessed) {
        this.isProcessed = isProcessed;
    }

    public int getM_AttributeSetInstance_ID() {
        return m_AttributeSetInstance_ID;
    }

    public void setM_AttributeSetInstance_ID(int attributeSetInstance_ID) {
        m_AttributeSetInstance_ID = attributeSetInstance_ID;
    }

    public int getM_CostElement_ID() {
        return m_CostElement_ID;
    }

    public void setM_CostElement_ID(int costElement_ID) {
        m_CostElement_ID = costElement_ID;
    }

    public int getM_CostType_ID() {
        return m_CostType_ID;
    }

    public void setM_CostType_ID(int costType_ID) {
        m_CostType_ID = costType_ID;
    }

    public int getM_Product_ID() {
        return m_Product_ID;
    }

    public void setM_Product_ID(int product_ID) {
        m_Product_ID = product_ID;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public int getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(int updatedby) {
        this.updatedby = updatedby;
    }

    public static int getTable_ID() {
        return Table_ID;
    }

    public static String getTable_Name() {
        return Table_Name;
    }
}
