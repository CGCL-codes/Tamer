package com.esri.gpt.server.csw.provider.components;

/**
 * Defines a supported parameter and it's possible values.
 */
public class SupportedParameter {

    /** instance variables ====================================================== */
    private String name;

    private ISupportedValues supportedValues;

    /** Default constructor */
    public SupportedParameter() {
    }

    /**
   * Constructs with a supplied parameter name and a collection of supported values.
   * @param name the parameter name
   * @param supportedValues the supported values
   */
    public SupportedParameter(String name, ISupportedValues supportedValues) {
        this.setName(name);
        this.setSupportedValues(supportedValues);
    }

    /**
   * Gets the parameter name.
   * @return the parameter name
   */
    public String getName() {
        return this.name;
    }

    /**
   * Sets the parameter name.
   * @param name the parameter name
   */
    public void setName(String name) {
        this.name = name;
    }

    /**
   * Gets the supported values.
   * @return the supported values
   */
    public ISupportedValues getSupportedValues() {
        return this.supportedValues;
    }

    /**
   * Sets the supported values.
   * @param supportedValues the supported values
   */
    public void setSupportedValues(ISupportedValues supportedValues) {
        this.supportedValues = supportedValues;
    }
}
