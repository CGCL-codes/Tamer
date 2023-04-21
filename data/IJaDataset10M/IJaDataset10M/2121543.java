package org.cubictest.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author Stein K�re Skytteren
 *
 */
public abstract class PropertyAwareObject {

    public static final String CHILD = "CHILD";

    public static final String REORDER = "REORDER";

    public static final String BOUNDS = "BOUNDS";

    public static final String INPUT = "INPUT";

    public static final String OUTPUT = "OUTPUT";

    public static final String NAME = "NAME";

    public static final String VALUE = "VALUE";

    public static final String LAYOUT = "LAYOUT";

    public static final String STATUS = "STATUS";

    public static final String PARAM = "PARAM";

    protected TestPartStatus status;

    private transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    protected PropertyAwareObject() {
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        getListeners().addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        getListeners().removePropertyChangeListener(l);
    }

    protected void firePropertyChange(String prop, Object old, Object newValue) {
        getListeners().firePropertyChange(prop, old, newValue);
    }

    public TestPartStatus getStatus() {
        return this.status;
    }

    public void setStatus(TestPartStatus newStatus) {
        TestPartStatus old = status;
        status = newStatus;
        firePropertyChange(PropertyAwareObject.STATUS, old, newStatus);
    }

    public abstract void resetStatus();

    protected void setListeners(PropertyChangeSupport listeners) {
        this.listeners = listeners;
    }

    protected PropertyChangeSupport getListeners() {
        if (listeners == null) listeners = new PropertyChangeSupport(this);
        return listeners;
    }
}
