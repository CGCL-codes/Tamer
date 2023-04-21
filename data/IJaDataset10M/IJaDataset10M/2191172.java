package shu.math;

import Jama.*;
import Jama.Matrix;
import shu.math.array.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: </p>
 * �_���Ȥ���,�i��^�k�Y�Ʊ��ɩһ�.
 *
 * <p>Copyright: Copyright (c) 2001</p>
 *
 * <p>Company: </p>
 *
 * @author cms.shu.edu.tw
 * @version 1.0
 */
public class SVDLib {

    public static final boolean CLIP_MODE = false;

    public SVDLib() {
    }

    public SVDLib(double[][] input) {
        svd(input);
    }

    public static void main(String[] args) {
        int count = 10;
        double[][] RGB = new double[count][3];
        double[][] XYZ = new double[count][3];
        for (int x = 0; x < count; x++) {
            for (int y = 0; y < 3; y++) {
                RGB[x][y] = x + 100 + y;
                XYZ[x][y] = RGB[x][y] * 10;
            }
        }
        SVDLib svd = new SVDLib(RGB);
        double[][] U = svd.getU();
        double[][] V = DoubleArray.transpose(svd.getV());
        double[][] s = DoubleArray.diagonal(svd.getSingularValues());
        double[][] result = DoubleArray.times(DoubleArray.times(U, s), V);
        System.out.println(DoubleArray.toString(result));
    }

    protected double[] sv;

    protected double[][] v, u;

    public double[] getSingularValues() {
        return sv;
    }

    public double[][] getU() {
        return u;
    }

    public double[][] getV() {
        return v;
    }

    public void svd(double[][] input) {
        Matrix matrix = new Matrix(input);
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        u = svd.getU().getArray();
        sv = svd.getSingularValues();
        v = svd.getV().getArray();
        if (CLIP_MODE) {
            double wmax = 0.0, wmin;
            for (int i = 0; i < input.length; i++) {
                if (sv[i] > wmax) {
                    wmax = sv[i];
                }
            }
            wmin = wmax * (1.0e-6);
            for (int i = 1; i <= input[0].length; i++) {
                if (sv[i] < wmin) {
                    sv[i] = 0.0;
                }
            }
        }
    }

    /**
   * input���ȸg�Lsvd���ѫ�,�A�Noutput��,�H�˥N�k�P�_���ȩM�_���V�q�i��p��,�N�i�H��Xinput�Moutput�������p��Y��
   * @param output double[]
   * @return double[]
   */
    public double[] getCoefficients(double[] output) {
        double[] coef = svbksb(u, sv, v, output);
        return coef;
    }

    public static double[] getPredict(double[][] input, double[] coefs) {
        double[] predict = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                predict[i] += input[i][j] * coefs[j];
            }
        }
        return predict;
    }

    /**
   * SVBkSb �_���Ȧ^�N��k�]back substitution�^
   * The svbksb method performs back substitution on a SV decomposed matrix to
   *  find out the solution vector for a linear set of equations.
   *
   * @param u double[][]
   * @param sv double[]
   * @param v double[][]
   * @param output double[]
   * @return double[]
   */
    protected static double[] svbksb(final double[][] u, final double[] sv, final double[][] v, final double output[]) {
        double s;
        int m = u.length, n = u[0].length;
        double[] coef = new double[n];
        double[] tmp = new double[n];
        for (int i = 0; i < n; i++) {
            s = 0.0;
            if (sv[i] != 0) {
                for (int j = 0; j < m; j++) {
                    s += u[j][i] * output[j];
                }
                s /= sv[i];
            }
            tmp[i] = s;
        }
        for (int i = 0; i < n; i++) {
            s = 0.0;
            for (int j = 0; j < n; j++) {
                s += v[i][j] * tmp[j];
            }
            coef[i] = s;
        }
        return coef;
    }
}
