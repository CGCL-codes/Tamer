package org.gamegineer.client.ui.console.commandlet.util.attribute;

import org.gamegineer.client.ui.console.commandlet.IStatelet;

/**
 * A console statelet attribute.
 * 
 * <p>
 * Instances of this interface allow clients to manipulate a console statelet
 * attribute in a type-safe manner.
 * </p>
 * 
 * <p>
 * This interface is intended to be implemented but not extended by clients.
 * </p>
 * 
 * @param <T>
 *        The type of the attribute value.
 */
public interface IAttribute<T> {

    /**
     * Adds this attribute to the specified statelet with the specified value.
     * 
     * @param statelet
     *        The console statelet; must not be {@code null}.
     * @param value
     *        The attribute value; may be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If this attribute is present in the specified statelet or the
     *         attribute value is illegal.
     * @throws java.lang.NullPointerException
     *         If {@code statelet} is {@code null}.
     */
    public void add(IStatelet statelet, T value);

    public T ensureGetValue(IStatelet statelet);

    /**
     * Sets the value of this attribute in the specified statelet. The attribute
     * will be added to the statelet with the specified value if it is not
     * already present.
     * 
     * @param statelet
     *        The console statelet; must not be {@code null}.
     * @param value
     *        The attribute value; may be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If the attribute value is illegal.
     * @throws java.lang.NullPointerException
     *         If {@code statelet} is {@code null}.
     */
    public void ensureSetValue(IStatelet statelet, T value);

    public String getName();

    public T getValue(IStatelet statelet);

    /**
     * Indicates this attribute is present in the specified statelet.
     * 
     * @param statelet
     *        The console statelet; must not be {@code null}.
     * 
     * @return {@code true} if this attribute is present in the specified
     *         statelet attribute; otherwise {@code false}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code statelet} is {@code null}.
     */
    public boolean isPresent(IStatelet statelet);

    /**
     * Removes this attribute from the specified statelet.
     * 
     * @param statelet
     *        The console statelet; must not be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If this attribute is not present in the specified statelet.
     * @throws java.lang.NullPointerException
     *         If {@code statelet} is {@code null}.
     */
    public void remove(IStatelet statelet);

    /**
     * Sets the value of this attribute in the specified statelet.
     * 
     * @param statelet
     *        The console statelet; must not be {@code null}.
     * @param value
     *        The attribute value; may be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If this attribute is not present in the specified statelet or the
     *         attribute value is illegal.
     * @throws java.lang.NullPointerException
     *         If {@code statelet} is {@code null}.
     */
    public void setValue(IStatelet statelet, T value);
}
