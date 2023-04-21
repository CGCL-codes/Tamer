package wsl.fw.report;

import java.awt.Point;

/**
 * Represents a field in a DataObject. Adds entityName to TextFieldElement
 */
public class DataFieldElement extends TextFieldElement {

    private String _entityName = "";

    /**
     * Argument ctor
     * @param entityName the name of the entity
     * @param fieldName the name of the field
     * @param pos the position to draw the text at
     */
    public DataFieldElement(String entityName, String fieldName, WslPos pos) {
        super(fieldName, pos);
        setEntityName(entityName);
    }

    /**
     * Set the entity name
     * @param entityName
     */
    public void setEntityName(String entityName) {
        _entityName = entityName;
    }

    /**
     * @return String the entity name
     */
    public String getEntityName() {
        return _entityName;
    }
}
