package org.exolab.jms.selector;

/**
 * This class is the base class for classes adapting simple Java types.
 * This is necessary to:
 * <ul>
 * <li>reduce the number of types that the selector has to deal with.
 *     Expressions only evaluate to boolean, numeric and string types.
 * </li>
 * <li>Simplify operations (comparison, type checking etc.) on these types.
 * </li>
 * </ul>
 *
 * @version     $Revision: 1.1 $ $Date: 2004/11/26 01:50:44 $
 * @author      <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @see         Expression
 * @see         SObjectFactory
 */
abstract class SObject {

    /**
     * Returns the underlying object
     *
     * @return the underlying object
     */
    public abstract Object getObject();

    /**
     * Determines if this is equal to another object.
     *
     * @param obj the object to compare
     * @return <code>null</code> if the comparison is undefined,
     * <code>SBool.TRUE</code> if <code>this = obj</code>, otherwise
     * <code>SBool.FALSE</code> if <code>this &lt;&gt; obj</code>
     */
    public SBool equal(final SObject obj) {
        SBool result = SBool.FALSE;
        if (getObject().equals(obj.getObject())) {
            result = SBool.TRUE;
        }
        return result;
    }

    /**
     * Determines if this is not equal to another object
     *
     * @param obj the object to compare
     * @return <code>null</code> if the comparison is undefined,
     * <code>SBool.TRUE</code> if <code>this &lt;&gt; obj</code>, otherwise
     * <code>SBool.FALSE</code> if <code>this = obj</code>
     */
    public SBool notEqual(final SObject obj) {
        SBool result = equal(obj);
        if (result != null) {
            result = result.not();
        }
        return result;
    }

    /**
     * Determines if this is less than another object.
     *
     * @param obj the object to compare
     * @return <code>null</code> if the comparison is undefined,
     * <code>SBool.TRUE</code> if <code>this &lt; obj</code>, otherwise
     * <code>SBool.FALSE</code> if <code>this &gt;= obj</code>
     */
    public SBool less(final SObject obj) {
        return null;
    }

    /**
     * Determines if this is greater than another object.
     *
     * @param obj the object to compare
     * @return <code>null</code> if the comparison is undefined,
     * <code>SBool.TRUE</code> if <code>this &gt; obj</code>, otherwise
     * <code>SBool.FALSE</code> if <code>this &lt;= obj</code>
     */
    public SBool greater(final SObject obj) {
        return null;
    }

    /**
     * Determines if this is less than or equal to another object.
     *
     * @param obj the object to compare
     * @return <code>null</code> if the comparison is undefined,
     * <code>SBool.TRUE</code> if <code>this &lt;= obj</code>, otherwise
     * <code>SBool.FALSE</code> if <code>this &gt; obj</code>
     */
    public SBool lessEqual(final SObject obj) {
        SBool result = less(obj);
        if (result != null && !result.value()) {
            result = equal(obj);
        }
        return result;
    }

    /**
     * Determines if this is greater than or equal to another object.
     *
     * @param obj the object to compare
     * @return <code>null</code> if the comparison is undefined,
     * <code>SBool.TRUE</code> if <code>this &gt;= obj</code>, otherwise
     * <code>SBool.FALSE</code> if <code>this &lt; obj</code>
     */
    public SBool greaterEqual(final SObject obj) {
        SBool result = greater(obj);
        if (result != null && !result.value()) {
            result = equal(obj);
        }
        return result;
    }

    /**
     * Returns a string representation of this
     *
     * @return a string representation of this
     */
    public String toString() {
        return getObject().toString();
    }

    /**
     * Determines the type of this
     *
     * @return the type of this
     */
    public abstract Type type();
}
