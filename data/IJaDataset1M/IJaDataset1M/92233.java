package com.be.http.forms;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class LocationForm extends ValidatorForm {

    private static final long serialVersionUID = -8557056870897746497L;

    private String action;

    private long id;

    private long type1;

    private long plz;

    private long type2;

    private String ort18;

    private String ort;

    private String kanton;

    public void setAction(String action) {
        this.action = action;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setType1(long type1) {
        this.type1 = type1;
    }

    public void setPlz(long plz) {
        this.plz = plz;
    }

    public void setType2(long type2) {
        this.type2 = type2;
    }

    public void setOrt18(String ort18) {
        this.ort18 = ort18;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public void setKanton(String kanton) {
        this.kanton = kanton;
    }

    public String getAction() {
        return action;
    }

    public long getId() {
        return id;
    }

    public long getType1() {
        return type1;
    }

    public long getPlz() {
        return plz;
    }

    public long getType2() {
        return type2;
    }

    public String getOrt18() {
        return ort18;
    }

    public String getOrt() {
        return ort;
    }

    public String getKanton() {
        return kanton;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
        }
        action = "";
        id = 0;
        type1 = 0;
        plz = 0;
        type2 = 0;
        ort18 = "";
        ort = "";
        kanton = "";
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
        return action + ";" + id + ";" + type1 + ";" + plz + ";" + type2 + ";" + ort18 + ";" + ort + ";" + kanton;
    }
}
