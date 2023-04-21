package facerecognition.javafaces;

public class EigenvalueDecomposition extends cern.colt.matrix.linalg.EigenvalueDecomposition {

    public EigenvalueDecomposition(Matrix2D dmat) {
        super(dmat);
    }

    public double[] getEigenValues() {
        return diag(getD().toArray());
    }

    public double[][] getEigenVectors() {
        return getV().viewDice().toArray();
    }

    private double[] diag(double[][] m) {
        double[] d = new double[m.length];
        for (int i = 0; i < m.length; i++) d[i] = m[i][i];
        return d;
    }
}
