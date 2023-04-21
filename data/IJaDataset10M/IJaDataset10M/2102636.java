package edu.emory.mathcs.jplasma.test;

import edu.emory.mathcs.jplasma.tdouble.Dplasma;

/**
 * Test of plasma_DPOSV
 * 
 * @author Piotr Wendykier (piotr.wendykier@gmail.com)
 * 
 */
public class DposvTest {

    public static void main(String[] args) {
        int n = 10;
        for (int i = 0; i < n; i++) {
            dposvTest(args);
        }
    }

    public static void dposvTest(String[] args) {
        if (args.length != 4) {
            System.out.print(" Proper Usage is : java edu.emory.mathcs.jplasma.test.DposvTest N LDA NRHS LDB with \n - N : the size of the matrix \n - LDA : leading dimension of the matrix A \n - NRHS : number of RHS \n - LDB : leading dimension of the RHS B \n");
            System.exit(1);
        }
        int N = Integer.parseInt(args[0]);
        int LDA = Integer.parseInt(args[1]);
        int NRHS = Integer.parseInt(args[2]);
        int LDB = Integer.parseInt(args[3]);
        double eps;
        int uplo;
        int info_solution, info_factorization;
        int i, j;
        double[] A1 = new double[LDA * N];
        double[] A2 = new double[LDA * N];
        double[] B1 = new double[LDB * NRHS];
        double[] B2 = new double[LDB * NRHS];
        Dplasma.plasma_Init(N, N, NRHS);
        for (i = 0; i < N; i++) for (j = 0; j < N; j++) A1[LDA * j + i] = A2[LDA * j + i] = 0.5 - Math.random();
        for (i = 0; i < N; i++) {
            A1[LDA * i + i] = A1[LDA * i + i] + N;
            A2[LDA * i + i] = A1[LDA * i + i];
        }
        for (i = 0; i < N; i++) for (j = 0; j < N; j++) A2[LDA * j + i] = A1[LDA * j + i] = A1[LDA * i + j];
        for (i = 0; i < N; i++) for (j = 0; j < NRHS; j++) B1[LDB * j + i] = B2[LDB * j + i] = 0.5 - Math.random();
        Dplasma.plasma_DPOSV(Dplasma.PlasmaUpper, N, NRHS, A2, 0, LDA, B2, 0, LDB);
        Dplasma.plasma_Finalize();
        uplo = Dplasma.PlasmaUpper;
        eps = 1e-10;
        System.out.print("\n");
        System.out.print("------ TESTS FOR PLASMA DPOSV ROUTINE -------  \n");
        System.out.print(String.format("            Size of the Matrix %d by %d\n", N, N));
        System.out.print("\n");
        System.out.print(" The matrix A is randomly generated for each test.\n");
        System.out.print("============\n");
        System.out.print(String.format(" The relative machine precision (eps) is to be %e \n", eps));
        System.out.print(" Computational tests pass if scaled residuals are less than 10.\n");
        info_factorization = check_factorization(N, A1, A2, LDA, uplo, eps);
        info_solution = check_solution(N, NRHS, A1, LDA, B1, B2, LDB, eps);
        if ((info_solution == 0) & (info_factorization == 0)) {
            System.out.print("************************************************\n");
            System.out.print(" ---- TESTING DPOSV ... PASSED !\n");
            System.out.print("************************************************\n");
        } else {
            System.out.print("************************************************\n");
            System.out.print(" ---- TESTING DPOSV ... FAILED !\n");
            System.out.print("************************************************\n");
        }
        for (i = 0; i < N; i++) for (j = 0; j < N; j++) A1[LDA * j + i] = A2[LDA * j + i] = 0.5 - Math.random();
        for (i = 0; i < N; i++) {
            A1[LDA * i + i] = A1[LDA * i + i] + N;
            A2[LDA * i + i] = A1[LDA * i + i];
        }
        for (i = 0; i < N; i++) for (j = 0; j < N; j++) A2[LDA * j + i] = A1[LDA * j + i] = A1[LDA * i + j];
        for (i = 0; i < N; i++) for (j = 0; j < NRHS; j++) B1[LDB * j + i] = B2[LDB * j + i] = 0.5 - Math.random();
        Dplasma.plasma_Init(N, N, NRHS);
        Dplasma.plasma_DPOTRF(Dplasma.PlasmaUpper, N, A2, 0, LDA);
        Dplasma.plasma_Finalize();
        Dplasma.plasma_Init(N, N, NRHS);
        Dplasma.plasma_DPOTRS(Dplasma.PlasmaUpper, N, NRHS, A2, 0, LDA, B2, 0, LDB);
        Dplasma.plasma_Finalize();
        uplo = Dplasma.PlasmaUpper;
        System.out.print("\n");
        System.out.print("------ TESTS FOR PLASMA DPOTRF + DPOTRS ROUTINE -------  \n");
        System.out.print(String.format("            Size of the Matrix %d by %d\n", N, N));
        System.out.print("\n");
        System.out.print(" The matrix A is randomly generated for each test.\n");
        System.out.print("============\n");
        System.out.print(String.format(" The relative machine precision (eps) is to be %e \n", eps));
        System.out.print(" Computational tests pass if scaled residuals are less than 10.\n");
        info_factorization = check_factorization(N, A1, A2, LDA, uplo, eps);
        info_solution = check_solution(N, NRHS, A1, LDA, B1, B2, LDB, eps);
        if ((info_solution == 0) & (info_factorization == 0)) {
            System.out.print("************************************************\n");
            System.out.print(" ---- TESTING DPOTRF + DPOTRS ... PASSED !\n");
            System.out.print("************************************************\n");
        } else {
            System.out.print("************************************************\n");
            System.out.print(" ---- TESTING DPOTRF + DPOTRS ... FAILED !\n");
            System.out.print("************************************************\n");
        }
        for (i = 0; i < N; i++) for (j = 0; j < N; j++) A1[LDA * j + i] = A2[LDA * j + i] = 0.5 - Math.random();
        for (i = 0; i < N; i++) {
            A1[LDA * i + i] = A1[LDA * i + i] + N;
            A2[LDA * i + i] = A1[LDA * i + i];
        }
        for (i = 0; i < N; i++) for (j = 0; j < N; j++) A2[LDA * j + i] = A1[LDA * j + i] = A1[LDA * i + j];
        for (i = 0; i < N; i++) for (j = 0; j < NRHS; j++) B1[LDB * j + i] = B2[LDB * j + i] = 0.5 - Math.random();
        Dplasma.plasma_Init(N, N, NRHS);
        Dplasma.plasma_DPOTRF(Dplasma.PlasmaLower, N, A2, 0, LDA);
        Dplasma.plasma_Finalize();
        Dplasma.plasma_Init(N, N, NRHS);
        Dplasma.plasma_DTRSM(Dplasma.PlasmaLeft, Dplasma.PlasmaLower, Dplasma.PlasmaNoTrans, Dplasma.PlasmaNonUnit, N, NRHS, A2, 0, LDA, B2, 0, LDB);
        Dplasma.plasma_Finalize();
        Dplasma.plasma_Init(N, N, NRHS);
        Dplasma.plasma_DTRSM(Dplasma.PlasmaLeft, Dplasma.PlasmaLower, Dplasma.PlasmaTrans, Dplasma.PlasmaNonUnit, N, NRHS, A2, 0, LDA, B2, 0, LDB);
        Dplasma.plasma_Finalize();
        uplo = Dplasma.PlasmaLower;
        System.out.print("\n");
        System.out.print("------ TESTS FOR PLASMA DPOTRF + DTRSM + DTRSM  ROUTINE -------  \n");
        System.out.print(String.format("            Size of the Matrix %d by %d\n", N, N));
        System.out.print("\n");
        System.out.print(" The matrix A is randomly generated for each test.\n");
        System.out.print("============\n");
        System.out.print(String.format(" The relative machine precision (eps) is to be %e \n", eps));
        System.out.print(" Computational tests pass if scaled residuals are less than 10.\n");
        info_factorization = check_factorization(N, A1, A2, LDA, uplo, eps);
        info_solution = check_solution(N, NRHS, A1, LDA, B1, B2, LDB, eps);
        if ((info_solution == 0) & (info_factorization == 0)) {
            System.out.print("************************************************\n");
            System.out.print(" ---- TESTING DPOTRF + DTRSM + DTRSM ... PASSED !\n");
            System.out.print("************************************************\n");
        } else {
            System.out.print("************************************************\n");
            System.out.print(" ---- TESTING DPOTRF + DTRSM + DTRSM ... FAILED !\n");
            System.out.print("************************************************\n");
        }
    }

    private static int check_factorization(int N, double[] A1, double[] A2, int LDA, int uplo, double eps) {
        double Anorm, Rnorm;
        double alpha;
        String norm = "I";
        int info_factorization;
        int i, j;
        double[] Residual = new double[N * N];
        double[] L1 = new double[N * N];
        double[] L2 = new double[N * N];
        double[] work = new double[N];
        alpha = 1.0;
        org.netlib.lapack.Dlacpy.dlacpy("ALL", N, N, A1, 0, LDA, Residual, 0, N);
        if (uplo == Dplasma.PlasmaUpper) {
            org.netlib.lapack.Dlacpy.dlacpy(Dplasma.lapack_const(Dplasma.PlasmaUpper), N, N, A2, 0, LDA, L1, 0, N);
            org.netlib.lapack.Dlacpy.dlacpy(Dplasma.lapack_const(Dplasma.PlasmaUpper), N, N, A2, 0, LDA, L2, 0, N);
            org.netlib.blas.Dtrmm.dtrmm("L", "U", "T", "N", N, N, alpha, L1, 0, N, L2, 0, N);
        } else {
            org.netlib.lapack.Dlacpy.dlacpy(Dplasma.lapack_const(Dplasma.PlasmaLower), N, N, A2, 0, LDA, L1, 0, N);
            org.netlib.lapack.Dlacpy.dlacpy(Dplasma.lapack_const(Dplasma.PlasmaLower), N, N, A2, 0, LDA, L2, 0, N);
            org.netlib.blas.Dtrmm.dtrmm("R", "L", "T", "N", N, N, alpha, L1, 0, N, L2, 0, N);
        }
        for (i = 0; i < N; i++) for (j = 0; j < N; j++) Residual[j * N + i] = L2[j * N + i] - Residual[j * N + i];
        Rnorm = org.netlib.lapack.Dlange.dlange(norm, N, N, Residual, 0, N, work, 0);
        Anorm = org.netlib.lapack.Dlange.dlange(norm, N, N, A1, 0, LDA, work, 0);
        System.out.print("============\n");
        System.out.print("Checking the Cholesky Factorization \n");
        System.out.print(String.format("-- ||L'L-A||_oo/(||A||_oo.N.eps) = %e \n", Rnorm / (Anorm * N * eps)));
        if (Rnorm / (Anorm * N * eps) > 10.0) {
            System.out.print("-- Factorization is suspicious ! \n");
            info_factorization = 1;
        } else {
            System.out.print("-- Factorization is CORRECT ! \n");
            info_factorization = 0;
        }
        return info_factorization;
    }

    private static int check_solution(int N, int NRHS, double[] A1, int LDA, double[] B1, double[] B2, int LDB, double eps) {
        int info_solution;
        double Rnorm, Anorm, Xnorm, Bnorm;
        String norm = "I";
        double alpha, beta;
        double[] work = new double[N];
        alpha = 1.0;
        beta = -1.0;
        Xnorm = org.netlib.lapack.Dlange.dlange(norm, N, NRHS, B2, 0, LDB, work, 0);
        Anorm = org.netlib.lapack.Dlange.dlange(norm, N, N, A1, 0, LDA, work, 0);
        Bnorm = org.netlib.lapack.Dlange.dlange(norm, N, NRHS, B1, 0, LDB, work, 0);
        org.netlib.blas.Dgemm.dgemm("N", "N", N, NRHS, N, alpha, A1, 0, LDA, B2, 0, LDB, beta, B1, 0, LDB);
        Rnorm = org.netlib.lapack.Dlange.dlange(norm, N, NRHS, B1, 0, LDB, work, 0);
        System.out.print("============\n");
        System.out.print("Checking the Residual of the solution \n");
        System.out.print(String.format("-- ||Ax-B||_oo/((||A||_oo||x||_oo+||B||_oo).N.eps) = %e \n", Rnorm / ((Anorm * Xnorm + Bnorm) * N * eps)));
        if (Rnorm / ((Anorm * Xnorm + Bnorm) * N * eps) > 10.0) {
            System.out.print("-- The solution is suspicious ! \n");
            info_solution = 1;
        } else {
            System.out.print("-- The solution is CORRECT ! \n");
            info_solution = 0;
        }
        return info_solution;
    }
}
