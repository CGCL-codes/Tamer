package com.forms;

import org.apache.struts.action.ActionErrors;

public class ApproveForm extends BaseCommunityForm {

    private String type;

    private String objKeyId;

    private String approved;

    public ActionErrors getActionErrors() {
        if (errors == null) {
            errors = new ActionErrors();
        }
        return errors;
    }

    public String getObjKeyId() {
        return objKeyId;
    }

    public void setObjKeyId(String objKeyId) {
        this.objKeyId = objKeyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }
}
