package com.oat.junit;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.oat.utils.BitStringUtils;

/**
 * Type: BitStringUtilsTests<br/>
 * Date: 19/07/2007<br/>
 * <br/>
 * Description: Test bit string utilities
 * <pre>
 *            Dec  Gray   Binary
 *             0   000    000
 *             1   001    001
 *             2   011    010
 *             3   010    011
 *             4   110    100
 *             5   111    101
 *             6   101    110
 *             7   100    111
 * </pre>
 * <br/>
 * @author Jason Brownlee
 *
 * 
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 
 * </pre>
 *
 */
public class BitStringUtilsTests {

    @Test
    public void testBinaryDecode() {
        int length = 3;
        boolean[] zero = { false, false, false };
        boolean[] two = { false, true, false };
        boolean[] seven = { true, true, true };
        assertEquals(0.0, BitStringUtils.binaryBitsToDouble(zero, 0, length, 0.0, Math.pow(2, length) - 1));
        assertEquals(2.0, BitStringUtils.binaryBitsToDouble(two, 0, length, 0.0, Math.pow(2, length) - 1));
        assertEquals(7.0, BitStringUtils.binaryBitsToDouble(seven, 0, length, 0.0, Math.pow(2, length) - 1));
    }

    @Test
    public void testGrayDecode() {
        int length = 3;
        boolean[] zero = { false, false, false };
        boolean[] two = { false, true, true };
        boolean[] seven = { true, false, false };
        assertEquals(0.0, BitStringUtils.grayBitsToDouble(zero, 0, length, 0.0, Math.pow(2, length) - 1));
        assertEquals(2.0, BitStringUtils.grayBitsToDouble(two, 0, length, 0.0, Math.pow(2, length) - 1));
        assertEquals(7.0, BitStringUtils.grayBitsToDouble(seven, 0, length, 0.0, Math.pow(2, length) - 1));
    }

    @Test
    public void testHammingDistance() {
    }

    public void testArrays(boolean[] expected, boolean[] got) {
        assertEquals(expected.length, got.length);
        for (int i = 0; i < got.length; i++) {
            assertEquals(expected[i], got[i]);
        }
    }
}
