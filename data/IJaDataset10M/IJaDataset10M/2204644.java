package net.sf.ij_plugins.beans;

import java.beans.PropertyChangeListener;

/**
 * @author Jarek Sacha
 * @version $Revision: 1.2 $
 */
public interface IModel {

    void addPropertyChangeListener(final PropertyChangeListener listener);

    void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener);

    PropertyChangeListener[] getPropertyChangeListeners();

    PropertyChangeListener[] getPropertyChangeListeners(final String propertyName);

    boolean hasListeners(final String propertyName);

    void removePropertyChangeListener(final PropertyChangeListener listener);

    void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener);
}
