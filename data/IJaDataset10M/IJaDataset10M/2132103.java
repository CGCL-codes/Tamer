package jve.generated;

import java.beans.PropertyChangeListener;

/**
 * This interface represents a reference to an Object.
 */
public interface IBoundObject {

    /** Property delimiter (for example: contact.address.zip) */
    public static final String DELIMITER = ".";

    /** Property change property when the entire Object has changed */
    public static final String PROPERTY_OBJ_CHANGED = "*";

    /**
   * Set the property of the source object that will be the bound object.
   * This value may be null.
   * 
   * @param property the property of the source object
   */
    public void setProperty(String property);

    /**
   * Get the property of the source object that will be used as the bound object.
   * 
   * @return the property
   */
    public String getProperty();

    /**
   * Set the source object that will be used as a reference for the bound object.
   * 
   * @param object the source object
   */
    public void setSourceObject(Object object);

    /**
   * Set the source object and target property simultaneously.  Using this method
   * will prevent multiple event firings of changing both the sourceObject and property
   * separately.
   * 
   * @param object the source object
   * @param property the target property
   */
    public void setSourceObject(Object object, String property);

    /**
   * Get the source object that is used to reference the bound object.
   * 
   * @return the source object
   */
    public Object getSourceObject();

    /**
   * Get the bound object.  Depending on the implementation this
   * may be the same as the sourceObject.
   * 
   * @return referenced object 
   */
    public Object getObject();

    /**
   * Notify the bound object that an action was performed where
   * the reference object was used to perform an action. Typically a binder
   * can choose the manner of reaction, if any.
   * 
   * @param action the action that used the referenced object
   */
    public void actionPerformed(IActionBinder action);

    /**
   * Refresh the referenced object.
   * A call to this method denotes to the reference object that any cached
   * information should be discarted, or that the referenced object has changed.  
   * If the referenced object is dynamic (such as the result of a method invocation) 
   * the object will be refreshed.
   * 
   */
    public void refresh();

    /**
   * Get the class type of the referenced object.
   * This is necessary to enable design time visual support through introspection.
   * 
   * @return the Class type of the configured object. If it is an array or a List,
   *         it will return the type of the elements that comprises the array or 
   *         List rather than the type array or List type.
   * @throws IllegalStateException if the target object can not be determined due to a 
   * 		   miss-configuration.  The exception includes a message denoting the reason.
   */
    public Class getType() throws IllegalStateException;

    /**
   * Adds a PropertyChangeListener to the listener list.  The listner will be notified 
   * of any changes to bound properties on the referenced object.  The listener will also
   * be fired when the entire object is changed, with {@link IBoundObject#PROPERTY_OBJ_CHANGED PROPERTY_OBJ_CHANGED} as the 
   * property type.
   * 
   * @param l the listener to add.
   */
    public void addPropertyChangeListener(PropertyChangeListener l);

    /**
   * Remove an existing PropertyChangeListener from the listener list.
   * 
   * @param l the listener to remove.
   */
    public void removePropertyChangeListener(PropertyChangeListener l);
}
