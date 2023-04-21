package org.koossery.adempiere.core.contract.dto.generated;

import java.sql.Timestamp;
import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;

public class C_ElementValueDTO extends KTADempiereBaseDTO {

    private static final long serialVersionUID = 1L;

    private String accountSign;

    private String accountType;

    private int c_BankAccount_ID;

    private int c_Currency_ID;

    private int c_Element_ID;

    private int c_ElementValue_ID;

    private String description;

    private String name;

    private Timestamp validFrom;

    private Timestamp validTo;

    private String value;

    private String isBankAccount;

    private String isDocControlled;

    private String isForeignCurrency;

    private String isPostActual;

    private String isPostBudget;

    private String isPostEncumbrance;

    private String isPostStatistical;

    private String isSummary;

    private String isActive;

    public String getAccountSign() {
        return accountSign;
    }

    public void setAccountSign(String accountSign) {
        this.accountSign = accountSign;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getC_BankAccount_ID() {
        return c_BankAccount_ID;
    }

    public void setC_BankAccount_ID(int c_BankAccount_ID) {
        this.c_BankAccount_ID = c_BankAccount_ID;
    }

    public int getC_Currency_ID() {
        return c_Currency_ID;
    }

    public void setC_Currency_ID(int c_Currency_ID) {
        this.c_Currency_ID = c_Currency_ID;
    }

    public int getC_Element_ID() {
        return c_Element_ID;
    }

    public void setC_Element_ID(int c_Element_ID) {
        this.c_Element_ID = c_Element_ID;
    }

    public int getC_ElementValue_ID() {
        return c_ElementValue_ID;
    }

    public void setC_ElementValue_ID(int c_ElementValue_ID) {
        this.c_ElementValue_ID = c_ElementValue_ID;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIsBankAccount() {
        return isBankAccount;
    }

    public void setIsBankAccount(String isBankAccount) {
        this.isBankAccount = isBankAccount;
    }

    public String getIsDocControlled() {
        return isDocControlled;
    }

    public void setIsDocControlled(String isDocControlled) {
        this.isDocControlled = isDocControlled;
    }

    public String getIsForeignCurrency() {
        return isForeignCurrency;
    }

    public void setIsForeignCurrency(String isForeignCurrency) {
        this.isForeignCurrency = isForeignCurrency;
    }

    public String getIsPostActual() {
        return isPostActual;
    }

    public void setIsPostActual(String isPostActual) {
        this.isPostActual = isPostActual;
    }

    public String getIsPostBudget() {
        return isPostBudget;
    }

    public void setIsPostBudget(String isPostBudget) {
        this.isPostBudget = isPostBudget;
    }

    public String getIsPostEncumbrance() {
        return isPostEncumbrance;
    }

    public void setIsPostEncumbrance(String isPostEncumbrance) {
        this.isPostEncumbrance = isPostEncumbrance;
    }

    public String getIsPostStatistical() {
        return isPostStatistical;
    }

    public void setIsPostStatistical(String isPostStatistical) {
        this.isPostStatistical = isPostStatistical;
    }

    public String getIsSummary() {
        return isSummary;
    }

    public void setIsSummary(String isSummary) {
        this.isSummary = isSummary;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
