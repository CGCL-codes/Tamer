package ncorps.fourier;

import ncorps.commun.ConditionInitiale;

public class PotentielGrille {

    private ConditionInitiale _CI;

    private double[][] R;

    private double[][] TFR;

    private double[][] density;

    private double[][] TFdensity;

    private double[][] potentiel;

    PotentielGrille(ConditionInitiale CI) {
        this._CI = CI;
        r();
        TFR = Dfft.dfft2d(R);
        density(CI);
        TFdensity = Dfft.dfft2d(density);
        double[][] prod = product(TFdensity, TFR);
        potentiel = Dfft.idfft2d(prod);
    }

    private void density(ConditionInitiale CI) {
        double[][] gcoord = CI.getGcoord();
        density = new double[_CI.get_XDimG()][_CI.get_YDimG()];
        int xn, yn;
        for (int n = 0; n < _CI.get_N(); n++) {
            xn = (int) gcoord[n][1];
            yn = (int) gcoord[n][2];
            if (inbounds(xn, yn)) {
                density[xn][yn] += 1;
            }
        }
    }

    private boolean inbounds(int xn, int yn) {
        return ((xn >= 0) && (yn >= 0) && (xn < _CI.get_XDimG()) && (yn < _CI.get_YDimG()));
    }

    public double[][] product(double[][] g1, double[][] g2) {
        double[][] prod = new double[_CI.get_XDimG()][_CI.get_YDimG()];
        for (int i = 0; i < _CI.get_XDimG(); i++) {
            for (int j = 0; j < _CI.get_YDimG(); j++) {
                prod[i][j] = (g1[i][j]) * (g2[i][j]);
            }
        }
        return prod;
    }

    private void r() {
        R = new double[_CI.get_XDimG()][_CI.get_YDimG()];
        int X0 = 0;
        int Y0 = 0;
        for (int i = 0; i < _CI.get_XDimG(); i++) {
            for (int j = 0; j < _CI.get_YDimG(); j++) {
                if (i == X0 & j == Y0) {
                } else {
                    R[i][j] = 1 / (Math.sqrt((i - X0) * (i - X0) + (j - Y0) * (j - Y0)));
                }
            }
        }
    }

    public double[][] getPotentiel() {
        return potentiel;
    }
}
