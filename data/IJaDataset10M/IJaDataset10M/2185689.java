package org.jia.ptrack.domain;

import java.io.Serializable;
import java.util.*;

/**
 * Abstract utility class for impelementing an enumerated type in Java.
 * Usage:
 * <code> GenderType gender = GenderType.MALE; <code>
 *
 * Subclasses should create a set of unique public final static instances, and
 * then add them to the static lists in the static constructor with <pre>addInstance</pre>.
 *
 * See {@link OutputType} for an example.
 *
 * @author Kito D. Mann
 * @version 1.0
 */
public abstract class EnumeratedType implements Serializable {

    public static class EnumManager {

        private HashMap instancesByDesc = new HashMap();

        private HashMap instancesByValue = new HashMap();

        public EnumManager() {
        }

        public void addInstance(EnumeratedType enumeration) {
            instancesByDesc.put(enumeration.getDescription().toLowerCase(), enumeration);
            instancesByValue.put(enumeration.getValue(), enumeration);
        }

        public EnumeratedType getInstanceByValue(String value) throws IllegalArgumentException {
            EnumeratedType instance = (EnumeratedType) instancesByValue.get(value);
            if (instance == null) throw new IllegalArgumentException("Undefined value " + value + " for this type.");
            return instance;
        }

        public EnumeratedType getInstanceByDescription(String description) throws IllegalArgumentException {
            EnumeratedType instance = (EnumeratedType) instancesByDesc.get(description.toLowerCase());
            if (instance == null) throw new IllegalArgumentException("Undefined description (" + description + ") for this type.");
            return instance;
        }

        public Map getInstancesByValue() {
            return Collections.unmodifiableMap(instancesByValue);
        }

        public Map getInstancesByDescription() {
            return Collections.unmodifiableMap(instancesByDesc);
        }

        public Collection getInstances() {
            return Collections.unmodifiableCollection(instancesByDesc.values());
        }
    }

    private String value;

    private String description;

    EnumeratedType() {
    }

    protected EnumeratedType(int value, String description) {
        this(Integer.toString(value), description);
    }

    protected EnumeratedType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static EnumManager getEnumManager() {
        throw new UnsupportedOperationException("The method getEnumManager() needs to be overridden");
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    /** Returns a string representation of the current value of this enumerated type. */
    public String toString() {
        return description;
    }

    /** Compares the value of two EnumeratedType objects. */
    public boolean equals(Object other) {
        if (other == null) return false;
        if (this == other) return true; else if (other.getClass().equals(this.getClass())) {
            String otherValue = ((EnumeratedType) other).getValue();
            return value == null || otherValue == null ? otherValue == value : (otherValue.equals(value));
        } else return false;
    }

    public boolean equals(int value) {
        throw new UnsupportedOperationException("equals(int)");
    }

    /**
   * Returns 0 if the two values are equal, 1 if the value of this object is greater than the value of comparisonType,
   * and -1 if it is less than the value of this type.
   */
    public int compare(EnumeratedType comparisonType) {
        return value.compareTo(comparisonType.getValue());
    }

    public int hashCode() {
        return getValue().hashCode();
    }
}
