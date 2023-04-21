package org.addressformats.upu.s42.impl;

import org.addressformats.upu.s42.CountryLevelInformation;
import org.addressformats.upu.s42.DeliveryPointLocation;
import org.addressformats.upu.s42.DeliveryPointSpecification;
import org.addressformats.upu.s42.Locality;
import org.addressformats.upu.s42.ServicePointIdentifier;

public class DefaultDeliveryPointSpecification implements DeliveryPointSpecification {

    protected ServicePointIdentifier servicePointIdentifier;

    protected String definingAuthority;

    protected String primaryPostcode;

    protected String secondaryPostcode;

    protected String tertiaryPostcode;

    protected CountryLevelInformation countryLevelInformation;

    protected Locality locality;

    protected DeliveryPointLocation deliveryPointLocation;

    public ServicePointIdentifier getServicePointIdentifier() {
        return servicePointIdentifier;
    }

    public void setServicePointIdentifier(ServicePointIdentifier servicePointIdentifier) {
        this.servicePointIdentifier = servicePointIdentifier;
    }

    public String getDefiningAuthority() {
        return definingAuthority;
    }

    public void setDefiningAuthority(String definingAuthority) {
        this.definingAuthority = definingAuthority;
    }

    public String getPrimaryPostcode() {
        return primaryPostcode;
    }

    public void setPrimaryPostcode(String primaryPostcode) {
        this.primaryPostcode = primaryPostcode;
    }

    public String getSecondaryPostcode() {
        return secondaryPostcode;
    }

    public void setSecondaryPostcode(String secondaryPostcode) {
        this.secondaryPostcode = secondaryPostcode;
    }

    public String getTertiaryPostcode() {
        return tertiaryPostcode;
    }

    public void setTertiaryPostcode(String tertiaryPostcode) {
        this.tertiaryPostcode = tertiaryPostcode;
    }

    public CountryLevelInformation getCountryLevelInformation() {
        return countryLevelInformation;
    }

    public void setCountryLevelInformation(CountryLevelInformation countryLevelInformation) {
        this.countryLevelInformation = countryLevelInformation;
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    public DeliveryPointLocation getDeliveryPointLocation() {
        return deliveryPointLocation;
    }

    public void setDeliveryPointLocation(DeliveryPointLocation deliveryPointLocation) {
        this.deliveryPointLocation = deliveryPointLocation;
    }
}
