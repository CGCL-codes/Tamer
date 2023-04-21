package com.koossery.adempiere.fe.actions.security.role.userAssigment.createModifyUserAssigment;

import java.util.ArrayList;
import java.util.Map;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseForm;
import com.koossery.adempiere.fe.beans.security.user.userRole.AD_User_RolesBean;

public class UserAssigmentForm extends KTAdempiereBaseForm {

    public UserAssigmentForm() {
    }

    public void finalize() throws Throwable {
        super.finalize();
    }

    private ArrayList listOfClientAllowed;

    private ArrayList listOfOrgAllowed;

    public ArrayList listOfUserAllowed;

    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    private int idUserAssignmentSelected;

    public int getIdUserAssignmentSelected() {
        return this.idUserAssignmentSelected;
    }

    public void setIdUserAssignmentSelected(int _idUserAssignmentSelected) {
        this.idUserAssignmentSelected = _idUserAssignmentSelected;
    }

    private AD_User_RolesBean userAssignmentBean;

    private Map userAssignmentMap;

    public Map getUserAssignmentMap() {
        return this.userAssignmentMap;
    }

    public void setUserAssignmentMap(Map _userAssignmentMap) {
        this.userAssignmentMap = _userAssignmentMap;
    }

    private Map userAssignmentMap1;

    public Map getUserAssignmentMap1() {
        return this.userAssignmentMap1;
    }

    public void setUserAssignmentMap1(Map _userAssignmentMap) {
        this.userAssignmentMap1 = _userAssignmentMap;
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
        setIdUserAssignmentSelected(0);
        setUserAssignmentBean(new AD_User_RolesBean());
        if (getUserAssignmentMap() != null) getUserAssignmentMap().clear();
    }

    public int getIdOrgSelected() {
        return idOrgSelected;
    }

    public void setIdOrgSelected(int idOrgSelected) {
        this.idOrgSelected = idOrgSelected;
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

    public ArrayList getListOfUserAllowed() {
        return listOfUserAllowed;
    }

    public void setListOfUserAllowed(ArrayList listOfUserAllowed) {
        this.listOfUserAllowed = listOfUserAllowed;
    }

    public AD_User_RolesBean getUserAssignmentBean() {
        return userAssignmentBean;
    }

    public void setUserAssignmentBean(AD_User_RolesBean userAssignmentBean) {
        this.userAssignmentBean = userAssignmentBean;
    }
}
