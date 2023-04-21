package net.entelijan.cobean.examples.util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Locale {

    private String language;

    private String country;

    private String displayLanguage;

    private String displayCountry;

    private String iso3Language;

    private String iso3Country;

    public Locale(java.util.Locale locale) {
        super();
        this.language = locale.getLanguage();
        this.country = locale.getCountry();
        this.displayLanguage = locale.getDisplayLanguage(java.util.Locale.ENGLISH);
        this.displayCountry = locale.getDisplayCountry(java.util.Locale.ENGLISH);
        this.iso3Country = locale.getISO3Language();
        this.iso3Language = locale.getISO3Language();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDisplayLanguage() {
        return displayLanguage;
    }

    public void setDisplayLanguage(String displayLanguage) {
        this.displayLanguage = displayLanguage;
    }

    public String getDisplayCountry() {
        return displayCountry;
    }

    public void setDisplayCountry(String displayCountry) {
        this.displayCountry = displayCountry;
    }

    public String getISO3Language() {
        return iso3Language;
    }

    public void setISO3Language(String iSO3Language) {
        iso3Language = iSO3Language;
    }

    public String getISO3Country() {
        return iso3Country;
    }

    public void setISO3Country(String iSO3Country) {
        iso3Country = iSO3Country;
    }
}
