package com.medcentrex.bridge.interfaces;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class ApptTypeBean {

    private String apptname;

    private String appttype_id;

    private String sort;

    private String status_id;

    private int recnum = 0;

    private Collection appts;

    public ApptTypeBean() {
        apptname = appttype_id = "";
        status_id = "";
        sort = "";
        appts = null;
        recnum = 0;
    }

    public void setApptTypeName(String ApptName) {
        apptname = ApptName;
    }

    public String getApptTypeName() {
        return apptname;
    }

    public void setStatus_Id(String Status_Id) {
        status_id = Status_Id;
    }

    public String getStatus_Id() {
        return status_id;
    }

    public void setSort(String Sort) {
        sort = Sort;
    }

    public String getSort() {
        return sort;
    }

    public void setRecNum(int RecNum) {
        recnum = RecNum;
    }

    public int getRecNum() {
        return recnum;
    }

    public void setApptList(Collection apptList) {
        appts = apptList;
    }

    public Collection getApptList() {
        return appts;
    }

    public void setApptType_Id(String Appt_Id) {
        appttype_id = Appt_Id;
    }

    public String getApptType_Id() {
        return appttype_id;
    }
}
