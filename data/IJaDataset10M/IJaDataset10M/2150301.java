package org.jcvi.assembly.coverage;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jcvi.assembly.Placed;
import org.jcvi.common.util.Range;
import org.jcvi.common.util.Range.CoordinateSystem;
import org.jcvi.testUtil.TestUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestDefaultCoverageMap {

    Placed seq_0_9 = Range.buildRange(0, 9);

    Placed seq_0_12 = Range.buildRange(0, 12);

    Placed seq_5_14 = Range.buildRange(5, 14);

    Placed seq_5_5 = Range.buildEmptyRange(5);

    Placed seq_8_12 = Range.buildRange(8, 12);

    Placed seq_10_12 = Range.buildRange(10, 12);

    Placed seq_11_15 = Range.buildRange(11, 15);

    Placed seq_11_20 = Range.buildRange(11, 20);

    Placed seq_11_18 = Range.buildRange(11, 18);

    Placed seq_12_18 = Range.buildRange(12, 18);

    Placed seq_16_20 = Range.buildRange(16, 20);

    private DefaultCoverageRegion<Placed> createCoverageRegion(int start, int end, Placed... elements) {
        return new DefaultCoverageRegion.Builder<Placed>(start, Arrays.asList(elements)).end(end).build();
    }

    @Test
    public void emptyListShouldReturnEmptyCoverageMap() {
        final DefaultCoverageMap.Builder<Placed> builder = new DefaultCoverageMap.Builder<Placed>(Collections.<Placed>emptyList());
        CoverageMap<CoverageRegion<Placed>> map = builder.build();
        assertEquals(0, map.getNumberOfRegions());
        assertEquals(0, map.getLength());
        assertTrue(map.isEmpty());
    }

    @Test
    public void sizeOf1() {
        Placed read = Range.buildRange(0, 0);
        final DefaultCoverageMap.Builder<Placed> builder = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(read));
        CoverageMap<CoverageRegion<Placed>> map = builder.build();
        assertEquals(1, map.getNumberOfRegions());
        assertEquals(1, map.getLength());
    }

    @Test
    public void ignoreSequenceOfZeroSize() {
        final DefaultCoverageMap.Builder<Placed> builder = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_5_5));
        CoverageMap<CoverageRegion<Placed>> map = builder.build();
        assertEquals(0, map.getNumberOfRegions());
        assertEquals(0, map.getLength());
        assertTrue(map.isEmpty());
    }

    @Test
    public void oneSequenceShouldHaveOneCoverageRegion() {
        final DefaultCoverageMap.Builder<Placed> builder = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9));
        CoverageMap<CoverageRegion<Placed>> map = builder.build();
        assertEquals(1, map.getNumberOfRegions());
        CoverageRegion<Placed> expectedRegion = createCoverageRegion(0, 9, seq_0_9);
        assertEquals(expectedRegion, map.getRegion(0));
        assertEquals(10, map.getLength());
        assertFalse(map.isEmpty());
    }

    @Test
    public void twoTiledSequencesShouldHave3CoverageRegions() {
        final DefaultCoverageMap.Builder<Placed> builder = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9, seq_5_14));
        CoverageMap<CoverageRegion<Placed>> map = builder.build();
        assertEquals(3, map.getNumberOfRegions());
        assertEquals(createCoverageRegion(0, 4, seq_0_9), map.getRegion(0));
        assertEquals(createCoverageRegion(5, 9, seq_0_9, seq_5_14), map.getRegion(1));
        assertEquals(createCoverageRegion(10, 14, seq_5_14), map.getRegion(2));
        assertEquals(15, map.getLength());
        assertFalse(map.isEmpty());
    }

    @Test
    public void hasleavingSequence() {
        final DefaultCoverageMap.Builder<Placed> builder = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9, seq_5_14, seq_0_12));
        CoverageMap<CoverageRegion<Placed>> map = builder.build();
        assertEquals(4, map.getNumberOfRegions());
        assertEquals(createCoverageRegion(0, 4, seq_0_9, seq_0_12), map.getRegion(0));
        assertEquals(createCoverageRegion(5, 9, seq_0_9, seq_0_12, seq_5_14), map.getRegion(1));
        assertEquals(createCoverageRegion(10, 12, seq_0_12, seq_5_14), map.getRegion(2));
        assertEquals(createCoverageRegion(13, 14, seq_5_14), map.getRegion(3));
        assertEquals(15, map.getLength());
        assertFalse(map.isEmpty());
    }

    @Test
    public void abutment() {
        final DefaultCoverageMap.Builder<Placed> builder = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9, seq_10_12));
        CoverageMap<CoverageRegion<Placed>> map = builder.build();
        assertEquals(2, map.getNumberOfRegions());
        assertEquals(createCoverageRegion(0, 9, seq_0_9), map.getRegion(0));
        assertEquals(createCoverageRegion(10, 12, seq_10_12), map.getRegion(1));
        assertEquals(13, map.getLength());
        assertFalse(map.isEmpty());
    }

    @Test
    public void zeroCoverageRegion() {
        final DefaultCoverageMap.Builder<Placed> builder = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9, seq_5_14, seq_0_12, seq_10_12, seq_16_20));
        CoverageMap<CoverageRegion<Placed>> map = builder.build();
        assertEquals(6, map.getNumberOfRegions());
        assertEquals(createCoverageRegion(0, 4, seq_0_9, seq_0_12), map.getRegion(0));
        assertEquals(createCoverageRegion(5, 9, seq_0_9, seq_0_12, seq_5_14), map.getRegion(1));
        assertEquals(createCoverageRegion(10, 12, seq_0_12, seq_5_14, seq_10_12), map.getRegion(2));
        assertEquals(createCoverageRegion(13, 14, seq_5_14), map.getRegion(3));
        assertEquals(createCoverageRegion(15, 15), map.getRegion(4));
        assertEquals(createCoverageRegion(16, 20, seq_16_20), map.getRegion(5));
        assertEquals(21, map.getLength());
        assertFalse(map.isEmpty());
    }

    @Test
    public void threeConsecutiveReadsEndAtSamePoint() {
        final DefaultCoverageMap.Builder<Placed> builder = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_12, seq_8_12, seq_10_12, seq_11_15));
        CoverageMap<CoverageRegion<Placed>> map = builder.build();
        assertEquals(5, map.getNumberOfRegions());
        assertEquals(createCoverageRegion(0, 7, seq_0_12), map.getRegion(0));
        assertEquals(createCoverageRegion(8, 9, seq_0_12, seq_8_12), map.getRegion(1));
        assertEquals(createCoverageRegion(10, 10, seq_0_12, seq_8_12, seq_10_12), map.getRegion(2));
        assertEquals(createCoverageRegion(11, 12, seq_0_12, seq_8_12, seq_10_12, seq_11_15), map.getRegion(3));
        assertEquals(createCoverageRegion(13, 15, seq_11_15), map.getRegion(4));
        assertEquals(16, map.getLength());
        assertFalse(map.isEmpty());
    }

    @Test
    public void enteringSequenceStartsAtPreviousRegionsEnd() {
        final DefaultCoverageMap.Builder<Placed> builder = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_12, seq_8_12, seq_10_12, seq_11_15, seq_12_18));
        CoverageMap<CoverageRegion<Placed>> map = builder.build();
        assertEquals(7, map.getNumberOfRegions());
        assertEquals(createCoverageRegion(0, 7, seq_0_12), map.getRegion(0));
        assertEquals(createCoverageRegion(8, 9, seq_0_12, seq_8_12), map.getRegion(1));
        assertEquals(createCoverageRegion(10, 10, seq_0_12, seq_8_12, seq_10_12), map.getRegion(2));
        assertEquals(createCoverageRegion(11, 11, seq_0_12, seq_8_12, seq_10_12, seq_11_15), map.getRegion(3));
        assertEquals(createCoverageRegion(12, 12, seq_0_12, seq_8_12, seq_10_12, seq_11_15, seq_12_18), map.getRegion(4));
        assertEquals(createCoverageRegion(13, 15, seq_11_15, seq_12_18), map.getRegion(5));
        assertEquals(createCoverageRegion(16, 18, seq_12_18), map.getRegion(6));
        assertEquals(19, map.getLength());
        assertFalse(map.isEmpty());
    }

    @Test
    public void threeConsecutiveReadsStartAtSamePoint() {
        final DefaultCoverageMap.Builder<Placed> builder = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_12, seq_11_15, seq_11_18, seq_11_20));
        CoverageMap<CoverageRegion<Placed>> map = builder.build();
        assertEquals(5, map.getNumberOfRegions());
        assertEquals(createCoverageRegion(0, 10, seq_0_12), map.getRegion(0));
        assertEquals(createCoverageRegion(11, 12, seq_0_12, seq_11_15, seq_11_18, seq_11_20), map.getRegion(1));
        assertEquals(createCoverageRegion(13, 15, seq_11_15, seq_11_18, seq_11_20), map.getRegion(2));
        assertEquals(createCoverageRegion(16, 18, seq_11_18, seq_11_20), map.getRegion(3));
        assertEquals(createCoverageRegion(19, 20, seq_11_20), map.getRegion(4));
        assertEquals(21, map.getLength());
        assertFalse(map.isEmpty());
    }

    @Test
    public void equalsSameRef() {
        CoverageMap<CoverageRegion<Placed>> map = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9, seq_5_14)).build();
        TestUtil.assertEqualAndHashcodeSame(map, map);
    }

    @Test
    public void iterator() {
        CoverageMap<CoverageRegion<Placed>> map = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9, seq_5_14)).build();
        Iterator<CoverageRegion<Placed>> iter = map.iterator();
        assertEquals(createCoverageRegion(0, 4, seq_0_9), iter.next());
        assertEquals(createCoverageRegion(5, 9, seq_0_9, seq_5_14), iter.next());
        assertEquals(createCoverageRegion(10, 14, seq_5_14), iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    public void equalsSameVaues() {
        CoverageMap<CoverageRegion<Placed>> map = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9, seq_5_14)).build();
        CoverageMap<CoverageRegion<Placed>> sameValues = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9, seq_5_14)).build();
        TestUtil.assertEqualAndHashcodeSame(map, sameValues);
    }

    @Test
    public void notEqualsNull() {
        CoverageMap<CoverageRegion<Placed>> map = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9, seq_5_14)).build();
        assertFalse(map.equals(null));
    }

    @Test
    public void notEqualsDifferentClass() {
        CoverageMap<CoverageRegion<Placed>> map = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9, seq_5_14)).build();
        assertFalse(map.equals("not a coverage Map"));
    }

    @Test
    public void notEqualsDifferentNumberOfRegions() {
        CoverageMap<CoverageRegion<Placed>> map = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9, seq_5_14)).build();
        CoverageMap<CoverageRegion<Placed>> differentNumberOfRegions = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9)).build();
        TestUtil.assertNotEqualAndHashcodeDifferent(map, differentNumberOfRegions);
    }

    @Test
    public void coverageRegionNotEqual() {
        Placed differentSequence = Range.buildRange(seq_5_14.getStart(), seq_5_14.getEnd() + 1);
        CoverageMap<CoverageRegion<Placed>> map = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9, seq_5_14)).build();
        CoverageMap<CoverageRegion<Placed>> differentRegions = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9, differentSequence)).build();
        TestUtil.assertNotEqualAndHashcodeDifferent(map, differentRegions);
    }

    @Test
    public void getRegionsWithin() {
        CoverageMap<CoverageRegion<Placed>> map = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9, seq_5_14, seq_0_12)).build();
        List<CoverageRegion<Placed>> regions = map.getRegionsWithin(Range.buildRange(5, 12));
        assertEquals(2, regions.size());
        assertEquals(createCoverageRegion(5, 9, seq_0_9, seq_0_12, seq_5_14), regions.get(0));
        assertEquals(createCoverageRegion(10, 12, seq_0_12, seq_5_14), regions.get(1));
    }

    @Test
    public void getRegionsWhichIntersect() {
        CoverageMap<CoverageRegion<Placed>> map = new DefaultCoverageMap.Builder<Placed>(Arrays.asList(seq_0_9, seq_5_14, seq_0_12)).build();
        List<CoverageRegion<Placed>> regions = map.getRegionsWhichIntersect(Range.buildRange(6, 11));
        assertEquals(2, regions.size());
        assertEquals(createCoverageRegion(5, 9, seq_0_9, seq_0_12, seq_5_14), regions.get(0));
        assertEquals(createCoverageRegion(10, 12, seq_0_12, seq_5_14), regions.get(1));
    }

    @Test
    public void checkAlternateCoordinateSystemRanges() {
        long residueStart = 1;
        long residueStop = 100;
        Range residueRange = Range.buildRange(CoordinateSystem.RESIDUE_BASED, residueStart, residueStop);
        Range zeroRange = residueRange.convertRange(CoordinateSystem.ZERO_BASED);
        CoverageMap<CoverageRegion<Placed>> map = new DefaultCoverageMap.Builder<Placed>(Arrays.asList((Placed) zeroRange)).build();
        List<CoverageRegion<Placed>> regions = map.getRegions();
        assertEquals(1, regions.size());
        assertEquals(0, regions.get(0).getStart());
        assertEquals(99, regions.get(0).getEnd());
        CoverageMap<CoverageRegion<Placed>> residueMap = new DefaultCoverageMap.Builder<Placed>(Arrays.asList((Placed) residueRange)).build();
        List<CoverageRegion<Placed>> residueRegions = residueMap.getRegions();
        assertEquals(1, residueRegions.size());
        assertEquals(0, residueRegions.get(0).getStart());
        assertEquals(99, residueRegions.get(0).getEnd());
    }
}
