package edu.ucsb.ccs.jaqual;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * Quantifier to find a subset of a collection that matches certain
 * criteria.  For example, in the collection {1, 2, 3, 4, 5}, one
 * might wish to extract the integers that are greater than zero.
 *  We can code this as:
 *
 * <p>Example:
 * <pre>
 *   Vector positiveEls = Elements.in(myCollection).suchThat(new InRange(0, Integer.MAX_VALUE));
 * </pre>
 *
 * @author Parker Abercrombie
 * @version $Id: Elements.java,v 1.5 2002/07/14 07:37:59 parkera Exp $
 */
public class Elements {

    /**
   * The collection of objects to which the quantifier applies.
   */
    protected Collection theCollection;

    /**
   * Create a new elements quantifier.
   *
   * @param c the collection of object to which quantifier applies.
   */
    public Elements(Collection c) {
        theCollection = c;
    }

    protected boolean Elements_Precondition(Collection c) {
        return c != null;
    }

    /**
   * Create a new Elements quantifier for an array.
   *
   * @param array the array of objects to which quantifier applies.
   */
    public Elements(Object[] array) {
        theCollection = Arrays.asList(array);
    }

    protected boolean Elements_Precondition(Object[] array) {
        return array != null;
    }

    /**
   * Create a new elements quantifier.
   *
   * @param c the collection of object to which quantifier applies.
   */
    public static Elements in(Collection c) {
        return new Elements(c);
    }

    protected static boolean in_Precondition(Collection c) {
        return c != null;
    }

    /**
   * Find the subset of the collection that meets a given assertion.
   *
   * @param a the test to apply to objects in the collection.
   *
   * @return the elements that meet `a'.
   */
    public Vector suchThat(Assertion a) {
        Vector result = new Vector();
        Object current;
        Iterator i = theCollection.iterator();
        while (i.hasNext()) {
            current = i.next();
            if (a.eval(current)) {
                result.addElement(current);
            }
        }
        return result;
    }

    protected boolean suchThat_Postcondition(Assertion a, Vector RESULT) {
        return (RESULT != null) && (RESULT.size() <= theCollection.size()) && ForAll.in(RESULT).ensure(a) && Logical.implies(RESULT.size() == theCollection.size(), ForAll.in(theCollection).ensure(a));
    }

    protected boolean _Invariant() {
        return theCollection != null;
    }
}
