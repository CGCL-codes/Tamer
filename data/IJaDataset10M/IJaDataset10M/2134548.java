package org.hydracache.server.data.versioning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.hydracache.server.data.AbstractVersionTest;
import org.junit.Test;

/**
 * @author David Dossot (david@dossot.net)
 */
public class VectorClockTest extends AbstractVersionTest {

    @Test
    public void create() {
        final VectorClock a1 = new VectorClock(A);
        final List<VectorClockEntry> expected = Collections.singletonList(new VectorClockEntry(A, 1));
        assertEquals(expected, a1.getEntries());
    }

    @Test
    public void incrementSameNode() {
        final VectorClock a2 = (VectorClock) new VectorClock(A).incrementFor(A);
        final List<VectorClockEntry> expected = Collections.singletonList(new VectorClockEntry(A, 2));
        assertEquals(expected, a2.getEntries());
    }

    @Test
    public void incrementOtherNode() {
        final VectorClock a1b1 = (VectorClock) new VectorClock(A).incrementFor(B);
        final List<VectorClockEntry> expected = Arrays.asList(new VectorClockEntry[] { new VectorClockEntry(A, 1), new VectorClockEntry(B, 1) });
        assertEquals(expected, a1b1.getEntries());
    }

    @Test
    public void incrementFirstOfManyNode() {
        final VectorClock a2b1c1 = (VectorClock) new VectorClock(A).incrementFor(A).incrementFor(B).incrementFor(C).incrementFor(A);
        final List<VectorClockEntry> expected = Arrays.asList(new VectorClockEntry[] { new VectorClockEntry(A, 3), new VectorClockEntry(B, 1), new VectorClockEntry(C, 1) });
        assertEquals(expected, a2b1c1.getEntries());
    }

    @Test
    public void incrementMiddleOfManyNode() {
        final VectorClock a1b2c1 = (VectorClock) new VectorClock(A).incrementFor(B).incrementFor(C).incrementFor(B);
        final List<VectorClockEntry> expected = Arrays.asList(new VectorClockEntry[] { new VectorClockEntry(A, 1), new VectorClockEntry(B, 2), new VectorClockEntry(C, 1) });
        assertEquals(expected, a1b2c1.getEntries());
    }

    @Test
    public void isDescendantOfSame() {
        final VectorClock a1 = new VectorClock(A);
        final VectorClock a1bis = new VectorClock(A);
        assertEquals(a1bis, a1);
        assertFalse(a1bis.toString(), a1bis.isDescendantOf(a1));
        final VectorClock a2 = (VectorClock) a1.incrementFor(A);
        final VectorClock a2bis = (VectorClock) a1bis.incrementFor(A);
        assertEquals(a2bis, a2);
        assertFalse(a2bis.toString(), a2bis.isDescendantOf(a2));
        final VectorClock a1b1 = (VectorClock) a1.incrementFor(B);
        final VectorClock a1b1bis = (VectorClock) a1bis.incrementFor(B);
        assertEquals(a1b1bis, a1b1);
        assertFalse(a1b1bis.toString(), a1b1bis.isDescendantOf(a1b1));
    }

    @Test
    public void isDescendantOfSingleNode() {
        final VectorClock a1 = new VectorClock(A);
        final VectorClock a2 = (VectorClock) a1.incrementFor(A);
        assertTrue(a2 + "::" + a1, a2.isDescendantOf(a1));
        assertFalse(a1 + "::" + a2, a1.isDescendantOf(a2));
    }

    @Test
    public void isNotDescendantOfSingleNode() {
        final VectorClock a1 = new VectorClock(A);
        final VectorClock b1 = new VectorClock(B);
        assertFalse(a1 + "::" + b1, a1.isDescendantOf(b1));
        assertFalse(b1 + "::" + a1, b1.isDescendantOf(a1));
    }

    @Test
    public void isDescendantOfMultipleNodes() {
        final VectorClock a1 = new VectorClock(A);
        final VectorClock a1b1 = (VectorClock) new VectorClock(A).incrementFor(B);
        assertTrue(a1b1 + "::" + a1, a1b1.isDescendantOf(a1));
        assertFalse(a1 + "::" + a1b1, a1.isDescendantOf(a1b1));
        final VectorClock a1b1c1 = (VectorClock) a1.incrementFor(B).incrementFor(C);
        assertTrue(a1b1c1 + "::" + a1, a1b1c1.isDescendantOf(a1));
        assertFalse(a1 + "::" + a1b1c1, a1.isDescendantOf(a1b1c1));
        assertTrue(a1b1c1 + "::" + a1b1, a1b1c1.isDescendantOf(a1b1));
        assertFalse(a1b1 + "::" + a1b1c1, a1b1.isDescendantOf(a1b1c1));
    }

    @Test
    public void isNotDescendantOfMultipleNodes() {
        final VectorClock a1 = new VectorClock(A);
        final VectorClock a1b1 = (VectorClock) a1.incrementFor(B);
        final VectorClock a1c1 = (VectorClock) a1.incrementFor(C);
        assertFalse(a1b1 + "::" + a1c1, a1b1.isDescendantOf(a1c1));
        assertFalse(a1c1 + "::" + a1b1, a1c1.isDescendantOf(a1b1));
    }
}
