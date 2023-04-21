package org.ejml.alg.dense.decomposition.hessenberg;

import org.ejml.data.DenseMatrix64F;

/**
 * @author Peter Abeles
 */
public class TestTridiagonalDecompositionBlock extends StandardTridiagonalTests {

    @Override
    protected TridiagonalSimilarDecomposition<DenseMatrix64F> createDecomposition() {
        return new TridiagonalDecompositionBlock(3);
    }
}
