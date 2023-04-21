package edu.emory.mathcs.csparsej.tdouble;

import edu.emory.mathcs.csparsej.tdouble.Dcs_common.Dcs;
import edu.emory.mathcs.csparsej.tdouble.Dcs_common.Dcsd;

/**
 * Dulmage-Mendelsohn decomposition.
 * 
 * @author Piotr Wendykier (piotr.wendykier@gmail.com)
 * 
 */
public class Dcs_dmperm {

    private static boolean cs_bfs(Dcs A, int n, int[] wi, int[] wj, int[] queue, int[] imatch, int imatch_offset, int[] jmatch, int jmatch_offset, int mark) {
        int Ap[], Ai[], head = 0, tail = 0, j, i, p, j2;
        Dcs C;
        for (j = 0; j < n; j++) {
            if (imatch[imatch_offset + j] >= 0) continue;
            wj[j] = 0;
            queue[tail++] = j;
        }
        if (tail == 0) return (true);
        C = (mark == 1) ? A : Dcs_transpose.cs_transpose(A, false);
        if (C == null) return (false);
        Ap = C.p;
        Ai = C.i;
        while (head < tail) {
            j = queue[head++];
            for (p = Ap[j]; p < Ap[j + 1]; p++) {
                i = Ai[p];
                if (wi[i] >= 0) continue;
                wi[i] = mark;
                j2 = jmatch[jmatch_offset + i];
                if (wj[j2] >= 0) continue;
                wj[j2] = mark;
                queue[tail++] = j2;
            }
        }
        if (mark != 1) C = null;
        return (true);
    }

    private static void cs_matched(int n, int[] wj, int[] imatch, int imatch_offset, int[] p, int[] q, int[] cc, int[] rr, int set, int mark) {
        int kc = cc[set], j;
        int kr = rr[set - 1];
        for (j = 0; j < n; j++) {
            if (wj[j] != mark) continue;
            p[kr++] = imatch[imatch_offset + j];
            q[kc++] = j;
        }
        cc[set + 1] = kc;
        rr[set] = kr;
    }

    private static void cs_unmatched(int m, int[] wi, int[] p, int[] rr, int set) {
        int i, kr = rr[set];
        for (i = 0; i < m; i++) if (wi[i] == 0) p[kr++] = i;
        rr[set + 1] = kr;
    }

    private static class Cs_rprune implements Dcs_ifkeep {

        @Override
        public boolean fkeep(int i, int j, double aij, Object other) {
            int[] rr = (int[]) other;
            return (i >= rr[1] && i < rr[2]);
        }
    }

    /**
     * Compute coarse and then fine Dulmage-Mendelsohn decompositionm. seed
     * optionally selects a randomized algorithm.
     * 
     * @param A
     *            column-compressed matrix
     * @param seed
     *            0: natural, -1: reverse, random order oterwise
     * @return Dulmage-Mendelsohn analysis, null on error
     */
    public static Dcsd cs_dmperm(Dcs A, int seed) {
        int m, n, i, j, k, cnz, nc, jmatch[], imatch[], wi[], wj[], pinv[], Cp[], Ci[], ps[], rs[], nb1, nb2, p[], q[], cc[], rr[], r[], s[];
        boolean ok;
        Dcs C;
        Dcsd D, scc;
        if (!Dcs_util.CS_CSC(A)) return (null);
        m = A.m;
        n = A.n;
        D = Dcs_util.cs_dalloc(m, n);
        if (D == null) return (null);
        p = D.p;
        q = D.q;
        r = D.r;
        s = D.s;
        cc = D.cc;
        rr = D.rr;
        jmatch = Dcs_maxtrans.cs_maxtrans(A, seed);
        imatch = jmatch;
        int imatch_offset = m;
        if (jmatch == null) return (null);
        wi = r;
        wj = s;
        for (j = 0; j < n; j++) wj[j] = -1;
        for (i = 0; i < m; i++) wi[i] = -1;
        cs_bfs(A, n, wi, wj, q, imatch, imatch_offset, jmatch, 0, 1);
        ok = cs_bfs(A, m, wj, wi, p, jmatch, 0, imatch, imatch_offset, 3);
        if (!ok) return (null);
        cs_unmatched(n, wj, q, cc, 0);
        cs_matched(n, wj, imatch, imatch_offset, p, q, cc, rr, 1, 1);
        cs_matched(n, wj, imatch, imatch_offset, p, q, cc, rr, 2, -1);
        cs_matched(n, wj, imatch, imatch_offset, p, q, cc, rr, 3, 3);
        cs_unmatched(m, wi, p, rr, 3);
        jmatch = null;
        pinv = Dcs_pinv.cs_pinv(p, m);
        if (pinv == null) return (null);
        C = Dcs_permute.cs_permute(A, pinv, q, false);
        pinv = null;
        if (C == null) return (null);
        Cp = C.p;
        nc = cc[3] - cc[2];
        if (cc[2] > 0) for (j = cc[2]; j <= cc[3]; j++) Cp[j - cc[2]] = Cp[j];
        C.n = nc;
        if (rr[2] - rr[1] < m) {
            Dcs_fkeep.cs_fkeep(C, new Cs_rprune(), rr);
            cnz = Cp[nc];
            Ci = C.i;
            if (rr[1] > 0) for (k = 0; k < cnz; k++) Ci[k] -= rr[1];
        }
        C.m = nc;
        scc = Dcs_scc.cs_scc(C);
        if (scc == null) return (null);
        ps = scc.p;
        rs = scc.r;
        nb1 = scc.nb;
        for (k = 0; k < nc; k++) wj[k] = q[ps[k] + cc[2]];
        for (k = 0; k < nc; k++) q[k + cc[2]] = wj[k];
        for (k = 0; k < nc; k++) wi[k] = p[ps[k] + rr[1]];
        for (k = 0; k < nc; k++) p[k + rr[1]] = wi[k];
        nb2 = 0;
        r[0] = s[0] = 0;
        if (cc[2] > 0) nb2++;
        for (k = 0; k < nb1; k++) {
            r[nb2] = rs[k] + rr[1];
            s[nb2] = rs[k] + cc[2];
            nb2++;
        }
        if (rr[2] < m) {
            r[nb2] = rr[2];
            s[nb2] = cc[3];
            nb2++;
        }
        r[nb2] = m;
        s[nb2] = n;
        D.nb = nb2;
        return D;
    }
}
