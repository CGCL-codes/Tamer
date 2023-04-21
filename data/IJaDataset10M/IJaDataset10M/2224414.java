package jat.alg.solver;

import jat.math.*;
import jat.matvec.data.*;

public class GaussSeidel {

    Matrix A;

    VectorN x, x0, xold, b;

    int n, maxIter;

    double tol;

    public GaussSeidel(Matrix A, VectorN b, VectorN x0, int maxIter, double tol) {
        this.A = A;
        this.x0 = x0;
        this.b = b;
        this.maxIter = maxIter;
        this.tol = tol;
        this.n = x0.length;
    }

    /**
	 * @return solution vector
	 */
    public VectorN iterate() {
        int k = 0;
        double err = 1.;
        x = new VectorN(x0);
        xold = new VectorN(x0);
        while (k < maxIter && tol <= err) {
            k++;
            int i, j;
            for (i = 0; i < n; i++) {
                double sum = 0;
                for (j = 0; j < n; j++) {
                    if (j != i) sum += A.A[i][j] * x.x[j];
                }
                x.x[i] = (-sum + b.x[i]) / A.A[i][i];
            }
            x.print("x");
            err = MathUtils.abs(x.mag() - xold.mag());
            System.out.println("Error: " + err);
            System.out.println(" " + x.mag());
            System.out.println(" " + xold.mag());
            xold = x.copy();
        }
        return x;
    }
}
