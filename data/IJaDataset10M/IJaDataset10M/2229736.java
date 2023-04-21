package com.struts.formbeans;

import org.apache.struts.action.ActionForm;

public class POSPRoductRepairActionForm extends ActionForm {

    private String productrepairUserAction = "initialUserAction";

    private String tagno;

    private String description;

    private String jobtype;

    private String priority;

    private String issue;

    private String notes;

    private String assignedby;

    private String processdate;

    private String assignedto;

    private String deliverydate;

    private String status;

    public String getTagno() {
        return tagno;
    }

    public void setTagno(String tagno) {
        this.tagno = tagno;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobtype() {
        return jobtype;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAssignedby() {
        return assignedby;
    }

    public void setAssignedby(String assignedby) {
        this.assignedby = assignedby;
    }

    public String getProcessdate() {
        return processdate;
    }

    public void setProcessdate(String processdate) {
        this.processdate = processdate;
    }

    public String getAssignedto() {
        return assignedto;
    }

    public void setAssignedto(String assignedto) {
        this.assignedto = assignedto;
    }

    public String getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(String deliverydate) {
        this.deliverydate = deliverydate;
    }

    public String getProductrepairUserAction() {
        return productrepairUserAction;
    }

    public void setProductrepairUserAction(String productrepairUserAction) {
        this.productrepairUserAction = productrepairUserAction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
