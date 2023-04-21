package com.byterefinery.rmbench.extension;

/**
 * @author cse
 */
public class VariableDescriptor {

    public static final String USE_OPTIONAL = "optional";

    public static final String USE_REQUIRED = "required";

    public static final String TYPE_INT = "int";

    public static final String TYPE_STRING = "string";

    public static final String TYPE_JAVA_NAME = "java_name";

    public static final String TYPE_BOOLEAN = "boolean";

    private final String name;

    private final String type;

    private final String use;

    private final String label;

    private final String defaultValue;

    /**
     * @param varName variable name
     * @param type one of int, java_name or string
     * @param use either optional or required
     * @param label the displayable label
     */
    public VariableDescriptor(String name, String type, String use, String defaultValue, String label) {
        this.name = name;
        if (TYPE_INT.equals(type)) this.type = TYPE_INT; else if (TYPE_JAVA_NAME.equals(type)) this.type = TYPE_JAVA_NAME; else this.type = TYPE_STRING;
        this.use = USE_REQUIRED.equals(use) ? USE_REQUIRED : USE_OPTIONAL;
        this.defaultValue = defaultValue;
        this.label = label != null ? label : name;
    }

    /**
     * @return the displayable label (defaults to the symbolic name)
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the symbolic name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the default value if defined, or null
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @return whether this is a required field
     */
    public boolean isRequired() {
        return use == USE_REQUIRED;
    }

    /**
     * @return whether this variable has a default value definition
     */
    public boolean hasDefault() {
        return defaultValue != null;
    }

    /**
     * @return the type identifier, one of the TYPE_xx constants
     */
    public String getType() {
        return type;
    }

    public boolean isIntType() {
        return type == TYPE_INT;
    }

    public boolean isStringType() {
        return type == TYPE_STRING;
    }

    public boolean isJavaNameType() {
        return type == TYPE_JAVA_NAME;
    }

    public boolean isBooleanType() {
        return type == TYPE_BOOLEAN;
    }

    /**
     * @return whether the type is text-based, i.e. requires input through a 
     * normal text field
     */
    public boolean isTextType() {
        return type == TYPE_INT | type == TYPE_JAVA_NAME | type == TYPE_STRING;
    }
}
