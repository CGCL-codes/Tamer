package org.ejml.ops;

import org.ejml.data.DenseMatrix64F;
import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.assertEquals;

public class TestCovarianceRandomDraw {

    public static int N = 6000;

    /**
     * Do a lot of draws on the distribution and see if a similar distribution is computed
     * in the end.
     */
    @Test
    public void testStatistics() {
        DenseMatrix64F orig_P = new DenseMatrix64F(new double[][] { { 6, -2 }, { -2, 10 } });
        CovarianceRandomDraw dist = new CovarianceRandomDraw(new Random(0xfeed), orig_P);
        DenseMatrix64F draws[] = new DenseMatrix64F[N];
        for (int i = 0; i < N; i++) {
            DenseMatrix64F x = new DenseMatrix64F(2, 1);
            dist.next(x);
            draws[i] = x;
        }
        double raw_comp_x[] = new double[2];
        for (int i = 0; i < N; i++) {
            raw_comp_x[0] += draws[i].get(0, 0);
            raw_comp_x[1] += draws[i].get(1, 0);
        }
        raw_comp_x[0] /= N;
        raw_comp_x[1] /= N;
        assertEquals(0, raw_comp_x[0], 0.1);
        assertEquals(0.0, raw_comp_x[1], 0.1);
        DenseMatrix64F comp_P = new DenseMatrix64F(2, 2);
        DenseMatrix64F temp = new DenseMatrix64F(2, 1);
        for (int i = 0; i < N; i++) {
            temp.set(0, 0, draws[i].get(0, 0) - raw_comp_x[0]);
            temp.set(1, 0, draws[i].get(1, 0) - raw_comp_x[1]);
            CommonOps.multAddTransB(temp, temp, comp_P);
        }
        CommonOps.scale(1.0 / N, comp_P);
        MatrixFeatures.isIdentical(comp_P, orig_P, 0.3);
    }
}
