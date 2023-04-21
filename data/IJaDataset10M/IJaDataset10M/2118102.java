package org.alcibiade.composer.composer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.alcibiade.composer.piece.Key;
import org.alcibiade.composer.piece.Pitch;
import org.alcibiade.composer.piece.PitchRange;
import org.alcibiade.composer.piece.Scale;
import org.junit.Test;

public class PitchSelectorTest extends AbstractSelectorTest {

    private static final int ITERATIONS = 10000;

    @Test
    public void testPitchSelector() {
        Key key = new Key(Pitch.A4, Scale.MINOR);
        PitchRange range = PitchRange.BARITONE;
        PitchSelector s = new PitchSelector(key, range);
        Map<Pitch, Integer> selections = runSelections(s, ITERATIONS);
        int averageOccurrences = ITERATIONS / selections.size();
        for (Map.Entry<Pitch, Integer> entry : selections.entrySet()) {
            Pitch pitch = entry.getKey();
            assertTrue(key.contains(pitch));
            assertTrue(range.contains(pitch));
            Integer occurrences = entry.getValue();
            assertEquals(averageOccurrences, occurrences, averageOccurrences / 4);
        }
    }

    @Test
    public void testWeightPitchesCollectionOfPitchDouble() {
        Key key = new Key(Pitch.C4, Scale.MAJOR);
        PitchRange range = PitchRange.BARITONE;
        PitchSelector s = new PitchSelector(key, range);
        Set<Pitch> pitches = new HashSet<Pitch>();
        pitches.add(Pitch.A4);
        pitches.add(Pitch.E4);
        s.weightPitches(pitches, 2.);
        Map<Pitch, Integer> selections = runSelections(s, ITERATIONS);
        int averageOccurrences = ITERATIONS / selections.size();
        for (Map.Entry<Pitch, Integer> entry : selections.entrySet()) {
            Pitch pitch = entry.getKey();
            assertTrue(key.contains(pitch));
            assertTrue(range.contains(pitch));
            Integer occurrences = entry.getValue();
            char noteLetter = pitch.getChromaticInformations().getNoteLetter();
            if (noteLetter == 'A' || noteLetter == 'E') {
                assertTrue(occurrences > averageOccurrences);
            } else {
                assertTrue(occurrences < averageOccurrences);
            }
        }
    }
}
