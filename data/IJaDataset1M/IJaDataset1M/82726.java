package org.saosis.core.models;

/**
 * A model of a geographic location.
 * 
 * @author Daniel Allen Prust (danprust@yahoo.com)
 * 
 */
public class GeographicLocation implements IEmptiable {

    private Vocabularies.Countries country;

    private String locality;

    private String region;

    public GeographicLocation() {
        empty();
    }

    public void empty() {
        setCountry(null);
        setLocality(null);
        setRegion(null);
    }

    public Vocabularies.Countries getCountry() {
        return this.country;
    }

    public String getLocality() {
        return this.locality;
    }

    public String getRegion() {
        return this.region;
    }

    public boolean isEmpty() {
        if (getCountry() == Vocabularies.Countries.EMPTY && getLocality().isEmpty() && getRegion().isEmpty()) return true;
        return false;
    }

    public void setCountry(Vocabularies.Countries country) {
        this.country = country == null ? Vocabularies.Countries.EMPTY : country;
    }

    public void setLocality(String locality) {
        this.locality = locality == null ? "" : locality.trim();
    }

    public void setRegion(String region) {
        this.region = region == null ? "" : region.trim();
    }
}
