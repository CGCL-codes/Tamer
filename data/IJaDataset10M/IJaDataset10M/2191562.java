package org.jcvi.tasker.analyzers;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jcvi.Range;
import org.jcvi.assembly.PlacedRead;
import org.jcvi.assembly.coverage.CoverageMap;
import org.jcvi.assembly.coverage.CoverageRegion;
import org.jcvi.assembly.coverage.DefaultCoverageMap;
import org.jcvi.sequence.SequenceDirection;
import org.jcvi.tasker.BasicFeatureTypeLookup;
import org.jcvi.tasker.Tasker;
import org.junit.Before;
import org.junit.Test;

public class TestUniDirectionalAnalyzer {

    private Tasker tasker;

    private Logger logger;

    UniDirectionalReadCoverageAnalyzer sut;

    String contigId = "contigId";

    BasicFeatureTypeLookup typeLookup = new BasicFeatureTypeLookup();

    private final int minCoverage = 5;

    private final Range codingRegion = Range.buildRange(5, 15);

    @Before
    public void setup() {
        tasker = createMock(Tasker.class);
        logger = createMock(Logger.class);
        expect(tasker.getLogger()).andReturn(logger);
        expect(tasker.getFeatureTypeLookup()).andStubReturn(typeLookup);
        replay(tasker, logger);
        sut = new UniDirectionalReadCoverageAnalyzer(tasker, 0, minCoverage);
    }

    @Test
    public void noUniDirCoverageRegionsShouldReturnEmptyList() {
        List<CoverageRegion<PlacedRead>> regions = new ArrayList<CoverageRegion<PlacedRead>>();
        regions.add(createMockCoverageRegion(0, 20, minCoverage, minCoverage));
        for (CoverageRegion<PlacedRead> mockRegion : regions) {
            replay(mockRegion);
        }
        CoverageMap<CoverageRegion<PlacedRead>> coverageMap = new DefaultCoverageMap<PlacedRead, CoverageRegion<PlacedRead>>(regions);
        List<Range> taskRanges = sut.analyze(null, codingRegion, coverageMap);
        assertTrue(taskRanges.isEmpty());
        for (CoverageRegion<PlacedRead> mockRegion : regions) {
            verify(mockRegion);
        }
    }

    @Test
    public void highUniDirCoverageRegionsShouldReturnEmptyList() {
        List<CoverageRegion<PlacedRead>> regions = new ArrayList<CoverageRegion<PlacedRead>>();
        regions.add(createMockCoverageRegion(0, 20, minCoverage, 0));
        for (CoverageRegion<PlacedRead> mockRegion : regions) {
            replay(mockRegion);
        }
        CoverageMap<CoverageRegion<PlacedRead>> coverageMap = new DefaultCoverageMap<PlacedRead, CoverageRegion<PlacedRead>>(regions);
        List<Range> taskRanges = sut.analyze(null, codingRegion, coverageMap);
        assertTrue(taskRanges.isEmpty());
        for (CoverageRegion<PlacedRead> mockRegion : regions) {
            verify(mockRegion);
        }
    }

    @Test
    public void noUniDirCoverageRegionsInsideCodingRegionShouldReturnEmptyList() {
        List<CoverageRegion<PlacedRead>> regions = new ArrayList<CoverageRegion<PlacedRead>>();
        regions.add(createMockCoverageRegion(0, codingRegion.getStart() - 1, minCoverage, 0));
        regions.add(createMockCoverageRegion(codingRegion.getStart(), codingRegion.getEnd(), minCoverage - 1, 1));
        regions.add(createMockCoverageRegion(codingRegion.getEnd() + 1, codingRegion.getEnd() + 5, minCoverage - 1, 0));
        for (CoverageRegion<PlacedRead> mockRegion : regions) {
            replay(mockRegion);
        }
        CoverageMap<CoverageRegion<PlacedRead>> coverageMap = new DefaultCoverageMap<PlacedRead, CoverageRegion<PlacedRead>>(regions);
        List<Range> taskRanges = sut.analyze(null, codingRegion, coverageMap);
        assertTrue(taskRanges.isEmpty());
    }

    @Test
    public void zeroCoverageRegionShouldBeIgnored() {
        List<CoverageRegion<PlacedRead>> regions = new ArrayList<CoverageRegion<PlacedRead>>();
        regions.add(createMockCoverageRegion(0, codingRegion.getStart() - 1, minCoverage, 0));
        regions.add(createMockCoverageRegion(codingRegion.getStart(), codingRegion.getEnd(), 0, 0));
        regions.add(createMockCoverageRegion(codingRegion.getEnd() + 1, codingRegion.getEnd() + 5, minCoverage - 1, 0));
        for (CoverageRegion<PlacedRead> mockRegion : regions) {
            replay(mockRegion);
        }
        CoverageMap<CoverageRegion<PlacedRead>> coverageMap = new DefaultCoverageMap<PlacedRead, CoverageRegion<PlacedRead>>(regions);
        List<Range> taskRanges = sut.analyze(null, codingRegion, coverageMap);
        assertTrue(taskRanges.isEmpty());
        for (CoverageRegion<PlacedRead> mockRegion : regions) {
            verify(mockRegion);
        }
    }

    @Test
    public void lowCoverageRegionsInsideCodingRegionShouldReturnList() {
        List<CoverageRegion<PlacedRead>> regions = new ArrayList<CoverageRegion<PlacedRead>>();
        regions.add(createMockCoverageRegion(codingRegion.getStart(), codingRegion.getStart() + 5, minCoverage - 1, 0));
        regions.add(createMockCoverageRegion(codingRegion.getStart() + 6, codingRegion.getEnd() - 6, minCoverage - 1, 1));
        regions.add(createMockCoverageRegion(codingRegion.getEnd() - 5, codingRegion.getEnd(), 0, minCoverage - 1));
        for (CoverageRegion<PlacedRead> mockRegion : regions) {
            replay(mockRegion);
        }
        CoverageMap<CoverageRegion<PlacedRead>> coverageMap = new DefaultCoverageMap<PlacedRead, CoverageRegion<PlacedRead>>(regions);
        List<Range> taskRanges = sut.analyze(null, codingRegion, coverageMap);
        assertEquals(2, taskRanges.size());
        assertTrue(taskRanges.contains(Range.buildRange(codingRegion.getStart(), codingRegion.getStart() + 5)));
        assertTrue(taskRanges.contains(Range.buildRange(codingRegion.getEnd() - 5, codingRegion.getEnd())));
        for (CoverageRegion<PlacedRead> mockRegion : regions) {
            verify(mockRegion);
        }
    }

    private CoverageRegion<PlacedRead> createMockCoverageRegion(long start, long end, int numForward, int numReverse) {
        CoverageRegion<PlacedRead> region = createMock(CoverageRegion.class);
        expect(region.getCoverage()).andStubReturn(numForward + numReverse);
        expect(region.getStart()).andStubReturn(start);
        expect(region.getEnd()).andStubReturn(end);
        List<PlacedRead> reads = new ArrayList<PlacedRead>();
        for (int i = 0; i < numForward; i++) {
            reads.add(createMockPlacedRead(SequenceDirection.FORWARD));
        }
        for (int i = 0; i < numReverse; i++) {
            reads.add(createMockPlacedRead(SequenceDirection.FORWARD));
        }
        expect(region.getElements()).andStubReturn(reads);
        return region;
    }

    private PlacedRead createMockPlacedRead(SequenceDirection dir) {
        PlacedRead mockRead = createMock(PlacedRead.class);
        expect(mockRead.getSequenceDirection()).andStubReturn(dir);
        replay(mockRead);
        return mockRead;
    }
}
