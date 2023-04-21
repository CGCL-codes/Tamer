package com.rooster.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;
import com.rooster.action.HomeAction;
import com.rooster.general.MyValidations;

public class VendorSearchForm extends ActionForm {

    static Logger logger = Logger.getLogger(VendorSearchForm.class.getName());

    private String mbr = new String();

    private String duration = new String();

    private String temp2perm = new String();

    private String clrjobid = new String();

    private String days = new String();

    private String lesser_or_greater = new String();

    String selectedreqId[];

    MyValidations myvalid = new MyValidations();

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMbr() {
        return mbr;
    }

    public void setMbr(String mbr) {
        this.mbr = mbr;
    }

    public String getTemp2perm() {
        return temp2perm;
    }

    public void setTemp2perm(String temp2perm) {
        this.temp2perm = temp2perm;
    }

    public String[] getSelectedreqId() {
        return selectedreqId;
    }

    public void setSelectedreqId(String[] selectedreqId) {
        this.selectedreqId = selectedreqId;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.selectedreqId = null;
        this.temp2perm = "";
        this.mbr = "";
        this.duration = "";
        this.clrjobid = "";
        this.days = "0";
    }

    ;

    public ActionErrors validate(ActionMapping map, HttpServletRequest req) {
        ActionErrors errors = new ActionErrors();
        if (mbr.length() != 0) {
            boolean validMbr = myvalid.isRateCheck(mbr);
            if (validMbr == false) {
                logger.debug("mbr  : " + mbr);
                this.mbr = "";
                errors.add("nullvalue", new ActionError("mbrInValid.required"));
            }
            if (validMbr == true) {
                int a = Integer.parseInt(mbr);
                if (a < 0) {
                    this.mbr = "";
                    errors.add("nullvalue", new ActionError("mbrInValidNo.required"));
                }
            }
        }
        if (duration.length() != 0) {
            boolean validDuration = myvalid.isRateCheck(duration);
            if (validDuration == false) {
                this.duration = "";
                errors.add("nullvalue", new ActionError("DurationInValid.required"));
            }
        }
        return errors;
    }

    public String getClrjobid() {
        return clrjobid;
    }

    public void setClrjobid(String clrjobid) {
        this.clrjobid = clrjobid;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getLesser_or_greater() {
        return lesser_or_greater;
    }

    public void setLesser_or_greater(String lesser_or_greater) {
        this.lesser_or_greater = lesser_or_greater;
    }
}
