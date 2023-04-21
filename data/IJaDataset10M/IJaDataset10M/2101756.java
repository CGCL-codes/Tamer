package net.sf.molae.pipe.binop;

/**
 * Skeleton implementation of <code>BinaryFunction</code> for symmetric
 * functions.
 * @version 2.0
 * @author Ralph Wagner
 */
public abstract class SymmetricBinaryFunction<S, T> extends AbstractBinaryFunction<S, S, T> {

    /**
     * Constructs a new SymmetricBinaryFunction.
     * @param name the result of the {@link #toString()} method
     * @param associative this value is returned by the 
     * @param identityElement the identity element of this function
     * <code>null</code> if no such element exists or is not known.
     * @param sink the sink of this function
     * <code>null</code> if no such element exists or is not known.
     * {@link #isAssociative()} method.
     */
    public SymmetricBinaryFunction(String name, boolean associative, S identityElement, S sink) {
        super(name, associative, (identityElement != null), (sink != null));
        setPermuted(this);
        this.identityElement = identityElement;
        this.sink = sink;
    }

    final S identityElement;

    /**
     * Returns the identity element of this function.
     * The identitiy element fulfils for every object
     * <code>a</code> where <code>compute</code> is defined:
     * <code>a.equals(compute(getIdentityElement(), a))</code> and
     * <code>a.equals(compute(a, getIdentityElement()))</code>.
     * @return the identity element of this function or
     * <code>null</code> if no such element exists or is not known.
     * @throws UnsupportedOperationException if not implemented
     */
    public S getIdentityElement() {
        return identityElement;
    }

    final S sink;

    /**
     * Returns the sink of this function.
     * The sink fulfils for every object
     * <code>a</code> where <code>compute</code> is defined:
     * <code>getSink().equals(compute(getSink(), a))</code> and
     * <code>getSink().equals(compute(a, getSink()))</code>.
     * @return the sink of this function or
     * <code>null</code> if no such element exists or is not known.
     */
    public S getSink() {
        return sink;
    }

    /**
     * Tests if the specified element is the identity element of this
     * function.
     * This implementation compares the specified element with the
     * identity element of this function.
     * @param obj the candidate for identity element
     * @return <code>true</code> if the specified object is the identity
     * element of this function.
     */
    protected final boolean isAnIdentityElement(Object obj) {
        return ((obj != null) && obj.equals(getIdentityElement()));
    }

    /**
     * Tests if the specified element is the sink of this
     * function.
     * This implementation compares the specified element with the
     * sink of this function.
     * @param obj the candidate for sink
     * @return <code>true</code> if the specified object is the sink
     *  of this function.
     */
    protected final boolean isASink(Object obj) {
        return ((obj != null) && obj.equals(getSink()));
    }
}
