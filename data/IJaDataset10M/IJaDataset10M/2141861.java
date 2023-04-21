package snap.likelihood;

import snap.matrix.Array2d;

public class ExpQT {

    static native void expQTtx(int N, double u, double v, double coalescenceRate, double t, double[] x);

    static native void expM(double[] x, int n);

    static void expM(Array2d A, Array2d F) {
        int n = A.getNrOfRows();
        double[] x = new double[n * n];
        System.arraycopy(A.asZeroBasedArray(), 0, x, 0, n * n);
        expM(x, n);
        F.set(x, n);
    }
}
