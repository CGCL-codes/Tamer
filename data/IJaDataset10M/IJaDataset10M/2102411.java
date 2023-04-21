package edu.ucla.sspace.matrix;

import org.junit.*;
import static org.junit.Assert.*;

public class MatricesTest {

    @Test
    public void multiplyTest() {
        double[] data1 = { 1, 2, 3, 4, 1, 1, 1, 1, 5, 4, 3, 2 };
        Matrix leftMatrix = new ArrayMatrix(3, 4, data1);
        double[] data2 = { 1, 2, 1, 2, 1, 2, 1, 2 };
        Matrix rightMatrix = new ArrayMatrix(4, 2, data2);
        Matrix result = Matrices.multiply(leftMatrix, rightMatrix);
        double[][] expectations = { { 10, 20 }, { 4, 8 }, { 14, 28 } };
        assertEquals(3, result.rows());
        assertEquals(2, result.columns());
        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 2; ++c) {
                assertEquals(expectations[r][c], result.get(r, c), .00001);
            }
        }
    }

    @Test
    public void multipleBothDiagonalTest() {
        double[] data1 = { 1, 2, 3, 4, 5 };
        Matrix leftMatrix = new DiagonalMatrix(data1);
        double[] data2 = { 5, 4, 3, 2, 1 };
        Matrix rightMatrix = new DiagonalMatrix(data2);
        Matrix result = Matrices.multiply(leftMatrix, rightMatrix);
        assertEquals(data1.length, result.rows());
        assertEquals(data1.length, result.columns());
        assertTrue(result instanceof DiagonalMatrix);
        for (int i = 0; i < data1.length; ++i) {
            for (int j = 0; j < data1.length; ++j) {
                if (i == j) assertEquals(data1[i] * data2[i], result.get(i, j), .0001); else assertEquals(0, result.get(i, j), .0001);
            }
        }
    }

    @Test
    public void multiplyLeftDiagonalTemultiplest() {
        double[] data2 = { 1, 2, 3, 0 };
        Matrix leftMatrix = new DiagonalMatrix(data2);
        double[] data1 = { 1, 2, 3, 4 };
        Matrix rightMatrix = new ArrayMatrix(4, 1, data1);
        Matrix result = Matrices.multiply(leftMatrix, rightMatrix);
        assertEquals(4, result.rows());
        assertEquals(1, result.columns());
        for (int r = 0; r < 4; ++r) {
            assertEquals(data2[r] * data1[r], result.get(r, 0), .00001);
        }
    }

    @Test
    public void multiplyRightDiagonalTemultiplest() {
        double[] data1 = { 1, 2, 3, 4, 1, 1, 1, 1, 5, 4, 3, 2 };
        Matrix leftMatrix = new ArrayMatrix(3, 4, data1);
        double[] data2 = { 1, 2, 3, 0 };
        Matrix rightMatrix = new DiagonalMatrix(data2);
        Matrix result = Matrices.multiply(leftMatrix, rightMatrix);
        double[][] expectations = { { 1, 4, 9, 0 }, { 1, 2, 3, 0 }, { 5, 8, 9, 0 } };
        assertEquals(3, result.rows());
        assertEquals(4, result.columns());
        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 4; ++c) {
                assertEquals(expectations[r][c], result.get(r, c), .00001);
            }
        }
    }
}
