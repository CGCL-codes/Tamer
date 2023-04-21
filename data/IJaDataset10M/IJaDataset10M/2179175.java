package com.amazon.webservices.awsecommerceservice.x20090201;

public class HelpRequest implements java.io.Serializable {

    private java.lang.String about;

    public java.lang.String getAbout() {
        return this.about;
    }

    public void setAbout(java.lang.String about) {
        this.about = about;
    }

    private com.amazon.webservices.awsecommerceservice.x20090201.HelpType helpType;

    public com.amazon.webservices.awsecommerceservice.x20090201.HelpType getHelpType() {
        return this.helpType;
    }

    public void setHelpType(com.amazon.webservices.awsecommerceservice.x20090201.HelpType helpType) {
        this.helpType = helpType;
    }

    private java.lang.String[] responseGroup;

    public java.lang.String[] getResponseGroup() {
        return this.responseGroup;
    }

    public void setResponseGroup(java.lang.String[] responseGroup) {
        this.responseGroup = responseGroup;
    }
}
