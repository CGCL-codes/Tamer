package edu.rice.cs.drjava.config;

/** Class representing values that are eagerly computed, so they are never stale.
 *  @version $Id: EagerProperty.java 5175 2010-01-20 08:46:32Z mgricken $
 */
public abstract class EagerProperty extends DrJavaProperty {

    /** Create a property. */
    public EagerProperty(String name, String help) {
        super(name, help);
    }

    /** Create a property. */
    public EagerProperty(String name, String value, String help) {
        super(name, value, help);
    }

    /** Return the value of the property by recomputing it.
    * @param pm PropertyMaps used for substitution when replacing variables */
    public String getLazy(PropertyMaps pm) {
        return getCurrent(pm);
    }

    /** Return that it is never current.
    * @return always false. */
    public boolean isCurrent() {
        return false;
    }
}
