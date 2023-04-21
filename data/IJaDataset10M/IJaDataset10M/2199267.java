package com.volantis.mcs.model.property;

/**
 * Uniquely identifier a property for a specific model class.
 *
 * @todo Remove containing class as that is not actually used to uniquely identify a property identifier, the name has to do that. This is because in some places a String is provided as the property identifier.
 */
public class PropertyIdentifier {

    private final Class containingClass;

    private final String name;

    private String description;

    /**
     * Initialise.
     *
     * <p>Together the name and containing class must uniquely identifier
     * the property.</p>
     *
     * @param containingClass The class that contains this property.
     * @param name The name of the property.
     */
    public PropertyIdentifier(Class containingClass, String name) {
        this(containingClass, name, name);
    }

    /**
     * Initialise.
     *
     * <p>Together the name and containing class must uniquely identifier
     * the property.</p>
     *
     * @param containingClass The class that contains this property.
     * @param name            The name of the property.
     * @param description     The description of the property, this must be
     *                        used in all messages reported to the user.
     */
    public PropertyIdentifier(Class containingClass, String name, String description) {
        if (containingClass == null) {
            throw new IllegalArgumentException("containingClass cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (name.length() == 0) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        if (description == null) {
            throw new IllegalArgumentException("description cannot be null");
        }
        if (description.length() == 0) {
            throw new IllegalArgumentException("description cannot be empty");
        }
        char ch = name.charAt(0);
        if (!Character.isLetter(ch)) {
            throw new IllegalArgumentException("name must start with a letter");
        }
        for (int i = 1; i < name.length(); i += 1) {
            ch = name.charAt(i);
            if (!Character.isLetterOrDigit(ch) && ch != '-') {
                throw new IllegalArgumentException("name can only contain letters, digits, or -");
            }
        }
        this.containingClass = containingClass;
        this.name = name;
        this.description = description;
    }

    /**
     * Get the name of the property within the class.
     *
     * @return The name of the property within the class.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the class that contains this property.
     *
     * @return The class that contains this property.
     */
    public Class getContainingClass() {
        return containingClass;
    }

    /**
     * Get the human readable description of this property.
     *
     * @return The human readable description of this property.
     */
    public String getDescription() {
        return description;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PropertyIdentifier)) {
            return false;
        }
        PropertyIdentifier other = (PropertyIdentifier) obj;
        return getContainingClass() == other.getContainingClass() && getName().equals(other.getName());
    }

    public int hashCode() {
        int result = getContainingClass().hashCode();
        result = result * 37 + getName().hashCode();
        return result;
    }

    public String toString() {
        return containingClass + "[" + name + "]";
    }
}
