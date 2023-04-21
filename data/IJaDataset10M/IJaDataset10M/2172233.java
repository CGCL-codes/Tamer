package org.jquantlib.methods.finitedifferences;

import org.jquantlib.math.TransformedGrid;
import org.jquantlib.math.matrixutilities.Array;

/**
 * @author Srinivas Hasti
 * 
 */
public abstract class PdeSecondOrderParabolic implements Pde {

    public void generateOperator(double t, TransformedGrid tg, TridiagonalOperator L) {
        for (int i = 1; i < tg.size() - 1; i++) {
            double sigma = diffusion(t, tg.grid(i));
            double nu = drift(t, tg.grid(i));
            double r = discount(t, tg.grid(i));
            double sigma2 = sigma * sigma;
            double pd = -(sigma2 / tg.dxm(i) - nu) / tg.dx(i);
            double pu = -(sigma2 / tg.dxp(i) + nu) / tg.dx(i);
            double pm = sigma2 / (tg.dxm(i) * tg.dxp(i)) + r;
            L.setMidRow(i, pd, pm, pu);
        }
    }

    public TransformedGrid applyGridType(Array a) {
        return null;
    }
}
