package org.freeorion.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/** An Enumeration that filters another Enumeration by dropping such
 * elements which don't pass a given test.
 *
 * The actual test has to be implemented by subclasses.
 */
public abstract class FilterEnumeration implements Enumeration {

    /** The original Enumeration. */
    protected Enumeration enumeration;

    /** The next Object. */
    protected Object theNextObject;

    /** Creates a filter for a Enumeration. Only thus elements which
	 * are instances of a given class will be enumerated.
	 *
	 * @param enumeration the Enumeration to be filtered
	 */
    public FilterEnumeration(Enumeration enumeration) {
        this.enumeration = enumeration;
    }

    /** Tests if the Enumeration has more elements. This is the case
	 * if the underlying Enumeration has more elements which will pass
	 * the test.
	 *
	 * @return true if there are more elements, false otherwise.
	 */
    public boolean hasMoreElements() {
        return theNextObject != null;
    }

    /** Tests if the Enumeration has more elements. This is the case
	 * if the underlying Enumeration has more elements which will pass
	 * the test.
	 *
	 * @return true if there are more elements, false otherwise.
	 */
    public Object nextElement() throws NoSuchElementException {
        if (!hasMoreElements()) throw new NoSuchElementException();
        return moveOn();
    }

    /** This sets theNextObject to the next accessible Object in the
	 * numeration.
	 *
	 * @return the actual accessible Object.
	 */
    protected Object moveOn() {
        Object actualObject = theNextObject;
        while (enumeration.hasMoreElements()) {
            theNextObject = enumeration.nextElement();
            if (test(theNextObject)) {
                return actualObject;
            }
        }
        theNextObject = null;
        return actualObject;
    }

    /** A test Objects have to pass to be enumerated. This method has
	 * to be implemented by subclasses.
	 *
	 * @param object an element of the Enumeration.
	 *
	 * @return true if the object shall appear in the filtered
	 * Enumeration, false otherwise.
	 */
    public abstract boolean test(Object object);
}
