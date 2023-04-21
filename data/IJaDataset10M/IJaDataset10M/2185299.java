package visad;

/**
 * Scalar is the superclass of the VisAD hierarchy of scalar data.<P>
 */
public abstract class Scalar extends DataImpl implements ScalarIface, Comparable {

    public Scalar(ScalarType type) {
        super(type);
    }

    /**
   * Adds a listener for changes to this instance.  Because instances of this
   * class don't change, this method does nothing.
   *
   * @param listener                     The listener for changes.
   */
    public final void addReference(ThingReference listener) {
    }

    /**
   * Removes a listener for changes to this instance.  Because instances of this
   * class don't change, this method does nothing.
   *
   * @param listener                    The change listener to be removed.
   */
    public final void removeReference(ThingReference listener) {
    }

    /**
   * Indicates if this scalar is semantically identical to an object.
   * @param obj			The object.
   * @return			<code>true</code> if and only if this scalar
   *				is semantically identical to the object.
   */
    public abstract boolean equals(Object obj);

    /**
   * Clones this instance.
   *
   * @return                      A clone of this instance.
   */
    public final Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException("Assertion failure");
        }
    }
}
