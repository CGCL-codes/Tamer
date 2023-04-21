package mis2.markov;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.sparse.SparseVector;
import no.uib.cipr.matrix.sparse.BiCGstab;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import no.uib.cipr.matrix.sparse.IterativeSolver;
import no.uib.cipr.matrix.sparse.IterativeSolverNotConvergedException;
import no.uib.cipr.matrix.*;
import no.uib.cipr.matrix.sparse.*;

public class StateProbability {

    private Matrix qMatrix;

    public StateProbability(Matrix qMatrix) {
        this.qMatrix = qMatrix;
    }

    public DenseVector calcPi() {
        int dim = this.qMatrix.numRows();
        for (int j = 0; j < dim; j++) {
            qMatrix.set(qMatrix.numRows() - 1, j, 1);
        }
        DenseVector x, b;
        x = new DenseVector(dim);
        b = new DenseVector(dim);
        b.set(dim - 1, 1);
        IterativeSolver solver = new BiCGstab(b);
        try {
            solver.solve(this.qMatrix, b, x);
        } catch (IterativeSolverNotConvergedException e) {
            System.err.println("Iterative solver failed to converge");
        }
        System.out.println(" FATTO !!!\n");
        return x;
    }

    public void printX(DenseVector x) {
        for (int i = 0; i < x.size(); i++) {
            System.out.print(x.get(i) + " ");
        }
    }
}
