package com.koossery.adempiere.fe.actions.hr.job.createModifyJob;

import java.util.ArrayList;
import java.util.Map;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseForm;
import com.koossery.adempiere.fe.beans.humanResources.job.JobBean;

/**
 * @author Cedrick Essale
 *
 */
public class JobForm extends KTAdempiereBaseForm {

    private int idClientSelected;

    private int idDepartementSelected;

    private int idOrgSelected;

    private int idJobSelected;

    private ArrayList listOfClientAllowed;

    public ArrayList listOfDepartementAllowed;

    public ArrayList listOfOrgAllowed;

    private JobBean jobBean;

    private Map jobMap;

    private Map jobMap1;

    private int flag;

    private int display;

    private boolean active;

    private boolean parent;

    private String nomOrg;

    private String nomClient;

    public JobBean getJobBean() {
        return jobBean;
    }

    public void setJobBean(JobBean jobBean) {
        this.jobBean = jobBean;
    }

    public Map getJobMap() {
        return jobMap;
    }

    public void setJobMap(Map jobMap) {
        this.jobMap = jobMap;
    }

    public Map getJobMap1() {
        return jobMap1;
    }

    public void setJobMap1(Map jobMap1) {
        this.jobMap1 = jobMap1;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public int getIdClientSelected() {
        return idClientSelected;
    }

    public void setIdClientSelected(int idClientSelected) {
        this.idClientSelected = idClientSelected;
    }

    public int getIdDepartementSelected() {
        return idDepartementSelected;
    }

    public void setIdDepartementSelected(int idDepartementSelected) {
        this.idDepartementSelected = idDepartementSelected;
    }

    public int getIdOrgSelected() {
        return idOrgSelected;
    }

    public void setIdOrgSelected(int idOrgSelected) {
        this.idOrgSelected = idOrgSelected;
    }

    public int getIdJobSelected() {
        return idJobSelected;
    }

    public void setIdJobSelected(int idJobSelected) {
        this.idJobSelected = idJobSelected;
    }

    public ArrayList getListOfClientAllowed() {
        return listOfClientAllowed;
    }

    public ArrayList getListOfDepartementAllowed() {
        return listOfDepartementAllowed;
    }

    public ArrayList getListOfOrgAllowed() {
        return listOfOrgAllowed;
    }

    public void setListOfClientAllowed(ArrayList listOfClientAllowed) {
        this.listOfClientAllowed = listOfClientAllowed;
    }

    public void setListOfDepartementAllowed(ArrayList listOfDepartementAllowed) {
        this.listOfDepartementAllowed = listOfDepartementAllowed;
    }

    public void setListOfOrgAllowed(ArrayList listOfOrgAllowed) {
        this.listOfOrgAllowed = listOfOrgAllowed;
    }

    public void resetForm() {
        if (getJobMap() != null) getJobMap().clear();
        setJobBean(new JobBean());
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }
}
