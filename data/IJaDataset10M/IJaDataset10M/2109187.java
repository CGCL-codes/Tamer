package de.mogwai.common.web.jsfcodegen.configuration;

public class Property {

    private String propertyName;

    private String propertyType;

    private boolean generateInComponent = false;

    private boolean required = false;

    private boolean generateInTag = false;

    private String defaultValue;

    /**
     * Gibt den Wert des Attributs <code>propertyName</code> zur�ck.
     * 
     * @return Wert des Attributs propertyName.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Setzt den Wert des Attributs <code>propertyName</code>.
     * 
     * @param propertyName
     *                Wert f�r das Attribut propertyName.
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Gibt den Wert des Attributs <code>propertyType</code> zur�ck.
     * 
     * @return Wert des Attributs propertyType.
     */
    public String getPropertyType() {
        return propertyType;
    }

    /**
     * Setzt den Wert des Attributs <code>propertyType</code>.
     * 
     * @param propertyType
     *                Wert f�r das Attribut propertyType.
     */
    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    /**
     * @return the required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * @param required
     *                the required to set
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * Gibt den Wert des Attributs <code>generateInTag</code> zur�ck.
     * 
     * @return Wert des Attributs generateInTag.
     */
    public boolean isGenerateInTag() {
        return generateInTag;
    }

    /**
     * Setzt den Wert des Attributs <code>generateInTag</code>.
     * 
     * @param generateInTag
     *                Wert f�r das Attribut generateInTag.
     */
    public void setGenerateInTag(boolean generateInTag) {
        this.generateInTag = generateInTag;
    }

    /**
     * Gibt den Wert des Attributs <code>generateInComponent</code> zur�ck.
     * 
     * @return Wert des Attributs generateInComponent.
     */
    public boolean isGenerateInComponent() {
        return generateInComponent;
    }

    /**
     * Setzt den Wert des Attributs <code>generateInComponent</code>.
     * 
     * @param generateInComponent
     *                Wert f�r das Attribut generateInComponent.
     */
    public void setGenerateInComponent(boolean generateInComponent) {
        this.generateInComponent = generateInComponent;
    }

    /**
     * Gibt den Wert des Attributs <code>defaultValue</code> zur�ck.
     * 
     * @return Wert des Attributs defaultValue.
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Setzt den Wert des Attributs <code>defaultValue</code>.
     * 
     * @param defaultValue
     *                Wert f�r das Attribut defaultValue.
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
