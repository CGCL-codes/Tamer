package de.jaret.util.misc;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base implementation for an observable object.
 * 
 * @author Peter Kliem
 * @version $Id: PropertyObservableBase.java 250 2007-02-12 00:15:49Z olk $
 */
public class PropertyObservableBase implements PropertyObservable {

    /** PropertyChangeSupport for handling listeners. */
    protected PropertyChangeSupport _propertyChangeSupport;

    /**
     * Check whether two object differ including null checks.
     * 
     * @param o1 object 1
     * @param o2 object 2
     * @return true if the objects are not equal
     */
    public static boolean isRealModification(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return false;
        }
        if (o1 != null && o2 == null) {
            return true;
        }
        if (o2 != null && o1 == null) {
            return true;
        }
        return !o1.equals(o2);
    }

    /**
     * Inform listeners about a property change.
     * 
     * @param propName name of the property
     * @param oldVal old value
     * @param newVal new value
     */
    protected void firePropertyChange(String propName, Object oldVal, Object newVal) {
        if (_propertyChangeSupport != null) {
            _propertyChangeSupport.firePropertyChange(propName, oldVal, newVal);
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (_propertyChangeSupport == null) {
            _propertyChangeSupport = new PropertyChangeSupport(this);
        }
        _propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (_propertyChangeSupport != null) {
            _propertyChangeSupport.removePropertyChangeListener(listener);
        }
    }
}
