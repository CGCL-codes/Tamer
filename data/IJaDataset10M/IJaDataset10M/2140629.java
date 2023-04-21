package cruise.umple.compiler;

/**
 * Represents a constant.
 */
public class Constant {

    private String name;

    private String type;

    private String modifier;

    private String value;

    public Constant(String aName, String aType, String aModifier, String aValue) {
        name = aName;
        type = aType;
        modifier = aModifier;
        value = aValue;
    }

    public boolean setName(String aName) {
        boolean wasSet = false;
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
   * The name of the constant.
   */
    public String getName() {
        return name;
    }

    /**
   * The type of the constant.
   */
    public String getType() {
        return type;
    }

    /**
   * The modifier of the constant.
   */
    public String getModifier() {
        return modifier;
    }

    /**
   * The value of the constant.
   */
    public String getValue() {
        return value;
    }

    public void delete() {
    }
}
