package edu.ucsb.ccs.jaqual;

/**
 * Abstract description of an assertion that can be evaluated on an
 * element of a collection.
 *
 * <p>Assertions can be (fairly) easily defined inline using anonymous
 * classes:
 *
 * <pre>
 *    // Make sure that Vector `v' contains only Integers.
 *    ForAll.in(collection).ensure(new Assertion() {
 *      public boolean eval(Object o) {
 *        return (o instanceof Integer);
 *      }
 *     });
 * </pre>
 *
 * @author Parker Abercrombie
 * @version $Id: Assertion.java,v 1.1 2002/05/02 06:27:15 parkera Exp $
 */
public interface Assertion {

    /**
   * Evaluate the assertion on an object.
   *
   * @param o the object to test.
   *
   * @return true if the assertion evaluates true.
   */
    public boolean eval(Object o);
}
