package com.koossery.adempiere.fe.actions.systemRule.menu.createModifyMenu;

import java.util.ArrayList;
import java.util.Map;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseForm;
import com.koossery.adempiere.fe.beans.systemRules.AD_MenuBean;

public class MenuForm extends KTAdempiereBaseForm {

    public MenuForm() {
    }

    public void finalize() throws Throwable {
        super.finalize();
    }

    private int idMenuSelected;

    private boolean readOnly;

    private boolean summaryLevel;

    private boolean salesTransaction;

    private ArrayList listOfClientAllowed;

    private ArrayList listOfOrgAllowed;

    public ArrayList listOfAction;

    public ArrayList listOfForm;

    public ArrayList listOfProcess;

    public ArrayList listOfWorkflow;

    public ArrayList listOfReport;

    public ArrayList listOfTask;

    public ArrayList listOfWindow;

    public ArrayList listOfWorkbench;

    public ArrayList listOfEntityType;

    private AD_MenuBean menuBean;

    private Map menuMap;

    public Map getMenuMap() {
        return this.menuMap;
    }

    public void setMenuMap(Map _menuMap) {
        this.menuMap = _menuMap;
    }

    private Map menuMap1;

    public Map getMenuMap1() {
        return this.menuMap1;
    }

    public void setMenuMap1(Map _menuMap) {
        this.menuMap1 = _menuMap;
    }

    private int idClientSelected;

    private int idOrgSelected;

    private String nomOrg;

    private String nomClient;

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
        setMenuBean(new AD_MenuBean());
        if (getMenuMap() != null) getMenuMap().clear();
    }

    public int getIdMenuSelected() {
        return idMenuSelected;
    }

    public void setIdMenuSelected(int idMenuSelected) {
        this.idMenuSelected = idMenuSelected;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isSummaryLevel() {
        return summaryLevel;
    }

    public void setSummaryLevel(boolean summaryLevel) {
        this.summaryLevel = summaryLevel;
    }

    public boolean isSalesTransaction() {
        return salesTransaction;
    }

    public void setSalesTransaction(boolean salesTransaction) {
        this.salesTransaction = salesTransaction;
    }

    public ArrayList getListOfClientAllowed() {
        return listOfClientAllowed;
    }

    public void setListOfClientAllowed(ArrayList listOfClientAllowed) {
        this.listOfClientAllowed = listOfClientAllowed;
    }

    public ArrayList getListOfOrgAllowed() {
        return listOfOrgAllowed;
    }

    public void setListOfOrgAllowed(ArrayList listOfOrgAllowed) {
        this.listOfOrgAllowed = listOfOrgAllowed;
    }

    public AD_MenuBean getMenuBean() {
        return menuBean;
    }

    public void setMenuBean(AD_MenuBean menuBean) {
        this.menuBean = menuBean;
    }

    public int getIdOrgSelected() {
        return idOrgSelected;
    }

    public void setIdOrgSelected(int idOrgSelected) {
        this.idOrgSelected = idOrgSelected;
    }

    public ArrayList getListOfAction() {
        return listOfAction;
    }

    public void setListOfAction(ArrayList listOfAction) {
        this.listOfAction = listOfAction;
    }

    public ArrayList getListOfEntityType() {
        return listOfEntityType;
    }

    public void setListOfEntityType(ArrayList listOfEntityType) {
        this.listOfEntityType = listOfEntityType;
    }

    public ArrayList getListOfForm() {
        return listOfForm;
    }

    public void setListOfForm(ArrayList listOfForm) {
        this.listOfForm = listOfForm;
    }

    public ArrayList getListOfProcess() {
        return listOfProcess;
    }

    public void setListOfProcess(ArrayList listOfProcess) {
        this.listOfProcess = listOfProcess;
    }

    public ArrayList getListOfWorkflow() {
        return listOfWorkflow;
    }

    public void setListOfWorkflow(ArrayList listOfWorkflow) {
        this.listOfWorkflow = listOfWorkflow;
    }

    public ArrayList getListOfReport() {
        return listOfReport;
    }

    public void setListOfReport(ArrayList listOfReport) {
        this.listOfReport = listOfReport;
    }

    public ArrayList getListOfTask() {
        return listOfTask;
    }

    public void setListOfTask(ArrayList listOfTask) {
        this.listOfTask = listOfTask;
    }

    public ArrayList getListOfWindow() {
        return listOfWindow;
    }

    public void setListOfWindow(ArrayList listOfWindow) {
        this.listOfWindow = listOfWindow;
    }

    public ArrayList getListOfWorkbench() {
        return listOfWorkbench;
    }

    public void setListOfWorkbench(ArrayList listOfWorkbench) {
        this.listOfWorkbench = listOfWorkbench;
    }

    public int getIdClientSelected() {
        return idClientSelected;
    }

    public void setIdClientSelected(int idClientSelected) {
        this.idClientSelected = idClientSelected;
    }
}
