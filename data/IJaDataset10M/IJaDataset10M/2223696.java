package sparsematmult;

import jgfutil.*;

public class SparseMatmult {

    public static double ytotal = 0.0;

    public static void test(double y[], double val[], int row[], int col[], double x[], int NUM_ITERATIONS) {
        int nz = val.length;
        JGFInstrumentor.startTimer("Section2:SparseMatmult:Kernel");
        for (int reps = 0; reps < NUM_ITERATIONS; reps++) {
            for (int i = 0; i < nz; i++) {
                y[row[i]] += x[col[i]] * val[i];
            }
        }
        JGFInstrumentor.stopTimer("Section2:SparseMatmult:Kernel");
        for (int i = 0; i < nz; i++) {
            ytotal += y[row[i]];
        }
    }
}
