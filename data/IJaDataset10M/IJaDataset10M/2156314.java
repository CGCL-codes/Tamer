package org.jcvi.trace.fourFiveFour.flowgram.sff;

import org.jcvi.common.util.Range;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestNewblerSuffixNameConverter {

    private static final String ORIGINAL_NAME = "C3U5GNB01C40I3";

    @Test
    public void getSffNameWithRange() {
        String suffixed = ORIGINAL_NAME + ".1-60";
        assertEquals(ORIGINAL_NAME, NewblerSuffixNameConverter.getUnSuffixedNameFrom(suffixed));
    }

    @Test
    public void getSffNameWithFrom() {
        String suffixed = ORIGINAL_NAME + ".1-60.fm2";
        assertEquals(ORIGINAL_NAME, NewblerSuffixNameConverter.getUnSuffixedNameFrom(suffixed));
    }

    @Test
    public void getSffNameWithTo() {
        String suffixed = ORIGINAL_NAME + ".1-60.to45";
        assertEquals(ORIGINAL_NAME, NewblerSuffixNameConverter.getUnSuffixedNameFrom(suffixed));
    }

    @Test
    public void getSffNameWithPairedEnds() {
        String suffixedLeft = ORIGINAL_NAME + "_left";
        String suffixedRight = ORIGINAL_NAME + "_right";
        assertEquals(ORIGINAL_NAME, NewblerSuffixNameConverter.getUnSuffixedNameFrom(suffixedLeft));
        assertEquals(ORIGINAL_NAME, NewblerSuffixNameConverter.getUnSuffixedNameFrom(suffixedRight));
    }

    @Test
    public void getSffNameWithPairedEndsAlignToDifferentContigs() {
        String suffixedLeft = ORIGINAL_NAME + "_left.pr87";
        String suffixedRight = ORIGINAL_NAME + "_right.pr36";
        assertEquals(ORIGINAL_NAME, NewblerSuffixNameConverter.getUnSuffixedNameFrom(suffixedLeft));
        assertEquals(ORIGINAL_NAME, NewblerSuffixNameConverter.getUnSuffixedNameFrom(suffixedRight));
    }

    @Test
    public void getSffNameWithTrimmedPairedEndsAlignToDifferentContigs() {
        String suffixedLeft = ORIGINAL_NAME + "_left.1-60.pr87";
        String suffixedRight = ORIGINAL_NAME + "_right.61-248.pr36";
        assertEquals(ORIGINAL_NAME, NewblerSuffixNameConverter.getUnSuffixedNameFrom(suffixedLeft));
        assertEquals(ORIGINAL_NAME, NewblerSuffixNameConverter.getUnSuffixedNameFrom(suffixedRight));
    }

    @Test
    public void getRangeNoRangeShouldReturnNull() {
        assertNull(NewblerSuffixNameConverter.getSuffixedRangeFrom(ORIGINAL_NAME));
    }

    @Test
    public void getRangeShouldReturnZeroBased() {
        Range expectedRange = Range.buildRange(0, 10);
        assertEquals(expectedRange, NewblerSuffixNameConverter.getSuffixedRangeFrom(ORIGINAL_NAME + ".1-11"));
    }

    @Test
    public void getReversedRangeShouldSwapLeftAndRight() {
        Range expectedRange = Range.buildRange(23, 543);
        assertEquals(expectedRange, NewblerSuffixNameConverter.getSuffixedRangeFrom(ORIGINAL_NAME + ".544-24"));
    }
}
