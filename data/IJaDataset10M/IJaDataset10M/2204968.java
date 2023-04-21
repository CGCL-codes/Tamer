package org.javascriptxmldoc.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * An function or constructor argument
 * @author Luis Fernando Planella Gonzalez
 */
public abstract class Argument extends BaseEntity implements Enumerable {

    private String defaultValue = null;

    private List enums = new ArrayList();

    private String type = null;

    /**
     * @see org.javascriptxmldoc.meta.Enumerable#addEnum(org.javascriptxmldoc.meta.Enum)
     */
    public void addEnum(Enum enumItem) {
        enums.add(enumItem);
    }

    /**
     * Property Getter
     * @return Returns the default value.
     */
    public String getDefault() {
        return defaultValue;
    }

    /**
     * @see org.javascriptxmldoc.meta.Enumerable#getEnums()
     */
    public List getEnums() {
        return enums;
    }

    /**
     * Property Getter
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }

    /**
     * @see org.javascriptxmldoc.meta.Enumerable#isEnumerated()
     */
    public boolean isEnumerated() {
        return !enums.isEmpty();
    }

    /**
     * Property Setter
     * @param defaultValue The default value to set.
     */
    public void setDefault(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Property Setter
     * @param enums The enums to set.
     */
    public void setEnums(List enums) {
        this.enums = enums;
    }

    /**
     * Property Setter
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }
}
