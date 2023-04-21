package net.sourceforge.bprocessor.packages.skeleton;

public class StringValue implements ControlValue {

    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String format() {
        return value;
    }

    public void parse(String string) {
        value = string;
    }
}
