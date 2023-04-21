package org.commerce.mismo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.enums.Enum;

/**
 * Specifies the intended property ownership rights for the property.
 * 
 * @version $Id: PropertyRightsType.java,v 1.1.1.1 2007/04/16 05:07:03 clafonta Exp $
 * @see     org.commerce.mismo.LoanPurpose#getPropertyRightsType()
 * @see     org.commerce.mismo.LoanPurpose#setPropertyRightsType(PropertyRightsType)
 */
public final class PropertyRightsType extends Enum {

    /**
     * The greatest possible interest a person can have in real estate,
     * including the right to dispose of the property or pass it on to one�s
     * heirs. 
     */
    public static final PropertyRightsType FEE_SIMPLE = new PropertyRightsType("FeeSimple");

    /**
     * An estate or interest in real property held by virtue of a lease.
     */
    public static final PropertyRightsType LEASEHOLD = new PropertyRightsType("Leasehold");

    /**
     * Creates a new PropertyRightsType object
     *
     * @param name a brief description of this enum value
     */
    private PropertyRightsType(String name) {
        super(name);
    }

    /**
     * Returns the <code>PropertyRightsType</code> that maps to the given description
     * 
     * @param  type a short description of the <code>PropertyRightsType</code>
     * @return the <code>PropertyRightsType</code> that maps to the given description
     */
    public static PropertyRightsType getEnum(String type) {
        return (PropertyRightsType) getEnum(PropertyRightsType.class, type);
    }

    /**
     * Returns a map of enumerated string descriptions and their associated
     * <code>PropertyRightsType</code> objects.
     * 
     * @return a map of enumerated string descriptions and their associated
     *         <code>PropertyRightsType</code> objects.
     */
    public static Map getEnumMap() {
        return getEnumMap(PropertyRightsType.class);
    }

    /**
     * Returns a list of all <code>PropertyRightsType</code> types
     * 
     * @return a list of all <code>PropertyRightsType</code> types
     */
    public static List getEnumList() {
        return getEnumList(PropertyRightsType.class);
    }

    /**
     * Returns an iterator over the collection of possible
     * <code>PropertyRightsType</code> types
     * 
     * @return an iterator over the collection of possible
     *         <code>    PropertyRightsType</code> types
     */
    public static Iterator iterator() {
        return iterator(PropertyRightsType.class);
    }
}
