package org.apache.shindig.social.core.model;

import org.apache.shindig.social.opensocial.model.Address;
import org.apache.shindig.social.opensocial.model.Organization;
import java.util.Date;

public class OrganizationImpl implements Organization {

    private Address address;

    private String description;

    private Date endDate;

    private String field;

    private String name;

    private String salary;

    private Date startDate;

    private String subField;

    private String title;

    private String webpage;

    private String type;

    private Boolean primary;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndDate() {
        if (endDate == null) {
            return null;
        }
        return new Date(endDate.getTime());
    }

    public void setEndDate(Date endDate) {
        if (endDate == null) {
            this.endDate = null;
        } else {
            this.endDate = new Date(endDate.getTime());
        }
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public Date getStartDate() {
        if (startDate == null) {
            return null;
        }
        return new Date(startDate.getTime());
    }

    public void setStartDate(Date startDate) {
        if (startDate == null) {
            this.startDate = null;
        } else {
            this.startDate = new Date(startDate.getTime());
        }
    }

    public String getSubField() {
        return subField;
    }

    public void setSubField(String subField) {
        this.subField = subField;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }
}