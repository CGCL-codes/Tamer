package mipt.math.array.impl;

import mipt.math.Number;

/**
 * Calculates not only eigenvalues (interface method) but eigenvectors too (at the same time).
 * Works with arbitrary (non-symmetric) matrix performing Hessenberg and Schur decompositions.
 *  This code is totally refactored version of Jampack's code:
 *  ftp://math.nist.gov/pub/Jampack/Jampack/Doc/10_Decomp.html#eig by Prof. G. W. Stewart);
 *  the Russian description of it: http://alglib.sources.ru/eigen/nonsymmetric/nonsymmetricevd.php.
 * TO DO: the version for symmetric matrix (and real-only eigenvalues) by QR/QL decomposition:
 *  to refactor the code from Apache Commons Math / linear:
 *  http://commons.apache.org/math/apidocs/org/apache/commons/math/linear/EigenDecompositionImpl.html.
 * TO DO: a symmetric version for finding NOT ALL eigenvalues (by bisection method or ?)
 *  and the appropriate eigenvectors (inverse iteration method); e.g. see
 *  http://alglib.sources.ru/eigen/symmetric/symmbisectionandinverseiteration.php.
 * See also the {@link mipt.math.array.spectrum.PowMethod} for finding dominant (principal) eigenvalue and eigenvector.
 * Note: many of the above algorithms (all except Schur) is described in Numerical Recipes, chapter 11.
 * 
 * @author Evdokimov
 */
public class EigenDecomposition implements Decomposition {

    /**
	 * @see mipt.math.array.impl.Decomposition#decompose(mipt.math.Number[][])
	 */
    public void decompose(Number[][] A) {
    }

    /**
	 * @see mipt.math.array.impl.Decomposition#getEigenvalues(int)
	 */
    public Number[] getEigenvalues(int whatValues) {
        throw new UnsupportedOperationException("EigenDecomposition is not implemented yet");
    }
}
