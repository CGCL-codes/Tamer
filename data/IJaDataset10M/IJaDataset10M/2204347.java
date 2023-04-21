package com.koossery.adempiere.fe.actions.server.alert.alertLog.createModifyAlertLog;

import java.util.ArrayList;
import java.util.Map;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseForm;
import com.koossery.adempiere.fe.beans.server.AD_AlertProcessorLogBean;

public class AlertLogForm extends KTAdempiereBaseForm {

    public AlertLogForm() {
    }

    public void finalize() throws Throwable {
        super.finalize();
    }

    private AD_AlertProcessorLogBean alertLogBean;

    public AD_AlertProcessorLogBean getAlertLogBean() {
        return this.alertLogBean;
    }

    public void setAlertLogBean(AD_AlertProcessorLogBean _alertLogBean) {
        this.alertLogBean = _alertLogBean;
    }

    private Map alertLogMap;

    public Map getAlertLogMap() {
        return this.alertLogMap;
    }

    public void setAlertLogMap(Map _alertLogMap) {
        this.alertLogMap = _alertLogMap;
    }

    private Map alertLogMap1;

    public Map getAlertLogMap1() {
        return this.alertLogMap1;
    }

    public void setAlertLogMap1(Map _alertLogMap) {
        this.alertLogMap1 = _alertLogMap;
    }

    private ArrayList listOfClientAllowed;

    private ArrayList listOfOrgAllowed;

    private int idAlertLogSelected;

    private int idOrgSelected;

    private String nomOrg;

    private String nomClient;

    private String alertName;

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

    private boolean error;

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
        setAlertLogBean(new AD_AlertProcessorLogBean());
        if (getAlertLogMap() != null) getAlertLogMap().clear();
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

    public int getIdAlertLogSelected() {
        return idAlertLogSelected;
    }

    public void setIdAlertLogSelected(int idAlertLogSelected) {
        this.idAlertLogSelected = idAlertLogSelected;
    }

    public int getIdOrgSelected() {
        return idOrgSelected;
    }

    public void setIdOrgSelected(int idOrgSelected) {
        this.idOrgSelected = idOrgSelected;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
