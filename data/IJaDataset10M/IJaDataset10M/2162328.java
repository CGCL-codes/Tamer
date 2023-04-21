package com.be.http.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class ResourceParameterForm extends ValidatorForm {

    private static final long serialVersionUID = -5770470892612306871L;

    private String action;

    private String id;

    private String type;

    private String locale;

    private String key;

    private String value;

    private String modified;

    private String validFrom;

    private String validTo;

    private String searchString;

    public void setAction(String action) {
        this.action = action;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getAction() {
        return action;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getLocale() {
        return locale;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getModified() {
        return modified;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public String getSearchString() {
        return searchString;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        action = "";
        id = "";
        type = "";
        locale = "";
        key = "";
        value = "";
        modified = "";
        validFrom = "";
        validTo = "";
        searchString = "";
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        return errors;
    }

    public String toString() {
        return action + ";" + id + ";" + type + ";" + locale + ";" + key + ";" + value + ";" + modified + ";" + validFrom + ";" + validTo + ";" + searchString;
    }
}
