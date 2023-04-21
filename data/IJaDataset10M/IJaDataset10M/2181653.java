package org.mandarax.examples.crm.domainmodel;

/**
 * Represents a categories of customers. Something like "Gold customer program".
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 1.2
 */
public class Category extends BusinessObject {

    public static final Category PLATINUM = new Category("Platinum");

    public static final Category GOLD = new Category("Gold");

    public static final Category STANDARD = new Category("Standard");

    private String description = "?";

    /**
     * Constructor.
     * @param aDescription java.lang.String
     */
    public Category() {
        super();
    }

    /**
     * Constructor.
     * @param aDescription java.lang.String
     */
    public Category(String aDescription) {
        super();
        description = aDescription;
    }

    /**
     * Compares objects.
     * @return boolean
     * @param obj java.lang.Object
     */
    public boolean equals(Object obj) {
        return (obj instanceof Category) && description.equals(((Category) obj).description);
    }

    /**
     * Get the description.
     * @return java.lang.String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the hash code.
     * @return int
     */
    public int hashCode() {
        return (description == null) ? 0 : description.hashCode();
    }

    /**
     * Convert the object to a string.
     * @return java.lang.String
     */
    public String toString() {
        return getDescription();
    }
}
