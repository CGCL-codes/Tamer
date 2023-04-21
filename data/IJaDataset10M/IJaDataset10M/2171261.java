package net.sf.saxon.sort;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.value.AtomicValue;

/**
 * A comparer for comparing two "ordinary" atomic values, where the values implement the Comparable
 * interface and the equals() method with the appropriate XPath semantics. This rules out use of
 * collations, conversion of untyped atomic values, and context dependencies such as implicit timezone.
 */
public class ComparableAtomicValueComparer implements AtomicComparer {

    private static ComparableAtomicValueComparer THE_INSTANCE = new ComparableAtomicValueComparer();

    /**
     * Get the singleton instance of this class
     * @return  the singleton instance of this class
     */
    public static ComparableAtomicValueComparer getInstance() {
        return THE_INSTANCE;
    }

    protected ComparableAtomicValueComparer() {
    }

    /**
     * Supply the dynamic context in case this is needed for the comparison
     *
     * @param context the dynamic evaluation context
     * @return either the original AtomicComparer, or a new AtomicComparer in which the context
     *         is known. The original AtomicComparer is not modified
     */
    public AtomicComparer provideContext(XPathContext context) {
        return this;
    }

    /**
     * Compare two AtomicValue objects according to the rules for their data type. UntypedAtomic
     * values are compared as if they were strings; if different semantics are wanted, the conversion
     * must be done by the caller.
     *
     * @param a the first object to be compared. This must be an AtomicValue and it must implement
     * Comparable with context-free XPath comparison semantics
     * @param b the second object to be compared. This must be an AtomicValue and it must implement
     * Comparable with context-free XPath comparison semantics
     * @return <0 if a<b, 0 if a=b, >0 if a>b
     * @throws ClassCastException if the objects are not comparable
     */
    public int compareAtomicValues(AtomicValue a, AtomicValue b) {
        if (a == null) {
            return (b == null ? 0 : -1);
        } else if (b == null) {
            return +1;
        }
        return ((Comparable) a).compareTo(b);
    }

    /**
     * Compare two AtomicValue objects for equality according to the rules for their data type. UntypedAtomic
     * values are compared by converting to the type of the other operand.
     * @param a the first object to be compared. This must be an AtomicValue and it must implement
     * equals() with context-free XPath comparison semantics
     * @param b the second object to be compared. This must be an AtomicValue and it must implement
     * equals() with context-free XPath comparison semantics
     * @return true if the values are equal, false if not
     * @throws ClassCastException if the objects are not comparable
     */
    public boolean comparesEqual(AtomicValue a, AtomicValue b) {
        return a.equals(b);
    }

    /**
     * Get a comparison key for an object. This must satisfy the rule that if two objects are equal,
     * then their comparison keys are equal, and vice versa. There is no requirement that the
     * comparison keys should reflect the ordering of the underlying objects.
     */
    public ComparisonKey getComparisonKey(AtomicValue a) {
        return new ComparisonKey(a.getPrimitiveType().getFingerprint(), a);
    }
}
