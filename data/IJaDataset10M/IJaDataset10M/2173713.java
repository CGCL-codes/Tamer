package org.koossery.adempiere.core.contract.criteria.generated;

import org.koossery.adempiere.core.contract.criteria.KTADempiereBaseCriteria;

public class GL_BudgetCriteria extends KTADempiereBaseCriteria {

    private static final long serialVersionUID = 1L;

    private String budgetStatus;

    private String description;

    private int gl_Budget_ID;

    private String name;

    private String isPrimary;

    private String isActive;

    public String getBudgetStatus() {
        return budgetStatus;
    }

    public void setBudgetStatus(String budgetStatus) {
        this.budgetStatus = budgetStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGl_Budget_ID() {
        return gl_Budget_ID;
    }

    public void setGl_Budget_ID(int gl_Budget_ID) {
        this.gl_Budget_ID = gl_Budget_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(String isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
