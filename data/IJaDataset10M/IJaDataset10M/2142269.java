package edu.berkeley.guir.quill.gesturelib;

import java.beans.PropertyChangeListener;

/** Interface for gesture objects.  Note that clone() does a deep
    copy.  It emits propertyChangeEvents when any property changes
    (including author). 
 * <P>
 * This software is distributed under the 
 * <A HREF="http://guir.cs.berkeley.edu/projects/COPYRIGHT.txt">
 * Berkeley Software License</A>.
 */
public interface GestureObject extends Cloneable {

    /** emits property change for property AUTHOR_PROP */
    void setAuthor(String author);

    String getAuthor();

    /** emits property change for property ENABLED_PROP */
    void setEnabled(boolean on);

    boolean isEnabled();

    String PARENT_PROP = "parent";

    /** emits property change for property PARENT_PROP */
    void setParent(GestureContainer parent);

    GestureContainer getParent();

    /** Whether the GestureObject is enabled or not.  Should have value
      true or false.  (If not set, assumed it is enabled.)*/
    String ENABLED_PROP = "enabled";

    String AUTHOR_PROP = "author";

    /** Returns whether the object has the current property set */
    boolean hasProperty(String name);

    /** Set the named property to a value.  Null is <strong>not</strong>
      allowed as a key <strong>or</strong> value. */
    void setProperty(String name, Object value);

    /** Get the value of the named property (or null if it is not set). */
    Object getProperty(String name);

    /** Unset the property.  Unsetting an already unset property has no
      effect.  Predefined properties (e.g., author and enabled) cannot
      be removed and attempting to do so will result in an error. */
    void unsetProperty(String name);

    /** Fire a property change.  No event is fired if old and new values
      are the same and non-null. */
    void firePropertyChange(String propName, Object oldValue, Object newValue);

    /** A deep copy. */
    Object clone();

    /** Listen for all propertyChangeEvents */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /** Listen for propertyChangeEvents for a particular property */
    void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    /** Stop listening for all propertyChangeEvents */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /** Stop listening for propertyChangeEvents for a particular property*/
    void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}
