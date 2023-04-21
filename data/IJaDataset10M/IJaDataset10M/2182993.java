package org.mili.core.properties;

import java.beans.*;

/**
 * This interface defines a change support.
 *
 * @author Michael Lieshoff
 */
public interface ChangeSupport {

    PropertyChangeSupport getPropertyChangeSupport();

    VetoableChangeSupport getVetoableChangeSupport();

    void addPropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String prop, PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void addVetoableChangeListener(VetoableChangeListener listener);

    void addVetoableChangeListener(String prop, VetoableChangeListener listener);

    void removeVetoableChangeListener(VetoableChangeListener listener);

    void fireVetoableChange(String prop, Object o, Object n) throws PropertyVetoException;

    void firePropertyChange(String prop, Object o, Object n);
}
