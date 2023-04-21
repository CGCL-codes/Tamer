package net.sf.betterj.model.variable;

import net.sf.betterj.model.type.IType;
import net.sf.betterj.model.value.IValue;

/**
 * @author Oleh Sklyarenko
 */
public abstract class AbstractVariable implements IVariable {

    private IType _type;

    private String _identifier;

    private boolean _initialized;

    private IValue _value;

    private IVariableAttributes _attributes;

    @Override
    public IType getType() {
        return _type;
    }

    @Override
    public void setType(IType value) {
        _type = value;
    }

    @Override
    public String getIdentifier() {
        return _identifier;
    }

    @Override
    public void setIdentifier(String value) {
        _identifier = value;
    }

    @Override
    public boolean isInitialized() {
        return _initialized;
    }

    @Override
    public void setInitialized(boolean value) {
        _initialized = value;
    }

    @Override
    public IValue getValue() {
        return _value;
    }

    @Override
    public void setValue(IValue value) {
        _value = value;
    }

    @Override
    public IVariableAttributes getAttributes() {
        return _attributes;
    }

    @Override
    public void setAttributes(IVariableAttributes value) {
        _attributes = value;
    }
}
