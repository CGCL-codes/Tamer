package org.mxeclipse.object.property;

/**
 * <p>Title: MxObjectProperty</p>
 * <p>Description: </p>
 * <p>Company: ABB Switzerland</p>
 * @author Tihomir Ilic
 * @version 1.0
 *
 */
public class MxObjectProperty implements Comparable {

    public static String TYPE_BASIC = "Basic";

    public static String TYPE_ATTRIBUTE = "Attribute";

    public static String TYPE_HISTORY = "History";

    public static String BASIC_ID = "Id";

    public static String BASIC_NAME = "Name";

    public static String BASIC_REVISION = "Revision";

    public static String BASIC_TYPE = "Type";

    public static String BASIC_POLICY = "Policy";

    public static String BASIC_STATE = "State";

    private String name;

    private String value;

    private boolean modified;

    private String type;

    public MxObjectProperty(String name, String value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the value
	 */
    public String getValue() {
        return value;
    }

    /**
	 * @param value the value to set
	 */
    public void setValue(String value) {
        if (type.equals(TYPE_ATTRIBUTE) || (type.equals(TYPE_BASIC) && !name.equals(BASIC_ID))) {
            if (!(value != null && value.equals(this.value))) {
                this.modified = true;
            }
            this.value = value;
        }
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public String getType() {
        return type;
    }

    public int compareTo(Object o) {
        MxObjectProperty other = (MxObjectProperty) o;
        return this.getName().compareToIgnoreCase(other.getName());
    }
}
