package org.musicnotation.model.helpers;

import java.util.Comparator;
import org.musicnotation.model.ChordNote;

public class ChordNoteDiatonicPitchComparator implements Comparator<ChordNote> {

    protected ChordNoteDiatonicPitchComparator() {
    }

    public int compare(ChordNote o1, ChordNote o2) {
        return o1.getPitch().getDiatonic() - o2.getPitch().getDiatonic();
    }

    public static final ChordNoteDiatonicPitchComparator INSTANCE = new ChordNoteDiatonicPitchComparator();
}
