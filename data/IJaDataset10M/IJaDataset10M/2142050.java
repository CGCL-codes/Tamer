package de.hu.gralog.beans.support;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import de.hu.gralog.beans.event.PropertyChangeListenable;

/**
 * This class is a helper class to implement the
 * {@link PropertyChangeListenable} interface. It administers the listeners and
 * provides methods to fire {@link java.beans.PropertyChangeEvent PropertyChangeEvents}.
 * You should subclass this class to define a bean that allows others to listen
 * to changes of it's properties.
 * 
 * @author Sebastian
 * 
 */
public class DefaultPropertyChangeListenableBean implements PropertyChangeListenable, Serializable {

    protected final transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
