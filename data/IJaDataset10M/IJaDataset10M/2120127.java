package jp.go.aist.six.util.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A kind of the PropertyBinding that restricts a property
 * to match a value in a list.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: InBinding.java 300 2011-03-02 05:45:46Z nakamura5akihito $
 */
public class InBinding extends PropertyBinding {

    private static final long serialVersionUID = 7240395153573962419L;

    /**
     * The value list.
     * TODO: Object, not String ???
     */
    private final Collection<Object> _values = new ArrayList<Object>();

    /**
     *
     */
    private boolean _containsNull = false;

    /**
     * Constructor.
     */
    public InBinding() {
    }

    /**
     * Constructs an InBinding with the specified property and value list.
     *
     * @param   property
     *  the property to restrict.
     * @param   values
     *  the matching value list.
     */
    public InBinding(final String property, final Object[] values) {
        super(property);
        setValues(values);
    }

    /**
     * Constructs an InBinding with the specified property and value list.
     *
     * @param   property
     *  the property to restrict.
     * @param   values
     *  the matching value list.
     */
    public InBinding(final String property, final Collection<?> values) {
        super(property);
        setValues(values);
    }

    /**
     * Sets the specified values as the matching value list.
     * First, the previous list are cleared and all of
     * the specified values are appended to the list.
     *
     * @param   values
     *  a collection whose elements are appended
     *  to the matching value list.
     */
    public void setValues(final Collection<?> values) {
        if (values != _values) {
            clear();
            if (values == null || values.size() == 0) {
                return;
            }
            for (Object value : values) {
                addValue(value);
            }
        }
    }

    public void setValues(final Object[] values) {
        clear();
        if (values == null || values.length == 0) {
            return;
        }
        for (Object value : values) {
            addValue(value);
        }
    }

    /**
     * Appends the value to the matching value list.
     *
     * @param   value
     *  the matching value to be appended.
     */
    public void addValue(final Object value) {
        if (value == null) {
            setNullContained(true);
        } else {
            _values.add(String.valueOf(value));
        }
    }

    /**
     * Returns the matching values.
     *
     * @return
     *  the matching values.
     */
    public Collection<Object> getValues() {
        return _values;
    }

    /**
     * Returns an iterator over the matching values.
     *
     * @return
     *  an iterator over the matching values.
     */
    public Iterator<Object> values() {
        return _values.iterator();
    }

    /**
     * Tests if the value list contains null.
     *
     * @param   contained
     *  true if the list contains null.
     */
    public void setNullContained(final boolean contained) {
        _containsNull = contained;
    }

    /**
     * Tests if the value list contains null.
     *
     * @return
     *  true if the list contains null.
     */
    public boolean isNullContained() {
        return _containsNull;
    }

    /**
     * Removes all of the matching values and
     * sets the nullConatined property to false.
     */
    public void clear() {
        _values.clear();
        setNullContained(false);
    }

    /**
     * Returns the number of values.
     *
     * @return
     *  the number of values.
     */
    public int size() {
        return _values.size();
    }

    /**
     * Determines whether another object is equal to this InBinding.
     * The result is true if and only if the argument is not null
     * and is a InBinding object that has the same property
     * and value list.
     *
     * @param   obj
     *  the object to test for equality with this InBinding.
     * @return
     *  true if the given object equals this one;
     *  false otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof InBinding)) {
            return false;
        }
        if (super.equals(obj)) {
            InBinding other = (InBinding) obj;
            Collection<Object> other_values = other.getValues();
            Collection<Object> this_values = this.getValues();
            if (this_values == other_values || (this_values != null && this_values.equals(other_values))) {
                if (isNullContained() == other.isNullContained()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Computes the hash code for this InBinding.
     *
     * @return
     *  a hash code value for this object.
     */
    @Override
    public int hashCode() {
        final int prime = 37;
        int result = super.hashCode();
        Collection<Object> values = getValues();
        result = prime * result + (values == null ? 0 : values.hashCode());
        result = prime * result + (isNullContained() ? 0 : 1);
        return result;
    }

    /**
     * Returns a string representation of this InBinding.
     * This method is intended to be used only for debugging purposes.
     * The content and format of the returned string might not
     * conform to any query language syntax.
     *
     * @return
     *  a string representation of this InBinding.
     */
    @Override
    public String toString() {
        return getProperty() + " IN " + getValues() + (isNullContained() ? "+NULL" : "");
    }
}
