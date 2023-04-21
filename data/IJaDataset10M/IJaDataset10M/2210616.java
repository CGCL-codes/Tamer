package org.koossery.adempiere.core.contract.criteria.org.cash;

import org.koossery.adempiere.core.contract.criteria.KTADempiereBaseCriteria;
import org.koossery.adempiere.core.contract.itf.org.cash.IC_CashBookDTO;

public class C_CashBookCriteria extends KTADempiereBaseCriteria implements IC_CashBookDTO {

    private static final long serialVersionUID = 1L;

    private int c_CashBook_ID;

    private int c_Currency_ID;

    private String description;

    private String name;

    private String isDefault;

    private String isActive;

    public int getC_CashBook_ID() {
        return c_CashBook_ID;
    }

    public void setC_CashBook_ID(int c_CashBook_ID) {
        this.c_CashBook_ID = c_CashBook_ID;
    }

    public int getC_Currency_ID() {
        return c_Currency_ID;
    }

    public void setC_Currency_ID(int c_Currency_ID) {
        this.c_Currency_ID = c_Currency_ID;
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
