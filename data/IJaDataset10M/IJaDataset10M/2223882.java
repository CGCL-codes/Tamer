package ch.oscg.jreleaseinfo;

/**
 * Property class for JReleaseInfoAntTask task.
 *
 * <p>
 * This class holds three String values
 * </p>
 *
 * <ul>
 * <li>
 * name
 * </li>
 * <li>
 * type
 * </li>
 * <li>
 * value
 * </li>
 * </ul>
 *
 * <p>
 * From this data, we will create a class with fields and getter method.
 * Therefore we need the types to be defined.
 * </p>
 *
 * <p>
 * This types are implemented now:
 * </p>
 *
 * <ul>
 * <li>
 * String
 * </li>
 * <li>
 * Boolean
 * </li>
 * <li>
 * Integer
 * </li>
 * <li>
 * boolean
 * </li>
 * <li>
 * int
 * </li>
 * <li>
 * Date
 * </li>
 * </ul>
 *
 * <p></p>
 *
 * @author Thomas Cotting, Tangarena Engineering AG, Luzern
 * @version $Revision: 1.2 $ ($Date: 2005/08/06 14:12:35 $ / $Author: tcotting $)
 */
public class JReleaseInfoProperty {

    /** String Type. */
    public static final String TYPE_OBJ_STRING = "String";

    /** Boolean Type. */
    public static final String TYPE_OBJ_BOOLEAN = "Boolean";

    /** Integer Type. */
    public static final String TYPE_OBJ_INTEGER = "Integer";

    /** boolean primitive Type. */
    public static final String TYPE_PRI_BOOLEAN = "boolean";

    /** int primitive Type. */
    public static final String TYPE_PRI_INT = "int";

    /** Date Type. */
    public static final String TYPE_OBJ_DATE = "Date";

    /** Property Name e.g. BuildNumber. */
    private String name = null;

    /** Property Type e.g. Integer. */
    private String type = null;

    /** Property Value e.g. 8. */
    private String value = null;

    /**
    * Constructor.
    */
    public JReleaseInfoProperty() {
        super();
    }

    /**
    * Constructor.
    *
    * @param name name of the property
    * @param type name of the type
    * @param value value of the property
    */
    public JReleaseInfoProperty(String name, String type, String value) {
        super();
        this.setName(name);
        this.setType(type);
        this.setValue(value);
    }

    /**
    * Get name of Property (name, type, value).
    *
    * @return name String of Property
    */
    public String getName() {
        return this.name;
    }

    /**
    * Set name of Property (name, type, value).
    *
    * @param name String
    */
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Get type of Property (name, type, value).
    *
    * @return type String of Property
    */
    public String getType() {
        return this.type;
    }

    /**
    * Set type of Property (name, type, value).
    *
    * @param type String of Property
    */
    public void setType(String type) {
        this.type = type;
    }

    /**
    * Get value of Property (name, type, value).
    *
    * @return String value
    */
    public String getValue() {
        return this.value;
    }

    /**
    * Set value of Property (name, type, value).
    *
    * @param value String of Property
    */
    public void setValue(String value) {
        this.value = value;
    }
}
