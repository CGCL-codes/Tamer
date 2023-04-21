package org.aspectbrains.activebeans.core;

import java.beans.PropertyChangeListener;

/**
 * All ActiveBeans - classes annotated with {@link ActiveBean} - implement this
 * interface automatically. There is also a default implementation provided.
 * <p>
 * ATTENTION: This interface is not intended for implementation by clients.
 * </p>
 * <p>
 * Implementation note: This is achieved by AspectJ static crosscutting.
 * </p>
 * 
 * @author Heiko Seeberger
 */
public interface IPropertyChangeSupporting {

    /**
     * @param propertyChangeListener
     *            The {@link PropertyChangeListener} to add.
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
     */
    void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

    /**
     * @param propertyName
     *            The name of the property for which the given
     *            {@link PropertyChangeListener} is to be added.
     * @param propertyChangeListener
     *            The {@link PropertyChangeListener} to add.
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(String,
     *      PropertyChangeListener)
     */
    void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener);

    /**
     * @param propertyChangeListener
     *            The {@link PropertyChangeListener} to remove.
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(PropertyChangeListener)
     */
    void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);

    /**
     * @param propertyName
     *            The name of the property for which the given
     *            {@link PropertyChangeListener} is to be removed.
     * @param propertyChangeListener
     *            The {@link PropertyChangeListener} to remove.
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(String,
     *      PropertyChangeListener)
     */
    void removePropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener);
}
