package org.posterita.beans;

/**
 * @author Ashley G Ramdass
 * May 2, 2008
 */
public class CashBookBean extends UDIBean {

    /**
     * @return the cashBookId
     */
    public Integer getCashBookId() {
        return (cashBookId == null ? 0 : cashBookId);
    }

    /**
     * @param cashBookId the cashBookId to set
     */
    public void setCashBookId(Integer cashBookId) {
        this.cashBookId = cashBookId;
    }

    /**
     * @return the cashBookName
     */
    public String getCashBookName() {
        return cashBookName;
    }

    /**
     * @param cashBookName the cashBookName to set
     */
    public void setCashBookName(String cashBookName) {
        this.cashBookName = cashBookName;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the isActive
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the isDefault
     */
    public Boolean getIsDefault() {
        return isDefault;
    }

    /**
     * @param isDefault the isDefault to set
     */
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * @return the orgId
     */
    public Integer getOrgId() {
        return orgId;
    }

    /**
     * @param orgId the orgId to set
     */
    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    /**
     * @return The currencyId
     */
    public Integer getCurrencyId() {
        return (currencyId == null ? 0 : currencyId);
    }

    /**
     * @param currencyId The currencyId to set
     */
    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    /**
     * @return The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency The currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
