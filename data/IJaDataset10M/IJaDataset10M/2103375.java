package edu.emory.mathcs.csparsej.tdouble;

import edu.emory.mathcs.csparsej.tdouble.Dcs_common.Dcs;
import edu.emory.mathcs.csparsej.tdouble.Dcs_common.Dcsd;

/**
 * Various utilities.
 * 
 * @author Piotr Wendykier (piotr.wendykier@gmail.com)
 * 
 */
public class Dcs_util {

    /**
     * Allocate a sparse matrix (triplet form or compressed-column form).
     * 
     * @param m
     *            number of rows
     * @param n
     *            number of columns
     * @param nzmax
     *            maximum number of entries
     * @param values
     *            allocate pattern only if false, values and pattern otherwise
     * @param triplet
     *            compressed-column if false, triplet form otherwise
     * @return sparse matrix
     */
    public static Dcs cs_spalloc(int m, int n, int nzmax, boolean values, boolean triplet) {
        Dcs A = new Dcs();
        A.m = m;
        A.n = n;
        A.nzmax = nzmax = Math.max(nzmax, 1);
        A.nz = triplet ? 0 : -1;
        A.p = triplet ? new int[nzmax] : new int[n + 1];
        A.i = new int[nzmax];
        A.x = values ? new double[nzmax] : null;
        return A;
    }

    /**
     * Change the max # of entries a sparse matrix can hold.
     * 
     * @param A
     *            column-compressed matrix
     * @param nzmax
     *            new maximum number of entries
     * @return true if successful, false on error
     */
    public static boolean cs_sprealloc(Dcs A, int nzmax) {
        if (A == null) return (false);
        if (nzmax <= 0) nzmax = (Dcs_util.CS_CSC(A)) ? (A.p[A.n]) : A.nz;
        int[] Ainew = new int[nzmax];
        int length = Math.min(nzmax, A.i.length);
        System.arraycopy(A.i, 0, Ainew, 0, length);
        A.i = Ainew;
        if (Dcs_util.CS_TRIPLET(A)) {
            int[] Apnew = new int[nzmax];
            length = Math.min(nzmax, A.p.length);
            System.arraycopy(A.p, 0, Apnew, 0, length);
            A.p = Apnew;
        }
        if (A.x != null) {
            double[] Axnew = new double[nzmax];
            length = Math.min(nzmax, A.x.length);
            System.arraycopy(A.x, 0, Axnew, 0, length);
            A.x = Axnew;
        }
        A.nzmax = nzmax;
        return (true);
    }

    /**
     * Allocate a Dcsd object (a Dulmage-Mendelsohn decomposition).
     * 
     * @param m
     *            number of rows of the matrix A to be analyzed
     * @param n
     *            number of columns of the matrix A to be analyzed
     * @return Dulmage-Mendelsohn decomposition
     */
    public static Dcsd cs_dalloc(int m, int n) {
        Dcsd D;
        D = new Dcsd();
        D.p = new int[m];
        D.r = new int[m + 6];
        D.q = new int[n];
        D.s = new int[n + 6];
        D.cc = new int[5];
        D.rr = new int[5];
        return D;
    }

    protected static int CS_FLIP(int i) {
        return (-(i) - 2);
    }

    protected static int CS_UNFLIP(int i) {
        return (((i) < 0) ? CS_FLIP(i) : (i));
    }

    protected static boolean CS_MARKED(int[] w, int j) {
        return (w[j] < 0);
    }

    protected static void CS_MARK(int[] w, int j) {
        w[j] = CS_FLIP(w[j]);
    }

    /**
     * Returns true if A is in column-compressed form, false otherwise.
     * 
     * @param A
     *            sparse matrix
     * @return true if A is in column-compressed form, false otherwise
     */
    public static boolean CS_CSC(Dcs A) {
        return (A != null && (A.nz == -1));
    }

    /**
     * Returns true if A is in triplet form, false otherwise.
     * 
     * @param A
     *            sparse matrix
     * @return true if A is in triplet form, false otherwise
     */
    public static boolean CS_TRIPLET(Dcs A) {
        return (A != null && (A.nz >= 0));
    }
}
