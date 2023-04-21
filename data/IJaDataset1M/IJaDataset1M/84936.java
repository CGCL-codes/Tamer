package org.jabsorb.serializer;

/**
 * <p>
 * This class is returned from the Serializer tryUnmarshall method to indicate
 * number of mismatched fields. This is used to handle ambiguities with
 * JavaScript's typeless objects combined with and Java's operator overloading.
 * </p>
 * TODO: wouldn't a better name for this class be ObjectMismatch as it's would
 * be more descriptive. The name ObjectMatch is a little confusing because it
 * implies the opposite of what the class actually stores (ObjectMismatch)
 * either that, or I'm not understanding something correctly... [WB: I agree!]
 */
public class ObjectMatch {

    /**
   * The objects match
   */
    public static final ObjectMatch OKAY = new ObjectMatch(-3);

    /**
   * The objects can be converted into the same types, 
   * eg int converts into a string.
   */
    public static final ObjectMatch SIMILAR = new ObjectMatch(-2);

    /**
   * The objects can be converted into the same types, but it should be avoided 
   * eg any string can converts into a boolean, where "true" is true and anything else is false.
   */
    public static final ObjectMatch ROUGHLY_SIMILAR = new ObjectMatch(-1);

    /**
   * The object was null, and therefore matches any object,
   * (because any object reference could be null)
   */
    public static final ObjectMatch NULL = new ObjectMatch(0);

    /**
   * The number of mismatched fields that occurred on a tryUnmarshall call.
   * TODO: make this value final, so it can't be changed!
   */
    private int mismatch;

    /**
   * Create a new ObjectMatch object with the given number of mismatches.
   * 
   * @param mismatch the number of mismatched fields that occured on a
   *          tryUnmarshall call.
   */
    public ObjectMatch(int mismatch) {
        this.mismatch = mismatch;
    }

    /**
   * Get the number of mismatched fields that occured on a tryUnmarshall call.
   * 
   * @return the number of mismatched fields that occured on a tryUnmarshall
   *         call.
   */
    public int getMismatch() {
        return mismatch;
    }

    /**
   * Set the mismatch on this ObjectMatch.
   * The ObjectMatch cannot be immutable anymore (at least in the current design--
   * because the same mismatch object must be maintained through recursive processing
   * to properly handle circular references detection)
   *
   * @param mismatch the mismatch value to set for this ObjectMatch.
   */
    public void setMismatch(int mismatch) {
        this.mismatch = mismatch;
    }

    /**
   * Compare another ObjectMatch with this ObjectMatch and return the one that
   * has the most mismatches.
   * 
   * @param m ObjectMatch to compare this ObjectMatch to.
   * 
   * @return this ObjectMatch if it has more mismatches, else the passed in
   *         ObjectMatch.
   */
    public ObjectMatch max(ObjectMatch m) {
        if (m == null) {
            return this;
        }
        if (this.mismatch > m.mismatch) {
            return this;
        }
        return m;
    }

    public String toString() {
        return Integer.toString(this.mismatch);
    }
}
