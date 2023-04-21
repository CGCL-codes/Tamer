package org.arpenteur.common.math.matrices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import org.arpenteur.common.math.Complex;

public strictfp class ComplexMatrix {

    public static Complex[][] getMatrixArray(double mat[][]) {
        Complex result[][] = new Complex[mat.length][mat[0].length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                result[i][j] = new Complex(mat[i][j]);
            }
        }
        return result;
    }

    public static double[][] getDoubleArray(Complex mat[][]) {
        double result[][] = new double[mat.length][mat[0].length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                result[i][j] = mat[i][j].real();
            }
        }
        return result;
    }

    public static void solve(final Complex A[][], final Complex Y[], Complex X[]) {
        int n = A.length;
        int m = n + 1;
        Complex B[][] = new Complex[n][m];
        int row[] = new int[n];
        int hold, I_pivot;
        Complex pivot;
        double abs_pivot;
        if (A[0].length != n || Y.length != n || X.length != n) {
            System.out.println("Error in ComplexMatrix.solve," + " inconsistent array sizes.");
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                B[i][j] = A[i][j];
            }
            B[i][n] = Y[i];
        }
        for (int k = 0; k < n; k++) {
            row[k] = k;
        }
        for (int k = 0; k < n; k++) {
            pivot = B[row[k]][k];
            abs_pivot = pivot.abs();
            I_pivot = k;
            for (int i = k + 1; i < n; i++) {
                if (B[row[i]][k].abs() > abs_pivot) {
                    I_pivot = i;
                    pivot = B[row[i]][k];
                    abs_pivot = pivot.abs();
                }
            }
            hold = row[k];
            row[k] = row[I_pivot];
            row[I_pivot] = hold;
            if (abs_pivot < 1.0E-10) {
                for (int j = k + 1; j < n + 1; j++) {
                    B[row[k]][j] = new Complex(0.0, 0.0);
                }
                System.out.println("redundant row (singular) " + row[k]);
            } else {
                for (int j = k + 1; j < n + 1; j++) {
                    B[row[k]][j] = B[row[k]][j].divide(B[row[k]][k]);
                }
                for (int i = 0; i < n; i++) {
                    if (i != k) {
                        for (int j = k + 1; j < n + 1; j++) {
                            B[row[i]][j] = B[row[i]][j].subtract(B[row[i]][k].multiply(B[row[k]][j]));
                        }
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            X[i] = B[row[i]][n];
        }
    }

    public static final boolean invert(Complex A[][]) {
        int n = A.length;
        int row[] = new int[n];
        int col[] = new int[n];
        Complex temp[] = new Complex[n];
        int hold, I_pivot, J_pivot;
        Complex pivot;
        double abs_pivot;
        if (A[0].length != n) {
            System.out.println("Error in Complex.Matrix.invert," + " matrix not square.");
            return false;
        }
        for (int k = 0; k < n; k++) {
            row[k] = k;
            col[k] = k;
        }
        for (int k = 0; k < n; k++) {
            pivot = A[row[k]][col[k]];
            I_pivot = k;
            J_pivot = k;
            for (int i = k; i < n; i++) {
                for (int j = k; j < n; j++) {
                    abs_pivot = pivot.abs();
                    if (A[row[i]][col[j]].abs() > abs_pivot) {
                        I_pivot = i;
                        J_pivot = j;
                        pivot = A[row[i]][col[j]];
                    }
                }
            }
            if (pivot.abs() < 1.0E-10) {
                System.out.println("ComplexMatrix is singular !");
                return false;
            }
            hold = row[k];
            row[k] = row[I_pivot];
            row[I_pivot] = hold;
            hold = col[k];
            col[k] = col[J_pivot];
            col[J_pivot] = hold;
            A[row[k]][col[k]] = (new Complex(1.0, 0.0)).divide(pivot);
            for (int j = 0; j < n; j++) {
                if (j != k) {
                    A[row[k]][col[j]] = A[row[k]][col[j]].multiply(A[row[k]][col[k]]);
                }
            }
            for (int i = 0; i < n; i++) {
                if (k != i) {
                    for (int j = 0; j < n; j++) {
                        if (k != j) {
                            A[row[i]][col[j]] = A[row[i]][col[j]].subtract(A[row[i]][col[k]].multiply(A[row[k]][col[j]]));
                        }
                    }
                    A[row[i]][col[k]] = A[row[i]][col[k]].multiply(A[row[k]][col[k]]).negate();
                }
            }
        }
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                temp[col[i]] = A[row[i]][j];
            }
            for (int i = 0; i < n; i++) {
                A[i][j] = temp[i];
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                temp[row[j]] = A[i][col[j]];
            }
            for (int j = 0; j < n; j++) {
                A[i][j] = temp[j];
            }
        }
        return true;
    }

    public static final Complex determinant(final Complex A[][]) {
        int n = A.length;
        Complex D = new Complex(1.0, 0.0);
        Complex B[][] = new Complex[n][n];
        int row[] = new int[n];
        int hold, I_pivot;
        Complex pivot;
        double abs_pivot;
        if (A[0].length != n) {
            System.out.println("Error in ComplexMatrix.determinant," + " inconsistent array sizes.");
        }
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) B[i][j] = A[i][j];
        for (int k = 0; k < n; k++) {
            row[k] = k;
        }
        for (int k = 0; k < n - 1; k++) {
            pivot = B[row[k]][k];
            abs_pivot = pivot.abs();
            I_pivot = k;
            for (int i = k; i < n; i++) {
                if (B[row[i]][k].abs() > abs_pivot) {
                    I_pivot = i;
                    pivot = B[row[i]][k];
                    abs_pivot = pivot.abs();
                }
            }
            if (I_pivot != k) {
                hold = row[k];
                row[k] = row[I_pivot];
                row[I_pivot] = hold;
                D = D.negate();
            }
            if (abs_pivot < 1.0E-10) {
                return new Complex(0.0, 0.0);
            } else {
                D = D.multiply(pivot);
                for (int j = k + 1; j < n; j++) {
                    B[row[k]][j] = B[row[k]][j].divide(B[row[k]][k]);
                }
                for (int i = 0; i < n; i++) {
                    if (i != k) {
                        for (int j = k + 1; j < n; j++) {
                            B[row[i]][j] = B[row[i]][j].subtract(B[row[i]][k].multiply(B[row[k]][j]));
                        }
                    }
                }
            }
        }
        return D.multiply(B[row[n - 1]][n - 1]);
    }

    public static final void eigenvalues(final Complex A[][], Complex V[][], Complex Y[]) {
        int n = A.length;
        Complex AA[][] = new Complex[n][n];
        double norm;
        Complex c = new Complex(1.0, 0.0);
        Complex s = new Complex(0.0, 0.0);
        if (A[0].length != n || V.length != n || V[0].length != n || Y.length != n) {
            System.out.println("Error in ComplexMatrix.eigenvalues," + " inconsistent array sizes.");
        }
        identity(V);
        copy(A, AA);
        for (int k = 0; k < n; k++) {
            norm = norm4(AA);
            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    schur2(AA, i, j, c, s);
                    mat44(i, j, c, s, AA, V);
                }
            }
        }
        norm = norm4(AA);
        for (int i = 0; i < n; i++) Y[i] = AA[i][i];
    }

    public static final void eigenCheck(final Complex A[][], final Complex V[][], final Complex Y[]) {
        if (A == null || V == null || Y == null) return;
        int n = A.length;
        Complex B[][] = new Complex[n][n];
        Complex C[][] = new Complex[n][n];
        Complex X[] = new Complex[n];
        Complex Z[] = new Complex[n];
        Complex T[] = new Complex[n];
        double norm = 0.0;
        if (A[0].length != n || V.length != n || V[0].length != n || Y.length != n) {
            System.out.println("Error in ComplexMatrix.eigenCheck," + " inconsistent array sizes.");
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                X[j] = V[j][i];
            }
            multiply(A, X, T);
            for (int j = 0; j < n; j++) {
                Z[j] = T[j].subtract(Y[i].multiply(X[j]));
            }
            System.out.println("check for near zero norm of Z[" + i + "]=" + Z[i]);
        }
        norm = norm2(Z);
        System.out.println("norm =" + norm + " is eigen vector error indication 1.");
        System.out.println("det V = " + ComplexMatrix.determinant(V));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Z[j] = V[j][i];
            }
            System.out.println("check for 1.0 = " + norm2(Z));
        }
        for (int i = 0; i < n; i++) {
            identity(B);
            multiply(B, Y[i], C);
            subtract(A, C, B);
            Z[i] = determinant(B);
        }
        norm = norm2(Z);
        System.out.println("norm =" + norm + " is eigen value error indication.");
    }

    static void schur2(final Complex A[][], final int p, final int q, Complex c, Complex s) {
        Complex tau;
        Complex tau_tau_1;
        Complex t;
        if (A[0].length != A.length) {
            System.out.println("Error in schur2 of Complex jacobi," + " inconsistent array sizes.");
        }
        if (A[p][q].abs() != 0.0) {
            tau = (A[q][q].subtract(A[p][p])).divide((A[p][q].multiply(2.0)));
            tau_tau_1 = (tau.multiply(tau).add(1.0)).sqrt();
            if (tau.abs() >= 0.0) t = tau.add(tau_tau_1).invert(); else t = (tau_tau_1.subtract(tau)).invert().negate();
            c = (t.multiply(t)).add(1.0).sqrt().invert();
            s = t.multiply(c);
        } else {
            c = new Complex(1.0, 0.0);
            s = new Complex(0.0, 0.0);
        }
    }

    static void mat22(final Complex c, final Complex s, final Complex A[][], Complex B[][]) {
        if (A.length != 2 || A[0].length != 2 || B.length != 2 || B[0].length != 2) {
            System.out.println("Error in mat22 of Jacobi, not both 2 by 2");
        }
        Complex T[][] = new Complex[2][2];
        T[0][0] = c.multiply(A[0][0]).subtract(s.multiply(A[0][1]));
        T[0][1] = s.multiply(A[0][0]).add(c.multiply(A[0][1]));
        T[1][0] = c.multiply(A[1][0]).subtract(s.multiply(A[1][1]));
        T[1][1] = s.multiply(A[1][0]).add(c.multiply(A[1][1]));
        B[0][0] = c.multiply(T[0][0]).subtract(s.multiply(T[1][0]));
        B[0][1] = c.multiply(T[0][1]).subtract(s.multiply(T[1][1]));
        B[1][0] = s.multiply(T[0][0]).add(c.multiply(T[1][0]));
        B[1][1] = s.multiply(T[0][1]).add(c.multiply(T[1][1]));
    }

    static void mat44(final int p, final int q, final Complex c, final Complex s, final Complex A[][], Complex V[][]) {
        int n = A.length;
        Complex B[][] = new Complex[n][n];
        Complex J[][] = new Complex[n][n];
        if (A[0].length != n || V.length != n || V[0].length != n) {
            System.out.println("Error in mat44 of Complex Jacobi," + " A or V not same and square");
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                J[i][j] = new Complex(0.0, 0.0);
            }
            J[i][i] = new Complex(1.0, 0.0);
        }
        J[p][p] = c;
        J[p][q] = s.negate();
        J[q][q] = c;
        J[q][p] = s;
        multiply(J, A, B);
        J[p][q] = s;
        J[q][p] = s.negate();
        multiply(B, J, A);
        multiply(V, J, B);
        copy(B, V);
    }

    static double norm4(final Complex A[][]) {
        int n = A.length;
        int nr = A[0].length;
        double nrm = 0.0;
        if (n != nr) {
            System.out.println("Error in Complex norm4, non square A[" + n + "][" + nr + "]");
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                nrm = nrm + A[i][j].abs() + A[j][i].abs();
            }
        }
        return nrm / (n * n - n);
    }

    public static final void multiply(final Complex A[][], final Complex B[][], Complex C[][]) {
        int ni = A.length;
        int nk = A[0].length;
        int nj = B[0].length;
        if (B.length != nk || C.length != ni || C[0].length != nj) {
            System.out.println("Error in ComplexMatrix.multiply," + " incompatible sizes");
        }
        for (int i = 0; i < ni; i++) for (int j = 0; j < nj; j++) {
            C[i][j] = new Complex(0.0, 0.0);
            for (int k = 0; k < nk; k++) C[i][j] = C[i][j].add(A[i][k].multiply(B[k][j]));
        }
    }

    /**
   * C=A*B
   * 
   * @param A complex matrix
   * @param B complex value
   * @param C resultant matrix
   */
    public static final void multiply(final Complex A[][], final Complex B, Complex C[][]) {
        int ni = A.length;
        int nj = A[0].length;
        if (C.length != ni || C[0].length != nj) {
            System.out.println("Error in ComplexMatrix.multiply," + " incompatible sizes");
        }
        for (int i = 0; i < ni; i++) for (int j = 0; j < nj; j++) C[i][j] = A[i][j].multiply(B);
    }

    public static final void add(final Complex A[][], final Complex B[][], Complex C[][]) {
        int ni = A.length;
        int nj = A[0].length;
        if (B.length != ni || C.length != ni || B[0].length != nj || C[0].length != nj) {
            System.out.println("Error in ComplexMatrix.add," + " incompatible sizes");
        }
        for (int i = 0; i < ni; i++) for (int j = 0; j < nj; j++) C[i][j] = A[i][j].add(B[i][j]);
    }

    public static final void add(final Complex A[][], final Complex B, Complex C[][]) {
        int ni = A.length;
        int nj = A[0].length;
        if (C.length != ni || C[0].length != nj) {
            System.out.println("Error in ComplexMatrix.add," + " incompatible sizes");
        }
        for (int i = 0; i < ni; i++) for (int j = 0; j < nj; j++) C[i][j] = A[i][j].add(B);
    }

    public static final void subtract(final Complex A[][], final Complex B[][], Complex C[][]) {
        int ni = A.length;
        int nj = A[0].length;
        if (B.length != ni || C.length != ni || B[0].length != nj || C[0].length != nj) {
            System.out.println("Error in ComplexMatrix.subtract," + " incompatible sizes");
        }
        for (int i = 0; i < ni; i++) for (int j = 0; j < nj; j++) C[i][j] = A[i][j].subtract(B[i][j]);
    }

    public static final void subtract(final Complex A[][], final Complex B, Complex C[][]) {
        int ni = A.length;
        int nj = A[0].length;
        if (C.length != ni || C[0].length != nj) {
            System.out.println("Error in ComplexMatrix.subtract," + " incompatible sizes");
        }
        for (int i = 0; i < ni; i++) for (int j = 0; j < nj; j++) C[i][j] = A[i][j].subtract(B);
    }

    public static final double norm1(final Complex A[][]) {
        double norm = 0.0;
        double colSum;
        int ni = A.length;
        int nj = A[0].length;
        for (int j = 0; j < nj; j++) {
            colSum = 0.0;
            for (int i = 0; i < ni; i++) colSum = colSum + A[i][j].abs();
            norm = Math.max(norm, colSum);
        }
        return norm;
    }

    public static final double normInf(final Complex A[][]) {
        double norm = 0.0;
        double rowSum;
        int ni = A.length;
        int nj = A[0].length;
        for (int i = 0; i < ni; i++) {
            rowSum = 0.0;
            for (int j = 0; j < nj; j++) rowSum = rowSum + A[i][j].abs();
            norm = Math.max(norm, rowSum);
        }
        return norm;
    }

    public static final double normFro(final Complex A[][]) {
        double norm = 0.0;
        int n = A.length;
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) norm = norm + A[i][j].abs() * A[i][j].abs();
        return Math.sqrt(norm);
    }

    public static final double norm2(final Complex A[][]) {
        double r = 0.0;
        int n = A.length;
        Complex B[][] = new Complex[n][n];
        Complex V[][] = new Complex[n][n];
        Complex BI[] = new Complex[n];
        if (A[0].length != n) {
            System.out.println("Error in ComplexMatrix.norm2," + " matrix not square.");
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                B[i][j] = new Complex(0.0, 0.0);
                for (int k = 0; k < n; k++) B[i][j] = B[i][j].add(A[k][i].multiply(A[k][j]));
            }
        }
        eigenvalues(B, V, BI);
        for (int i = 0; i < n; i++) r = Math.max(r, BI[i].abs());
        return Math.sqrt(r);
    }

    public static final void copy(final Complex A[][], Complex B[][]) {
        int ni = A.length;
        int nj = A[0].length;
        if (B.length != ni || B[0].length != nj) {
            System.out.println("Error in ComplexMatrix.copy," + " inconsistent sizes.");
        }
        for (int i = 0; i < ni; i++) for (int j = 0; j < nj; j++) B[i][j] = A[i][j];
    }

    public static final boolean equals(final Complex A[][], final Complex B[][]) {
        int ni = A.length;
        int nj = A[0].length;
        boolean same = true;
        if (B.length != ni || B[0].length != nj) {
            System.out.println("Error in ComplexMatrix.equals," + " inconsistent sizes.");
        }
        for (int i = 0; i < ni; i++) for (int j = 0; j < nj; j++) same = same && A[i][j].equals(B[i][j]);
        return same;
    }

    public static final void fromDouble(final double A[][], final double B[][], Complex C[][]) {
        int ni = A.length;
        int nj = A[0].length;
        if (C.length != ni || C[0].length != nj || B.length != ni || B[0].length != nj) {
            System.out.println("Error in ComplexMatrix.fromDouble," + " inconsistent sizes.");
        }
        for (int i = 0; i < ni; i++) for (int j = 0; j < nj; j++) C[i][j] = new Complex(A[i][j], B[i][j]);
    }

    public static final void fromDouble(final double A[][], Complex C[][]) {
        int ni = A.length;
        int nj = A[0].length;
        if (C.length != ni || C[0].length != nj) {
            System.out.println("Error in ComplexMatrix.fromDouble," + " inconsistent sizes.");
        }
        for (int i = 0; i < ni; i++) for (int j = 0; j < nj; j++) C[i][j] = new Complex(A[i][j]);
    }

    public static final void identity(Complex A[][]) {
        int n = A.length;
        if (n != A[0].length) {
            System.out.println("Error in ComplexMatrix.identity," + " inconsistent sizes.");
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) A[i][j] = new Complex(0.0);
            A[i][i] = new Complex(1.0);
        }
    }

    public static final void zero(Complex A[][]) {
        int ni = A.length;
        int nj = A[0].length;
        for (int i = 0; i < ni; i++) for (int j = 0; j < nj; j++) A[i][j] = new Complex(0.0);
    }

    /**
  Output field width.
*/
    protected static int OutputFieldWidth = 12;

    /**
  Number of places to the right of the decimal point.
*/
    protected static int OutputFracPlaces = 3;

    /**
  Output page width
*/
    protected static int PageWidth = 180;

    public static final void print(final Complex A[][]) {
        if (A == null) throw new IllegalArgumentException("Null Argument in Print Method");
        int nr = A.length;
        int nc = A[0].length;
        int w = OutputFieldWidth;
        int d = OutputFracPlaces;
        boolean real = true;
        real: for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                if (A[i][j].imaginary() != 0.) {
                    real = false;
                    break real;
                }
            }
        }
        if (!real) {
            String temp = Integer.toString(nr - 1);
            int rfw = temp.length() + 1;
            int ww = w + d + 10;
            int ncp = (PageWidth - rfw) / ww;
            int jl = 0;
            while (jl < nc) {
                int ju = Math.min(nc, jl + ncp);
                System.out.print("\n");
                String head = "";
                while (head.length() < rfw) head = head + " ";
                System.out.print(head);
                for (int j = jl; j < ju; j++) {
                    head = Integer.toString(j);
                    while (head.length() < ww) head = " " + head;
                    System.out.print(head);
                }
                System.out.print("\n");
                for (int i = 0; i < nr; i++) {
                    String row = Integer.toString(i);
                    while (row.length() < rfw) row = " " + row;
                    System.out.print(row);
                    for (int j = jl; j < ju; j++) {
                        String snum = DoubletoEstring(A[i][j].real(), w, d);
                        if (A[i][j].imaginary() < 0) snum = snum + " - " + DoubletoEstring(-A[i][j].imaginary(), d + 6, d) + "i"; else snum = snum + " + " + DoubletoEstring(A[i][j].imaginary(), d + 6, d) + "i";
                        System.out.print(snum);
                    }
                    System.out.print("\n");
                }
                jl = jl + ncp;
            }
        } else {
            String temp = Integer.toString(nr);
            int rfw = temp.length() + 1;
            int ncp = (PageWidth - rfw) / w;
            int jl = 0;
            while (jl < nc) {
                int ju = Math.min(nc, jl + ncp - 1);
                System.out.print("\n");
                String head = "";
                while (head.length() < rfw) head = head + " ";
                System.out.print(head);
                for (int j = jl; j <= ju; j++) {
                    head = Integer.toString(j);
                    while (head.length() < w) head = " " + head;
                    System.out.print(head);
                }
                System.out.print("\n");
                for (int i = 0; i < nr; i++) {
                    String row = Integer.toString(i);
                    while (row.length() < rfw) row = " " + row;
                    System.out.print(row);
                    for (int j = jl; j <= ju; j++) System.out.print(DoubletoEstring(A[i][j].real(), w, d));
                    System.out.print("\n");
                }
                jl = jl + ncp;
            }
        }
    }

    /**
  Converts a double to w.d e format.
*/
    public static String DoubletoEstring(double num, int w, int d) {
        boolean minusf = false;
        boolean minuse = false;
        String snum;
        if (Double.isNaN(num)) {
            snum = "NaN";
        } else if (Double.isInfinite(num)) {
            snum = "Infty";
        } else if (num == 0.0) {
            snum = "e+00";
            for (int i = 0; i < d; i++) snum = "0" + snum;
            snum = "0." + snum;
        } else {
            if (num < 0) {
                minusf = true;
                num = -num;
            }
            int exp = (int) (Math.log(num) / Math.log(10.0));
            if (num < 1) {
                exp = exp - 1;
            }
            double frac = num / Math.pow(10, exp);
            if (frac > 10. - Math.pow(10., -d)) {
                frac = frac / 10;
                exp = exp + 1;
            }
            java.text.DecimalFormat fmt = new java.text.DecimalFormat();
            fmt.setMaximumFractionDigits(d);
            fmt.setMinimumFractionDigits(d);
            fmt.setGroupingUsed(false);
            String sfrac = fmt.format(frac);
            if (exp < 0) {
                minuse = true;
                exp = -exp;
            }
            String sexp = Integer.toString(exp);
            snum = sexp;
            if (snum.length() < 2) snum = "0" + snum;
            if (minuse) snum = "e-" + snum; else snum = "e+" + snum;
            snum = sfrac + snum;
            if (minusf) snum = "-" + snum;
        }
        while (snum.length() < w) snum = " " + snum;
        return snum;
    }

    public static final void multiply(final Complex A[][], final Complex B[], Complex C[]) {
        int ni = A.length;
        int nj = A[0].length;
        if (B.length != nj || C.length != ni) {
            System.out.println("Error in ComplexMatrix.multiply," + " incompatible sizes.");
        }
        for (int i = 0; i < ni; i++) {
            C[i] = new Complex(0.0, 0.0);
            for (int j = 0; j < nj; j++) {
                C[i] = C[i].add(A[i][j].multiply(B[j]));
            }
        }
    }

    public static final void add(final Complex X[], final Complex Y[], Complex Z[]) {
        int n = X.length;
        if (Y.length != n || Z.length != n) {
            System.out.println("Error in ComplexMatrix.add," + " incompatible sizes.");
        }
        for (int i = 0; i < n; i++) Z[i] = X[i].add(Y[i]);
    }

    public static final void subtract(final Complex X[], final Complex Y[], Complex Z[]) {
        int n = X.length;
        if (Y.length != n || Z.length != n) {
            System.out.println("Error in ComplexMatrix.subtract," + " incompatible sizes.");
        }
        for (int i = 0; i < n; i++) Z[i] = X[i].subtract(Y[i]);
    }

    public static final double norm1(final Complex X[]) {
        double norm = 0.0;
        int n = X.length;
        for (int i = 0; i < n; i++) norm = norm + X[i].abs();
        return norm;
    }

    public static final double norm2(final Complex X[]) {
        double norm = 0.0;
        int n = X.length;
        for (int i = 0; i < n; i++) norm = norm + X[i].abs() * X[i].abs();
        return StrictMath.sqrt(norm);
    }

    public static final double normInf(final Complex X[]) {
        double norm = 0.0;
        int n = X.length;
        for (int i = 0; i < n; i++) norm = Math.max(norm, X[i].abs());
        return norm;
    }

    public static final void copy(final Complex X[], Complex Y[]) {
        int n = X.length;
        if (Y.length != n) {
            System.out.println("Error in ComplexMatrix.copy," + " incompatible sizes");
        }
        for (int i = 0; i < n; i++) Y[i] = X[i];
    }

    public static final boolean equals(final Complex X[], final Complex Y[]) {
        int n = X.length;
        boolean same = true;
        if (Y.length != n) {
            System.out.println("Error in ComplexMatrix.equals," + " incompatible sizes");
        }
        for (int i = 0; i < n; i++) same = same && X[i].equals(Y[i]);
        return same;
    }

    public static final void fromDouble(final double X[], Complex Z[]) {
        int n = X.length;
        if (Z.length != n) {
            System.out.println("Error in ComplexMatrix.fromDouble," + " incompatible sizes");
        }
        for (int i = 0; i < n; i++) Z[i] = new Complex(X[i]);
    }

    public static final void fromDouble(final double X[], final double Y[], Complex Z[]) {
        int n = X.length;
        if (Z.length != n || Y.length != n) {
            System.out.println("Error in ComplexMatrix.fromDouble," + " incompatible sizes");
        }
        for (int i = 0; i < n; i++) Z[i] = new Complex(X[i], Y[i]);
    }

    public static void fromRoots(Complex X[], Complex Y[]) {
        int n = X.length;
        if (Y.length != n + 1) {
            System.out.println("Error in ComplexMatrix.fromRoots," + " incompatible sizes");
        }
        Y[0] = X[0].negate();
        Y[1] = new Complex(1.0);
        if (n == 1) return;
        for (int i = 1; i < n; i++) {
            Y[i + 1] = new Complex(0.0);
            for (int j = 0; j <= i; j++) {
                Y[i + 1 - j] = Y[i - j].subtract(Y[i + 1 - j].multiply(X[i]));
            }
            Y[0] = Y[0].multiply(X[i]).negate();
        }
    }

    public static final void unitVector(Complex X[], int j) {
        int n = X.length;
        for (int i = 0; i < n; i++) X[i] = new Complex(0.0);
        X[j] = new Complex(1.0);
    }

    public static final void zero(Complex X[]) {
        int n = X.length;
        for (int i = 0; i < n; i++) X[i] = new Complex(0.0);
    }

    public static final void print(final Complex X[]) {
        if (X == null) throw new IllegalArgumentException("Null Argument in Print Method");
        int n = X.length;
        int w = OutputFieldWidth;
        int d = OutputFracPlaces;
        int ww = w + d + 10;
        int ncp = (PageWidth) / ww;
        int jl = 0;
        while (jl < n) {
            int ju = Math.min(n, jl + ncp);
            System.out.print("\n  ");
            String head = "";
            for (int j = jl; j < ju; j++) {
                head = Integer.toString(j);
                while (head.length() < ww) head = " " + head;
                System.out.print(head);
            }
            System.out.print("\n  ");
            for (int j = jl; j < ju; j++) {
                System.out.print(ZtoEstring(X[j], w, d));
            }
            System.out.print("\n");
            jl = jl + ncp;
        }
    }

    /**
  Converts a Z to w.d e format.
*/
    public static String ZtoEstring(Complex num, int w, int d) {
        String snum = DoubletoEstring(num.real(), w, d);
        if (num.imaginary() < 0) snum = snum + " - " + DoubletoEstring(-num.imaginary(), d + 6, d) + "i"; else snum = snum + " + " + DoubletoEstring(num.imaginary(), d + 6, d) + "i";
        return snum;
    }

    private static String input_file_name;

    private static BufferedReader in;

    private static String output_file_name;

    private static BufferedWriter out;

    private static PrintWriter file_out;

    public static final void readSize(String file_name, int rowCol[]) {
        String input_line = new String("@");
        int len;
        int index;
        int last;
        String intStr;
        int ni;
        int nj;
        if (input_file_name == null || !file_name.equals(input_file_name)) {
            input_file_name = file_name;
            try {
                in = new BufferedReader(new FileReader(file_name));
            } catch (Exception e) {
                System.out.println("ComplexMatrix.read unable to open file " + file_name);
                return;
            }
        }
        ni = 0;
        nj = 0;
        try {
            input_line = in.readLine();
            while (input_line != null) {
                input_line = input_line.trim();
                len = input_line.length();
                if (len == 0) {
                    input_line = in.readLine();
                    continue;
                }
                if (input_line.charAt(0) == '(') {
                    System.out.println("ComplexMatrix.readSize unable to get size " + file_name);
                    break;
                }
                index = 0;
                last = input_line.indexOf(' ');
                if (last == -1) last = len;
                intStr = input_line.substring(index, last);
                ni = Integer.parseInt(intStr);
                input_line = input_line.substring(last, len);
                input_line = input_line.trim();
                len = input_line.length();
                if (len == 0) {
                    nj = ni;
                    break;
                }
                index = 0;
                last = input_line.indexOf(' ');
                if (last == -1) last = len;
                intStr = input_line.substring(index, last);
                nj = Integer.parseInt(intStr);
                break;
            }
        } catch (Exception e) {
            System.out.println("ComplexMatrix.readSize unable to get size " + file_name);
        }
        rowCol[0] = ni;
        rowCol[1] = nj;
    }

    public static final void read(String file_name, Complex A[][]) {
        String input_line = new String("@");
        int len;
        int index;
        int last;
        String intStr;
        int i, ni;
        int j, nj;
        boolean have_line = false;
        if (input_file_name == null || !file_name.equals(input_file_name)) {
            input_file_name = file_name;
            try {
                in = new BufferedReader(new FileReader(file_name));
            } catch (Exception e) {
                System.out.println("ComplexMatrix.read unable to open file " + file_name);
                return;
            }
        }
        ni = 0;
        nj = 0;
        try {
            input_line = in.readLine();
            while (input_line != null) {
                input_line = input_line.trim();
                len = input_line.length();
                if (len == 0) {
                    input_line = in.readLine();
                    continue;
                }
                if (input_line.charAt(0) == '(') {
                    ni = A.length;
                    nj = A[0].length;
                    have_line = true;
                    break;
                }
                index = 0;
                last = input_line.indexOf(' ');
                if (last == -1) last = len;
                intStr = input_line.substring(index, last);
                ni = Integer.parseInt(intStr);
                input_line = input_line.substring(last, len);
                input_line = input_line.trim();
                len = input_line.length();
                if (len == 0) {
                    nj = ni;
                    break;
                }
                index = 0;
                last = input_line.indexOf(' ');
                if (last == -1) last = len;
                intStr = input_line.substring(index, last);
                nj = Integer.parseInt(intStr);
                break;
            }
        } catch (Exception e) {
            System.out.println("ComplexMatrix.read unable to get size " + file_name);
        }
        i = 0;
        j = 0;
        if (A.length != ni || A[0].length != nj) {
            System.out.println("incompatible size in ComplexMatrix.read");
            return;
        }
        try {
            if (!have_line) input_line = in.readLine();
            have_line = false;
            while (input_line != null) {
                input_line = input_line.trim();
                len = input_line.length();
                if (len == 0) {
                    input_line = in.readLine();
                    continue;
                }
                index = 0;
                last = input_line.indexOf(')');
                if (last == -1) {
                    input_line = in.readLine();
                    continue;
                }
                intStr = input_line.substring(index, last + 1);
                A[i][j] = Complex.parseComplex(intStr);
                j++;
                if (j == nj) {
                    j = 0;
                    i++;
                }
                if (i == ni) break;
                input_line = input_line.substring(last + 1);
            }
        } catch (Exception e) {
            System.out.println("ComplexMatrix.read unable to read data " + file_name);
        }
    }

    public static final void closeInput() {
        try {
            in.close();
        } catch (Exception e) {
            System.out.println("ComplexMatrix.closeInput not closed");
        }
        input_file_name = null;
    }

    public static final void write(String file_name, Complex A[][]) {
        int ni = A.length;
        int nj = A[0].length;
        if (output_file_name == null || !file_name.equals(output_file_name)) {
            output_file_name = file_name;
            try {
                out = new BufferedWriter(new FileWriter(file_name));
                file_out = new PrintWriter(out);
            } catch (Exception e) {
                System.out.println("ComplexMatrix.write unable to open file " + file_name);
                return;
            }
        }
        if (ni == nj) file_out.println(ni); else file_out.println(ni + " " + nj);
        try {
            for (int i = 0; i < ni; i++) for (int j = 0; j < nj; j++) file_out.println(A[i][j].toString());
            file_out.println();
        } catch (Exception e) {
            System.out.println("ComplexMatrix.write unable to write data " + file_name);
        }
    }

    public static final void closeOutput() {
        file_out.close();
        output_file_name = null;
    }

    /**
   * Get a submatrix.
   * 
   * @param Inmat Full matrix
   * @param i0 Initial row index
   * @param i1 Final row index
   * @param j0 Initial column index
   * @param j1 Final column index
   * @return X(i0:i1,j0:j1)
   */
    public static Complex[][] getSubMatrix(Complex[][] Inmat, int i0, int i1, int j0, int j1) {
        Complex[][] X = new Complex[i1 - i0 + 1][j1 - j0 + 1];
        for (int i = i0; i <= i1; i++) {
            for (int j = j0; j <= j1; j++) {
                X[i - i0][j - j0] = Inmat[i][j];
            }
        }
        return X;
    }

    /**
   * Make a d-power of all the elements of the matrix A
   * 
   * @param A input matrix
   * @param d index of pow
   * 
   * @return a matrix with the elements that are the d-power of A elements
   */
    public static Complex[][] powEbE(final Complex A[][], double d) {
        int ni = A.length;
        int nj = A[0].length;
        Complex[][] C = new Complex[ni][nj];
        for (int i = 0; i < ni; i++) for (int j = 0; j < nj; j++) C[i][j] = A[i][j].pow(d);
        return C;
    }

    public static Complex[] sum(Complex A[][]) {
        int nj = A[0].length;
        int ni = A.length;
        Complex[] X = new Complex[nj];
        for (int j = 0; j < nj; j++) {
            Complex s = new Complex(0);
            for (int i = 0; i < ni; i++) {
                s = s.add(A[i][j]);
            }
            X[j] = s;
        }
        return X;
    }

    /**
   * Return a new matrix of double with the column of A with only real value
   * 
   * @param A complex matrix
   * @return double matrix of the only columns of A with the real value in the
   *         first row
   */
    public static double[][] FindNotImage(final Complex A[][]) {
        int ni = A.length;
        int nj = A[0].length;
        double[][] C = new double[ni][nj];
        int i = 0;
        int kj = 0;
        for (int j = 0; j < nj; j++) {
            if (A[i][j].imaginary() == 0) {
                for (int ki = 0; ki < ni; ki++) {
                    C[ki][kj] = A[ki][j].real();
                }
                kj++;
            }
        }
        if (kj == 0) return null;
        double[][] rea = new double[ni][kj];
        for (int ii = 0; ii < ni; ii++) for (int jj = 0; jj < kj; jj++) rea[ii][jj] = C[ii][jj];
        return rea;
    }

    /**
   * Element-by-element right division, C = A./B
   * 
   * @param B another matrix
   */
    public static Complex[][] EbEDivide(Complex a[][], Complex b[][]) {
        int ni = a.length;
        int nj = a[0].length;
        Complex[][] c = new Complex[ni][nj];
        for (int i = 0; i < ni; i++) {
            for (int j = 0; j < nj; j++) {
                c[i][j] = a[i][j].divide(b[i][j]);
            }
        }
        return c;
    }

    /**
   * Element-by-element right mult, C = A.*B
   * 
   * @param B another matrix
   */
    public static Complex[][] EbEmult(Complex a[][], Complex b[][]) {
        int ni = a.length;
        int nj = a[0].length;
        Complex[][] c = new Complex[ni][nj];
        for (int i = 0; i < ni; i++) {
            for (int j = 0; j < nj; j++) {
                c[i][j] = a[i][j].multiply(b[i][j]);
            }
        }
        return c;
    }

    /**
   * Generate a Ones Complex matrix with input rows and columns number
   * 
   * @param row number of rows
   * @param col number of columns
   * 
   * @return a ones complex matrix
   */
    public static Complex[][] ones(int row, int col) {
        Complex[][] c = new Complex[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                c[i][j] = new Complex(1);
            }
        }
        return c;
    }

    /**
   * Make sqrt of all the elements of the matrix A
   * 
   * @param A input matrix
   * 
   * @return a matrix with the elements that are the sqrt of A elements
   */
    public static Complex[][] sqrtEbE(final Complex A[][]) {
        int ni = A.length;
        int nj = A[0].length;
        Complex[][] C = new Complex[ni][nj];
        for (int i = 0; i < ni; i++) for (int j = 0; j < nj; j++) C[i][j] = A[i][j].sqrt();
        return C;
    }

    /**
   * Make sqrt of all the elements of the array A
   * 
   * @param A input vector
   * 
   * @return a matrix with the elements that are the sqrt of A elements
   */
    public static Complex[][] sqrtEbE(final Complex A[]) {
        int ni = A.length;
        Complex[][] C = new Complex[1][ni];
        for (int i = 0; i < ni; i++) C[0][i] = A[i].sqrt();
        return C;
    }

    /**
  * Stack matrices vertically
  * <pre>
  *    .
  *        a
  *    C = -
  *        b
  * </pre>
  * @param a initial matrix
  * @param b matrix to join
  * 
  * @return C
  */
    public static Complex[][] stack(Complex a[][], Complex b[][]) {
        int ni = a.length;
        int nj = a[0].length;
        int nni = ni + b.length;
        Complex[][] c = new Complex[nni][nj];
        for (int i = 0; i < ni; i++) {
            for (int j = 0; j < nj; j++) {
                c[i][j] = a[i][j];
            }
        }
        for (int i = ni; i < nni; i++) {
            for (int j = 0; j < nj; j++) {
                c[i][j] = b[i - ni][j];
            }
        }
        return c;
    }
}
