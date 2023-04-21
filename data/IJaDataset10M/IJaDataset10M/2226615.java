package com.be.http.forms;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class TokenForm extends ValidatorForm {

    private static final long serialVersionUID = 3248103945142992595L;

    private String action;

    private long id;

    private String shortName;

    private String name;

    private String password;

    private String token;

    private String validFrom;

    private String validTo;

    public void setAction(String action) {
        this.action = action;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public String getAction() {
        return action;
    }

    public long getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
        }
        action = "";
        id = 0;
        shortName = "";
        name = "";
        password = "";
        token = "";
        validFrom = "";
        validTo = "";
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
        }
        ActionErrors errors = super.validate(mapping, request);
        return errors;
    }

    public String toString() {
        return action + ";" + id + ";" + shortName + ";" + name + ";" + password + ";" + token + ";" + validFrom + ";" + validTo;
    }
}
