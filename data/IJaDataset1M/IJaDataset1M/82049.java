package simpleorm.simpleweb.scalarFields;

import simpleorm.simpleweb.core.WField;

public class WFieldString extends WField {

    public WFieldString(String name) {
        super(name);
    }

    public WFieldString(String name, String widget) {
        super(name, widget);
    }

    protected String format() {
        return (String) value;
    }

    protected void parse(String rawText) {
        this.value = rawText;
    }

    public Class getValueClass() {
        return String.class;
    }

    public String getValue() {
        return (String) super.getValue();
    }

    /** Interpret the string as a boolean. */
    public boolean getBooleanValue(boolean defalt) {
        if (value == null) return false;
        return "true".equalsIgnoreCase((String) value) || "t".equalsIgnoreCase((String) value) || "yes".equalsIgnoreCase((String) value) || "y".equalsIgnoreCase((String) value) || "on".equalsIgnoreCase((String) value);
    }
}
