package org.jcvi.tasker.analyzers;

import static org.easymock.EasyMock.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.apache.log4j.Logger;
import org.jcvi.common.core.Range;
import org.jcvi.glk.task.TaskFeature;
import org.jcvi.tasker.AlignedContig;
import org.jcvi.tasker.BasicFeatureTypeLookup;
import org.jcvi.tasker.Reference;
import org.jcvi.tasker.ReferenceAlignment;
import org.jcvi.tasker.Tasker;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestAbstractContigAnalyzer {

    private static final class AbstractContigAnalyzerTestDouble extends AbstractContigAnalyzer {

        public AbstractContigAnalyzerTestDouble(Tasker tasker) {
            super(tasker);
        }

        @Override
        public Set<TaskFeature> analyze(Collection<AlignedContig> contigs, Reference reference) {
            return null;
        }
    }

    private AbstractContigAnalyzerTestDouble sut;

    private Tasker tasker;

    private Logger logger;

    BasicFeatureTypeLookup typeLookup = new BasicFeatureTypeLookup();

    @Before
    public void setup() {
        tasker = createMock(Tasker.class);
        logger = createMock(Logger.class);
        expect(tasker.getLogger()).andReturn(logger);
        expect(tasker.getFeatureTypeLookup()).andStubReturn(typeLookup);
        replay(tasker, logger);
        sut = new AbstractContigAnalyzerTestDouble(tasker);
    }

    @Test
    public void getLogger() {
        assertEquals(logger, sut.getLogger());
    }

    @Test
    public void getTypeLookup() {
        assertEquals(typeLookup, sut.getTypeLookup());
    }

    @Test
    public void getBestContigCoveringNoContigsShouldReturnNull() {
        Range featureRange = Range.create(5, 15);
        assertNull(sut.getBestContigCovering(featureRange, Collections.<AlignedContig>emptyList()));
    }

    @Test
    public void getBestContigCoveringOneContigDoesNotCoverShouldReturnNull() {
        Range featureRange = Range.create(5, 15);
        AlignedContig contig = createMock(AlignedContig.class);
        ReferenceAlignment alignment = createMock(ReferenceAlignment.class);
        expect(contig.getAlignment()).andReturn(alignment);
        expect(alignment.getReferenceRange()).andReturn(Range.create(20, 30));
        replay(contig, alignment);
        assertNull(sut.getBestContigCovering(featureRange, Arrays.asList(contig)));
        verify(contig, alignment);
    }

    @Test
    public void getBestContigCoveringOneContigThatCoversMustBeBest() {
        Range featureRange = Range.create(5, 15);
        AlignedContig bestContig = createMock(AlignedContig.class);
        ReferenceAlignment bestRefAlignment = createMock(ReferenceAlignment.class);
        expect(bestContig.getAlignment()).andReturn(bestRefAlignment);
        expect(bestRefAlignment.getReferenceRange()).andReturn(featureRange);
        replay(bestContig, bestRefAlignment);
        assertEquals(bestContig, sut.getBestContigCovering(featureRange, Arrays.asList(bestContig)));
        verify(bestContig, bestRefAlignment);
    }

    @Test
    public void getBestContigCoveringTwoContigsThatCoversShouldReturnLargestIntersection() {
        Range featureRange = Range.create(5, 15);
        AlignedContig bestContig = createMock(AlignedContig.class);
        ReferenceAlignment bestRefAlignment = createMock(ReferenceAlignment.class);
        expect(bestContig.getAlignment()).andReturn(bestRefAlignment);
        expect(bestRefAlignment.getReferenceRange()).andReturn(featureRange);
        AlignedContig otherContig = createMock(AlignedContig.class);
        ReferenceAlignment otherAlignment = createMock(ReferenceAlignment.class);
        expect(otherContig.getAlignment()).andReturn(otherAlignment);
        expect(otherAlignment.getReferenceRange()).andReturn(Range.create(5, 10));
        replay(bestContig, bestRefAlignment, otherContig, otherAlignment);
        assertEquals(bestContig, sut.getBestContigCovering(featureRange, Arrays.asList(otherContig, bestContig)));
        verify(bestContig, bestRefAlignment, otherContig, otherAlignment);
    }

    @Test
    public void getBestReferenceEmptyListShouldReturnNull() {
        assertNull(sut.getBestReference(Collections.<AlignedContig>emptyList()));
    }

    @Test
    public void getBestReferenceOneContigShouldHaveBestReference() {
        Range referenceRange = Range.create(5, 15);
        AlignedContig contig = createMock(AlignedContig.class);
        ReferenceAlignment alignment = createMock(ReferenceAlignment.class);
        Reference reference = createMock(Reference.class);
        expect(contig.getReference()).andReturn(reference);
        expect(contig.getAlignment()).andReturn(alignment);
        expect(alignment.getReferenceRange()).andReturn(referenceRange);
        replay(contig, alignment, reference);
        assertEquals(reference, sut.getBestReference(Arrays.asList(contig)));
        verify(contig, alignment, reference);
    }

    @Test
    public void getBestReferenceTwoContigsShouldReturnRefWithMostBasesCovered() {
        Range referenceRange = Range.create(5, 15);
        AlignedContig contig = createMock(AlignedContig.class);
        ReferenceAlignment alignment = createMock(ReferenceAlignment.class);
        Reference bestReference = createMock(Reference.class);
        expect(contig.getReference()).andReturn(bestReference);
        expect(contig.getAlignment()).andReturn(alignment);
        expect(alignment.getReferenceRange()).andReturn(referenceRange);
        AlignedContig contig2 = createMock(AlignedContig.class);
        ReferenceAlignment alignment2 = createMock(ReferenceAlignment.class);
        Reference reference2 = createMock(Reference.class);
        expect(contig2.getReference()).andReturn(reference2);
        expect(contig2.getAlignment()).andReturn(alignment2);
        expect(alignment2.getReferenceRange()).andReturn(referenceRange.shrink(2, 2));
        replay(contig, alignment, bestReference, contig2, alignment2, reference2);
        assertEquals(bestReference, sut.getBestReference(Arrays.asList(contig2, contig)));
        verify(contig, alignment, bestReference, contig2, alignment2, reference2);
    }

    @Test
    public void getBestReferenceThreeContigs2SmallSameRefOneLargeDiffRefShouldReturnRefWithMostBasesCovered() {
        AlignedContig contig = createMock(AlignedContig.class);
        ReferenceAlignment alignment = createMock(ReferenceAlignment.class);
        Reference reference = createMock(Reference.class);
        expect(contig.getReference()).andReturn(reference);
        expect(contig.getAlignment()).andReturn(alignment);
        expect(alignment.getReferenceRange()).andReturn(Range.create(5, 50));
        AlignedContig contig2 = createMock(AlignedContig.class);
        ReferenceAlignment alignment2 = createMock(ReferenceAlignment.class);
        Reference reference2 = createMock(Reference.class);
        expect(contig2.getReference()).andReturn(reference2);
        expect(contig2.getAlignment()).andReturn(alignment2);
        expect(alignment2.getReferenceRange()).andReturn(Range.create(5, 30));
        AlignedContig contig3 = createMock(AlignedContig.class);
        ReferenceAlignment alignment3 = createMock(ReferenceAlignment.class);
        expect(contig3.getReference()).andReturn(reference2);
        expect(contig3.getAlignment()).andReturn(alignment3);
        expect(alignment3.getReferenceRange()).andReturn(Range.create(30, 60));
        replay(contig, alignment, reference, contig2, alignment2, reference2, contig3, alignment3);
        assertEquals(reference2, sut.getBestReference(Arrays.asList(contig2, contig, contig3)));
        verify(contig, alignment, reference, contig2, alignment2, reference2, contig3, alignment3);
    }

    @Test
    public void getBestReferenceThreeContigs2SmallSameRefOneLargeDiffRefShouldReturnRefWithMostBasesCovered2() {
        AlignedContig contig = createMock(AlignedContig.class);
        ReferenceAlignment alignment = createMock(ReferenceAlignment.class);
        Reference reference = createMock(Reference.class);
        expect(contig.getReference()).andReturn(reference);
        expect(contig.getAlignment()).andReturn(alignment);
        expect(alignment.getReferenceRange()).andReturn(Range.create(5, 50));
        AlignedContig contig2 = createMock(AlignedContig.class);
        ReferenceAlignment alignment2 = createMock(ReferenceAlignment.class);
        Reference reference2 = createMock(Reference.class);
        expect(contig2.getReference()).andReturn(reference2);
        expect(contig2.getAlignment()).andReturn(alignment2);
        expect(alignment2.getReferenceRange()).andReturn(Range.create(5, 20));
        AlignedContig contig3 = createMock(AlignedContig.class);
        ReferenceAlignment alignment3 = createMock(ReferenceAlignment.class);
        expect(contig3.getReference()).andReturn(reference2);
        expect(contig3.getAlignment()).andReturn(alignment3);
        expect(alignment3.getReferenceRange()).andReturn(Range.create(30, 40));
        replay(contig, alignment, reference, contig2, alignment2, reference2, contig3, alignment3);
        assertEquals(reference, sut.getBestReference(Arrays.asList(contig2, contig, contig3)));
        verify(contig, alignment, reference, contig2, alignment2, reference2, contig3, alignment3);
    }
}
