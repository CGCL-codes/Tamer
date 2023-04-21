package org.jcvi.common.core.seq.read.trace.pyro.sff;

import java.util.Arrays;
import org.jcvi.common.core.seq.read.trace.pyro.sff.DefaultSffReadData;
import org.jcvi.common.core.symbol.residue.nt.NucleotideSequence;
import org.jcvi.common.core.symbol.residue.nt.NucleotideSequenceBuilder;
import org.jcvi.common.core.testUtil.TestUtil;
import org.jcvi.common.core.util.MathUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestDefaultReadData {

    NucleotideSequence basecalls = new NucleotideSequenceBuilder("ACGTACGT").build();

    byte[] qualities = new byte[] { 20, 30, 40, 20, 30, 40, 50, 20 };

    short[] flowgramValues = new short[] { 100, 97, 110, 80, 120, 101, 100, 93 };

    byte[] indexes = new byte[] { 1, 1, 1, 1, 1, 1, 1, 1 };

    DefaultSffReadData sut = new DefaultSffReadData(basecalls, indexes, flowgramValues, qualities);

    @Test
    public void constructor() {
        assertEquals(basecalls, sut.getBasecalls());
        assertTrue(Arrays.equals(qualities, sut.getQualities()));
        assertTrue(Arrays.equals(indexes, sut.getFlowIndexPerBase()));
        assertTrue(Arrays.equals(flowgramValues, sut.getFlowgramValues()));
    }

    @Test
    public void equalsSameRef() {
        TestUtil.assertEqualAndHashcodeSame(sut, sut);
    }

    @Test
    public void notEqualsNull() {
        assertFalse(sut.equals(null));
    }

    @Test
    public void notEqualsWrongClass() {
        assertFalse(sut.equals("not a DefaultSFFReadData"));
    }

    @Test
    public void equalsSameValues() {
        DefaultSffReadData sameValues = new DefaultSffReadData(basecalls, indexes, flowgramValues, qualities);
        TestUtil.assertEqualAndHashcodeSame(sut, sameValues);
    }

    @Test
    public void constructorFailsNullBasesShouldThrowNullPointerException() {
        try {
            new DefaultSffReadData(null, indexes, flowgramValues, qualities);
            fail("should throw NullPointerException when bases is null");
        } catch (NullPointerException expected) {
            assertEquals("basecalls can not be null", expected.getMessage());
        }
    }

    @Test
    public void constructorFailsDifferentBasesShouldThrowIllegalArgumentException() {
        try {
            new DefaultSffReadData(new NucleotideSequenceBuilder(basecalls).append("A").build(), indexes, flowgramValues, qualities);
            fail("should throw IllegalArgumentException when bases length is different");
        } catch (IllegalArgumentException expected) {
            assertEquals("basecalls, indexes and qualities must be the same length", expected.getMessage());
        }
    }

    @Test
    public void constructorFailsNullIndexesShouldThrowNullPointerException() {
        try {
            new DefaultSffReadData(basecalls, null, flowgramValues, qualities);
            fail("should throw NullPointerException when indexes is null");
        } catch (NullPointerException expected) {
            assertEquals("indexes can not be null", expected.getMessage());
        }
    }

    @Test
    public void constructorFailsDifferentIndexesShouldThrowIllegalArgumentException() {
        try {
            new DefaultSffReadData(basecalls, new byte[] { 0, 0, 0, 0, 0, 0 }, flowgramValues, qualities);
            fail("should throw IllegalArgumentException when indexes length is different");
        } catch (IllegalArgumentException expected) {
            assertEquals("basecalls, indexes and qualities must be the same length", expected.getMessage());
        }
    }

    @Test
    public void constructorFailsNullFlowgramValuesShouldNullPointerException() {
        try {
            new DefaultSffReadData(basecalls, indexes, null, qualities);
            fail("should throw NullPointerException when values is null");
        } catch (NullPointerException expected) {
            assertEquals("flowgram values can not be null", expected.getMessage());
        }
    }

    @Test
    public void constructorFailsDifferentFlowgramValuesShouldThrowArrayIndexOutOfBoundsException() {
        final short[] differentValues = new short[] { 1, 2, 3, 4, 500, 1200 };
        try {
            new DefaultSffReadData(basecalls, indexes, differentValues, qualities);
            fail("should throw ArrayIndexOutOfBoundsException when values length is less than last indexed index");
        } catch (ArrayIndexOutOfBoundsException expected) {
            String expectedMessage = "indexed flowgram value refers to " + MathUtil.sumOf(indexes) + "flowgram value length is" + differentValues.length;
            assertEquals(expectedMessage, expected.getMessage());
        }
    }

    @Test
    public void constructorFailsNullQualitiesShouldThrowNullPointerException() {
        try {
            new DefaultSffReadData(basecalls, indexes, flowgramValues, null);
            fail("should throw NullPointerException when qualities is null");
        } catch (NullPointerException expected) {
            assertEquals("qualities can not be null", expected.getMessage());
        }
    }

    @Test
    public void constructorFailsDifferentQualitiesShouldThrowIllegalArgumentException() {
        try {
            new DefaultSffReadData(basecalls, indexes, flowgramValues, new byte[] { 0, 0, 0, 0, 0, 0 });
            fail("should throw IllegalArgumentException when qualities length is different");
        } catch (IllegalArgumentException expected) {
            assertEquals("basecalls, indexes and qualities must be the same length", expected.getMessage());
        }
    }
}
