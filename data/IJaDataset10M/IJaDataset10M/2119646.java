package org.progeeks.meta;

import java.beans.*;

/**
 *  A default implementation of PropertyMutator that will work
 *  with any MetaObject.  It can be used as an implementation
 *  on its own or subclassed to provide performance improvements
 *  in metakit specific cases.
 *
 *  @version   $Revision: 1.1 $
 *  @author    Paul Speed
 */
public class DefaultPropertyMutator implements PropertyMutator {

    private String propertyName;

    private MetaObject metaObject;

    public DefaultPropertyMutator(String propertyName, MetaObject metaObject) {
        if (propertyName == null) throw new IllegalArgumentException("Property name cannot be null.");
        if (metaObject == null) throw new IllegalArgumentException("Meta-object cannot be null.");
        this.propertyName = propertyName;
        this.metaObject = metaObject;
    }

    /**
     *  Returns the name of this property.
     */
    public String getPropertyName() {
        return (propertyName);
    }

    /**
     *  Returns the object that contains this property.
     */
    public MetaObject getParentObject() {
        return (metaObject);
    }

    /**
     *  Returns the info associated with this property.
     */
    public PropertyInfo getPropertyInfo() {
        return (metaObject.getMetaClass().getPropertyInfo(propertyName));
    }

    /**
     *  Returns the value of this property.
     */
    public Object getValue() {
        return (metaObject.getProperty(propertyName));
    }

    /**
     *  Resets the value of this property.
     */
    public void setValue(Object value) {
        metaObject.setProperty(propertyName, value);
    }

    /**
     *  Adds the specified PropertyChangeListener to this
     *  mutator.
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        metaObject.addPropertyChangeListener(propertyName, l);
    }

    /**
     *  Removes the specified PropertyChangeListener from this
     *  mutator.
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        metaObject.removePropertyChangeListener(propertyName, l);
    }
}
