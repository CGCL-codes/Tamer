package mipt.math.array.impl;

import mipt.math.Number;

/**
 * Decomposition calculating some properties of matrix (given as Number[][] array).
 * An implementation must have default (argumentless) constructor.
// * Decomposition used to calculate determinant and/or inverse matrix OR eigenvalues
// *  (if you want to calculate some specific feature you do not need to implement this class!).
// * One implementation is not required to implement all methods: two or even more implementations
// *  are often used to implement all methods in LinAlgebra.
// * So the implementation usually should have some method body(ies) like this:
// *  throw new UnsupportedOperationException("** should be computed not by ***Decomposition");
 * Note: Decomposition concept is not a explicit part of LinAlgebra concept (and can be
 *  used independently of it!) but AbstractLinAlgebra subclasses should use Decompositions. 
 * @author Evdokimov
 */
public interface Decomposition {

    /**
	 * The method is often called once - just after the constructor.
	 * In implementation, the matrix given should not be stored itself,
	 *  it should be converted to some internal data structure.
	 */
    void decompose(Number[][] A);
}
