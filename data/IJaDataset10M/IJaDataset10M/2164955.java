package org.ejml.alg.dense.linsol;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.MatrixFeatures;
import org.ejml.ops.RandomMatrices;
import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestLinearSolverFactory {

    Random rand = new Random(234);

    @Test
    public void general() {
        DenseMatrix64F A = RandomMatrices.createRandom(5, 4, rand);
        DenseMatrix64F x = RandomMatrices.createRandom(4, 1, rand);
        DenseMatrix64F y = new DenseMatrix64F(5, 1);
        LinearSolver<DenseMatrix64F> solver = LinearSolverFactory.general(A.numRows, A.numCols);
        standardTest(A, x, y, solver);
    }

    @Test
    public void linear() {
        DenseMatrix64F A = RandomMatrices.createRandom(4, 4, rand);
        DenseMatrix64F x = RandomMatrices.createRandom(4, 1, rand);
        DenseMatrix64F y = new DenseMatrix64F(4, 1);
        LinearSolver<DenseMatrix64F> solver = LinearSolverFactory.linear(A.numRows);
        standardTest(A, x, y, solver);
    }

    @Test
    public void leastSquares() {
        DenseMatrix64F A = RandomMatrices.createRandom(5, 4, rand);
        DenseMatrix64F x = RandomMatrices.createRandom(4, 1, rand);
        DenseMatrix64F y = new DenseMatrix64F(5, 1);
        LinearSolver<DenseMatrix64F> solver = LinearSolverFactory.leastSquares(A.numRows, A.numCols);
        standardTest(A, x, y, solver);
    }

    @Test
    public void symmetric() {
        DenseMatrix64F A = RandomMatrices.createSymmPosDef(5, rand);
        DenseMatrix64F x = RandomMatrices.createRandom(5, 1, rand);
        DenseMatrix64F y = new DenseMatrix64F(5, 1);
        LinearSolver<DenseMatrix64F> solver = LinearSolverFactory.symmPosDef(A.numCols);
        standardTest(A, x, y, solver);
    }

    @Test
    public void adjustable() {
        DenseMatrix64F A = RandomMatrices.createRandom(5, 4, rand);
        DenseMatrix64F x = RandomMatrices.createRandom(4, 1, rand);
        DenseMatrix64F y = new DenseMatrix64F(5, 1);
        AdjustableLinearSolver solver = LinearSolverFactory.adjustable();
        standardTest(A, x, y, solver);
        solver.removeRowFromA(y.numRows - 1);
        y.numRows--;
        DenseMatrix64F x_adj = new DenseMatrix64F(4, 1);
        solver.solve(y, x_adj);
        assertTrue(MatrixFeatures.isIdentical(x, x_adj, 1e-8));
    }

    /**
     * Given A and x it computes the value of y.  This is then compared against what the solver computes
     * x should be.
     */
    private void standardTest(DenseMatrix64F a, DenseMatrix64F x, DenseMatrix64F y, LinearSolver<DenseMatrix64F> solver) {
        solver = new LinearSolverSafe<DenseMatrix64F>(solver);
        CommonOps.mult(a, x, y);
        DenseMatrix64F x_found = new DenseMatrix64F(x.numRows, 1);
        assertTrue(solver.setA(a));
        solver.solve(y, x_found);
        assertTrue(MatrixFeatures.isIdentical(x, x_found, 1e-8));
    }
}
