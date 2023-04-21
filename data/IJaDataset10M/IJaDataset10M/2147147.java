package la4j.decomposition;

import junit.framework.Test;
import junit.framework.TestSuite;

public class QRDecompositorTest extends AbstarctDecompositorTest {

    @Override
    public MatrixDecompositor decompositor() {
        return new QRDecompositor();
    }

    @Override
    public double[][] input() {
        return new double[][] { { 8.0, 0.0, 0.0 }, { 0.0, 4.0, 6.0 }, { 0.0, 0.0, 2.0 } };
    }

    @Override
    public double[][][] output() {
        return new double[][][] { { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } }, { { 8.0, 0.0, 0.0 }, { 0.0, 4.0, 6.0 }, { 0.0, 0.0, 2.0 } } };
    }

    public static Test suite() {
        return new TestSuite(QRDecompositorTest.class);
    }
}
