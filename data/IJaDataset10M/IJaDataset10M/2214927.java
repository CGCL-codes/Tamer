package com.festo.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The Class ModelAbstraction serves as the abstract parent class for all
 * models. It takes care of all PropertyChange listener concerns
 * 
 * @author Dimitrios Dentsas
 */
public abstract class ModelAbstraction {

    /** The property change support. */
    protected PropertyChangeSupport propertyChangeSupport;

    /**
	 * Instantiates the property change support.
	 */
    public ModelAbstraction() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
	 * Adds the property change listener.
	 * 
	 * @param propertyName
	 *            the property name
	 * @param listener
	 *            the listener
	 */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
	 * Adds the property change listener.
	 * 
	 * @param listener
	 *            the listener
	 */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
	 * Removes the property change listener.
	 * 
	 * @param listener
	 *            the listener
	 */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
	 * Fire property change.
	 * 
	 * @param propertyName
	 *            the property name
	 * @param oldValue
	 *            the old value
	 * @param newValue
	 *            the new value
	 */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
}
