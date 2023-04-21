package com.cosmos.acacia.crm.bl.reports;

import java.io.Serializable;
import com.cosmos.acacia.crm.data.contacts.Passport;

public class PrintableDocumentHeader implements Serializable {

    private String recipientUniqueIdentifier;

    private String recipientName;

    private String recipientAddress;

    private String recipientVATNumber;

    private String recipientBankAccount;

    private String recipientBankCode;

    private String recipientBank;

    private String recipientMOL;

    private Passport recipientPassport;

    private String totalPriceInWords;

    private String executorUniqueIdentifier;

    private String executorOrganizationName;

    private String executorAddress;

    private String executorVATNumber;

    private String executorBankAccount;

    private String executorBankCode;

    private String executorBank;

    private String executorMOL;

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getRecipientBankAccount() {
        return recipientBankAccount;
    }

    public void setRecipientBankAccount(String recipientBankAccount) {
        this.recipientBankAccount = recipientBankAccount;
    }

    public String getRecipientBankCode() {
        return recipientBankCode;
    }

    public void setRecipientBankCode(String recipientBankCode) {
        this.recipientBankCode = recipientBankCode;
    }

    public String getRecipientBank() {
        return recipientBank;
    }

    public void setRecipientBank(String recipientBank) {
        this.recipientBank = recipientBank;
    }

    public String getRecipientMOL() {
        return recipientMOL;
    }

    public void setRecipientMOL(String recipientMOL) {
        this.recipientMOL = recipientMOL;
    }

    public String getExecutorAddress() {
        return executorAddress;
    }

    public void setExecutorAddress(String executorAddress) {
        this.executorAddress = executorAddress;
    }

    public String getExecutorBankAccount() {
        return executorBankAccount;
    }

    public void setExecutorBankAccount(String executorBankAccount) {
        this.executorBankAccount = executorBankAccount;
    }

    public String getExecutorBankCode() {
        return executorBankCode;
    }

    public void setExecutorBankCode(String executorBankCode) {
        this.executorBankCode = executorBankCode;
    }

    public String getExecutorBank() {
        return executorBank;
    }

    public void setExecutorBank(String executorBank) {
        this.executorBank = executorBank;
    }

    public String getExecutorMOL() {
        return executorMOL;
    }

    public void setExecutorMOL(String executorMOL) {
        this.executorMOL = executorMOL;
    }

    public Passport getRecipientPassport() {
        return recipientPassport;
    }

    public void setRecipientPassport(Passport recipientPassport) {
        this.recipientPassport = recipientPassport;
    }

    public String getRecipientUniqueIdentifier() {
        return recipientUniqueIdentifier;
    }

    public void setRecipientUniqueIdentifier(String recipientUniqueIdentifier) {
        this.recipientUniqueIdentifier = recipientUniqueIdentifier;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientVATNumber() {
        return recipientVATNumber;
    }

    public void setRecipientVATNumber(String recipientVATNumber) {
        this.recipientVATNumber = recipientVATNumber;
    }

    public String getExecutorUniqueIdentifier() {
        return executorUniqueIdentifier;
    }

    public void setExecutorUniqueIdentifier(String executorUniqueIdentifier) {
        this.executorUniqueIdentifier = executorUniqueIdentifier;
    }

    public String getExecutorOrganizationName() {
        return executorOrganizationName;
    }

    public void setExecutorOrganizationName(String executorOrganizationName) {
        this.executorOrganizationName = executorOrganizationName;
    }

    public String getExecutorVATNumber() {
        return executorVATNumber;
    }

    public void setExecutorVATNumber(String executorVATNumber) {
        this.executorVATNumber = executorVATNumber;
    }

    public String getTotalPriceInWords() {
        return totalPriceInWords;
    }

    public void setTotalPriceInWords(String totalPriceInWords) {
        this.totalPriceInWords = totalPriceInWords;
    }
}
