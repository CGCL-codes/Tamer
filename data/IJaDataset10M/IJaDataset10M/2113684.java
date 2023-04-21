package org.springframework.richclient.test;

import java.beans.PropertyChangeListener;
import org.springframework.richclient.util.PropertyChangePublisher;
import org.springframework.richclient.util.PropertyChangeSupport;

/**
 * @author Oliver Hutchison
 */
public class TestBeanWithPCP implements PropertyChangePublisher {

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private Object boundProperty;

    public Object getBoundProperty() {
        return boundProperty;
    }

    public void setBoundProperty(Object boundProperty) {
        Object oldBoundProperty = this.boundProperty;
        this.boundProperty = boundProperty;
        pcs.firePropertyChange("boundProperty", oldBoundProperty, boundProperty);
    }

    public boolean hasListeners(String propertyName) {
        return pcs.hasListeners(propertyName);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }
}
