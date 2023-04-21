package com.creditors.http.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class TrxDetailForm extends ValidatorForm {

    private static final long serialVersionUID = 5997820285900930876L;

    private String action;

    private long id;

    private long trxID;

    private long referenceID;

    private long typeID;

    private String typeName;

    private String modified;

    public void setAction(String action) {
        this.action = action;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTrxID(long trxID) {
        this.trxID = trxID;
    }

    public void setReferenceID(long referenceID) {
        this.referenceID = referenceID;
    }

    public void setTypeID(long typeID) {
        this.typeID = typeID;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getAction() {
        return action;
    }

    public long getId() {
        return id;
    }

    public long getTrxID() {
        return trxID;
    }

    public long getReferenceID() {
        return referenceID;
    }

    public long getTypeID() {
        return typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getModified() {
        return modified;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        action = "";
        id = 0;
        trxID = 0;
        referenceID = 0;
        typeID = 0;
        typeName = "";
        modified = "";
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        return errors;
    }

    public String toString() {
        return action + ";" + id + ";" + trxID + ";" + referenceID + ";" + typeID + ";" + typeName + ";" + modified;
    }
}
