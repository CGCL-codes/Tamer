package org.jcvi.assembly.ace;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.jcvi.assembly.slice.DefaultSliceMap;
import org.jcvi.assembly.slice.Slice;
import org.jcvi.assembly.slice.SliceMap;
import org.jcvi.assembly.slice.TestSliceUtil;
import org.jcvi.common.util.Range;
import org.jcvi.glyph.nuc.DefaultNucleotideEncodedGlyphs;
import org.jcvi.glyph.nuc.NucleotideEncodedGlyphs;
import org.jcvi.sequence.SequenceDirection;
import org.junit.Test;

public abstract class AbstractTestAceBestSegmentMap {

    private final NucleotideEncodedGlyphs consensus = new DefaultNucleotideEncodedGlyphs("ACGT");

    AceBestSegmentMap sut;

    SliceMap sliceMap;

    protected abstract AceBestSegmentMap createSut(SliceMap sliceMap, NucleotideEncodedGlyphs consensus);

    @Test(expected = NullPointerException.class)
    public void nullSliceMapShouldThrowNPE() {
        new DefaultAceBestSegmentMap(null, consensus);
    }

    @Test(expected = NullPointerException.class)
    public void nullConsensusShouldThrowNPE() {
        new DefaultAceBestSegmentMap(createMock(SliceMap.class), null);
    }

    @Test
    public void oneReadProvidesEntireBestSegment() {
        List<Slice> slices = TestSliceUtil.createSlicesFrom(Arrays.asList("ACGT"), new byte[][] { new byte[] { 30, 30, 30, 30 } }, Arrays.asList(SequenceDirection.FORWARD));
        SliceMap sliceMap = new DefaultSliceMap(slices);
        sut = createSut(sliceMap, consensus);
        assertEquals(1, sut.getNumberOfBestSegments());
        DefaultAceBestSegment expectedSegment = new DefaultAceBestSegment("read_0", Range.buildRange(0, 3));
        assertEquals(expectedSegment, sut.getBestSegmentFor(0));
        assertEquals(expectedSegment, sut.getBestSegmentFor(1));
        assertEquals(expectedSegment, sut.getBestSegmentFor(2));
        assertEquals(expectedSegment, sut.getBestSegmentFor(3));
        Iterator<AceBestSegment> actualIter = sut.iterator();
        assertEquals(expectedSegment, actualIter.next());
        assertFalse(actualIter.hasNext());
    }

    @Test
    public void twoBestSegments() {
        List<Slice> slices = TestSliceUtil.createSlicesFrom(Arrays.asList("ACGN", "NNNT"), new byte[][] { new byte[] { 30, 30, 30, 30 }, new byte[] { 30, 30, 30, 30 } }, Arrays.asList(SequenceDirection.FORWARD, SequenceDirection.FORWARD));
        SliceMap sliceMap = new DefaultSliceMap(slices);
        sut = createSut(sliceMap, consensus);
        assertEquals(2, sut.getNumberOfBestSegments());
        DefaultAceBestSegment expectedSegment1 = new DefaultAceBestSegment("read_0", Range.buildRange(0, 2));
        DefaultAceBestSegment expectedSegment2 = new DefaultAceBestSegment("read_1", Range.buildRange(3, 3));
        assertEquals(expectedSegment1, sut.getBestSegmentFor(0));
        assertEquals(expectedSegment1, sut.getBestSegmentFor(1));
        assertEquals(expectedSegment1, sut.getBestSegmentFor(2));
        assertEquals(expectedSegment2, sut.getBestSegmentFor(3));
        Iterator<AceBestSegment> actualIter = sut.iterator();
        assertEquals(expectedSegment1, actualIter.next());
        assertEquals(expectedSegment2, actualIter.next());
        assertFalse(actualIter.hasNext());
    }

    @Test
    public void threeBestSegments() {
        List<Slice> slices = TestSliceUtil.createSlicesFrom(Arrays.asList("ACNT", "NNGN"), new byte[][] { new byte[] { 30, 30, 30, 30 }, new byte[] { 30, 30, 30, 30 } }, Arrays.asList(SequenceDirection.FORWARD, SequenceDirection.FORWARD));
        SliceMap sliceMap = new DefaultSliceMap(slices);
        sut = createSut(sliceMap, consensus);
        assertEquals(3, sut.getNumberOfBestSegments());
        DefaultAceBestSegment expectedSegment1 = new DefaultAceBestSegment("read_0", Range.buildRange(0, 1));
        DefaultAceBestSegment expectedSegment2 = new DefaultAceBestSegment("read_1", Range.buildRange(2, 2));
        DefaultAceBestSegment expectedSegment3 = new DefaultAceBestSegment("read_0", Range.buildRange(3, 3));
        assertEquals(expectedSegment1, sut.getBestSegmentFor(0));
        assertEquals(expectedSegment1, sut.getBestSegmentFor(1));
        assertEquals(expectedSegment2, sut.getBestSegmentFor(2));
        assertEquals(expectedSegment3, sut.getBestSegmentFor(3));
        Iterator<AceBestSegment> actualIter = sut.iterator();
        assertEquals(expectedSegment1, actualIter.next());
        assertEquals(expectedSegment2, actualIter.next());
        assertEquals(expectedSegment3, actualIter.next());
        assertFalse(actualIter.hasNext());
    }

    @Test
    public void keepExtendingBestSegments() {
        List<Slice> slices = TestSliceUtil.createSlicesFrom(Arrays.asList("ACNT", "NNGT"), new byte[][] { new byte[] { 30, 30, 30, 30 }, new byte[] { 30, 30, 30, 30 } }, Arrays.asList(SequenceDirection.FORWARD, SequenceDirection.FORWARD));
        SliceMap sliceMap = new DefaultSliceMap(slices);
        sut = createSut(sliceMap, consensus);
        assertEquals(2, sut.getNumberOfBestSegments());
        DefaultAceBestSegment expectedSegment1 = new DefaultAceBestSegment("read_0", Range.buildRange(0, 1));
        DefaultAceBestSegment expectedSegment2 = new DefaultAceBestSegment("read_1", Range.buildRange(2, 3));
        assertEquals(expectedSegment1, sut.getBestSegmentFor(0));
        assertEquals(expectedSegment1, sut.getBestSegmentFor(1));
        assertEquals(expectedSegment2, sut.getBestSegmentFor(2));
        assertEquals(expectedSegment2, sut.getBestSegmentFor(3));
        Iterator<AceBestSegment> actualIter = sut.iterator();
        assertEquals(expectedSegment1, actualIter.next());
        assertEquals(expectedSegment2, actualIter.next());
        assertFalse(actualIter.hasNext());
    }
}
