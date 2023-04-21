package org.jcvi.assembly;

import org.jcvi.Range;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

public class TestSectionOfPlacedRead {

    PlacedRead actualRead;

    String sectionId = "sectionRead_id";

    Range leftSectionRange = Range.buildRange(100, 10200);

    Range leftValidRange = Range.buildRange(20, 220);

    Range rightSectionRange = Range.buildRange(10201, 10600);

    Range rightValidRange = Range.buildRange(221, 500);

    SectionOfPlacedRead<PlacedRead> leftSectionSut;

    SectionOfPlacedRead<PlacedRead> rightSectionSut;

    int leftStartOffsetIntoRealRead = 0;

    int rightStartOffsetIntoRealRead = 221;

    @Before
    public void setup() {
        actualRead = createMock(PlacedRead.class);
        leftSectionSut = new SectionOfPlacedRead<PlacedRead>(sectionId, actualRead, leftStartOffsetIntoRealRead, leftSectionRange, leftValidRange);
        rightSectionSut = new SectionOfPlacedRead<PlacedRead>(sectionId, actualRead, rightStartOffsetIntoRealRead, rightSectionRange, rightValidRange);
    }

    @Test
    public void constructor() {
        assertEquals(sectionId, leftSectionSut.getId());
        assertEquals(actualRead, leftSectionSut.getRealPlacedRead());
        assertEquals(leftSectionRange, Range.buildRange(leftSectionSut.getStart(), leftSectionSut.getEnd()));
        assertEquals(leftValidRange, leftSectionSut.getValidRange());
        assertEquals(sectionId, rightSectionSut.getId());
        assertEquals(actualRead, rightSectionSut.getRealPlacedRead());
        assertEquals(rightSectionRange, Range.buildRange(rightSectionSut.getStart(), rightSectionSut.getEnd()));
        assertEquals(rightValidRange, rightSectionSut.getValidRange());
    }

    @Test
    public void getRealReadIndex() {
        for (int i = 0; i < 20; i++) {
            assertEquals(leftStartOffsetIntoRealRead + i, leftSectionSut.getRealIndexOf(i));
            assertEquals(rightStartOffsetIntoRealRead + i, rightSectionSut.getRealIndexOf(i));
        }
    }
}
