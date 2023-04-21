package net.sf.saxon.om;

import net.sf.saxon.trans.XPathException;

/**
 * A ValueRepresentation is a representation of a Value. This is a marker interface
 * used to represent the union of two classes: Value, and NodeInfo.
 * Either of these two classes can be used to represent a value. The class is used primarily
 * to represent the value of a variable.
 * <p>
 * This class is intended primarily for internal use, and should not be considered part
 * of the Saxon public API.
 */
public interface ValueRepresentation {

    /**
     * An empty array of ValueRepresentation objects
     */
    public static final ValueRepresentation[] EMPTY_VALUE_ARRAY = new ValueRepresentation[0];

    /**
     * Convert the value to a string, using the serialization rules.
     * For atomic values this is the same as a cast; for sequence values
     * it gives a space-separated list. For nodes, it returns the string value of the
     * node as defined in XDM.
     * @throws XPathException The method can fail if evaluation of the value
     * has been deferred, and if a failure occurs during the deferred evaluation.
     * No failure is possible in the case of an AtomicValue or a Node.
     */
    public String getStringValue() throws XPathException;

    /**
     * Convert the value to a string, using the serialization rules,
     * and returning the result as a CharSequence. In some cases this may be more
     * efficient than obtaining the result as a string.
     * For atomic values the result is the same as a cast; for sequence values
     * it gives a space-separated list. For nodes, it returns the string value of the
     * node as defined in XDM.
     * @throws XPathException The method can fail if evaluation of the value
     * has been deferred, and if a failure occurs during the deferred evaluation.
     * No failure is possible in the case of an AtomicValue or a Node.
     */
    public CharSequence getStringValueCS() throws XPathException;
}
