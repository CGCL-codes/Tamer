package edu.luc.cs.trull;

import java.beans.PropertyChangeListener;

/**
 * A source of PropertyChangeEvents.
 */
public interface PropertyChangeSource extends PropertyChangeListener {

    /**
   * Adds a PropertyChangeListener to this component.
   * Implementing classes that do not emit any PropertyChangeEvents
   * may choose to implement this an empty method.
   * @param l the listener to be added.
   */
    void addPropertyChangeListener(PropertyChangeListener l);

    /**
   * Removes a PropertyChangeListener from this component.
   * Implementing classes that do not emit any PropertyChangeEvents
   * may choose to implement this an empty method.
   * @param l the listener to be removed.
   */
    void removePropertyChangeListener(PropertyChangeListener l);

    /**
   * Returns the array of PropertyChangeListeners listening
   * to this component.
   * Implementing classes that do not emit any PropertyChangeEvents
   * should return an empty array.
   * @return the array of PropertyChangeListeners.
   */
    PropertyChangeListener[] getPropertyChangeListeners();
}
