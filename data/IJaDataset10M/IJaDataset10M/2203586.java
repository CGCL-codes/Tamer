package com.volantis.mcs.eclipse.ab.editors.xml.schema;

/**
 * Represents the definition of an attribute within an
 * {@link ElementDefinition}. 
 */
public class AttributeDefinition {

    /**
     * This attribute's name.
     */
    private String name;

    /**
     * Indicates if the attribute is mandatory or optional.
     */
    private boolean required;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name     the attribute's name
     * @param required <code>true</code> if the attribute is mandatory
     */
    public AttributeDefinition(String name, boolean required) {
        this.name = name;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }
}
