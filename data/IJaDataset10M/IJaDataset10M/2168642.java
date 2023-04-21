package la4j.linear;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JacobiSolverTest extends AbstractSolverTest {

    @Override
    public LinearSystemSolver solver() {
        return new JacobiSolver();
    }

    @Override
    public double[][] coefficientMatrix() {
        return new double[][] { { 5.0, 0.0, 0.0 }, { 1.0, 7.0, 0.0 }, { 4.0, 0.0, 9.0 } };
    }

    @Override
    public double[] rightHandVector() {
        return new double[] { 0.0, 2.0, 0.0 };
    }

    public static Test suite() {
        return new TestSuite(JacobiSolverTest.class);
    }
}
