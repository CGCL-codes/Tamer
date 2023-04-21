package org.plazmaforge.framework.core.data;

/**
 * Data Type definition
 * 
 */
public class DataType extends EntityBean<String> {

    public static String STRING_TYPE = "STRING";

    public static String INTEGER_TYPE = "INTEGER";

    public static String FLOAT_TYPE = "FLOAT";

    public static String DATE_TYPE = "DATE";

    public static String DATETIME_TYPE = "DATETIME";

    public static String BOOLEAN_TYPE = "BOOLEAN";

    public static String REFERENCE_TYPE = "REFERENCE";

    private String code;

    private String name;

    private boolean enable;

    public String getKey() {
        String id = getId();
        return id == null ? null : id.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String toString() {
        return getName();
    }

    protected boolean equalsId(Object id1, Object id2) {
        String st1 = (String) id1;
        String st2 = (String) id2;
        return st1.trim().equals(st2.trim());
    }

    /**
     * Return true if data type is reference (enumeration, lookup, dictionary and etc.)
     * @return
     */
    public boolean isReference() {
        return DataType.REFERENCE_TYPE.equals(getKey());
    }
}
