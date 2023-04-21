package edu.ucla.sspace.matrix;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Keith Stevens
 */
public class RowMaskedMatrixTest {

    public static final double[][] VALUES = { { 1, 2, 4, 5, 6 }, { .1, .2, .3, .4, .5 }, { 7, 8, 9, 0, 11 }, { .1, .2, .3, .4, .5 } };

    @Test
    public void testReorderdMatrix() {
        Matrix baseMatrix = new ArrayMatrix(VALUES);
        int[] reordering = new int[] { 1, 2, 3 };
        RowMaskedMatrix mapped = new RowMaskedMatrix(baseMatrix, reordering);
        assertEquals(baseMatrix, mapped.backingMatrix());
        assertEquals(3, mapped.rows());
        assertEquals(5, mapped.columns());
        assertEquals(baseMatrix.get(1, 1), mapped.get(0, 1), .0001);
        assertEquals(baseMatrix.get(2, 1), mapped.get(1, 1), .0001);
        assertEquals(baseMatrix.get(3, 1), mapped.get(2, 1), .0001);
        assertEquals(3, mapped.reordering().length);
        assertEquals(1, mapped.reordering()[0]);
        assertEquals(2, mapped.reordering()[1]);
        assertEquals(3, mapped.reordering()[2]);
    }

    @Test
    public void testTwoLevelReorderdMatrix() {
        Matrix baseMatrix = new ArrayMatrix(VALUES);
        int[] reordering = new int[] { 1, 2, 3 };
        RowMaskedMatrix mapped = new RowMaskedMatrix(baseMatrix, reordering);
        reordering = new int[] { 0, 2, 1, 0 };
        mapped = new RowMaskedMatrix(mapped, reordering);
        assertEquals(baseMatrix, mapped.backingMatrix());
        assertEquals(4, mapped.rows());
        assertEquals(5, mapped.columns());
        assertEquals(baseMatrix.get(1, 1), mapped.get(0, 1), .001);
        assertEquals(baseMatrix.get(1, 1), mapped.get(3, 1), .001);
        assertEquals(baseMatrix.get(3, 1), mapped.get(1, 1), .001);
        assertEquals(baseMatrix.get(2, 1), mapped.get(2, 1), .001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBelowZeroOrdering() {
        Matrix baseMatrix = new ArrayMatrix(VALUES);
        int[] reordering = new int[] { 1, -1, 3 };
        RowMaskedMatrix mapped = new RowMaskedMatrix(baseMatrix, reordering);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOutOfBoundsOrdering() {
        Matrix baseMatrix = new ArrayMatrix(VALUES);
        int[] reordering = new int[] { 1, 2, 4 };
        RowMaskedMatrix mapped = new RowMaskedMatrix(baseMatrix, reordering);
    }
}
