package edu.ucsd.ncmir.spl.numerical;

import java.util.Arrays;

/**
 *
 * @author spl
 */
public class LaGrangianInterpolator extends Interpolator {

    public LaGrangianInterpolator(double[][] xy) {
        super(xy);
    }

    public LaGrangianInterpolator(double[] x, double[] y) {
        super(x, y);
    }

    private int _degree = 3;

    public void setDegree(int degree) {
        this._degree = degree;
    }

    public double evaluate(double xval) {
        double result;
        if ((1 < this._degree) && (this._degree < this._x.length)) {
            int where = Arrays.binarySearch(this._x, xval);
            if (where < 0) {
                where = -where - 2;
                int i0 = (where - (this._degree / 2)) + 1;
                int i1 = where + (this._degree / 2) + (this._degree % 2);
                if (i0 < 0) {
                    i1 -= i0;
                    i0 = 0;
                } else if (i1 >= this._x.length) {
                    i0 -= (this._x.length - 1) - i0;
                    i1 = this._x.length - 1;
                }
                result = 0.0;
                double product;
                for (int i = i0; i <= i1; i++) {
                    product = 1.0;
                    for (int j = i0; j <= i1; j++) if (j != i) product *= (xval - this._x[j]) / (this._x[i] - this._x[j]);
                    result += product * this._y[i];
                }
            } else result = this._y[where];
        } else throw new IllegalArgumentException("Polynomial degree invalid");
        return result;
    }
}
