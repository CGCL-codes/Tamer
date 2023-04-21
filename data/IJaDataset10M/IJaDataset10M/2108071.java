package de.molimo.container.exceptions;

/**
 @author Marcus Schiesser
 */
public class PropertyNotFoundException extends AbstractNotFoundException {

    public PropertyNotFoundException(String propName) {
        super(propName);
    }

    public String getType() {
        return "property";
    }
}
