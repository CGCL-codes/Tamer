package simpleorm.simplewebapp.scalarFields;

import simpleorm.simplewebapp.core.WField;

public class WFieldLong extends WField {

    public WFieldLong(String name) {
        super(name);
    }

    public WFieldLong(String name, String widget) {
        super(name, widget);
    }

    public long getLongValue(long defalt) {
        return value != null ? (Long) value : defalt;
    }

    protected String format() {
        return value == null ? null : value.toString();
    }

    protected void parse(String rawText) {
        this.value = rawText == null ? null : Long.valueOf(rawText);
    }

    public Class getValueClass() {
        return Long.class;
    }

    public boolean getBooleanValue(boolean defalt) {
        if (value == null) return false;
        return (Long) value != 0;
    }

    public Long getValue() {
        return (Long) super.getValue();
    }
}
