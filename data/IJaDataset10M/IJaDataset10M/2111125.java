package com.koossery.adempiere.fe.actions.organizationRules.bank.accounting.createModifyAccounting;

import java.util.ArrayList;
import java.util.Map;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseForm;
import com.koossery.adempiere.fe.beans.bank.C_BankAccount_AcctBean;

public class BankAccountingForm extends KTAdempiereBaseForm {

    public BankAccountingForm() {
    }

    public void finalize() throws Throwable {
        super.finalize();
    }

    private C_BankAccount_AcctBean accountingBean;

    public C_BankAccount_AcctBean getAccountingBean() {
        return this.accountingBean;
    }

    public void setAccountingBean(C_BankAccount_AcctBean _accountingBean) {
        this.accountingBean = _accountingBean;
    }

    private Map accountingMap;

    public Map getAccountingMap() {
        return this.accountingMap;
    }

    public void setAccountingMap(Map _accountingMap) {
        this.accountingMap = _accountingMap;
    }

    private Map accountingMap1;

    public Map getAccountingMap1() {
        return this.accountingMap1;
    }

    public void setAccountingMap1(Map _accountingMap) {
        this.accountingMap1 = _accountingMap;
    }

    private int idOrgSelected;

    private String nomOrg;

    private String nomClient;

    private int bankAsset;

    private int bankUndentiffiersReceipts;

    private int paymentSelection;

    private int bankInterestExpense;

    private int bankInterestRevenue;

    private int bankRevaluationGain;

    private int bankSettlementLoss;

    private int bankInTransit;

    private int unallocatedCash;

    private int bankExpense;

    private int bankRevaluationLoss;

    private int bankSettlementGain;

    private int display;

    public int getDisplay() {
        return this.display;
    }

    public void setDisplay(int _display) {
        this.display = _display;
    }

    private boolean active;

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void resetForm() {
        if (getAccountingMap() != null) getAccountingMap().clear();
        setAccountingBean(new C_BankAccount_AcctBean());
    }

    private String bankAccountName;

    private String accSchemaName;

    public String getBankAccountName() {
        return bankAccountName;
    }

    private int idBankAcctSelected;

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public int getIdBankAcctSelected() {
        return idBankAcctSelected;
    }

    public void setIdBankAcctSelected(int idBankAcctSelected) {
        this.idBankAcctSelected = idBankAcctSelected;
    }

    public String getAccSchemaName() {
        return accSchemaName;
    }

    public void setAccSchemaName(String accSchemaName) {
        this.accSchemaName = accSchemaName;
    }

    public int getIdOrgSelected() {
        return idOrgSelected;
    }

    public void setIdOrgSelected(int idOrgSelected) {
        this.idOrgSelected = idOrgSelected;
    }

    public int getBankAsset() {
        return bankAsset;
    }

    public void setBankAsset(int bankAsset) {
        this.bankAsset = bankAsset;
    }

    public int getBankUndentiffiersReceipts() {
        return bankUndentiffiersReceipts;
    }

    public void setBankUndentiffiersReceipts(int bankUndentiffiersReceipts) {
        this.bankUndentiffiersReceipts = bankUndentiffiersReceipts;
    }

    public int getPaymentSelection() {
        return paymentSelection;
    }

    public void setPaymentSelection(int paymentSelection) {
        this.paymentSelection = paymentSelection;
    }

    public int getBankInterestExpense() {
        return bankInterestExpense;
    }

    public void setBankInterestExpense(int bankInterestExpense) {
        this.bankInterestExpense = bankInterestExpense;
    }

    public int getBankInterestRevenue() {
        return bankInterestRevenue;
    }

    public void setBankInterestRevenue(int bankInterestRevenue) {
        this.bankInterestRevenue = bankInterestRevenue;
    }

    public int getBankRevaluationGain() {
        return bankRevaluationGain;
    }

    public void setBankRevaluationGain(int bankRevaluationGain) {
        this.bankRevaluationGain = bankRevaluationGain;
    }

    public int getBankSettlementLoss() {
        return bankSettlementLoss;
    }

    public void setBankSettlementLoss(int bankSettlementLoss) {
        this.bankSettlementLoss = bankSettlementLoss;
    }

    public int getBankInTransit() {
        return bankInTransit;
    }

    public void setBankInTransit(int bankInTransit) {
        this.bankInTransit = bankInTransit;
    }

    public int getUnallocatedCash() {
        return unallocatedCash;
    }

    public void setUnallocatedCash(int unallocatedCash) {
        this.unallocatedCash = unallocatedCash;
    }

    public int getBankExpense() {
        return bankExpense;
    }

    public void setBankExpense(int bankExpense) {
        this.bankExpense = bankExpense;
    }

    public int getBankRevaluationLoss() {
        return bankRevaluationLoss;
    }

    public void setBankRevaluationLoss(int bankRevaluationLoss) {
        this.bankRevaluationLoss = bankRevaluationLoss;
    }

    public int getBankSettlementGain() {
        return bankSettlementGain;
    }

    public void setBankSettlementGain(int bankSettlementGain) {
        this.bankSettlementGain = bankSettlementGain;
    }

    public void setNomOrg(String nomOrg) {
        this.nomOrg = nomOrg;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getNomOrg() {
        return nomOrg;
    }

    public String getNomClient() {
        return nomClient;
    }
}
