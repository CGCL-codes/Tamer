package org.databene.commons.bean;

import java.beans.PropertyChangeListener;

/**
 * Interface for all JavaBeans that serve as observable.<br/>
 * <br/>
 * Created at 17.07.2008 14:46:21
 * @since 0.4.5
 * @author Volker Bergmann
 */
public interface ObservableBean {

    void addPropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}
