package com.neuemusic.eartoner;

/**
 * Contains the basic values for a single note, no time is included in this,
 * only the value of the note.  I'm still not sure this is a good idea, rather
 * than just using the actual note class, but the justification is that when
 * it is held within an interval or chord you don't want it to accidentally
 * have its time edited.
 * @author Tom Jensen
 *
 */
public class BasicNote implements Comparable<BasicNote> {

    private int noteValue;

    private int detune;

    /**
	 * @return the detune
	 */
    public int getDetune() {
        return detune;
    }

    /**
	 * @param detune the detune to set
	 */
    public void setDetune(int detune) {
        if (detune > 63) {
            detune = 63;
        } else if (detune < -64) {
            detune = -64;
        }
        this.detune = detune;
    }

    /**
	 * @return the midiVal
	 */
    public int getNoteValue() {
        return noteValue;
    }

    /**
	 * @param midiVal the midiVal to set
	 */
    public void setNoteValue(int noteValue) {
        if (noteValue > 127) {
            noteValue = 127;
        }
        this.noteValue = noteValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BasicNote) {
            BasicNote note = (BasicNote) obj;
            if (this.noteValue == note.noteValue && this.detune == note.detune) {
                return true;
            }
        }
        return false;
    }

    public int compareTo(BasicNote o) {
        if (this.noteValue == o.noteValue) {
            return this.detune - o.noteValue;
        }
        return this.noteValue - o.noteValue;
    }
}
