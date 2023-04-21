package org.jcvi.assembly;

import org.jcvi.common.util.Range;
import org.jcvi.common.util.Range.CoordinateSystem;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestAssemblyUtil_reverseComplimentRange {

    private final Range range = Range.buildRange(0, 9);

    @Test
    public void reverseFullRange() {
        assertEquals(range, AssemblyUtil.reverseComplimentValidRange(range, range.size()));
    }

    @Test
    public void reverse() {
        Range expectedRange = Range.buildRange(5, 14);
        assertEquals(expectedRange, AssemblyUtil.reverseComplimentValidRange(range, 15));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fullLengthSmallerThanValidRangeShouldThrowIllegalArgumentException() {
        AssemblyUtil.reverseComplimentValidRange(range, range.size() - 1);
    }

    @Test
    public void keepCoordinateSystem() {
        Range spacedBasedRange = range.convertRange(CoordinateSystem.SPACE_BASED);
        assertEquals(spacedBasedRange, AssemblyUtil.reverseComplimentValidRange(spacedBasedRange, range.size()));
    }

    @Test
    public void validRangeInMiddleOfFullRange() {
        Range validRange = Range.buildRange(5, 9);
        assertEquals(Range.buildRange(10, 14), AssemblyUtil.reverseComplimentValidRange(validRange, 20));
    }
}
