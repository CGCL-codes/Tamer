package net.sourceforge.xmlfit.property;

public class PropertyNotFoundException extends RuntimeException {

    private String propertyName;

    public PropertyNotFoundException(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String getMessage() {
        return "The Property with the name: " + propertyName + " was not found";
    }
}
