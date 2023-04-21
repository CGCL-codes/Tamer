package edu.emory.mathcs.csparsej.tfloat;

import edu.emory.mathcs.csparsej.tfloat.Scs_common.Scs;

/**
 * Remove (and sum) duplicates.
 * 
 * @author Piotr Wendykier (piotr.wendykier@gmail.com)
 * 
 */
public class Scs_dupl {

    /**
     * Removes and sums duplicate entries in a sparse matrix.
     * 
     * @param A
     *            column-compressed matrix
     * @return true if successful, false on error
     */
    public static boolean cs_dupl(Scs A) {
        int i, j, p, q, nz = 0, n, m, Ap[], Ai[], w[];
        float Ax[];
        if (!Scs_util.CS_CSC(A)) return (false);
        m = A.m;
        n = A.n;
        Ap = A.p;
        Ai = A.i;
        Ax = A.x;
        w = new int[m];
        for (i = 0; i < m; i++) w[i] = -1;
        for (j = 0; j < n; j++) {
            q = nz;
            for (p = Ap[j]; p < Ap[j + 1]; p++) {
                i = Ai[p];
                if (w[i] >= q) {
                    Ax[w[i]] += Ax[p];
                } else {
                    w[i] = nz;
                    Ai[nz] = i;
                    Ax[nz++] = Ax[p];
                }
            }
            Ap[j] = q;
        }
        Ap[n] = nz;
        return Scs_util.cs_sprealloc(A, 0);
    }
}
