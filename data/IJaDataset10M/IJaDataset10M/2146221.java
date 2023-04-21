package org.openimmunizationsoftware.dqa.db.model.received.types;

import org.openimmunizationsoftware.dqa.db.model.CodeTable;

public class Address {

    private String street = "";

    private String street2 = "";

    private String city = "";

    private CodedEntity state = new CodedEntity(CodeTable.Type.ADDRESS_STATE);

    private String zip = "";

    private CodedEntity country = new CodedEntity(CodeTable.Type.ADDRESS_COUNTRY);

    private CodedEntity countyParish = new CodedEntity(CodeTable.Type.ADDRESS_COUNTY);

    private CodedEntity type = new CodedEntity(CodeTable.Type.ADDRESS_TYPE);

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public CodedEntity getState() {
        return state;
    }

    public String getStateCode() {
        return state.getCode();
    }

    public void setStateCode(String stateCode) {
        this.state.setCode(stateCode);
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public CodedEntity getCountry() {
        return country;
    }

    public String getCountryCode() {
        return country.getCode();
    }

    public void setCountryCode(String countryCode) {
        this.country.setCode(countryCode);
    }

    public CodedEntity getCountyParish() {
        return countyParish;
    }

    public void setCountyParishCode(String countyParishCode) {
        this.countyParish.setCode(countyParishCode);
    }

    public String getCountyParishCode() {
        return countyParish.getCode();
    }

    public CodedEntity getType() {
        return type;
    }

    public String getTypeCode() {
        return type.getCode();
    }

    public void setTypeCode(String typeCode) {
        this.type.setCode(typeCode);
    }
}
