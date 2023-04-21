package dryven.view.engine.component.metadata;

public class AttributeDeclaration {

    private Class<?> _valueType;

    private String _name;

    private boolean _required;

    public AttributeDeclaration(Class<?> valueType, String name, boolean required) {
        super();
        _valueType = valueType;
        _name = name;
        _required = required;
    }

    public Class<?> getValueType() {
        return _valueType;
    }

    public String getName() {
        return _name;
    }

    public boolean isRequired() {
        return _required;
    }
}
