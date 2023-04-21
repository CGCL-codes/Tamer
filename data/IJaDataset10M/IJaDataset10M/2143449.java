package com.ecmdeveloper.plugin.diagrams.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The Class ClassDiagramBase.
 * 
 * @author Ricardo Belfor
 */
public abstract class ClassDiagramBase {

    private transient PropertyChangeSupport pcsDelegate = new PropertyChangeSupport(this);

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException();
        }
        pcsDelegate.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        if (listener != null) {
            pcsDelegate.removePropertyChangeListener(listener);
        }
    }

    protected void firePropertyChange(String property, Object oldValue, Object newValue) {
        if (pcsDelegate.hasListeners(property)) {
            pcsDelegate.firePropertyChange(property, oldValue, newValue);
        }
    }
}
