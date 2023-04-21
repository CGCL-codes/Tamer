package jegg.impl;

/**
 * 
 *
 * @author Bruce Lowery
 */
public class Property {

    private final String _name;

    private final String _value;

    Property(String name, String value) {
        _name = name;
        _value = value;
    }

    public String getName() {
        return _name;
    }

    public String getValue() {
        return _value;
    }
}
