package com.tinywebgears.tuatara.webapp.gui.model;

public class CreateBusinessProperties extends CreateAbstractContactProperties {

    private BusinessProperties businessProperties;

    public CreateBusinessProperties(BusinessProperties businessProperties, ContactProperties contactProperties) {
        super(contactProperties);
        this.businessProperties = businessProperties;
    }

    public BusinessProperties getBusinessProperties() {
        return businessProperties;
    }

    public void setBusinessProperties(BusinessProperties properties) {
        this.businessProperties = properties;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[");
        sb.append("businessProperties=").append(businessProperties);
        sb.append(",").append("contactProperties=").append(getContactProperties());
        sb.append("]");
        return sb.toString();
    }
}
