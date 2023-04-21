package com.ssg.tools.jsonxml.json.schema;

/**
 * Provides String|JSchema attribute value wrapper for implementation and 
 * use e.g. for "type" attribute.
 * 
 * @author ssg
 */
public abstract class JSONMixedAttribute {

    protected Object _value;

    protected boolean _custom = false;

    public JSONMixedAttribute() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JSONMixedAttribute other = (JSONMixedAttribute) obj;
        if (this._value != other._value && (this._value == null || !this._value.equals(other._value))) {
            return false;
        }
        return true;
    }

    public Object get() {
        return _value;
    }

    public JSchema getSchema() {
        return (_value instanceof JSchema) ? (JSchema) _value : null;
    }

    public String getValue() {
        return (_value instanceof String) ? (String) _value : null;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (this._value != null ? this._value.hashCode() : 0);
        return hash;
    }

    public boolean isSchema() {
        return _value instanceof JSchema;
    }

    public boolean isCustom() {
        return _custom;
    }

    @Override
    public String toString() {
        return (isSchema()) ? getSchema().toString() : (_value != null) ? _value.toString() : "###NULL###";
    }

    public abstract boolean validate(Object value);
}
