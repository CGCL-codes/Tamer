package net.maizegenetics.matrixalgebra.decomposition;

import net.maizegenetics.matrixalgebra.Matrix.DoubleMatrix;
import net.maizegenetics.matrixalgebra.Matrix.EJMLDoubleMatrix;
import org.ejml.alg.dense.decomposition.EigenDecomposition;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.EigenOps;

public class EJMLEigenvalueDecomposition implements EigenvalueDecomposition {

    final EigenDecomposition myDecomposition;

    boolean successful;

    public EJMLEigenvalueDecomposition(DoubleMatrix dm) {
        myDecomposition = EigenOps.decompositionSymmetric(true);
        successful = myDecomposition.decompose(((EJMLDoubleMatrix) dm).myMatrix);
    }

    public EJMLEigenvalueDecomposition(DenseMatrix64F matrix) {
        myDecomposition = EigenOps.decompositionSymmetric(true);
        successful = myDecomposition.decompose(matrix);
    }

    public EJMLEigenvalueDecomposition(EigenDecomposition decomp) {
        myDecomposition = decomp;
    }

    @Override
    public double getEigenvalue(int i) {
        return myDecomposition.getEigenvalue(i).getReal();
    }

    @Override
    public DoubleMatrix getEigenvalueMatrix() {
        return new EJMLDoubleMatrix(EigenOps.createMatrixD(myDecomposition));
    }

    @Override
    public double[] getEigenvalues() {
        int n = myDecomposition.getNumberOfEigenvalues();
        double[] values = new double[n];
        for (int i = 0; i < n; i++) {
            values[i] = myDecomposition.getEigenvalue(i).getReal();
        }
        return values;
    }

    @Override
    public DoubleMatrix getEigenvectors() {
        return new EJMLDoubleMatrix(EigenOps.createMatrixV(myDecomposition));
    }

    public boolean wasSuccessful() {
        return successful;
    }
}
