package org.base.apps.beans.events;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Implementation of {@link PropertyChangeEmitter} which does not emit any 
 * notifications, but can be re-enabled by delegating event firing to the
 * <code>PropertyChangeEmitter</code> it disabled.
 * 
 * @author Kevan Simpson
 */
public class NoPropertyChangeSupport extends BasePropertyChangeSupport {

    private static final long serialVersionUID = -5726176194182700848L;

    private transient PropertyChangeEmitter mDisabledSupport;

    /**
     * @param disabledSupport The disabled <code>PropertyChangeEmitter</code>.
     */
    public NoPropertyChangeSupport(PropertyChangeEmitter disabledSupport) {
        super(disabledSupport);
        setDisabledSupport(disabledSupport);
    }

    /**
     * @return the disabledSupport
     */
    public PropertyChangeEmitter getDisabledSupport() {
        return mDisabledSupport;
    }

    /**
     * @param disabledSupport the disabledSupport to set
     */
    public void setDisabledSupport(PropertyChangeEmitter disabledSupport) {
        mDisabledSupport = disabledSupport;
    }

    /** @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener) */
    @Override
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    /** @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener) */
    @Override
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
    }

    /** @see java.beans.PropertyChangeSupport#getPropertyChangeListeners() */
    @Override
    public synchronized PropertyChangeListener[] getPropertyChangeListeners() {
        return new PropertyChangeListener[0];
    }

    /** @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener) */
    @Override
    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    }

    /** @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener) */
    @Override
    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    }

    /** @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(java.lang.String) */
    @Override
    public synchronized PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return new PropertyChangeListener[0];
    }

    /** @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object) */
    @Override
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    }

    /** @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, int, int) */
    @Override
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
    }

    /** @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, boolean, boolean) */
    @Override
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    }

    /** @see java.beans.PropertyChangeSupport#firePropertyChange(java.beans.PropertyChangeEvent) */
    @Override
    public void firePropertyChange(PropertyChangeEvent evt) {
    }

    /** @see java.beans.PropertyChangeSupport#fireIndexedPropertyChange(java.lang.String, int, java.lang.Object, java.lang.Object) */
    @Override
    public void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {
    }

    /** @see java.beans.PropertyChangeSupport#fireIndexedPropertyChange(java.lang.String, int, int, int) */
    @Override
    public void fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue) {
    }

    /** @see java.beans.PropertyChangeSupport#fireIndexedPropertyChange(java.lang.String, int, boolean, boolean) */
    @Override
    public void fireIndexedPropertyChange(String propertyName, int index, boolean oldValue, boolean newValue) {
    }

    /** @see java.beans.PropertyChangeSupport#hasListeners(java.lang.String) */
    @Override
    public synchronized boolean hasListeners(String propertyName) {
        return false;
    }
}
