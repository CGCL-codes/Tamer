package org.ejml.alg.dense.linsol.chol;

import org.junit.Test;

/**
 * @author Peter Abeles
 */
public class TestLinearSolverCholBlock64 {

    @Test
    public void standardTests() {
        LinearSolverCholBlock64 solver = new LinearSolverCholBlock64();
        BaseCholeskySolveTests tests = new BaseCholeskySolveTests();
        tests.standardTests(solver);
    }
}
