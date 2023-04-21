package org.nakedobjects.progmodel.facets.jsr303;

public class DomainObjectWithCustomValidation {

    private String serialNumber;

    @CustomPatterns({ @CustomPattern(regex = "^[A-Z0-9-]+$", message = "must contain alphabetical characters only"), @CustomPattern(regex = "^....-....-....$", message = "must match ....-....-....") })
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
