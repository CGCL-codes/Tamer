package edu.emory.mathcs.csparsej.tfloat;

import edu.emory.mathcs.csparsej.tfloat.Scs_common.Scs;
import edu.emory.mathcs.csparsej.tfloat.Scs_common.Scss;

/**
 * Symbolic QR or LU ordering and analysis.
 * 
 * @author Piotr Wendykier (piotr.wendykier@gmail.com)
 * 
 */
public class Scs_sqr {

    private static boolean cs_vcount(Scs A, Scss S) {
        int i, k, p, pa, n = A.n, m = A.m, Ap[] = A.p, Ai[] = A.i, next[], head[], tail[], nque[], pinv[], leftmost[], w[], parent[] = S.parent;
        S.pinv = pinv = new int[m + n];
        S.leftmost = leftmost = new int[m];
        w = new int[m + 3 * n];
        next = w;
        head = w;
        int head_offset = m;
        tail = w;
        int tail_offset = m + n;
        nque = w;
        int nque_offset = m + 2 * n;
        for (k = 0; k < n; k++) head[head_offset + k] = -1;
        for (k = 0; k < n; k++) tail[tail_offset + k] = -1;
        for (k = 0; k < n; k++) nque[nque_offset + k] = 0;
        for (i = 0; i < m; i++) leftmost[i] = -1;
        for (k = n - 1; k >= 0; k--) {
            for (p = Ap[k]; p < Ap[k + 1]; p++) {
                leftmost[Ai[p]] = k;
            }
        }
        for (i = m - 1; i >= 0; i--) {
            pinv[i] = -1;
            k = leftmost[i];
            if (k == -1) continue;
            if (nque[nque_offset + k]++ == 0) tail[tail_offset + k] = i;
            next[i] = head[head_offset + k];
            head[head_offset + k] = i;
        }
        S.lnz = 0;
        S.m2 = m;
        for (k = 0; k < n; k++) {
            i = head[head_offset + k];
            S.lnz++;
            if (i < 0) i = S.m2++;
            pinv[i] = k;
            if (--nque[nque_offset + k] <= 0) continue;
            S.lnz += nque[nque_offset + k];
            if ((pa = parent[k]) != -1) {
                if (nque[nque_offset + pa] == 0) tail[tail_offset + pa] = tail[tail_offset + k];
                next[tail[tail_offset + k]] = head[head_offset + pa];
                head[head_offset + pa] = next[i];
                nque[nque_offset + pa] += nque[nque_offset + k];
            }
        }
        for (i = 0; i < m; i++) if (pinv[i] < 0) pinv[i] = k++;
        w = null;
        return (true);
    }

    /**
     * Symbolic QR or LU ordering and analysis.
     * 
     * @param order
     *            ordering method to use (0 to 3)
     * @param A
     *            column-compressed matrix
     * @param qr
     *            analyze for QR if true or LU if false
     * @return symbolic analysis for QR or LU, null on error
     */
    public static Scss cs_sqr(int order, Scs A, boolean qr) {
        int n, k, post[];
        Scss S;
        boolean ok = true;
        if (!Scs_util.CS_CSC(A)) return (null);
        n = A.n;
        S = new Scss();
        S.q = Scs_amd.cs_amd(order, A);
        if (order > 0 && S.q == null) return (null);
        if (qr) {
            Scs C = order > 0 ? Scs_permute.cs_permute(A, null, S.q, false) : A;
            S.parent = Scs_etree.cs_etree(C, true);
            post = Scs_post.cs_post(S.parent, n);
            S.cp = Scs_counts.cs_counts(C, S.parent, post, true);
            ok = C != null && S.parent != null && S.cp != null && cs_vcount(C, S);
            if (ok) for (S.unz = 0, k = 0; k < n; k++) S.unz += S.cp[k];
            ok = ok && S.lnz >= 0 && S.unz >= 0;
        } else {
            S.unz = 4 * (A.p[n]) + n;
            S.lnz = S.unz;
        }
        return (ok ? S : null);
    }
}
