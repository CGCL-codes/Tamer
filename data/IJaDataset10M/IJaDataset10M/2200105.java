package pcgen.cdom.reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import pcgen.base.lang.StringUtil;
import pcgen.cdom.base.PrereqObject;

/**
 * A CDOMTypeRef is a CDOMReference which is intended to contain objects of a
 * given Type for the Class this CDOMTypeRef represents.
 * 
 * @param <T>
 *            The Class of the underlying objects contained by this CDOMTypeRef
 */
public final class CDOMTypeRef<T extends PrereqObject> extends CDOMGroupRef<T> {

    /**
	 * The objects of the Class this CDOMTypeRef represents
	 */
    private List<T> referencedList = null;

    /**
	 * The Types of objects this CDOMTypeRef contains
	 */
    private final String[] types;

    /**
	 * Constructs a new CDOMTypeRef for the given Class to be represented by
	 * this CDOMTypeRef and the given types.
	 * 
	 * @param cl
	 *            The Class of the underlying objects contained by this
	 *            reference.
	 * @param val
	 *            An array of the Types of objects this CDOMTypeRef contains.
	 */
    public CDOMTypeRef(Class<T> cl, String[] val) {
        super(cl, cl.getSimpleName() + " " + Arrays.deepToString(val));
        types = new String[val.length];
        System.arraycopy(val, 0, types, 0, val.length);
    }

    /**
	 * Returns a representation of this CDOMTypeRef, suitable for storing in an
	 * LST file.
	 * 
	 * Note that this will return the identifier of the "Type" reference (of the
	 * types given at construction), not an expanded list of the contents of
	 * this CDOMTypeRef.
	 * 
	 * @return A representation of this CDOMTypeRef, suitable for storing in an
	 *         LST file.
	 * @see pcgen.cdom.base.CDOMReference#getLSTformat()
	 */
    @Override
    public String getLSTformat() {
        return "TYPE=" + StringUtil.join(types, ".");
    }

    /**
	 * Returns true if the given Object is included in the Collection of Objects
	 * to which this CDOMTypeRef refers.
	 * 
	 * Note that the behavior of this class is undefined if the CDOMTypeRef has
	 * not yet been resolved.
	 * 
	 * @param obj
	 *            The object to be tested to see if it is referred to by this
	 *            CDOMTypeRef.
	 * @return true if the given Object is included in the Collection of Objects
	 *         to which this CDOMTypeRef refers; false otherwise.
	 * @throws IllegalStateException
	 *             if the CDOMTypeRef has not been resolved
	 */
    @Override
    public boolean contains(T obj) {
        if (referencedList == null) {
            throw new IllegalStateException("Cannot ask for contains: Reference has not been resolved");
        }
        return referencedList.contains(obj);
    }

    /**
	 * Returns true if this CDOMTypeRef is equal to the given Object. Equality
	 * is defined as being another CDOMTypeRef object with equal Class
	 * represented by the reference and equal types. This is NOT a deep .equals,
	 * in that the actual contents of this CDOMTypeRef are not tested.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object o) {
        if (o instanceof CDOMTypeRef) {
            CDOMTypeRef<?> ref = (CDOMTypeRef<?>) o;
            return getReferenceClass().equals(ref.getReferenceClass()) && getName().equals(ref.getName()) && Arrays.deepEquals(types, ref.types);
        }
        return false;
    }

    /**
	 * Returns the consistent-with-equals hashCode for this CDOMTypeRef
	 * 
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        return getReferenceClass().hashCode() ^ getName().hashCode();
    }

    /**
	 * Adds an object to be included in the Collection of objects to which this
	 * CDOMTypeRef refers.
	 * 
	 * @param obj
	 *            An object to be included in the Collection of objects to which
	 *            this CDOMTypeRef refers.
	 * @throws IllegalArgumentException
	 *             if the given object for addition to this CDOMTypeRef is not
	 *             of the class that this CDOMTypeRef represents
	 * @throws NullPointerException
	 *             if the given object is null
	 */
    @Override
    public void addResolution(T obj) {
        if (obj.getClass().equals(getReferenceClass())) {
            if (referencedList == null) {
                referencedList = new ArrayList<T>();
            }
            referencedList.add(obj);
        } else {
            throw new IllegalArgumentException("Cannot resolve a " + getReferenceClass().getSimpleName() + " Reference to a " + obj.getClass().getSimpleName());
        }
    }

    /**
	 * Returns the count of the number of objects included in the Collection of
	 * Objects to which this CDOMTypeRef refers.
	 * 
	 * Note that the behavior of this class is undefined if the CDOMTypeRef has
	 * not yet been resolved.
	 * 
	 * @return The count of the number of objects included in the Collection of
	 *         Objects to which this CDOMTypeRef refers.
	 */
    @Override
    public int getObjectCount() {
        return referencedList == null ? 0 : referencedList.size();
    }

    /**
	 * Returns a Collection containing the Objects to which this CDOMTypeRef
	 * refers.
	 * 
	 * This method is reference-semantic, meaning that ownership of the
	 * Collection returned by this method is transferred to the calling object.
	 * Modification of the returned Collection should not result in modifying
	 * the CDOMTypeRef, and modifying the CDOMTypeRef after the Collection is
	 * returned should not modify the Collection.
	 * 
	 * Note that the behavior of this class is undefined if the CDOMTypeRef has
	 * not yet been resolved. (It may return null or an empty Collection; that
	 * is implementation dependent)
	 * 
	 * @return A Collection containing the Objects to which this CDOMTypeRef
	 *         refers.
	 */
    @Override
    public Collection<T> getContainedObjects() {
        if (referencedList == null) {
            throw new IllegalStateException("Cannot ask for contained objects: Reference has not been resolved");
        }
        return Collections.unmodifiableList(referencedList);
    }
}
