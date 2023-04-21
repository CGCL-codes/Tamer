package com.koossery.adempiere.fe.actions.accounting.element.value;

import java.util.ArrayList;
import java.util.Map;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseForm;
import com.koossery.adempiere.fe.beans.accounting.element.value.ElementValueBean;

/**
 * @version 1.0
 * @created 25-ao�t-2008 15:12:07
 */
public class ValueForm extends KTAdempiereBaseForm {

    private ElementValueBean valeurBean;

    private Map valeurMap;

    private Map valeurMap1;

    private int idValeurSelected;

    private int idClientSelected;

    private int idCurrencySelected;

    private int idBankSelected;

    private int idAccountTypeSelected;

    private int idAccountSignSelected;

    private int idOrgSelected;

    private ArrayList listOfBankAllowed;

    private ArrayList listOfClientAllowed;

    private ArrayList listOfOrgAllowed;

    private ArrayList listOfCurrencyAllowed;

    private ArrayList listOfAccountTypeAllowed;

    private ArrayList listOfAccountSignAllowed;

    private int display;

    private int flag;

    private boolean docControl;

    private String nomClient;

    private String nomOrg;

    private boolean active;

    private boolean bankAccount;

    private boolean currencyAmount;

    private boolean summary;

    private boolean postActual;

    private boolean postBudget;

    private boolean postStatiscal;

    private String nomElt;

    public String getNomElt() {
        return nomElt;
    }

    public void setNomElt(String nomElt) {
        this.nomElt = nomElt;
    }

    public boolean isNaturalAccount() {
        return docControl;
    }

    public void setNaturalAccount(boolean naturalAccount) {
        this.docControl = naturalAccount;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getNomOrg() {
        return nomOrg;
    }

    public void setNomOrg(String nomOrg) {
        this.nomOrg = nomOrg;
    }

    public boolean isBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(boolean bankAccount) {
        this.bankAccount = bankAccount;
    }

    public boolean isCurrencyAmount() {
        return currencyAmount;
    }

    public void setCurrencyAmount(boolean currencyAmount) {
        this.currencyAmount = currencyAmount;
    }

    public boolean isSummary() {
        return summary;
    }

    public void setSummary(boolean summary) {
        this.summary = summary;
    }

    public boolean isPostActual() {
        return postActual;
    }

    public void setPostActual(boolean postActual) {
        this.postActual = postActual;
    }

    public boolean isPostBudget() {
        return postBudget;
    }

    public void setPostBudget(boolean postBudget) {
        this.postBudget = postBudget;
    }

    public boolean isPostStatiscal() {
        return postStatiscal;
    }

    public void setPostStatiscal(boolean postStatiscal) {
        this.postStatiscal = postStatiscal;
    }

    public ElementValueBean getValeurBean() {
        return valeurBean;
    }

    public void setValeurBean(ElementValueBean valeurBean) {
        this.valeurBean = valeurBean;
    }

    public Map getValeurMap() {
        return valeurMap;
    }

    public void setValeurMap(Map valeurMap) {
        this.valeurMap = valeurMap;
    }

    public int getIdValeurSelected() {
        return idValeurSelected;
    }

    public void setIdValeurSelected(int idValeurSelected) {
        this.idValeurSelected = idValeurSelected;
    }

    public int getIdCurrencySelected() {
        return idCurrencySelected;
    }

    public void setIdCurrencySelected(int idCurrencySelected) {
        this.idCurrencySelected = idCurrencySelected;
    }

    public int getIdBankSelected() {
        return idBankSelected;
    }

    public void setIdBankSelected(int idBankSelected) {
        this.idBankSelected = idBankSelected;
    }

    public int getIdAccountTypeSelected() {
        return idAccountTypeSelected;
    }

    public void setIdAccountTypeSelected(int idAccountTypeSelected) {
        this.idAccountTypeSelected = idAccountTypeSelected;
    }

    public int getIdAccountSignSelected() {
        return idAccountSignSelected;
    }

    public void setIdAccountSignSelected(int idAccountSignSelected) {
        this.idAccountSignSelected = idAccountSignSelected;
    }

    public int getIdOrgSelected() {
        return idOrgSelected;
    }

    public void setIdOrgSelected(int idOrgSelected) {
        this.idOrgSelected = idOrgSelected;
    }

    public ArrayList getListOfBankAllowed() {
        return listOfBankAllowed;
    }

    public void setListOfBankAllowed(ArrayList listOfBankAllowed) {
        this.listOfBankAllowed = listOfBankAllowed;
    }

    public ArrayList getListOfOrgAllowed() {
        return listOfOrgAllowed;
    }

    public void setListOfOrgAllowed(ArrayList listOfOrgAllowed) {
        this.listOfOrgAllowed = listOfOrgAllowed;
    }

    public ArrayList getListOfCurrencyAllowed() {
        return listOfCurrencyAllowed;
    }

    public void setListOfCurrencyAllowed(ArrayList listOfCurrencyAllowed) {
        this.listOfCurrencyAllowed = listOfCurrencyAllowed;
    }

    public ArrayList getListOfAccountTypeAllowed() {
        return listOfAccountTypeAllowed;
    }

    public void setListOfAccountTypeAllowed(ArrayList listOfAccountTypeAllowed) {
        this.listOfAccountTypeAllowed = listOfAccountTypeAllowed;
    }

    public ArrayList getListOfAccountSignAllowed() {
        return listOfAccountSignAllowed;
    }

    public void setListOfAccountSignAllowed(ArrayList listOfAccountSignAllowed) {
        this.listOfAccountSignAllowed = listOfAccountSignAllowed;
    }

    public ValueForm() {
    }

    public void finalize() throws Throwable {
        super.finalize();
    }

    public void resetForm() {
        setIdValeurSelected(0);
        setValeurBean(new ElementValueBean());
        if (getValeurMap() != null) getValeurMap().clear();
    }

    public int getIdClientSelected() {
        return idClientSelected;
    }

    public void setIdClientSelected(int idClientSelected) {
        this.idClientSelected = idClientSelected;
    }

    public ArrayList getListOfClientAllowed() {
        return listOfClientAllowed;
    }

    public void setListOfClientAllowed(ArrayList listOfClientAllowed) {
        this.listOfClientAllowed = listOfClientAllowed;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDocControl() {
        return docControl;
    }

    public void setDocControl(boolean docControl) {
        this.docControl = docControl;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public Map getValeurMap1() {
        return valeurMap1;
    }

    public void setValeurMap1(Map valeurMap1) {
        this.valeurMap1 = valeurMap1;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
