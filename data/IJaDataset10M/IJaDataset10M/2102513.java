package org.commerce.mismo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.enums.Enum;

/**
 * Specifies the purpose for which the loan proceeds will be used.
 *  
 * @version $Id: LoanPurposeType.java,v 1.1.1.1 2007/04/16 05:07:03 clafonta Exp $
 */
public final class LoanPurposeType extends Enum {

    /**
     * A short term, interim loan for financing the cost of construction. The
     * lender advances funds to the builder at periodic intervals as work
     * progresses.
     */
    public static final LoanPurposeType CONSTRUCTION_ONLY = new LoanPurposeType("ConstructionOnly");

    /**
     * The subject loan will be used for construction financing and then be
     * converted to permanent financing.
     */
    public static final LoanPurposeType CONSTRUCTION_TO_PERMANENT = new LoanPurposeType("ConstructionToPermanent");

    /**
     * Other
     */
    public static final LoanPurposeType OTHER = new LoanPurposeType("Other");

    /**
     * A loan made in association with the original purchase of a piece of
     * property.
     */
    public static final LoanPurposeType PURCHASE = new LoanPurposeType("Purchase");

    /**
     * The repayment of a debt from proceeds of a new loan using the same.
     */
    public static final LoanPurposeType REFINANCE = new LoanPurposeType("Refinance");

    /**
     * Creates a new LoanPurposeType object
     *
     * @param name a brief description of this enum value
     */
    private LoanPurposeType(String name) {
        super(name);
    }

    /**
     * Returns the <code>LoanPurposeType</code> that maps to the given description
     * 
     * @param  type a short description of the <code>LoanPurposeType</code>
     * @return the <code>LoanPurposeType</code> that maps to the given description
     */
    public static LoanPurposeType getEnum(String type) {
        return (LoanPurposeType) getEnum(LoanPurposeType.class, type);
    }

    /**
     * Returns a map of enumerated string descriptions and their associated
     * <code>LoanPurposeType</code> objects.
     * 
     * @return a map of enumerated string descriptions and their associated
     *         <code>LoanPurposeType</code> objects.
     */
    public static Map getEnumMap() {
        return getEnumMap(LoanPurposeType.class);
    }

    /**
     * Returns a list of all <code>LoanPurposeType</code> types
     * 
     * @return a list of all <code>LoanPurposeType</code> types
     */
    public static List getEnumList() {
        return getEnumList(LoanPurposeType.class);
    }

    /**
     * Returns an iterator over the collection of possible
     * <code>LoanPurposeType</code> types
     * 
     * @return an iterator over the collection of possible
     *         <code>    LoanPurposeType</code> types
     */
    public static Iterator iterator() {
        return iterator(LoanPurposeType.class);
    }
}
