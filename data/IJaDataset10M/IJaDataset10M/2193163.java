package edu.emory.mathcs.csparsej.tdouble;

import edu.emory.mathcs.csparsej.tdouble.Dcs_common.Dcs;

/**
 * Find elimination tree.
 * 
 * @author Piotr Wendykier (piotr.wendykier@gmail.com)
 * 
 */
public class Dcs_etree {

    /**
     * Compute the elimination tree of A or A'A (without forming A'A).
     * 
     * @param A
     *            column-compressed matrix
     * @param ata
     *            analyze A if false, A'A oterwise
     * @return elimination tree, null on error
     */
    public static int[] cs_etree(Dcs A, boolean ata) {
        int i, k, p, m, n, inext, Ap[], Ai[], w[], parent[], ancestor[], prev[];
        if (!Dcs_util.CS_CSC(A)) return (null);
        m = A.m;
        n = A.n;
        Ap = A.p;
        Ai = A.i;
        parent = new int[n];
        w = new int[n + (ata ? m : 0)];
        ancestor = w;
        prev = w;
        int prev_offset = n;
        if (ata) for (i = 0; i < m; i++) prev[prev_offset + i] = -1;
        for (k = 0; k < n; k++) {
            parent[k] = -1;
            ancestor[k] = -1;
            for (p = Ap[k]; p < Ap[k + 1]; p++) {
                i = ata ? (prev[prev_offset + Ai[p]]) : (Ai[p]);
                for (; i != -1 && i < k; i = inext) {
                    inext = ancestor[i];
                    ancestor[i] = k;
                    if (inext == -1) parent[i] = k;
                }
                if (ata) prev[prev_offset + Ai[p]] = k;
            }
        }
        return parent;
    }
}
