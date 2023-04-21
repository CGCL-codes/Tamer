package org.koossery.adempiere.core.contract.criteria.hr;

import org.koossery.adempiere.core.contract.criteria.KTADempiereBaseCriteria;

public class HR_JobCriteria extends KTADempiereBaseCriteria {

    private static final long serialVersionUID = 1L;

    private String description;

    private int hr_Department_ID;

    private int hr_Job_ID;

    private int jobCant;

    private String name;

    private int next_Job_ID;

    private int supervisor_ID;

    private String value;

    private String isParent;

    private String isActive;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHr_Department_ID() {
        return hr_Department_ID;
    }

    public void setHr_Department_ID(int hr_Department_ID) {
        this.hr_Department_ID = hr_Department_ID;
    }

    public int getHr_Job_ID() {
        return hr_Job_ID;
    }

    public void setHr_Job_ID(int hr_Job_ID) {
        this.hr_Job_ID = hr_Job_ID;
    }

    public int getJobCant() {
        return jobCant;
    }

    public void setJobCant(int jobCant) {
        this.jobCant = jobCant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNext_Job_ID() {
        return next_Job_ID;
    }

    public void setNext_Job_ID(int next_Job_ID) {
        this.next_Job_ID = next_Job_ID;
    }

    public int getSupervisor_ID() {
        return supervisor_ID;
    }

    public void setSupervisor_ID(int supervisor_ID) {
        this.supervisor_ID = supervisor_ID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
