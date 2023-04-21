package com.google.zxing.qrcode.encoder;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author satorux@google.com (Satoru Takabayashi) - creator
 * @author mysen@google.com (Chris Mysen) - ported from C++
 */
public final class MaskUtilTestCase extends Assert {

    @Test
    public void testApplyMaskPenaltyRule1() {
        {
            ByteMatrix matrix = new ByteMatrix(4, 1);
            matrix.set(0, 0, 0);
            matrix.set(1, 0, 0);
            matrix.set(2, 0, 0);
            matrix.set(3, 0, 0);
            assertEquals(0, MaskUtil.applyMaskPenaltyRule1(matrix));
        }
        {
            ByteMatrix matrix = new ByteMatrix(6, 1);
            matrix.set(0, 0, 0);
            matrix.set(1, 0, 0);
            matrix.set(2, 0, 0);
            matrix.set(3, 0, 0);
            matrix.set(4, 0, 0);
            matrix.set(5, 0, 1);
            assertEquals(3, MaskUtil.applyMaskPenaltyRule1(matrix));
            matrix.set(5, 0, 0);
            assertEquals(4, MaskUtil.applyMaskPenaltyRule1(matrix));
        }
        {
            ByteMatrix matrix = new ByteMatrix(1, 6);
            matrix.set(0, 0, 0);
            matrix.set(0, 1, 0);
            matrix.set(0, 2, 0);
            matrix.set(0, 3, 0);
            matrix.set(0, 4, 0);
            matrix.set(0, 5, 1);
            assertEquals(3, MaskUtil.applyMaskPenaltyRule1(matrix));
            matrix.set(0, 5, 0);
            assertEquals(4, MaskUtil.applyMaskPenaltyRule1(matrix));
        }
    }

    @Test
    public void testApplyMaskPenaltyRule2() {
        {
            ByteMatrix matrix = new ByteMatrix(1, 1);
            matrix.set(0, 0, 0);
            assertEquals(0, MaskUtil.applyMaskPenaltyRule2(matrix));
        }
        {
            ByteMatrix matrix = new ByteMatrix(2, 2);
            matrix.set(0, 0, 0);
            matrix.set(1, 0, 0);
            matrix.set(0, 1, 0);
            matrix.set(1, 1, 1);
            assertEquals(0, MaskUtil.applyMaskPenaltyRule2(matrix));
        }
        {
            ByteMatrix matrix = new ByteMatrix(2, 2);
            matrix.set(0, 0, 0);
            matrix.set(1, 0, 0);
            matrix.set(0, 1, 0);
            matrix.set(1, 1, 0);
            assertEquals(3, MaskUtil.applyMaskPenaltyRule2(matrix));
        }
        {
            ByteMatrix matrix = new ByteMatrix(3, 3);
            matrix.set(0, 0, 0);
            matrix.set(1, 0, 0);
            matrix.set(2, 0, 0);
            matrix.set(0, 1, 0);
            matrix.set(1, 1, 0);
            matrix.set(2, 1, 0);
            matrix.set(0, 2, 0);
            matrix.set(1, 2, 0);
            matrix.set(2, 2, 0);
            assertEquals(3 * 4, MaskUtil.applyMaskPenaltyRule2(matrix));
        }
    }

    @Test
    public void testApplyMaskPenaltyRule3() {
        {
            ByteMatrix matrix = new ByteMatrix(11, 1);
            matrix.set(0, 0, 0);
            matrix.set(1, 0, 0);
            matrix.set(2, 0, 0);
            matrix.set(3, 0, 0);
            matrix.set(4, 0, 1);
            matrix.set(5, 0, 0);
            matrix.set(6, 0, 1);
            matrix.set(7, 0, 1);
            matrix.set(8, 0, 1);
            matrix.set(9, 0, 0);
            matrix.set(10, 0, 1);
            assertEquals(40, MaskUtil.applyMaskPenaltyRule3(matrix));
        }
        {
            ByteMatrix matrix = new ByteMatrix(11, 1);
            matrix.set(0, 0, 1);
            matrix.set(1, 0, 0);
            matrix.set(2, 0, 1);
            matrix.set(3, 0, 1);
            matrix.set(4, 0, 1);
            matrix.set(5, 0, 0);
            matrix.set(6, 0, 1);
            matrix.set(7, 0, 0);
            matrix.set(8, 0, 0);
            matrix.set(9, 0, 0);
            matrix.set(10, 0, 0);
            assertEquals(40, MaskUtil.applyMaskPenaltyRule3(matrix));
        }
        {
            ByteMatrix matrix = new ByteMatrix(1, 11);
            matrix.set(0, 0, 0);
            matrix.set(0, 1, 0);
            matrix.set(0, 2, 0);
            matrix.set(0, 3, 0);
            matrix.set(0, 4, 1);
            matrix.set(0, 5, 0);
            matrix.set(0, 6, 1);
            matrix.set(0, 7, 1);
            matrix.set(0, 8, 1);
            matrix.set(0, 9, 0);
            matrix.set(0, 10, 1);
            assertEquals(40, MaskUtil.applyMaskPenaltyRule3(matrix));
        }
        {
            ByteMatrix matrix = new ByteMatrix(1, 11);
            matrix.set(0, 0, 1);
            matrix.set(0, 1, 0);
            matrix.set(0, 2, 1);
            matrix.set(0, 3, 1);
            matrix.set(0, 4, 1);
            matrix.set(0, 5, 0);
            matrix.set(0, 6, 1);
            matrix.set(0, 7, 0);
            matrix.set(0, 8, 0);
            matrix.set(0, 9, 0);
            matrix.set(0, 10, 0);
            assertEquals(40, MaskUtil.applyMaskPenaltyRule3(matrix));
        }
    }

    @Test
    public void testApplyMaskPenaltyRule4() {
        {
            ByteMatrix matrix = new ByteMatrix(1, 1);
            matrix.set(0, 0, 0);
            assertEquals(100, MaskUtil.applyMaskPenaltyRule4(matrix));
        }
        {
            ByteMatrix matrix = new ByteMatrix(2, 1);
            matrix.set(0, 0, 0);
            matrix.set(0, 0, 1);
            assertEquals(0, MaskUtil.applyMaskPenaltyRule4(matrix));
        }
        {
            ByteMatrix matrix = new ByteMatrix(6, 1);
            matrix.set(0, 0, 0);
            matrix.set(1, 0, 1);
            matrix.set(2, 0, 1);
            matrix.set(3, 0, 1);
            matrix.set(4, 0, 1);
            matrix.set(5, 0, 0);
            assertEquals(30, MaskUtil.applyMaskPenaltyRule4(matrix));
        }
    }

    private static boolean TestGetDataMaskBitInternal(int maskPattern, int[][] expected) {
        for (int x = 0; x < 6; ++x) {
            for (int y = 0; y < 6; ++y) {
                if ((expected[y][x] == 1) != MaskUtil.getDataMaskBit(maskPattern, x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Test
    public void testGetDataMaskBit() {
        int[][] mask0 = { { 1, 0, 1, 0, 1, 0 }, { 0, 1, 0, 1, 0, 1 }, { 1, 0, 1, 0, 1, 0 }, { 0, 1, 0, 1, 0, 1 }, { 1, 0, 1, 0, 1, 0 }, { 0, 1, 0, 1, 0, 1 } };
        assertTrue(TestGetDataMaskBitInternal(0, mask0));
        int[][] mask1 = { { 1, 1, 1, 1, 1, 1 }, { 0, 0, 0, 0, 0, 0 }, { 1, 1, 1, 1, 1, 1 }, { 0, 0, 0, 0, 0, 0 }, { 1, 1, 1, 1, 1, 1 }, { 0, 0, 0, 0, 0, 0 } };
        assertTrue(TestGetDataMaskBitInternal(1, mask1));
        int[][] mask2 = { { 1, 0, 0, 1, 0, 0 }, { 1, 0, 0, 1, 0, 0 }, { 1, 0, 0, 1, 0, 0 }, { 1, 0, 0, 1, 0, 0 }, { 1, 0, 0, 1, 0, 0 }, { 1, 0, 0, 1, 0, 0 } };
        assertTrue(TestGetDataMaskBitInternal(2, mask2));
        int[][] mask3 = { { 1, 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0, 1 }, { 0, 1, 0, 0, 1, 0 }, { 1, 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0, 1 }, { 0, 1, 0, 0, 1, 0 } };
        assertTrue(TestGetDataMaskBitInternal(3, mask3));
        int[][] mask4 = { { 1, 1, 1, 0, 0, 0 }, { 1, 1, 1, 0, 0, 0 }, { 0, 0, 0, 1, 1, 1 }, { 0, 0, 0, 1, 1, 1 }, { 1, 1, 1, 0, 0, 0 }, { 1, 1, 1, 0, 0, 0 } };
        assertTrue(TestGetDataMaskBitInternal(4, mask4));
        int[][] mask5 = { { 1, 1, 1, 1, 1, 1 }, { 1, 0, 0, 0, 0, 0 }, { 1, 0, 0, 1, 0, 0 }, { 1, 0, 1, 0, 1, 0 }, { 1, 0, 0, 1, 0, 0 }, { 1, 0, 0, 0, 0, 0 } };
        assertTrue(TestGetDataMaskBitInternal(5, mask5));
        int[][] mask6 = { { 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 0, 0, 0 }, { 1, 1, 0, 1, 1, 0 }, { 1, 0, 1, 0, 1, 0 }, { 1, 0, 1, 1, 0, 1 }, { 1, 0, 0, 0, 1, 1 } };
        assertTrue(TestGetDataMaskBitInternal(6, mask6));
        int[][] mask7 = { { 1, 0, 1, 0, 1, 0 }, { 0, 0, 0, 1, 1, 1 }, { 1, 0, 0, 0, 1, 1 }, { 0, 1, 0, 1, 0, 1 }, { 1, 1, 1, 0, 0, 0 }, { 0, 1, 1, 1, 0, 0 } };
        assertTrue(TestGetDataMaskBitInternal(7, mask7));
    }
}
