package com.entelience.objects.directory;

import java.io.Serializable;
import java.util.Date;

/**
 * BEAN for location history
 */
public class LocationHistory implements Serializable {

    private Integer locationId;

    private String locationName;

    private Date changeDate;

    private int modifierId;

    private String modifierName;

    private String building;

    private String site;

    private String city;

    private String countryIso;

    private String zipcode;

    private int regionId;

    private String timezone;

    private boolean deleted;

    public LocationHistory() {
    }

    public void setLocationId(Integer _locationId) {
        locationId = _locationId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationName(String _locationName) {
        locationName = _locationName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setChangeDate(Date _changeDate) {
        changeDate = _changeDate;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setModifierId(int _modifierId) {
        modifierId = _modifierId;
    }

    public int getModifierId() {
        return modifierId;
    }

    public void setModifierName(String _modifierName) {
        modifierName = _modifierName;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setBuilding(String _building) {
        building = _building;
    }

    public String getBuilding() {
        return building;
    }

    public void setSite(String _site) {
        site = _site;
    }

    public String getSite() {
        return site;
    }

    public void setCountryIso(String _countryIso) {
        countryIso = _countryIso;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public void setCity(String _city) {
        city = _city;
    }

    public String getCity() {
        return city;
    }

    public void setZipcode(String _zipcode) {
        zipcode = _zipcode;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setRegionId(int _regionId) {
        regionId = _regionId;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setTimezone(String _timezone) {
        timezone = _timezone;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setDeleted(boolean _deleted) {
        deleted = _deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
