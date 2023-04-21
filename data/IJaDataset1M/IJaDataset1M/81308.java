package annas.test.math;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import annas.math.Matrix;

public class TestMatrix {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMatrixIntInt() {
        Matrix m = new Matrix(3, 3);
        assertTrue(m.getMatrix().length == 3);
        assertTrue(m.getMatrix()[0].length == 3);
        m = new Matrix(3, 4);
        assertTrue(m.getMatrix().length == 4);
        assertTrue(m.getMatrix()[0].length == 3);
    }

    @Test
    public void testMatrixFloatArrayArray() {
        float[][] array = new float[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        Matrix m = new Matrix(array);
        float[][] n = m.getMatrix();
        assertTrue(java.util.Arrays.deepEquals(n, array));
    }

    @Test
    public void testSetMatrix() {
        Matrix m = Matrix.createIdentity(3);
        float[][] array = new float[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        float[][] n = m.getMatrix();
        assertFalse(java.util.Arrays.deepEquals(n, array));
        m.SetMatrix(array);
        n = m.getMatrix();
        assertTrue(java.util.Arrays.deepEquals(n, array));
    }

    @Test
    public void testGetMatrix() {
        Matrix m = Matrix.createIdentity(3);
        float[][] array = new float[][] { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
        float[][] n = m.getMatrix();
        assertTrue(java.util.Arrays.deepEquals(n, array));
    }

    @Test
    public void testCreateIdentity() throws Exception {
        Matrix m = Matrix.createIdentity(9);
        assertTrue(m.getMatrix().length == 9);
        assertTrue(m.getMatrix()[0].length == 9);
        assertTrue(m.MultiplyMatrix(m).eq(m));
    }

    @Test
    public void testMultiplyMatrix() throws Exception {
        Matrix m = new Matrix(new float[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        Matrix n = Matrix.createIdentity(3);
        assertTrue(m.MultiplyMatrix(n).eq(m));
    }

    @Test
    public void testTranspose() {
        Matrix m = new Matrix(new float[][] { { 1, 2 }, { 3, 4 } });
        Matrix n = new Matrix(new float[][] { { 1, 3 }, { 2, 4 } });
        assertTrue(m.Transpose().eq(n));
    }

    @Test
    public void testInverse() throws Exception {
        Matrix m = new Matrix(new float[][] { { 1, 2 }, { 3, 4 } });
        Matrix n = new Matrix(new float[][] { { -2, 1 }, { 1.5f, -0.5f } });
        assertTrue(m.Inverse().eq(n));
    }

    @Test
    public void testAdjoint() throws Exception {
        Matrix m = new Matrix(new float[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        Matrix n = new Matrix(new float[][] { { -3, 6, -3 }, { 6, -12, 6 }, { -3, 6, -3 } });
        assertTrue(m.Adjoint().eq(n, 0.1));
    }

    @Test
    public void testDeterminant() {
        Matrix m = new Matrix(new float[][] { { 1, 2 }, { 3, 4 } });
        assertTrue(m.Determinant() == -2f);
    }

    @Test
    public void testAddMatrix() throws Exception {
        Matrix m = new Matrix(new float[][] { { 1, 2 }, { 3, 4 } });
        Matrix n = new Matrix(new float[][] { { 1, 3 }, { 2, 4 } });
        Matrix p = new Matrix(new float[][] { { 2, 5 }, { 5, 8 } });
        assertTrue(m.addMatrix(n).eq(p));
    }

    @Test
    public void testSubtractMatrix() throws Exception {
        Matrix m = new Matrix(new float[][] { { 1, 2 }, { 3, 4 } });
        Matrix n = new Matrix(new float[][] { { 1, 3 }, { 2, 4 } });
        Matrix p = new Matrix(new float[][] { { 0, -1 }, { 1, 0 } });
        assertTrue(m.subtractMatrix(n).eq(p));
    }

    @Test
    public void testDivide() {
        Matrix m = new Matrix(new float[][] { { 1, 2 }, { 3, 4 } });
        Matrix n = new Matrix(new float[][] { { 1, 3 }, { 2, 4 } });
        Matrix p = new Matrix(new float[][] { { 0, -2 }, { 0.5f, 2.5f } });
        assertTrue(m.divide(n).eq(p));
    }
}
