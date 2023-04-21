package org.equanda.persistence;

import java.util.Collection;

/**
 * Base implementation of the ObjectType implementations
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public abstract class EquandaObjectType implements ObjectType {

    /**
     * Check whether the given table type identifier is a valid (specialized) version
     * of this table/class.
     *
     * @param type table type identifier
     * @return true when it is a more specialized table
     */
    public boolean isAllowed(String type) {
        return isParentType(type);
    }

    /**
     * Check whether this type is "instanceof" the given type (type itself or a child thereof).
     * <p/>
     * <p/>
     * For example, in a hierarchy Object-Vehicle-Car,
     * Vehicle.isType(Vehicle) returns true
     * Vehicle.isType(Car) returns false
     * Vehicle.isType(Object) returns true
     *
     * @param type type identifier (four letters) for the type to test
     * @return true when it is an "instanceof"
     */
    public boolean isType(String type) {
        if (type == null) return false;
        if (type.length() < 4) type = type + "    ";
        String[] types = getParentTypes();
        for (String t : types) if (type.startsWith(t)) return true;
        return false;
    }

    /**
     * Check whether the given type "is a child of" (or the type itself).
     * <p/>
     * <p/>
     * For example, in a hierarchy Object-Vehicle-Car,
     * Vehicle.isParentType(Vehicle) returns true
     * Vehicle.isParentType(Car) returns true
     * Vehicle.isParentType(Object) returns false
     *
     * @param type type identifier (four letters) for the type to test
     * @return true when it is an "instanceof"
     */
    public boolean isParentType(String type) {
        if (type == null) return false;
        if (type.length() < 4) type = type + "    ";
        String[] types = getChildTypes();
        for (String t : types) if (type.startsWith(t)) return true;
        return false;
    }

    public boolean isAllowed(boolean b) {
        return false;
    }

    public boolean isAllowed(int i) {
        return false;
    }

    public boolean isAllowed(double v) {
        return false;
    }

    public Collection<Integer> getAllowedIntValues() {
        return null;
    }

    public Collection<String> getAllowedIntNames() {
        return null;
    }

    public Collection<Boolean> getAllowedBooleanValues() {
        return null;
    }

    public Collection<String> getAllowedBooleanNames() {
        return null;
    }

    public Collection<Double> getAllowedDoubleValues() {
        return null;
    }

    public Collection<String> getAllowedDoubleNames() {
        return null;
    }
}
