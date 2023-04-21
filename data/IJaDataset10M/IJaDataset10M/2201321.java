package tudresden.ocl20.pivot.essentialocl.standardlibrary;

public interface OclComparable extends OclAny {

    /**
	 * Compare two OCL objects.
	 * 
	 * @param object
	 *            the object to compare to
	 * 
	 * @return a negative integer, zero, or a positive integer as this object is
	 *         less than, equal to, or greater than the specified object.
	 */
    public OclInteger compareTo(OclComparable object);
}
