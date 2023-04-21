package app;

import java.util.Set;
import java.util.TreeSet;

public class Country {

    private String name;

    private Set<Province> provinces = new TreeSet<Province>();

    private Country(String name) {
        this.name = name;
    }

    public static Country canada() {
        Country canada = new Country("CANADA");
        canada.provinces.add(new Province("ALBERTA", "AB"));
        canada.provinces.add(new Province("BRITISH COLUMBIA", "BC"));
        canada.provinces.add(new Province("MANITOBA", "MB"));
        canada.provinces.add(new Province("NEW BRUNSWICK", "NB"));
        canada.provinces.add(new Province("NEWFOUNDLAND and LABRADOR", "NL"));
        canada.provinces.add(new Province("NOVA SCOTIA", "NS"));
        canada.provinces.add(new Province("NUNAVUT", "NU"));
        canada.provinces.add(new Province("ONTARIO", "ON"));
        canada.provinces.add(new Province("PRINCE EDWARD ISLAND", "PE"));
        canada.provinces.add(new Province("QUEBEC", "QC"));
        canada.provinces.add(new Province("SASKATCHEWAN", "SK"));
        canada.provinces.add(new Province("YUKON ", "YT"));
        return canada;
    }

    public Set<Province> provinces() {
        return provinces;
    }
}
