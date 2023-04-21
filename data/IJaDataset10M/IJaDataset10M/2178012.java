package coda.sim;

import coda.DataFrame;
import coda.Utils;
import coda.ext.jama.LUDecomposition;
import coda.ext.jama.Matrix;
import coda.gui.CoDaPackMain;
import coda.gui.menu.Biplot3dMenu;
import coda.gui.output.OutputTableTwoEntries;
import coda.gui.utils.DataSelector;
import java.util.Arrays;
import java.util.Random;
import javax.swing.DefaultListModel;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.linear.NotPositiveDefiniteMatrixException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.direct.MultiDirectional;

/**
 *
 * @author mcomas
 */
public class SIMLogNormalILR1 {

    static boolean withGraphics = false;

    static int SIZE = 66;

    static double LOGCONTRAST[][] = { { 2.5, 1, -3.5, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 2.5, 1, -3.5, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 2.5, 1, -3.5, 0, 0 } };

    static double EPS[] = { 0.0005 };

    static double CLR_E[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    static double W[] = { 0, 1, 1, 0, 1, 1, 1, 1, 1, 0 };

    static double CLR_VAR[] = { 0.020376762, 0.001495004, 0.022132686 };

    static Matrix F = Utils.getMatrixF(CLR_VAR.length);

    public static void main(String args[]) throws NotPositiveDefiniteMatrixException, FunctionEvaluationException, OptimizationException {
        CoDaPackMain main = new CoDaPackMain();
        int n = CLR_E.length;
        int m = LOGCONTRAST.length;
        String[] names = new String[n];
        String[] cnames = new String[n];
        for (int i = 0; i < n; i++) names[i] = cnames[i] = "c" + (i + 1);
        double a[][] = new double[m][n];
        for (short i = 0; i < m; i++) {
            System.arraycopy(LOGCONTRAST[i], 0, a[i], 0, n);
        }
        double clrPlane[][] = new double[1][n];
        Arrays.fill(clrPlane[0], 1);
        double b[] = new double[m];
        for (int i = 0; i < m; i++) {
            b[i] = 0;
            for (int j = 0; j < n; j++) b[i] += CLR_E[j] * a[i][j];
        }
        double Aclr[][] = new double[m + 1][];
        for (int i = 0; i < m; i++) Aclr[i] = a[i];
        Aclr[m] = clrPlane[0];
        double baseF[][] = getKernelBase(Aclr, n);
        double baseG[][] = LOGCONTRAST;
        double base[][] = new double[n - 1][];
        int dimF = baseF.length;
        int dimG = baseG.length;
        System.arraycopy(baseF, 0, base, 0, dimF);
        System.arraycopy(baseG, 0, base, dimF, dimG);
        String[] base_name = new String[n - 1];
        for (int i = 0; i < dimF; i++) base_name[i] = "f" + Integer.toString(i + 1);
        for (int i = 0; i < dimG; i++) base_name[dimF + i] = "g" + Integer.toString(i + 1);
        Matrix A = new Matrix(a);
        Matrix B = new Matrix(base).transpose();
        CoDaPackMain.outputPanel.addOutput(new OutputTableTwoEntries("Base B", base_name, null, B.getArray()));
        Matrix invB = B.inverse();
        CoDaPackMain.outputPanel.addOutput(new OutputTableTwoEntries("Matrix to coordinates respect B", null, null, invB.getArray()));
        Matrix ilrA = A.times(B);
        CoDaPackMain.outputPanel.addOutput(new OutputTableTwoEntries("Logcontrast in ILR", null, null, ilrA.getArray()));
        double var[] = new double[n];
        String labels[] = { "In", "Out" };
        for (int i = 0; i < n; i++) var[i] = CLR_VAR[i];
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair solution = optimizer.optimize(new F(B, invB, dimF), GoalType.MINIMIZE, var);
        double bestVAR[] = solution.getPoint();
        Matrix COV = getCovariance(B, invB, bestVAR, dimF);
        Matrix COVexp = B.times(COV).times(B.transpose());
        CoDaPackMain.outputPanel.addOutput(new OutputTableTwoEntries("Expected Covariance", null, null, COVexp.getArrayCopy()));
        Random generator = new Random();
        CoDaRandom random = new CoDaRandom();
        double[][] data1 = new double[n][SIZE];
        double[] vec;
        for (int i = 0; i < SIZE; i++) {
            vec = random.normalRandomVariable(COV.getArray());
            for (int c = 0; c < n; c++) {
                double vv = CLR_E[c];
                for (int k = 0; k < n - 1; k++) vv += vec[k] * base[k][c];
                data1[c][i] = Math.exp(vv);
            }
        }
        DataFrame df = new DataFrame();
        df.addData(names, data1);
        main.addDataFrame(df);
        main.setVisible(true);
        if (withGraphics) {
            Biplot3dMenu biplot = new Biplot3dMenu(main);
            DataSelector dataSel = biplot.getDataSelector();
            dataSel.setSelectedData((DefaultListModel) dataSel.getAvailableData());
            biplot.acceptButtonActionPerformed();
        }
    }

    public static double function(Matrix B, Matrix invB, double var[], int dimF) {
        Matrix COV = getCovariance(B, invB, var, dimF);
        Matrix COVexp = B.times(COV).times(B.transpose());
        double res = 0;
        for (int i = 0; i < var.length; i++) {
            double dist = (Math.log(CLR_VAR[i] / COVexp.get(i, i)));
            res += W[i] * dist * dist;
        }
        return res;
    }

    public static double[] getCovarianceDiagonal(Matrix B, Matrix invB, double var[], int dimF) {
        Matrix COV = getCovariance(B, invB, var, dimF);
        Matrix COVexp = B.times(COV).times(B.transpose());
        double res[] = new double[var.length];
        for (int i = 0; i < var.length; i++) res[i] = COVexp.get(i, i);
        return res;
    }

    public static Matrix getCovariance(Matrix B, Matrix invB, double var[], int dimF) {
        int n = var.length;
        Matrix CLR_SIGMA = F.times(Utils.getDiagonalMatrix(var)).times(F);
        Matrix C = invB.times(CLR_SIGMA).times(invB.transpose());
        double covF[][] = C.getMatrix(0, dimF - 1, 0, dimF - 1).getArray();
        double covG[][] = C.getMatrix(dimF, n - 2, dimF, n - 2).getArray();
        Matrix C11 = C.getMatrix(0, dimF - 1, 0, dimF - 1);
        Matrix C12 = C.getMatrix(0, dimF - 1, dimF, n - 2);
        Matrix C22 = C.getMatrix(dimF, n - 2, dimF, n - 2);
        Matrix C21 = C.getMatrix(dimF, n - 2, 0, dimF - 1);
        Matrix COV = new Matrix(n - 1, n - 1);
        COV.times(0);
        COV.setMatrix(0, dimF - 1, 0, dimF - 1, C11.minus(C12.times(C22.inverse().times(C21))));
        for (int i = 0; i < n - dimF - 1; i++) COV.set(dimF + i, dimF + i, EPS[i]);
        return COV;
    }

    public static double[][] unitaryVector(double a[][]) {
        double vec[][] = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            double norm = 0;
            for (int j = 0; j < a[0].length; j++) {
                norm += a[i][j] * a[i][j];
            }
            norm = Math.sqrt(norm);
            for (int j = 0; j < a[0].length; j++) {
                vec[i][j] = a[i][j] / norm;
            }
        }
        return vec;
    }

    public static double[][] getKernelBase(double a[][], int n) {
        if (a == null || a.length == 0) {
            double res[][] = new double[n][n];
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) res[i][j] = i == j ? 1 : 0;
            return res;
        }
        Matrix A = new Matrix(a);
        int r = A.rank();
        int d = n - r;
        double base[][] = new double[d][n];
        LUDecomposition ludecomp = new LUDecomposition(A.transpose());
        double l[][] = A.transpose().lu().getL().transpose().getArray();
        for (int i = r - 1; i >= 0; i--) {
            for (int k = 0; k < i; k++) {
                double piv = l[k][i];
                for (int j = 0; j < n; j++) {
                    l[k][j] = l[k][j] - piv * l[i][j];
                }
            }
        }
        for (int k = 0; k < d; k++) {
            for (int i = 0; i < r; i++) {
                base[k][i] = l[i][r + k];
            }
            for (int i = 0; i < d; i++) {
                base[k][r + i] = (i == k ? -1 : 0);
            }
        }
        int[] pivot = ludecomp.getPivot();
        double base_result[][] = new double[d][n];
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < n; j++) {
                base_result[i][pivot[j]] = base[i][j];
            }
        }
        return base_result;
    }

    public static class F implements MultivariateRealFunction {

        Matrix B;

        Matrix invB;

        int dimF;

        public F(Matrix B, Matrix invB, int dimF) {
            this.B = B;
            this.invB = invB;
            this.dimF = dimF;
        }

        public double value(double[] arg0) throws FunctionEvaluationException, IllegalArgumentException {
            boolean negative = false;
            for (int i = 0; i < arg0.length; i++) negative = arg0[i] < 0 ? true : negative;
            if (negative) return 100000;
            Matrix COV = getCovariance(B, invB, arg0, dimF);
            Matrix COVexp = B.times(COV).times(B.transpose());
            double res = 0;
            for (int i = 0; i < arg0.length; i++) {
                double dist = (Math.log(CLR_VAR[i] / COVexp.get(i, i)));
                res += W[i] * dist * dist;
            }
            return res;
        }
    }
}
