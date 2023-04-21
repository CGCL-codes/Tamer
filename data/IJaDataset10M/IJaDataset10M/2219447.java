package org.jcvi.common.core.seq.read.trace.sanger.phd;

import java.util.List;
import java.util.Properties;
import org.jcvi.common.core.seq.read.trace.sanger.phd.ArtificialPhd;
import org.jcvi.common.core.seq.read.trace.sanger.phd.Phd;
import org.jcvi.common.core.seq.read.trace.sanger.phd.PhdTag;
import org.jcvi.common.core.symbol.Sequence;
import org.jcvi.common.core.symbol.ShortSymbol;
import org.jcvi.common.core.symbol.qual.QualitySequence;
import org.jcvi.common.core.symbol.residue.nt.NucleotideSequence;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

public class TestBuildArtificialPhd {

    NucleotideSequence mockBasecalls;

    QualitySequence mockQualities;

    Properties mockProperties;

    List<PhdTag> mockTags;

    String id = "phdId";

    long lengthOfBases = 5;

    int numberOfPositionsForEachPeak = 13;

    @Before
    public void setup() {
        mockBasecalls = createMock(NucleotideSequence.class);
        mockQualities = createMock(QualitySequence.class);
        mockProperties = createMock(Properties.class);
        mockTags = createMock(List.class);
    }

    @Test
    public void noPropertiesAndTagsConstructor() {
        expect(mockBasecalls.getLength()).andReturn(lengthOfBases);
        replay(mockBasecalls, mockQualities);
        Phd phd = new ArtificialPhd(id, mockBasecalls, mockQualities, numberOfPositionsForEachPeak);
        assertEquals(id, phd.getId());
        assertEquals(mockBasecalls, phd.getNucleotideSequence());
        assertEquals(mockQualities, phd.getQualities());
        Sequence<ShortSymbol> actualPeaks = phd.getPeaks().getData();
        for (int i = 0; i < lengthOfBases; i++) {
            assertEquals(Short.valueOf((short) (i * numberOfPositionsForEachPeak + numberOfPositionsForEachPeak)), actualPeaks.get(i).getValue());
        }
        assertCommentsAndTagsAreEmpty(phd);
        verify(mockBasecalls, mockQualities);
    }

    private void assertCommentsAndTagsAreEmpty(Phd phd) {
        assertTrue(phd.getComments().isEmpty());
        assertTrue(phd.getTags().isEmpty());
    }

    @Test
    public void withProperties() {
        expect(mockBasecalls.getLength()).andReturn(lengthOfBases);
        replay(mockBasecalls, mockQualities, mockProperties, mockTags);
        Phd phd = new ArtificialPhd(id, mockBasecalls, mockQualities, mockProperties, mockTags, numberOfPositionsForEachPeak);
        assertEquals(id, phd.getId());
        assertEquals(mockBasecalls, phd.getNucleotideSequence());
        assertEquals(mockQualities, phd.getQualities());
        Sequence<ShortSymbol> actualPeaks = phd.getPeaks().getData();
        for (int i = 0; i < lengthOfBases; i++) {
            assertEquals(Short.valueOf((short) (i * numberOfPositionsForEachPeak + numberOfPositionsForEachPeak)), actualPeaks.get(i).getValue());
        }
        assertEquals(mockProperties, phd.getComments());
        assertEquals(mockTags, phd.getTags());
        verify(mockBasecalls, mockQualities, mockProperties, mockTags);
    }
}
