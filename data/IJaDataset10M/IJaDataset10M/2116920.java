package cruise.umple.compiler;

/**
 * Superclass for tracking member variables in all target languages
 * TODO: Code smell because we have both an association variable and also an association
 * This class should go and instead AssociationEnd should be the member variable
 */
public class UmpleVariable {

    private String name;

    private String type;

    private String modifier;

    private String value;

    public UmpleVariable(String aName, String aType, String aModifier, String aValue) {
        name = aName;
        type = aType;
        modifier = aModifier;
        value = aValue;
    }

    public boolean setName(String aName) {
        boolean wasSet = false;
        if (aName == null) {
            throw new RuntimeException("Name must be set, cannot be null");
        }
        name = aName;
        wasSet = true;
        return wasSet;
    }

    public boolean setType(String aType) {
        boolean wasSet = false;
        type = aType;
        wasSet = true;
        return wasSet;
    }

    public boolean setModifier(String aModifier) {
        boolean wasSet = false;
        modifier = aModifier;
        wasSet = true;
        return wasSet;
    }

    public boolean setValue(String aValue) {
        boolean wasSet = false;
        value = aValue;
        wasSet = true;
        return wasSet;
    }

    /**
   * The name of the Umple variable.
   */
    public String getName() {
        return name;
    }

    /**
   * The type of the Umple variable.
   */
    public String getType() {
        return type;
    }

    /**
   * The modifier associated with the Umple variable.
   */
    public String getModifier() {
        if (modifier == null) {
            return "settable";
        }
        return modifier;
    }

    /**
   * potential enum, 'settable'
   * The value of the Umple variable.
   */
    public String getValue() {
        return value;
    }

    public void delete() {
    }

    public String getUpperCaseName() {
        if (name == null || name.length() == 0) {
            return name;
        } else if (name.length() == 1) {
            return name.toUpperCase();
        } else {
            return name.toUpperCase().charAt(0) + name.substring(1);
        }
    }

    public boolean isImmutable() {
        return "immutable".equals(modifier);
    }
}
