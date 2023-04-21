package edu.rice.cs.drjava.config;

import java.util.HashSet;

/** Class representing values that are constant and that can be inserted as variables in external processes.
  * @version $Id: ConstantProperty.java 5175 2010-01-20 08:46:32Z mgricken $
  */
public class ConstantProperty extends EagerProperty {

    /** Create a constant property. */
    public ConstantProperty(String name, String value, String help) {
        super(name, help);
        if (value == null) {
            throw new IllegalArgumentException("DrJavaProperty value is null");
        }
        _value = value;
        _isCurrent = true;
        resetAttributes();
    }

    /** Update the property so the value is current.
    * @param pm PropertyMaps used for substitution when replacing variables */
    public void update(PropertyMaps pm) {
    }

    /** Return the value of the property. If it is not current, update first. 
    * @param pm PropertyMaps used for substitution when replacing variables*/
    public String getCurrent(PropertyMaps pm) {
        return _value;
    }

    /** Return the value. */
    public String toString() {
        return _value;
    }

    /** Return true if the value is current. */
    public boolean isCurrent() {
        return true;
    }

    /** Mark the value as stale. */
    public void invalidate() {
        invalidateOthers(new HashSet<DrJavaProperty>());
    }
}
