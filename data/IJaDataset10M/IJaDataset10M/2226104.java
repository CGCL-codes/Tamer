package genj.gedcom;

import java.util.Vector;
import genj.util.*;

/**
 * Gedcom Property : XYZ
 */
public class PropertyGenericAttribute extends Property {

    /** A generic Attribute's tag */
    private String tag;

    /** the value */
    private String value;

    /**
   * Constructor of address Gedcom-line
   */
    public PropertyGenericAttribute(String initTag) {
        tag = initTag;
    }

    /**
   * Constructor of address Gedcom-line
   */
    public PropertyGenericAttribute(String tag, String value) {
        this.tag = tag;
        setValue(value);
    }

    /**
   * Returns the tag of this property
   */
    public String getTag() {
        return tag;
    }

    /**
   * Returns the value of this property
   */
    public String getValue() {
        return value;
    }

    /**
   * Sets the value of this property
   */
    public boolean setValue(String value) {
        noteModifiedProperty();
        this.value = value;
        return true;
    }
}
