package genj.gedcom;

import java.util.Vector;
import genj.util.*;

/**
 * Gedcom Property : abc
 */
public class PropertyIndividualAttribute extends Property {

    /** the attribute tag */
    private String tag;

    /** the value */
    private String value;

    /**
   * Constructor of address Gedcom-line
   */
    public PropertyIndividualAttribute(String initTag) {
        tag = initTag;
    }

    /**
   * Constructor of address Gedcom-line
   */
    public PropertyIndividualAttribute(String tag, String value) {
        this.tag = tag;
        setValue(value);
    }

    /**
   * Adds all default properties to this property
   */
    public void addDefaultProperties() {
        if (getTag().equals("RESI")) {
            addProperty(new PropertyAddress());
            addProperty(new PropertyDate());
            return;
        }
    }

    /**
   * Accessor for Tag
   */
    public String getTag() {
        return tag;
    }

    /**
   * Accessor for Value
   */
    public String getValue() {
        return value;
    }

    /**
   * Accessor for Value
   */
    public boolean setValue(String value) {
        noteModifiedProperty();
        this.value = value;
        return true;
    }
}
