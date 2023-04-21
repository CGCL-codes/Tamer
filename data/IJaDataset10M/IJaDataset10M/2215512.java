package de.javagimmicks.math;

import java.math.BigInteger;
import java.util.Collection;

/**
 * A class that sequentially returns all combinations of a certain number out of
 * an array of given elements. Thanks to Michael Gillegand for the base
 * implementation: <a href="http://www.merriampark.com/comb.htm"/>
 * 
 * @author Hendrik Maryns
 * @param <T> The type of the elements of which combinations are to be returned.
 */
public class Combinator<T> extends CombinatoricOperator<T> {

    /**
	 * Initialise a new Combinator, with given elements and size of the arrays
	 * to be returned.
	 * 
	 * @param elements
	 *            The elements of which combinations have to be computed.
	 * @param r
	 *            The size of the combinations to compute.
	 */
    public Combinator(T[] elements, int r) {
        super(elements, r);
        assert r <= elements.length;
    }

    /**
	 * Initialise a new Combinator, with given elements and size of the arrays
	 * to be returned.
	 * 
	 * @param elements
	 *            The elements of which combinations have to be computed.
	 * @param r
	 *            The size of the combinations to compute.
	 */
    public Combinator(Collection<T> elements, int r) {
        super(elements, r);
        if (r > elements.size()) {
            throw new IllegalArgumentException("Size of lists to create mustn't be greater than number of provided elements!");
        }
    }

    /**
	 * Compute the total number of elements to return.
	 * 
	 * @return The factorial of the number of elements divided by the factorials
	 *         of the size of the combinations and the number of elements minus
	 *         the size of the combinations. That is, with the number of
	 *         elements = n and the size of the combinations = r: n n! ( ) =
	 *         --------- r (n-r)!r!
	 * @see CombinatoricOperator#initialiseTotal(int, int)
	 */
    @Override
    protected BigInteger initialiseTotal(int n, int r) {
        BigInteger nFact = factorial(n);
        BigInteger rFact = factorial(r);
        BigInteger nminusrFact = factorial(n - r);
        return nFact.divide(rFact.multiply(nminusrFact));
    }

    /**
	 * Compute the next array of indices.
	 * 
	 * @see CombinatoricOperator#computeNext()
	 */
    @Override
    protected void computeNext() {
        int r = indices.length;
        int i = r - 1;
        int n = elements.size();
        while (indices[i] == n - r + i) {
            i--;
        }
        indices[i] = indices[i] + 1;
        for (int j = i + 1; j < r; j++) {
            indices[j] = indices[i] + j - i;
        }
    }
}
