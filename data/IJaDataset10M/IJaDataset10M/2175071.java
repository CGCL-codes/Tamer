package com.rooster.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;
import com.rooster.general.MyValidations;

public class RecRegistrationForm extends ActionForm {

    private String firstname = new String();

    private String midinitial = new String();

    private String lastname = new String();

    private String email = new String();

    private String phone1 = new String();

    private String phone2 = new String();

    private String phone3 = new String();

    private String h_addr = new String();

    private String h_city = new String();

    private String h_state = new String();

    private String h_zip = new String();

    private String exp = new String();

    private String category = new String();

    private String repstaff = new String();

    private String repstaffname = new String();

    MyValidations myVal = new MyValidations();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getH_addr() {
        return h_addr;
    }

    public void setH_addr(String h_addr) {
        this.h_addr = h_addr;
    }

    public String getH_city() {
        return h_city;
    }

    public void setH_city(String h_city) {
        this.h_city = h_city;
    }

    public String getH_state() {
        return h_state;
    }

    public void setH_state(String h_state) {
        this.h_state = h_state;
    }

    public String getH_zip() {
        return h_zip;
    }

    public void setH_zip(String h_zip) {
        this.h_zip = h_zip;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMidinitial() {
        return midinitial;
    }

    public void setMidinitial(String midinitial) {
        this.midinitial = midinitial;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRepstaff() {
        return repstaff;
    }

    public void setRepstaff(String repstaff) {
        this.repstaff = repstaff;
    }

    public String getRepstaffname() {
        return repstaffname;
    }

    public void setRepstaffname(String repstaffname) {
        this.repstaffname = repstaffname;
    }

    public ActionErrors validate(ActionMapping map, HttpServletRequest req) {
        ActionErrors errors = new ActionErrors();
        if (category.equals("-Select-")) {
            errors.add("nullvalue", new ActionError("not_a_valid_selection"));
        }
        if (firstname.length() == 0) {
            errors.add("required", new ActionError("fristname"));
        }
        if (lastname.length() == 0) {
            errors.add("required", new ActionError("lastname"));
        }
        boolean validEmail = myVal.isEmail(email);
        if (email.length() == 0 || email.length() < 4 || email.equals(null)) {
            this.email = "";
            errors.add("required", new ActionError("email"));
        } else if (validEmail == false) {
            this.email = "";
            errors.add("required", new ActionError("email_wrong"));
        }
        boolean validphone1 = myVal.isNumeric(phone1);
        boolean validphone2 = myVal.isNumeric(phone2);
        boolean validphone3 = myVal.isNumeric(phone3);
        if (phone1.length() == 0 || phone2.length() == 0 || phone3.length() == 0) {
            errors.add("required", new ActionError("phone.required"));
        } else if (phone1.length() != 3 || phone2.length() != 3 || phone3.length() != 4 || validphone1 == false || validphone2 == false || validphone3 == false) {
            errors.add("required", new ActionError("phone"));
        }
        if (h_addr.length() == 0) {
            errors.add("required", new ActionError("h_addr"));
        }
        if (h_city.length() == 0) {
            errors.add("required", new ActionError("h_city"));
        }
        if (h_state.length() == 0) {
            errors.add("required", new ActionError("h_state"));
        }
        boolean validZip = myVal.isNumeric(h_zip);
        if (h_zip.length() == 0) {
            errors.add("required", new ActionError("h_zip.required"));
        } else if ((h_zip.length() < 5) || (h_zip.length() > 9) || (validZip == false) || h_zip.equals("00000")) {
            errors.add("nullvalue", new ActionError("h_zip"));
        }
        if (exp.equals("select")) {
            errors.add("required", new ActionError("exp"));
        }
        return errors;
    }
}
