package org.apache.myfaces.examples.inputSuggestAjax;

public class City {

    private String _city;

    private String _stateName;

    private String _stateCode;

    private String _zip;

    public City(String city, String stateName, String stateCode, String zip) {
        _city = city;
        _stateName = stateName;
        _stateCode = stateCode;
        _zip = zip;
    }

    public String getCity() {
        return _city;
    }

    public String getStateName() {
        return _stateName;
    }

    public String getStateCode() {
        return _stateCode;
    }

    public String getZip() {
        return _zip;
    }
}
