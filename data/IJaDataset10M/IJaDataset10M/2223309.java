package maths.matrix.complex;

import maths.matrix.real.MathRealMatrix;
import maths.numerical.complex.Complex;

/**
 * @author Lars
 *
 */
public class MathComplexMatrix {

    private Complex[][] Data;

    private int M;

    private int N;

    private boolean Quadratic;

    public MathComplexMatrix(int m, int n) {
        this.setData(new Complex[m][n]);
        this.setM(m);
        this.setN(n);
    }

    public MathComplexMatrix(double[][] data) {
        int m = data.length;
        this.setM(m);
        int n = data[0].length;
        this.setN(n);
        this.Data = new Complex[m][n];
        for (int ni = 0; ni < n; ni++) {
            for (int mi = 0; mi < m; mi++) {
                this.Data[mi][ni] = new Complex(data[mi][ni], 0);
            }
        }
    }

    public MathComplexMatrix(MathRealMatrix matrix) {
        double[][] data = matrix.getAllItems();
        int m = data.length;
        this.M = m;
        int n = data[0].length;
        this.N = n;
        this.Data = new Complex[m][n];
        for (int ni = 0; ni < n; ni++) {
            for (int mi = 0; mi < m; mi++) {
                this.Data[mi][ni] = new Complex(data[mi][ni], 0);
            }
        }
    }

    public MathComplexMatrix(Complex[][] data) {
        this.Data = data;
        this.M = Data.length;
        this.N = Data[0].length;
    }

    public Complex[][] getAllItems() {
        return this.Data;
    }

    /**
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
    public Complex getItem(int m, int n) {
        return this.Data[m][n];
    }

    /**
	 * 
	 * @param m
	 * @return
	 */
    public Complex[] getRowVector(int m) {
        Complex[] rowVector = new Complex[this.N];
        for (int n = 0; n < this.N; n++) {
            rowVector[n] = this.Data[m][n];
        }
        return rowVector;
    }

    /**
	 * 
	 * @param n
	 * @return
	 */
    public Complex[] getLineVector(int n) {
        Complex[] lineVector = new Complex[this.M];
        for (int m = 0; m < this.M; m++) {
            lineVector[m] = this.Data[m][n];
        }
        return lineVector;
    }

    /**
	 * @return the m
	 */
    public int getM() {
        return M;
    }

    /**
	 * @return the n
	 */
    public int getN() {
        return N;
    }

    public static MathComplexMatrix add(MathComplexMatrix matrixA, MathComplexMatrix matrixB) throws IllegalArgumentException {
        if (matrixA.getM() != matrixB.getM() && matrixA.getN() != matrixB.getN()) {
            throw new IllegalArgumentException("Matrices must have equal dimensions, for adding");
        } else {
            int m = matrixA.getM();
            int n = matrixA.getN();
            Complex[][] dataA = matrixA.getAllItems();
            Complex[][] dataB = matrixB.getAllItems();
            Complex[][] newData = new Complex[m][n];
            for (int ni = 0; ni < n; ni++) {
                for (int mi = 0; mi < m; mi++) {
                    newData[mi][ni] = Complex.add(dataA[mi][ni], dataB[mi][ni]);
                }
            }
            return new MathComplexMatrix(newData);
        }
    }

    public static MathComplexMatrix ScalarMultiplication(MathComplexMatrix matrix, double scalar) {
        int m = matrix.getM();
        int n = matrix.getN();
        Complex[][] data = matrix.getAllItems();
        Complex[][] newData = new Complex[m][n];
        for (int ni = 0; ni < n; ni++) {
            for (int mi = 0; mi < m; mi++) {
                newData[mi][ni] = Complex.mul(data[mi][ni], new Complex(scalar, 0));
            }
        }
        return new MathComplexMatrix(newData);
    }

    public static MathComplexMatrix MatrixMultiplication(MathComplexMatrix matrixA, MathComplexMatrix matrixB) throws IllegalArgumentException {
        if (matrixA.getN() != matrixB.getM()) {
            throw new IllegalArgumentException("MatrixB must have the same count of rows as the count of coloumns of MatrixA");
        } else {
            int am = matrixA.getM();
            int bn = matrixB.getN();
            int nm = am;
            int nn = bn;
            Complex[][] newData = new Complex[nm][nn];
            for (int nni = 0; nni < nn; nni++) {
                for (int nmi = 0; nmi < nm; nmi++) {
                    newData[nmi][nni] = MultiplyVectors(matrixA.getRowVector(nmi), matrixB.getLineVector(nni));
                }
            }
            return new MathComplexMatrix(newData);
        }
    }

    public MathComplexMatrix Transpone() {
        return MathComplexMatrix.Transpone(this);
    }

    public static MathComplexMatrix Transpone(MathComplexMatrix matrix) {
        int m = matrix.getM();
        int n = matrix.getN();
        Complex[][] mData = matrix.getAllItems();
        Complex[][] newData = new Complex[n][m];
        for (int ni = 0; ni < n; ni++) {
            for (int mi = 0; mi < m; mi++) {
                newData[ni][mi] = mData[mi][ni];
            }
        }
        return new MathComplexMatrix(newData);
    }

    /**
	 * @param rowVector
	 * @param coloumnVector
	 * @return
	 */
    private static Complex MultiplyVectors(Complex[] rowVector, Complex[] coloumnVector) {
        Complex result = new Complex(0, 0);
        for (int i = 0; i < rowVector.length; i++) {
            result = result.add(Complex.mul(rowVector[i], coloumnVector[i]));
        }
        return result;
    }

    /**
	 * @param m the m to set
	 */
    private void setM(int m) {
        this.M = m;
    }

    /**
	 * @param n the n to set
	 */
    private void setN(int n) {
        this.N = n;
    }

    /**
	 * @param data the data to set
	 */
    private void setData(Complex[][] data) {
        this.Data = data;
    }

    /**
	 * @return the quadratic
	 */
    public static boolean isQuadratic(MathComplexMatrix matrix) {
        return matrix.Quadratic;
    }

    public static boolean isSymmetric(MathComplexMatrix matrix) throws IllegalArgumentException {
        if (!MathComplexMatrix.isQuadratic(matrix)) {
            throw new IllegalArgumentException("Matrix must be quadratic!");
        } else {
            return true;
        }
    }

    public static boolean isSkewSymmetric(MathComplexMatrix matrix) throws IllegalArgumentException {
        if (!MathComplexMatrix.isQuadratic(matrix)) {
            throw new IllegalArgumentException("Matrix must be quadratic");
        } else {
            return true;
        }
    }
}
