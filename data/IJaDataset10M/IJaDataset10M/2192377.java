package edu.udo.scaffoldhunter.model.db;

/**
 * @author Till Schäfer, Thomas Schmitz
 * 
 */
public class ScaffoldStringProperty extends StringProperty {

    private Scaffold scaffold;

    private PropertyDefinition type;

    private String value;

    /**
     * default constructor
     */
    public ScaffoldStringProperty() {
    }

    /**
     * @param type
     * @param value
     */
    public ScaffoldStringProperty(PropertyDefinition type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * @return the type
     */
    @Override
    public PropertyDefinition getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    @Override
    public void setType(PropertyDefinition type) {
        this.type = type;
    }

    /**
     * @return the value
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    @Override
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the scaffold
     */
    public Scaffold getScaffold() {
        return scaffold;
    }

    /**
     * @param scaffold
     *            the scaffold to set
     */
    public void setScaffold(Scaffold scaffold) {
        this.scaffold = scaffold;
    }
}
