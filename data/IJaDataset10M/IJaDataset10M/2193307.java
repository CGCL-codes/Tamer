package calclipse.lib.math.mtrx;

import static calclipse.lib.math.mtrx.MatrixTest.getMatrix;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DenseMatrixTest {

    public DenseMatrixTest() {
    }

    @Test
    public void testTransform() {
        final Matrix m = getMatrix(2, 3, 1, 2, 3, 4, 5, 6);
        final Matrix m2 = getMatrix(3, 2, 7, 8, 9, 10, 11, 12);
        final Matrix expected = getMatrix(2, 2, 58, 64, 139, 154);
        final DenseMatrix dm = new DenseMatrix(0, 0);
        final DenseMatrix dm2 = new DenseMatrix(0, 0);
        dm.copyFrom(m);
        dm2.copyFrom(m2);
        dm.transform(dm2);
        assertEquals("1", expected, dm2);
    }

    @Test
    public void testSetSizeLargerAndPreserve() {
        final Matrix m = getMatrix(1, 1, 2);
        final DenseMatrix dm = new DenseMatrix(0, 0);
        dm.copyFrom(m);
        dm.setSize(2, 2, true);
        final Matrix expected = getMatrix(2, 2, 2, 0, 0, 0);
        assertEquals("2", expected, dm);
    }

    @Test
    public void testSetSizeSmallerAndPreserve() {
        final Matrix m = getMatrix(2, 2, 1, 2, 3, 4);
        final DenseMatrix dm = new DenseMatrix(0, 0);
        dm.copyFrom(m);
        dm.setSize(2, 1, true);
        final Matrix expected = getMatrix(2, 1, 1, 3);
        assertEquals("3", expected, dm);
    }

    @Test
    public void testSetSizeEqualAndPreserve() {
        final Matrix m = getMatrix(2, 2, 1, 2, 3, 4);
        final DenseMatrix dm = new DenseMatrix(0, 0);
        dm.copyFrom(m);
        dm.setSize(2, 2, true);
        final Matrix expected = getMatrix(2, 2, 1, 2, 3, 4);
        assertEquals("4", expected, dm);
    }

    @Test
    public void testSetSizeLargerAndNotPreserve() {
        final Matrix m = getMatrix(1, 1, 2);
        final DenseMatrix dm = new DenseMatrix(0, 0);
        dm.copyFrom(m);
        dm.setSize(2, 2, false);
        final Matrix expected = getMatrix(2, 2, 0, 0, 0, 0);
        assertEquals("5", expected, dm);
    }

    @Test
    public void testSetSizeSmallerAndNotPreserve() {
        final Matrix m = getMatrix(2, 2, 1, 2, 3, 4);
        final DenseMatrix dm = new DenseMatrix(0, 0);
        dm.copyFrom(m);
        dm.setSize(2, 1, false);
        final Matrix expected = getMatrix(2, 1, 0, 0);
        assertEquals("6", expected, dm);
    }

    @Test
    public void testSetSizeEqualAndNotPreserve() {
        final Matrix m = getMatrix(2, 2, 1, 2, 3, 4);
        final DenseMatrix dm = new DenseMatrix(0, 0);
        dm.copyFrom(m);
        dm.setSize(2, 2, false);
        final Matrix expected = getMatrix(2, 2, 0, 0, 0, 0);
        assertEquals("7", expected, dm);
    }
}
