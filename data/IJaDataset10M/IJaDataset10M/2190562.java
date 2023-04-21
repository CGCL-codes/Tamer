package org.nkumar.stl;

/**
 * A BinaryPredicate represents a method whose result represents the truth or
 * falsehood of some condition when applied on two objects.
 * @author Nishant Kumar
 */
public interface BinaryPredicate<T> {

    /**
     * Returns true if the objects passed to this method satisfy the condition that this method embodies.
     * @param t1 The first object.
     * @param t2 The second object.
     * @return <code>true</code> if the objects satisfy the condition.
     */
    @SuppressWarnings({ "BooleanMethodNameMustStartWithQuestion" })
    public boolean test(T t1, T t2);
}
