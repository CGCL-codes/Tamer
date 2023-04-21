package com.dcivision.alert.web;

import com.dcivision.framework.web.AbstractSearchForm;

/**
  ListUpdateAlertSummaryForm.java

  The ActionForm for ListUserRole

    @author          Zoe Shum
    @company         DCIVision Limited
    @creation date   30/01/2003
    @version         $Revision: 1.5 $
*/
public class ListUpdateAlertSummaryForm extends AbstractSearchForm {

    public static final String REVISION = "$Revision: 1.5 $";

    private String curFunctionCode = null;

    private String objectType = null;

    private String objectID = null;

    private String actionType = null;

    private String updateAlertID = null;

    private String updateAlertTypeID = null;

    private String updateAlertSystemLogID = null;

    private String message = null;

    public ListUpdateAlertSummaryForm() {
        super();
        this.setSortAttribute(null);
        this.setSortOrder("ASC");
    }

    public void setCurFunctionCode(String curFunctionCode) {
        this.curFunctionCode = curFunctionCode;
    }

    public String getCurFunctionCode() {
        return curFunctionCode;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectType() {
        return this.objectType;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getObjectID() {
        return this.objectID;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionType() {
        return this.actionType;
    }

    public void setUpdateAlertID(String updateAlertID) {
        this.updateAlertID = updateAlertID;
    }

    public String getUpdateAlertID() {
        return this.updateAlertID;
    }

    public void setUpdateAlertTypeID(String updateAlertTypeID) {
        this.updateAlertTypeID = updateAlertTypeID;
    }

    public String getUpdateAlertTypeID() {
        return this.updateAlertTypeID;
    }

    public void setUpdateAlertSystemLogID(String updateAlertSystemLogID) {
        this.updateAlertSystemLogID = updateAlertSystemLogID;
    }

    public String getUpdateAlertSystemLogID() {
        return this.updateAlertSystemLogID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
