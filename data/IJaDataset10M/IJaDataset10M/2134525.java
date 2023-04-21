package net.sf.kdgcommons.util;

import net.sf.kdgcommons.lang.ObjectUtil;

/**
 *  An immutable 2-tuple that associates a name with a value. This is
 *  particularly useful for programs that perform database operations,
 *  as a way of managing the data coming back from JDBC.
**/
public class NameValue<T> implements Comparable<NameValue<T>> {

    private String _name;

    private T _value;

    public NameValue(String name, T value) {
        _name = name;
        _value = value;
    }

    public String getName() {
        return _name;
    }

    public T getValue() {
        return _value;
    }

    /**
     *  Two <code>NameValue</code> instances are considered equal if both
     *  name and value components are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NameValue) {
            NameValue<T> that = (NameValue<T>) obj;
            return ObjectUtil.equals(_name, that._name) && ObjectUtil.equals(_value, that._value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(_name) ^ ObjectUtil.hashCode(_value);
    }

    /**
     *  Returns the string representation of this object, in the form
     *  <code>[NAME=VALUE]</code>.
     */
    @Override
    public String toString() {
        return "[" + _name + "=" + String.valueOf(_value) + "]";
    }

    /**
     *  Compares two <CODE>NameValue</CODE> instances. Instances are ordered
     *  by name first. If two instances have the same name, then the value is
     *  examined: if this object's value implements <code>Comparable</code>,
     *  we call <code>compareTo()</code>; otherwise, if the two objects are
     *  equal, we return 0; finally, we compare the string representation of
     *  the values.
     *
     *  @throws ClassCastException if the passed object is not a <code>
     *          NameValue</code> instance.
     */
    public int compareTo(NameValue<T> that) {
        int cmp = _name.compareTo(that._name);
        if (cmp != 0) return cmp;
        if (_value instanceof Comparable) return ((Comparable<T>) _value).compareTo(that._value);
        if (ObjectUtil.equals(_value, that._value)) return 0;
        return (String.valueOf(_value).compareTo(String.valueOf(that._value)));
    }
}
