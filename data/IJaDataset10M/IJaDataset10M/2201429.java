package com.koossery.adempiere.fe.actions.server.request.requestProcessor.createModifyRequestProcessor;

import java.util.ArrayList;
import java.util.Map;
import org.koossery.adempiere.core.contract.dto.server.request.R_RequestProcessorDTO;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseForm;
import com.koossery.adempiere.fe.beans.server.R_RequestProcessorBean;

public class RequestProcessorForm extends KTAdempiereBaseForm {

    public RequestProcessorForm() {
    }

    public void finalize() throws Throwable {
        super.finalize();
    }

    private ArrayList listOfClientAllowed;

    private ArrayList listOfOrgAllowed;

    private int idRequestSelected;

    public ArrayList listOfFrequencyTypeAllowed;

    public ArrayList getListOfFrequencyTypeAllowed() {
        return this.listOfFrequencyTypeAllowed;
    }

    public void setListOfFrequencyTypeAllowed(ArrayList _listOfFrequencyTypeAllowed) {
        this.listOfFrequencyTypeAllowed = _listOfFrequencyTypeAllowed;
    }

    public ArrayList listOfRequestTypeAllowed;

    public ArrayList getListOfRequestTypeAllowed() {
        return this.listOfRequestTypeAllowed;
    }

    public void setListOfRequestTypeAllowed(ArrayList _listOfRequestTypeAllowed) {
        this.listOfRequestTypeAllowed = _listOfRequestTypeAllowed;
    }

    public ArrayList listOfSupervisorTypeAllowed;

    public ArrayList getListOfSupervisorTypeAllowed() {
        return this.listOfSupervisorTypeAllowed;
    }

    public void setListOfSupervisorTypeAllowed(ArrayList _listOfSupervisorTypeAllowed) {
        this.listOfSupervisorTypeAllowed = _listOfSupervisorTypeAllowed;
    }

    private R_RequestProcessorBean requestProcessorBean;

    public R_RequestProcessorBean getRequestProcessorBean() {
        return this.requestProcessorBean;
    }

    public void setRequestProcessorBean(R_RequestProcessorBean _requestProcessorBean) {
        this.requestProcessorBean = _requestProcessorBean;
    }

    private Map requestProcessorMap;

    public Map getRequestProcessorMap() {
        return this.requestProcessorMap;
    }

    public void setRequestProcessorMap(Map _requestProcessorMap) {
        this.requestProcessorMap = _requestProcessorMap;
    }

    private Map requestProcessorMap1;

    public Map getRequestProcessorMap1() {
        return this.requestProcessorMap1;
    }

    public void setRequestProcessorMap1(Map _requestProcessorMap) {
        this.requestProcessorMap1 = _requestProcessorMap;
    }

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
        if (getRequestProcessorMap() != null) getRequestProcessorMap().clear();
        setRequestProcessorBean(new R_RequestProcessorBean());
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

    public int getIdRequestSelected() {
        return idRequestSelected;
    }

    public void setIdRequestSelected(int idRequestSelected) {
        this.idRequestSelected = idRequestSelected;
    }

    public int getIdOrgSelected() {
        return idOrgSelected;
    }

    public void setIdOrgSelected(int idOrgSelected) {
        this.idOrgSelected = idOrgSelected;
    }
}
