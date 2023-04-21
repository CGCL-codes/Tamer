package com.hibernate.mappings;

import java.io.Serializable;
import java.util.Date;

public class Repair_details implements Serializable {

    private Integer repair_id;

    private Date process_date;

    private Date delivery_date;

    private String product_tag_no;

    private String description;

    private String job_type;

    private String priority;

    private String issue;

    private String notes;

    private String assigned_to;

    private String assigned_by;

    private String status;

    public Integer getRepair_id() {
        return repair_id;
    }

    public void setRepair_id(Integer repair_id) {
        this.repair_id = repair_id;
    }

    public Date getProcess_date() {
        return process_date;
    }

    public void setProcess_date(Date process_date) {
        this.process_date = process_date;
    }

    public Date getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(Date delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getProduct_tag_no() {
        return product_tag_no;
    }

    public void setProduct_tag_no(String product_tag_no) {
        this.product_tag_no = product_tag_no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
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

    public String getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(String assigned_to) {
        this.assigned_to = assigned_to;
    }

    public String getAssigned_by() {
        return assigned_by;
    }

    public void setAssigned_by(String assigned_by) {
        this.assigned_by = assigned_by;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
