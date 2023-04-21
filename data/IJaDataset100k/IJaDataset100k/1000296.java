package org.jazzteam.edu.patterns.abstractFactory;

public class USAddress extends Address {

    private static final String COUNTRY = "UNITED STATES";

    private static final String COMMA = ",";

    @Override
    public String getCountry() {
        return COUNTRY;
    }

    public String getFullAddrress() {
        return getStreet() + EOL_STRING + getCity() + COMMA + SPACE + getRegion() + SPACE + getPostalCode() + EOL_STRING + COUNTRY;
    }
}
