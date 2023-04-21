package com.pr.http.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class WageRecordParameterForm extends ValidatorForm {

    private static final long serialVersionUID = -3815551329359478788L;

    private String action;

    private String id;

    private String companyID;

    private String name;

    private String type;

    private String percentage;

    private String amount;

    private String validFrom;

    private String validTo;

    private String searchString;

    public void setAction(String action) {
        this.action = action;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getCompanyID() {
        return companyID;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPercentage() {
        return percentage;
    }

    public String getAmount() {
        return amount;
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
        companyID = "";
        name = "";
        type = "";
        percentage = "";
        amount = "";
        validFrom = "";
        validTo = "";
        searchString = "";
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        return errors;
    }

    public String toString() {
        return action + ";" + id + ";" + companyID + ";" + name + ";" + type + ";" + percentage + ";" + amount + ";" + validFrom + ";" + validTo + ";" + searchString;
    }
}
