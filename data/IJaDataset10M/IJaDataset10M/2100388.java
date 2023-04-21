package org.jreform.core;

import java.util.List;

/**
 * An input that can have multiple values.
 * 
 * @author Arman Sharif
 * @see SingleValueInput
 */
public interface MultipleValueInput<T> extends Input<T> {

    /**
     * Returns this input's values, or <code>null</code>
     * if the input has no values or is not valid.
     */
    public List<T> getValues();

    /**
     * Sets this input's values.
     */
    public void setValues(List<T> value);

    /**
     * Return's this input's <tt>value</tt> attributes.
     */
    public String[] getValueAttributes();

    /**
     * Sets this input's <tt>value</tt> attributes.
     */
    public void setValueAttributes(String[] valueAttributes);
}
