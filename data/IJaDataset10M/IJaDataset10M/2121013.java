package org.ejml.alg.dense.decomposition.chol;

import org.ejml.alg.dense.decomposition.CholeskyDecomposition;
import org.ejml.alg.dense.decomposition.DecompositionFactory;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.EjmlUnitTests;
import org.ejml.ops.MatrixFeatures;
import org.ejml.ops.RandomMatrices;
import org.ejml.simple.SimpleMatrix;
import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public abstract class GenericCholeskyTests {

    Random rand = new Random(0x45478);

    boolean canL = true;

    boolean canR = true;

    public abstract CholeskyDecomposition<DenseMatrix64F> create(boolean lower);

    @Test
    public void testDecomposeL() {
        if (!canL) return;
        DenseMatrix64F A = new DenseMatrix64F(3, 3, true, 1, 2, 4, 2, 13, 23, 4, 23, 90);
        DenseMatrix64F L = new DenseMatrix64F(3, 3, true, 1, 0, 0, 2, 3, 0, 4, 5, 7);
        CholeskyDecomposition<DenseMatrix64F> cholesky = create(true);
        assertTrue(cholesky.decompose(A));
        DenseMatrix64F foundL = cholesky.getT(null);
        EjmlUnitTests.assertEquals(L, foundL, 1e-8);
    }

    @Test
    public void testDecomposeR() {
        if (!canR) return;
        DenseMatrix64F A = new DenseMatrix64F(3, 3, true, 1, 2, 4, 2, 13, 23, 4, 23, 90);
        DenseMatrix64F R = new DenseMatrix64F(3, 3, true, 1, 2, 4, 0, 3, 5, 0, 0, 7);
        CholeskyDecomposition<DenseMatrix64F> cholesky = create(false);
        assertTrue(cholesky.decompose(A));
        DenseMatrix64F foundR = cholesky.getT(null);
        EjmlUnitTests.assertEquals(R, foundR, 1e-8);
    }

    /**
     * If it is not positive definate it should fail
     */
    @Test
    public void testNotPositiveDefinite() {
        DenseMatrix64F A = new DenseMatrix64F(2, 2, true, 1, -1, -1, -2);
        CholeskyDecomposition<DenseMatrix64F> alg = create(true);
        assertFalse(alg.decompose(A));
    }

    /**
     * The correctness of getT(null) has been tested else where effectively.  This
     * checks to see if it handles the case where an input is provided correctly.
     */
    @Test
    public void getT() {
        DenseMatrix64F A = new DenseMatrix64F(3, 3, true, 1, 2, 4, 2, 13, 23, 4, 23, 90);
        CholeskyDecomposition<DenseMatrix64F> cholesky = create(true);
        assertTrue(cholesky.decompose(A));
        DenseMatrix64F L_null = cholesky.getT(null);
        DenseMatrix64F L_provided = RandomMatrices.createRandom(3, 3, rand);
        assertTrue(L_provided == cholesky.getT(L_provided));
        assertTrue(MatrixFeatures.isEquals(L_null, L_provided));
    }

    /**
     * Test across several different matrix sizes and upper/lower decompositions using
     * the definition of cholesky.
     */
    @Test
    public void checkWithDefinition() {
        for (int i = 0; i < 2; i++) {
            boolean lower = i == 0;
            if (lower && !canL) continue;
            if (!lower && !canR) continue;
            for (int size = 1; size < 10; size++) {
                checkWithDefinition(lower, size);
            }
            checkWithDefinition(lower, 5);
        }
    }

    private void checkWithDefinition(boolean lower, int size) {
        SimpleMatrix A = SimpleMatrix.wrap(RandomMatrices.createSymmPosDef(size, rand));
        CholeskyDecomposition<DenseMatrix64F> cholesky = create(lower);
        assertTrue(DecompositionFactory.decomposeSafe(cholesky, A.getMatrix()));
        SimpleMatrix T = SimpleMatrix.wrap(cholesky.getT(null));
        SimpleMatrix found;
        if (lower) {
            found = T.mult(T.transpose());
        } else {
            found = T.transpose().mult(T);
        }
        assertTrue(A.isIdentical(found, 1e-8));
    }
}
