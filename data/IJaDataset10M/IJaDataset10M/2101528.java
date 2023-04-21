package com.mat.param;

public class LocationParameter {

    private String id;

    private String codeAn;

    private String name;

    private String contactID;

    private String modified;

    private String validFrom;

    private String validTo;

    private String searchString;

    public void setId(String id) {
        this.id = id;
    }

    public void setCodeAn(String codeAn) {
        this.codeAn = codeAn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getId() {
        return id;
    }

    public String getCodeAn() {
        return codeAn;
    }

    public String getName() {
        return name;
    }

    public String getContactID() {
        return contactID;
    }

    public String getModified() {
        return modified;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public String getSearchString() {
        return searchString;
    }

    public String toString() {
        return id + ";" + codeAn + ";" + name + ";" + contactID + ";" + modified + ";" + validFrom + ";" + validTo + ";" + searchString;
    }

    public void init() {
    }
}
