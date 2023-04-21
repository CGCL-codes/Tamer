package com.koossery.adempiere.fe.actions.accounting.charge.chargeAcct;

import java.util.Map;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseForm;
import com.koossery.adempiere.fe.beans.accounting.charge.chargeAcct.ChargeAcctBean;

/**
 * @version 1.0
 * @created 25-ao�t-2008 15:10:49
 */
public class ChargeAcctForm extends KTAdempiereBaseForm {

    private ChargeAcctBean chargeAcctBean;

    private Map chargeAcctMap;

    private Map chargeAcctMap1;

    private int idChargeAcctSelected;

    private String nomCharge;

    private String nomOrg;

    private String nomClient;

    private String nomSchema;

    private String nomch_Expense_Acct;

    private String nomch_Revenue_Acct;

    private int ch_Expense_Acct;

    private int ch_Revenue_Acct;

    public int expense;

    public int revenue;

    private boolean active;

    public Map getChargeAcctMap() {
        return chargeAcctMap;
    }

    public void setChargeAcctMap(Map chargeAcctMap) {
        this.chargeAcctMap = chargeAcctMap;
    }

    public int getIdChargeAcctSelected() {
        return idChargeAcctSelected;
    }

    public void setIdChargeAcctSelected(int idChargeAcctSelected) {
        this.idChargeAcctSelected = idChargeAcctSelected;
    }

    public ChargeAcctBean getChargeAcctBean() {
        return chargeAcctBean;
    }

    public void setChargeAcctBean(ChargeAcctBean chargeAcctBean) {
        this.chargeAcctBean = chargeAcctBean;
    }

    public ChargeAcctForm() {
    }

    public void finalize() throws Throwable {
        super.finalize();
    }

    public void resetForm() {
        setChargeAcctBean(new ChargeAcctBean());
        if (getChargeAcctMap() != null) getChargeAcctMap().clear();
        setIdChargeAcctSelected(0);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getNomCharge() {
        return nomCharge;
    }

    public void setNomCharge(String nomCharge) {
        this.nomCharge = nomCharge;
    }

    public String getNomOrg() {
        return nomOrg;
    }

    public void setNomOrg(String nomOrg) {
        this.nomOrg = nomOrg;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getNomSchema() {
        return nomSchema;
    }

    public void setNomSchema(String nomSchema) {
        this.nomSchema = nomSchema;
    }

    public String getNomch_Expense_Acct() {
        return nomch_Expense_Acct;
    }

    public void setNomch_Expense_Acct(String nomch_Expense_Acct) {
        this.nomch_Expense_Acct = nomch_Expense_Acct;
    }

    public String getNomch_Revenue_Acct() {
        return nomch_Revenue_Acct;
    }

    public void setNomch_Revenue_Acct(String nomch_Revenue_Acct) {
        this.nomch_Revenue_Acct = nomch_Revenue_Acct;
    }

    public int getCh_Expense_Acct() {
        return ch_Expense_Acct;
    }

    public void setCh_Expense_Acct(int ch_Expense_Acct) {
        this.ch_Expense_Acct = ch_Expense_Acct;
    }

    public int getCh_Revenue_Acct() {
        return ch_Revenue_Acct;
    }

    public void setCh_Revenue_Acct(int ch_Revenue_Acct) {
        this.ch_Revenue_Acct = ch_Revenue_Acct;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public Map getChargeAcctMap1() {
        return chargeAcctMap1;
    }

    public void setChargeAcctMap1(Map chargeAcctMap1) {
        this.chargeAcctMap1 = chargeAcctMap1;
    }
}
