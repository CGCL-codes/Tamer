package net.community.chest.math.test;

import net.community.chest.math.DivisionSigns;
import org.junit.Assert;
import org.junit.Test;

/**
 * <P>Copyright as per GPLv2</P>
 * @author Lyor G.
 * @since Jun 12, 2011 8:20:57 AM
 */
public class DivisionSignsTest extends Assert {

    private static final long[] VALUES = { 0L, 12L, 123L, 10203L, 3777347L, 17041690L, 7031965L, 1669974L, 6334353L, 2541006L };

    public DivisionSignsTest() {
        super();
    }

    @Test
    public void testSumDigits() {
        for (final long v : VALUES) {
            final long strSum = DivisionSigns.sumDigits(String.valueOf(v)), numSum = DivisionSigns.sumDigits(v);
            assertEquals("[" + v + "] Mismatched string vs. number digits sum", strSum, numSum);
            assertEquals("[" + v + "] Mismatched negative digits sum", 0L - numSum, DivisionSigns.sumDigits(0L - v));
        }
    }

    @Test
    public void testIsMultiple3() {
        for (final long v : VALUES) {
            final boolean expected = (v != 0L) && ((v % 3L) == 0L);
            assertEquals("[" + v + "] mismatched multiple-3 numberical result", Boolean.valueOf(expected), Boolean.valueOf(DivisionSigns.isMultiple3(v)));
            assertEquals("[" + v + "] mismatched multiple-3 string result", Boolean.valueOf(expected), Boolean.valueOf(DivisionSigns.isMultiple3(String.valueOf(v))));
            if ((v != 0L) && (!expected)) assertTrue("[" + (3L * v) + "] non-triplet multiple-3", DivisionSigns.isMultiple3(3L * v));
        }
    }

    @Test
    public void testIsMultiple6() {
        for (final long v : VALUES) {
            final boolean expected = (v != 0L) && ((v % 6L) == 0L);
            assertEquals("[" + v + "] mismatched numerical multiple-6 result", Boolean.valueOf(expected), Boolean.valueOf(DivisionSigns.isMultiple6(v)));
            assertEquals("[" + v + "] mismatched string multiple-6 result", Boolean.valueOf(expected), Boolean.valueOf(DivisionSigns.isMultiple6(String.valueOf(v))));
            if ((v != 0L) && (!expected)) {
                if ((v & 0x01) == 0L) assertTrue("[" + (3L * v) + "] non-even multiple-6", DivisionSigns.isMultiple6(3L * v)); else if ((v % 3L) == 0L) assertTrue("[" + (2L * v) + "] non-triplet multiple-6", DivisionSigns.isMultiple6(2L * v)); else assertTrue("[" + (6L * v) + "] non-six multiple-6", DivisionSigns.isMultiple6(6L * v));
            }
        }
    }

    @Test
    public void testIsMultiple9() {
        for (final long v : VALUES) {
            final boolean expected = (v != 0L) && ((v % 9L) == 0L);
            assertEquals("[" + v + "] mismatched numerical multiple-9 result", Boolean.valueOf(expected), Boolean.valueOf(DivisionSigns.isMultiple9(v)));
            assertEquals("[" + v + "] mismatched string multiple-9 result", Boolean.valueOf(expected), Boolean.valueOf(DivisionSigns.isMultiple9(String.valueOf(v))));
            if ((v != 0L) && (!expected)) assertTrue("[" + (9L * v) + "] non-nine multiple-9", DivisionSigns.isMultiple3(9L * v));
        }
    }

    @Test
    public void testIsMultiple5() {
        for (final long v : VALUES) {
            final boolean expected = (v % 5L) == 0L;
            assertEquals("[" + v + "] mismatched multiple-5 result", Boolean.valueOf(expected), Boolean.valueOf(DivisionSigns.isMultiple5(String.valueOf(v))));
            if ((v != 0L) && (!expected)) assertTrue("[" + (5L * v) + "] non-nine multiple-9", DivisionSigns.isMultiple5(String.valueOf(5L * v)));
        }
    }
}
