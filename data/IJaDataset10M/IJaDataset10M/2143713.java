package net.jfipa.xml;

public class Parameter {

    private String _name;

    private String _value;

    public Parameter() {
        _name = null;
        _value = null;
    }

    public Parameter(String name, String value) {
        name = _name;
        value = _value;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        _value = value;
    }
}
