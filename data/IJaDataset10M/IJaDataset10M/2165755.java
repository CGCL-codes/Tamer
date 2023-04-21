package simpleorm.simplewebapp.scalarFields;

import simpleorm.simplewebapp.core.WField;

public class WFieldBoolean extends WField {

    public WFieldBoolean(String name) {
        this(name, WField.CHECKBOX);
    }

    public WFieldBoolean(String name, String widget) {
        super(name, widget);
        getOptions().add("True");
        getOptions().add("False");
    }

    /** This is used to actually set the check box to being checked. */
    public boolean getBooleanValue(boolean defalt) {
        return value != null ? (Boolean) value : defalt;
    }

    protected String format() {
        return value == null ? null : value.toString();
    }

    protected void parse(String rawText) {
        if (rawText == null) value = false; else if ("on".equals(rawText)) value = true; else value = Boolean.valueOf(rawText);
    }

    public Class getValueClass() {
        return Boolean.class;
    }

    public Boolean getValue() {
        return (Boolean) super.getValue();
    }
}
