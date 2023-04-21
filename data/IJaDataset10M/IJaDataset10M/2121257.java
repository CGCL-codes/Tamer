package org.jcvi.autoTasker.featureFinder.scaffold;

import java.util.Collections;
import org.jcvi.autoTasker.CodingRegionedScaffold;
import org.jcvi.autoTasker.DefaultCodingRegionScaffold;
import org.jcvi.autoTasker.feature.CompositeFeature;
import org.jcvi.autoTasker.feature.DefaultCompositeFeature;
import org.jcvi.autoTasker.feature.DefaultScaffoldFeature;
import org.jcvi.autoTasker.feature.CommonFeatureTypes;
import org.jcvi.autoTasker.featureFinder.FeatureFinderListener;
import org.jcvi.common.core.Range;
import org.jcvi.common.core.assembly.DefaultScaffold;
import org.jcvi.common.core.assembly.Scaffold;
import org.junit.Before;
import org.junit.Test;
import static org.easymock.EasyMock.*;

public class TestCodingRegionFeatureFinder {

    FeatureFinderListener mockListener;

    CodingRegionFeatureFinder sut;

    private final String scaffoldId = "scaffoldId";

    @Before
    public void setup() {
        mockListener = createMock(FeatureFinderListener.class);
        sut = new CodingRegionFeatureFinder(Collections.singleton(mockListener));
    }

    @Test
    public void fullyCoveredCdsShouldNotFindAnyFeatures() {
        Range codingRegion = Range.create(25, 75);
        Scaffold scaffold = DefaultScaffold.createBuilder(scaffoldId).add("contig1", Range.create(0, 100)).build();
        CodingRegionedScaffold cdsScaffold = new DefaultCodingRegionScaffold(scaffoldId, scaffold, codingRegion);
        replay(mockListener);
        sut.findFeaturesFor(cdsScaffold);
        verify(mockListener);
    }

    @Test
    public void missingStartCdsShouldFindMissingStartAnd0xFeatures() {
        Range codingRegion = Range.create(25, 75);
        Scaffold scaffold = DefaultScaffold.createBuilder(scaffoldId).add("contig1", Range.create(30, 100)).build();
        CodingRegionedScaffold cdsScaffold = new DefaultCodingRegionScaffold(scaffoldId, scaffold, codingRegion);
        CompositeFeature expected = createMissingCdsStartFeature(scaffoldId, Range.create(25, 29));
        mockListener.foundFeature(expected);
        mockListener.foundFeature(create0xFeature(scaffoldId, Range.create(25, 29)));
        replay(mockListener);
        sut.findFeaturesFor(cdsScaffold);
        verify(mockListener);
    }

    @Test
    public void missingEndCdsShouldFindMissingCDSEndAnd0xFeatures() {
        Range codingRegion = Range.create(25, 75);
        Scaffold scaffold = DefaultScaffold.createBuilder(scaffoldId).add("contig1", Range.create(25, 70)).build();
        CodingRegionedScaffold cdsScaffold = new DefaultCodingRegionScaffold(scaffoldId, scaffold, codingRegion);
        CompositeFeature expected = createMissingCdsStopFeature(scaffoldId, Range.create(71, 75));
        mockListener.foundFeature(expected);
        mockListener.foundFeature(create0xFeature(scaffoldId, Range.create(71, 75)));
        replay(mockListener);
        sut.findFeaturesFor(cdsScaffold);
        verify(mockListener);
    }

    @Test
    public void missingMiddleOfCdsShouldFindOneZeroCoverageFeature() {
        Range codingRegion = Range.create(25, 75);
        Scaffold scaffold = DefaultScaffold.createBuilder(scaffoldId).add("contig1", Range.create(25, 30)).add("contig1", Range.create(35, 75)).build();
        CodingRegionedScaffold cdsScaffold = new DefaultCodingRegionScaffold(scaffoldId, scaffold, codingRegion);
        CompositeFeature expected = create0xFeature(scaffoldId, Range.create(31, 34));
        mockListener.foundFeature(expected);
        replay(mockListener);
        sut.findFeaturesFor(cdsScaffold);
        verify(mockListener);
    }

    @Test
    public void missingLastBaseOfCdsShouldFindMissingCDSEndAnd0xFeatures() {
        Range codingRegion = Range.create(25, 75);
        Scaffold scaffold = DefaultScaffold.createBuilder(scaffoldId).add("contig1", Range.create(25, 74)).build();
        CodingRegionedScaffold cdsScaffold = new DefaultCodingRegionScaffold(scaffoldId, scaffold, codingRegion);
        CompositeFeature expected = createMissingCdsStopFeature(scaffoldId, Range.create(75));
        mockListener.foundFeature(expected);
        mockListener.foundFeature(create0xFeature(scaffoldId, Range.create(75)));
        replay(mockListener);
        sut.findFeaturesFor(cdsScaffold);
        verify(mockListener);
    }

    @Test
    public void missingStartStopAndHasZeroCoverageRegionShouldFind5Features() {
        Range codingRegion = Range.create(25, 75);
        Scaffold scaffold = DefaultScaffold.createBuilder(scaffoldId).add("contig1", Range.create(30, 40)).add("contig2", Range.create(45, 70)).build();
        CodingRegionedScaffold cdsScaffold = new DefaultCodingRegionScaffold(scaffoldId, scaffold, codingRegion);
        CompositeFeature expectedStartFeature = createMissingCdsStartFeature(scaffoldId, Range.create(25, 29));
        CompositeFeature expectedEndFeature = createMissingCdsStopFeature(scaffoldId, Range.create(71, 75));
        mockListener.foundFeature(expectedStartFeature);
        mockListener.foundFeature(create0xFeature(scaffoldId, Range.create(25, 29)));
        mockListener.foundFeature(create0xFeature(scaffoldId, Range.create(41, 44)));
        mockListener.foundFeature(expectedEndFeature);
        mockListener.foundFeature(create0xFeature(scaffoldId, Range.create(71, 75)));
        replay(mockListener);
        sut.findFeaturesFor(cdsScaffold);
        verify(mockListener);
    }

    @Test
    public void missingStartAndEndOfCdsShouldFind4Features() {
        Range codingRegion = Range.create(25, 75);
        Scaffold scaffold = DefaultScaffold.createBuilder(scaffoldId).add("contig1", Range.create(30, 70)).build();
        CodingRegionedScaffold cdsScaffold = new DefaultCodingRegionScaffold(scaffoldId, scaffold, codingRegion);
        CompositeFeature expectedStartFeature = createMissingCdsStartFeature(scaffoldId, Range.create(25, 29));
        CompositeFeature expectedEndFeature = createMissingCdsStopFeature(scaffoldId, Range.create(71, 75));
        mockListener.foundFeature(expectedStartFeature);
        mockListener.foundFeature(create0xFeature(scaffoldId, Range.create(25, 29)));
        mockListener.foundFeature(expectedEndFeature);
        mockListener.foundFeature(create0xFeature(scaffoldId, Range.create(71, 75)));
        replay(mockListener);
        sut.findFeaturesFor(cdsScaffold);
        verify(mockListener);
    }

    private CompositeFeature createMissingCdsStartFeature(String scaffoldId, Range range) {
        return new DefaultCompositeFeature.Builder(new DefaultScaffoldFeature(CommonFeatureTypes.MISSING_CDS_START, scaffoldId, range)).build();
    }

    private CompositeFeature createMissingCdsStopFeature(String scaffoldId, Range range) {
        return new DefaultCompositeFeature.Builder(new DefaultScaffoldFeature(CommonFeatureTypes.MISSING_CDS_STOP, scaffoldId, range)).build();
    }

    private CompositeFeature create0xFeature(String scaffoldId, Range range) {
        return new DefaultCompositeFeature.Builder(new DefaultScaffoldFeature(CommonFeatureTypes.ZEROCOVERAGE, scaffoldId, range)).build();
    }
}
