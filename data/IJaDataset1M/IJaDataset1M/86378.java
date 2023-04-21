package com.gxk.action;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public final class HelloWorldForm extends ActionForm {

    private String person = null;

    public String getPerson() {
        return (this.person);
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.person = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return (null);
    }
}
