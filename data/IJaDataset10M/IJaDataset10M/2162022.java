package org;

/**
 *
 * @author Ponec
 */
public interface UIComponent<T> {

    /** Get component value */
    public T getValue();

    /** Get component value */
    public void setValue(T value);

    /** Set HTML stype */
    public void setStyle(String attr, String value);

    /** Is the input value valid? */
    public boolean isValid();
}
