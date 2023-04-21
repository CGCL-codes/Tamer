package org.koossery.adempiere.core.contract.criteria.generated;

import org.koossery.adempiere.core.contract.criteria.KTADempiereBaseCriteria;

public class C_TaxCategoryCriteria extends KTADempiereBaseCriteria {

    private static final long serialVersionUID = 1L;

    private int c_TaxCategory_ID;

    private String commodityCode;

    private String description;

    private String name;

    private String isDefault;

    private String isActive;

    public int getC_TaxCategory_ID() {
        return c_TaxCategory_ID;
    }

    public void setC_TaxCategory_ID(int c_TaxCategory_ID) {
        this.c_TaxCategory_ID = c_TaxCategory_ID;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
