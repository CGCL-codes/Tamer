package info.nekonya.math.vector;

/**
 * Represents n-dimensional vectors over a numerical field (that is, any field whose elements can be represented
 * with java.lang.Number).  This interface supports vector-sums (see {@link Vector#sum(Vector) sum}),
 * scalar-products (see {@link Vector#product(Number) product}), and inner products 
 * (see {@link Vector#innerProduct(Vector) innerProduct}) -- and also vector-differences (see 
 * {@link Vector#difference(Vector) difference}) by way of inverses (see {@link Vector#inverse() inverse}).
 * 
 * <p>It extends the iterface java.util.List&lt;Number>, and so a vector can be thought of as an ordered
 * collection of numbers.</p>
 * 
 * <p>Implementations of this interface can be instantiated with 
 * {@link VectorFactory VectorFactory}.</p>
 * 
 * @see VectorFactory
 */
public interface Vector extends java.util.List<Number> {

    /**
	 * Returns the norm (magnitude) of the receiver.  This is the Euclidean norm -- that is, the
	 * square root of the sum of the squares of the components.
	 * @return The (Euclidean) norm of the receiver.
	 */
    double norm();

    /**
	 * Returns whether the receiver is equal to the given vector.  To vectors are considered equal if and
	 * only if their dimensionalities are equal and the members of each pair of corresponding 
	 * components area equal.
	 * @param vector The vector with which to compare the receiver.
	 * @return Whether the receiver is equal to <code>vector</code>.
	 */
    boolean equals(Vector vector);

    /**
	 * Returns the result of adding the given vector to the receiver.  The sum of two vectors 
	 * &lt;<i>a1</i>,&nbsp;<i>a2</i>,&nbsp;...,&nbsp;<i>aN</i>> and 
	 * &lt;<i>b1</i>,&nbsp;<i>b2</i>,&nbsp;...,&nbsp;<i>bN</i>>
	 * is &lt;<i>a1</i>+<i>b1</i>,&nbsp;<i>a2</i>+<i>b2</i>,&nbsp;...,&nbsp;<i>aN</i>+<i>bN</i>>.
	 * @param vector A vector with the same dimensionality as the receiver.
	 * @return The sum of the receiver and <code>vector</code>.
	 * @throws IllegalArgumentException If <code>vector</code>&rsquo;s dimensionality is different from
	 * the receiver&rsquo;s dimensionality.
	 */
    Vector sum(Vector vector) throws IllegalArgumentException;

    /**
	 * Returns the result of subtracting the given vector from the receiver.  The difference of two 
	 * vectors <b>v1</b> and <b>v2</b> is <b>v1</b>+(-1*<b>v2</b>).
	 * @param vector A vector with the same dimensionality as the receiver.
	 * @return The difference of the receiver and <code>vector</code>.
	 * @throws IllegalArgumentException If <code>vector</code>&rsquo;s dimensionality is different from
	 * the receiver&rsquo;s dimensionality.
	 */
    Vector difference(Vector vector) throws IllegalArgumentException;

    /**
	 * Returns the result of multiplying the given scalar value by the receiver.  The product of a scalar
	 * value <i>k</i> and a vector &lt;<i>a1</i>,&nbsp;<i>a2</i>,&nbsp;...,&nbsp;<i>aN</i>> is 
	 * &lt;<i>k</i>*<i>a1</i>,&nbsp;<i>k</i>*<i>a2</i>,&nbsp;...,&nbsp;<i>k</i>*<i>aN</i>>.
	 * @param scalar A scalar value.
	 * @return The product of <code>scalar</code> and the receiver.
	 */
    Vector product(Number scalar);

    /**
	 * Returns the inner product of the receiver and the given vector.
	 * @param vector A vector with the same dimensionality as the receiver.
	 * @return The inner product of the receiver and <code>vector</code>.
	 */
    Number innerProduct(Vector vector);

    /**
	 * Returns whether the receiver is equal to the zero vector (of its vector space).  The zero vector
	 * is the vector <b>Z</b> such that, for any vector <b>V</b> in the same vector space, 
	 * <b>Z</b>*<b>V</b>&nbsp;=&nbsp;<b>V</b>*<b>Z</b>&nbsp;=&nbsp;<b>V</b> -- that is, it
	 * is the identity element of vector addition for this vector space.
	 * @return Whether the receiver is equal to the zero vector.
	 */
    boolean isZero();

    /**
	 * Returns the additive inverse of the receiver.  The additive inverse of a vetor V is a vector I 
	 * such that V+I = I+V = Z, where Z is the zero vector for the vector space.
	 * @return The additive inverse of the receiver.
	 */
    Vector inverse();

    /**
	 * Returns the unit vector made by normalizing the receiver.
	 * @return A unit vector with the same direction as the receiver.
	 */
    Vector unitVector();

    /**
	 * Sorts the vector's components.
	 * @param descending If true, the components will be sorted from largest to smalled.
	 * @return A vector with the receiver's components sorted.
	 */
    Vector sorted(boolean descending);

    /**
	 * Sorts the vector's components from smallest to largest.
	 * @return A vector with the receiver's components sorted.
	 */
    Vector sorted();
}
