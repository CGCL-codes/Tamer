package com.koossery.adempiere.fe.actions.accounting.gl.glCategory;

import java.util.ArrayList;
import java.util.Map;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseForm;
import com.koossery.adempiere.fe.beans.accounting.gl.gLCategory.GLCategoryBean;

public class GLCategoryForm extends KTAdempiereBaseForm {

    private GLCategoryBean glcategoryBean;

    private int idCategorySelected;

    public ArrayList listOfCategoryAllowed;

    private ArrayList listOfClientAllowed;

    private ArrayList listOfOrgAllowed;

    private int idGLCategoryselected;

    private int idClientSelected;

    private int idOrgSelected;

    private Map glCategoryMap;

    private Map glCategoryMap2;

    private String nomOrg;

    private String nomClient;

    private boolean active;

    private boolean defaut;

    private int display;

    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public GLCategoryForm() {
    }

    public int getIdCategorySelected() {
        return idCategorySelected;
    }

    public void setIdCategorySelected(int idCategorySelected) {
        this.idCategorySelected = idCategorySelected;
    }

    public ArrayList getListOfCategoryAllowed() {
        return listOfCategoryAllowed;
    }

    public void setListOfCategoryAllowed(ArrayList listOfCategoryAllowed) {
        this.listOfCategoryAllowed = listOfCategoryAllowed;
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

    public int getIdGLCategoryselected() {
        return idGLCategoryselected;
    }

    public void setIdGLCategoryselected(int idGLCategoryselected) {
        this.idGLCategoryselected = idGLCategoryselected;
    }

    public int getIdClientSelected() {
        return idClientSelected;
    }

    public void setIdClientSelected(int idClientSelected) {
        this.idClientSelected = idClientSelected;
    }

    public int getIdOrgSelected() {
        return idOrgSelected;
    }

    public void setIdOrgSelected(int idOrgSelected) {
        this.idOrgSelected = idOrgSelected;
    }

    public Map getGlCategoryMap() {
        return glCategoryMap;
    }

    public void setGlCategoryMap(Map glCategoryMap) {
        this.glCategoryMap = glCategoryMap;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDefaut() {
        return defaut;
    }

    public void setDefaut(boolean defaut) {
        this.defaut = defaut;
    }

    public void resetForm() {
        setGlcategoryBean(new GLCategoryBean());
        setIdCategorySelected(0);
        if (getGlCategoryMap() != null) getGlCategoryMap().clear();
    }

    public GLCategoryBean getGlcategoryBean() {
        return glcategoryBean;
    }

    public void setGlcategoryBean(GLCategoryBean glcategoryBean) {
        this.glcategoryBean = glcategoryBean;
    }

    public Map getGlCategoryMap2() {
        return glCategoryMap2;
    }

    public void setGlCategoryMap2(Map glCategoryMap2) {
        this.glCategoryMap2 = glCategoryMap2;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }
}
