package de.helwich.linalg;

import static de.helwich.linalg.MatrixUtil.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Hendrik Helwich
 */
public class MatrixUtilTest {

    private double[][] A;

    private double[][] B;

    private double[][] D;

    private double[][] U;

    private double[][] SPD;

    private double[][] Id5;

    @Before
    public void setUp() throws Exception {
        A = new double[][] { { 1, 3.7, 5, 7, 0.1, 0.3, -2 }, { 9, 11.2, 13, 17, 111, -22, 1.2 }, { 7.7, 1.2, -1, 7, 0, 0.2, 3 }, { -0.5, 22, 65, -3, -3, 18, 3.1 }, { 19, 23.3, -0.002, 2, 12, 31, 55 } };
        B = new double[][] { { 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1 } };
        SPD = new double[][] { { 1, 1, 1, 1, 1 }, { 1, 2, 3, 4, 5 }, { 1, 3, 6, 10, 15 }, { 1, 4, 10, 20, 35 }, { 1, 5, 15, 35, 70 } };
        D = new double[][] { { 0.2, 0, 0, 0, 0 }, { 0, 0.1, 0, 0, 0 }, { 0, 0, 0.2857, 0, 0 }, { 0, 0, 0, 2.5, 0 }, { 0, 0, 0, 0, 70 } };
        U = new double[][] { { 1, 2, 1, 0.2, 0.0143 }, { 0, 1, 1.5, 0.6, 0.0714 }, { 0, 0, 1, 1, 0.2143 }, { 0, 0, 0, 1, 0.5 }, { 0, 0, 0, 0, 1 } };
        Id5 = new double[][] { { 1, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 1 } };
    }

    @Test
    public void testCopyNew() {
        double[][] C = copy(null, A, 5, 7);
        assertMatrixEquals(A, C, 5, 7);
    }

    @Test
    public void testCopyExisting() {
        double[][] C = copy(B, A, 5, 7);
        assertTrue(C == B);
        assertMatrixEquals(A, B, 5, 7);
    }

    @Test
    public void testFill() {
        fill(A, 5, 7, 1);
        assertMatrixEquals(A, B, 5, 7);
    }

    @Test
    public void testIsEqualExact() {
        assertTrue(isEqual(A, A, 5, 7));
        assertFalse(isEqual(A, B, 5, 7));
    }

    @Test
    public void testIsEqualNotExact() {
        double[][] A_ = copy(null, A, 5, 7);
        A_[2][3] += 0.01;
        assertFalse(isEqual(A, A_, 5, 7));
        assertFalse(isEqual(A, A_, 5, 7, 0));
        assertFalse(isEqual(A, A_, 5, 7, 0.009));
        assertTrue(isEqual(A, A_, 5, 7, 0.01));
    }

    @Test
    public void testTranspose() {
        double[][] At = transpose(null, A, 5, 7);
        double[][] A_ = transpose(null, At, 7, 5);
        assertMatrixEquals(A_, A, 5, 7);
    }

    @Test
    public void testTransposeInPlace() {
        double[][] SPDt = transpose(null, SPD, 5, 5);
        double[][] SPDt_ = transpose(SPD, SPD, 5, 5);
        assertTrue(SPD == SPDt_);
        assertMatrixEquals(SPDt, SPDt_, 5, 5);
    }

    @Test
    public void testMultiplySimple() {
        double[][] A_ = multiply(null, Id5, A, 5, 5, 7);
        assertMatrixEquals(A, A_, 5, 7);
        double[][] At = transpose(null, A, 5, 7);
        A_ = multiply(null, At, Id5, 7, 5, 5);
        assertMatrixEquals(At, A_, 7, 5);
    }

    @Test
    public void testMultiplyTransposed() {
        double[][] At = transpose(null, A, 5, 7);
        double[][] At_A = multiply(null, A, A, 5, 7, 5, 7, true, false);
        double[][] At_A_2 = multiply(null, At, A, 7, 5, 7);
        assertMatrixEquals(At_A_2, At_A, 7, 7);
        double[][] A_At = multiply(null, A, A, 5, 7, 5, 7, false, true);
        double[][] A_At_2 = multiply(null, A, At, 5, 7, 5);
        assertMatrixEquals(A_At_2, A_At, 5, 5);
        At = transpose(null, A, 5, 5);
        double[][] At_At = multiply(null, A, A, 5, 5, 5, 5, true, true);
        double[][] At_At_2 = multiply(null, At, At, 5, 5, 5);
        assertMatrixEquals(At_At_2, At_At, 5, 5);
    }

    @Test
    public void testMultiplyVector() {
        double[] v = new double[] { 1, 2, 3 };
        double[][] A = new double[][] { { 1, 5, 2 }, { 3, -2, 7 } };
        double[] w = multiply(null, A, v, 2, 3, false);
        double[] exp = new double[] { 17, 20 };
        assertArrayEquals(exp, w, 0);
        double[][] At = transpose(null, A, 2, 3);
        w = multiply(null, At, v, 3, 2, true);
        assertArrayEquals(exp, w, 0);
    }

    @Test
    public void testSumVector() {
        double[] v = new double[] { 1, 2, 3 };
        double[] w = new double[] { 7, -4, 5 };
        double[] x = sum(null, v, w, 3);
        double[] exp = new double[] { 8, -2, 8 };
        assertArrayEquals(exp, x, 0);
        x = sum(v, v, w, 3);
        assertEquals(x, v);
        assertArrayEquals(exp, v, 0);
    }

    @Test
    public void testSumMatrix() {
        double[][] A = new double[][] { { 1, 5, 2 }, { 3, -2, 7 } };
        double[][] B = new double[][] { { 7, 5, 1 }, { 3, -2, 12 } };
        double[][] exp = new double[][] { { 8, 10, 3 }, { 6, -4, 19 } };
        double[][] x = sum(null, A, B, 2, 3);
        assertMatrixEquals(exp, x, 2, 3);
        x = sum(A, A, B, 2, 3);
        assertTrue(x == A);
        assertMatrixEquals(exp, A, 2, 3);
    }

    @Test
    public void testMultiplyUD() {
        double[][] U_D = multiply(null, U, D, 5, 5, 5);
        double[][] U_D_Ut = multiply(null, U_D, U, 5, 5, 5, 5, false, true);
        assertMatrixEquals(SPD, U_D_Ut, 5, 5, 0.01);
    }

    @Test
    public void testDecomposeUD_A() throws SingularityException {
        double[][] A_At = multiply(null, A, A, 5, 7, 5, 7, false, true);
        double[][] D = new double[5][5];
        double[][] U = new double[5][5];
        addOnesOnDiagonal(U, 5);
        decomposeUD(D, U, A_At, 5);
        double[][] U_D = multiply(null, U, D, 5, 5, 5);
        double[][] U_D_Ut = multiply(null, U_D, U, 5, 5, 5, 5, false, true);
        assertMatrixEquals(A_At, U_D_Ut, 5, 5, 0.0000000000001);
    }

    @Test(expected = SingularityException.class)
    public void testDecomposeUD_A2() throws SingularityException {
        double[][] At_A = multiply(null, A, A, 5, 7, 5, 7, true, false);
        double[][] D = new double[7][7];
        double[][] U = new double[7][7];
        decomposeUD(D, U, At_A, 7);
    }

    @Test
    public void testDecomposeUD() throws SingularityException {
        double[][] D = new double[5][5];
        double[][] U = new double[5][5];
        addOnesOnDiagonal(U, 5);
        decomposeUD(D, U, SPD, 5);
        assertMatrixEquals(this.U, U, 5, 5, 0.0001);
        assertMatrixEquals(this.D, D, 5, 5, 0.0001);
        double[][] U_D = multiply(null, U, D, 5, 5, 5);
        double[][] U_D_Ut = multiply(null, U_D, U, 5, 5, 5, 5, false, true);
        assertMatrixEquals(SPD, U_D_Ut, 5, 5, 0.000000000000001);
    }

    @Test
    public void testDecomposeUDEqualsAll() throws SingularityException {
        double[][] X = copy(null, SPD, 5, 5);
        decomposeUD(X, X, X, 5);
        double[][] D = extractDiagonal(X, 5);
        double[][] U = extractUpper(X, 5, true);
        assertMatrixEquals(this.U, U, 5, 5, 0.0001);
        assertMatrixEquals(this.D, D, 5, 5, 0.0001);
        double[][] U_D = multiply(null, U, D, 5, 5, 5);
        double[][] U_D_Ut = multiply(null, U_D, U, 5, 5, 5, 5, false, true);
        assertMatrixEquals(SPD, U_D_Ut, 5, 5, 0.000000000000001);
    }

    @Test
    public void testDecomposeUDEqualsUSPD() throws SingularityException {
        double[][] X = copy(null, SPD, 5, 5);
        double[][] D = new double[5][5];
        decomposeUD(D, X, X, 5);
        double[][] U = extractUpper(X, 5, true);
        assertMatrixEquals(this.U, U, 5, 5, 0.0001);
        assertMatrixEquals(this.D, D, 5, 5, 0.0001);
        double[][] U_D = multiply(null, U, D, 5, 5, 5);
        double[][] U_D_Ut = multiply(null, U_D, U, 5, 5, 5, 5, false, true);
        assertMatrixEquals(SPD, U_D_Ut, 5, 5, 0.000000000000001);
    }

    @Test
    public void testDecomposeUDEqualsDSPD() throws SingularityException {
        double[][] X = copy(null, SPD, 5, 5);
        double[][] U = new double[5][5];
        addOnesOnDiagonal(U, 5);
        decomposeUD(X, U, X, 5);
        double[][] D = extractDiagonal(X, 5);
        assertMatrixEquals(this.U, U, 5, 5, 0.0001);
        assertMatrixEquals(this.D, D, 5, 5, 0.0001);
        double[][] U_D = multiply(null, U, D, 5, 5, 5);
        double[][] U_D_Ut = multiply(null, U_D, U, 5, 5, 5, 5, false, true);
        assertMatrixEquals(SPD, U_D_Ut, 5, 5, 0.000000000000001);
    }

    @Test
    public void testDecomposeUDEqualsUD() throws SingularityException {
        double[][] X = new double[5][5];
        decomposeUD(X, X, SPD, 5);
        double[][] D = extractDiagonal(X, 5);
        double[][] U = extractUpper(X, 5, true);
        assertMatrixEquals(this.U, U, 5, 5, 0.0001);
        assertMatrixEquals(this.D, D, 5, 5, 0.0001);
        double[][] U_D = multiply(null, U, D, 5, 5, 5);
        double[][] U_D_Ut = multiply(null, U_D, U, 5, 5, 5, 5, false, true);
        assertMatrixEquals(SPD, U_D_Ut, 5, 5, 0.000000000000001);
    }

    @Test
    public void testSolveDiagonal() throws SingularityException {
        double[][] D_A = multiply(null, D, A, 5, 5, 7);
        double[][] D_A_ = copy(null, D_A, 5, 5);
        double[][] A_ = solveDiagonal(null, D, D_A, 5, 7);
        assertMatrixEquals(A, A_, 5, 7, 0.0000000000001);
        assertMatrixEquals(D_A_, D_A, 5, 5);
    }

    @Test
    public void testSolveDiagonalInPlace() throws SingularityException {
        double[][] D_A = multiply(null, D, A, 5, 5, 7);
        double[][] A_ = solveDiagonal(D_A, D, D_A, 5, 7);
        assertMatrixEquals(A, A_, 5, 7, 0.0000000000001);
    }

    @Test
    public void testSolveDiagonalInPlaceNotDiagonalMatrix() throws SingularityException {
        double[][] D_A = multiply(null, D, A, 5, 5, 7);
        double[][] A_ = solveDiagonal(D_A, D, D_A, 5, 7);
        assertMatrixEquals(A, A_, 5, 7, 0.0000000000001);
    }

    @Test
    public void testSolveUnitTriangular() {
        double[][] U_A = multiply(null, U, A, 5, 5, 7);
        double[][] U_A_ = copy(null, U_A, 5, 5);
        double[][] A_ = solveUnitTriangular(null, U, U_A, 5, 7, false);
        assertMatrixEquals(A, A_, 5, 7, 0.0000000000001);
        assertMatrixEquals(U_A_, U_A, 5, 5);
    }

    @Test
    public void testSolveUnitTriangularTransposed() {
        double[][] Ut_A = multiply(null, U, A, 5, 5, 5, 7, true, false);
        double[][] Ut_A_ = copy(null, Ut_A, 5, 5);
        double[][] A_ = solveUnitTriangular(null, U, Ut_A, 5, 7, true);
        assertMatrixEquals(A, A_, 5, 7, 0.0000000000001);
        assertMatrixEquals(Ut_A_, Ut_A, 5, 5);
    }

    @Test
    public void testSolveUnitTriangularInPlace() {
        double[][] U_A = multiply(null, U, A, 5, 5, 7);
        double[][] A_ = solveUnitTriangular(U_A, U, U_A, 5, 7, false);
        assertMatrixEquals(A, A_, 5, 7, 0.0000000000001);
    }

    @Test
    public void testSolveUnitTriangularTransposedInPlace() {
        double[][] Ut_A = multiply(null, U, A, 5, 5, 5, 7, true, false);
        double[][] A_ = solveUnitTriangular(Ut_A, U, Ut_A, 5, 7, true);
        assertMatrixEquals(A, A_, 5, 7, 0.0000000000001);
    }

    @Test
    public void testSolveSPD() throws SingularityException {
        double[][] X = solveSPD(null, null, SPD, A, 5, 7);
        double[][] A_ = multiply(null, SPD, X, 5, 5, 7);
        assertMatrixEquals(A, A_, 5, 7, 0.0000000001);
    }

    @Test
    public void testSolveSPDInPlace() throws SingularityException {
        double[][] A_copy = copy(null, A, 5, 7);
        double[][] X = solveSPD(A_copy, null, SPD, A_copy, 5, 7);
        assertTrue(X == A_copy);
        double[][] A_ = multiply(null, SPD, X, 5, 5, 7);
        assertMatrixEquals(A, A_, 5, 7, 0.0000000001);
    }

    @Test
    public void testSolveSPDAllInPlace() throws SingularityException {
        double[][] A_copy = copy(null, A, 5, 7);
        double[][] SPD_copy = copy(null, SPD, 5, 5);
        double[][] X = solveSPD(A_copy, SPD_copy, SPD_copy, A_copy, 5, 7);
        assertTrue(X == A_copy);
        double[][] A_ = multiply(null, SPD, X, 5, 5, 7);
        assertMatrixEquals(A, A_, 5, 7, 0.0000000001);
    }

    private static double[][] extractUpper(double[][] M, int size, boolean unit) {
        double[][] U = copy(null, M, size, size);
        for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) if (i == j && unit) U[i][j] = 1; else if (i > j) U[i][j] = 0;
        return U;
    }

    private static double[][] extractDiagonal(double[][] M, int size) {
        double[][] D = copy(null, M, size, size);
        for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) if (i != j) D[i][j] = 0;
        return D;
    }

    /**
	 * Sets all diagonal elements of the given matrix to <code>1</code>.
	 * 
	 * @param  A
	 *         a matrix buffer
	 * @param  size
	 *         height and width of the given matrix
	 */
    private static void addOnesOnDiagonal(double[][] A, int size) {
        for (int i = 0; i < size; i++) A[i][i] = 1;
    }

    /**
	 * Fails if one of the corresponding elements of matrices A and B have a
	 * difference greater than e.
	 */
    public static void assertMatrixEquals(double[][] expected, double[][] actual, int height, int width, double e) {
        assertTrue(isEqual(expected, actual, height, width, e));
    }

    /**
	 * Fails if matrices A and B are not equal.
	 */
    public static void assertMatrixEquals(double[][] expected, double[][] actual, int height, int width) {
        assertMatrixEquals(expected, actual, height, width, 0);
    }
}
