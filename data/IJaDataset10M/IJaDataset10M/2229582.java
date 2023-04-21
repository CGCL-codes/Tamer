package org.jjazz.harmony;

import java.io.*;
import javax.swing.event.*;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.logging.Logger;

/**
 * A note has a pitch and a duration (symbolic and in beats)
 */
public class Note implements Cloneable, Comparable, Serializable {

    public static final String PROPERTY_PITCH_CHANGED = "NotePitchChanged";

    public static final String PROPERTY_DURATION_CHANGED = "NoteDurationChanged";

    public static final int PITCH_MIN = 0;

    public static final int PITCH_STD = 64;

    public static final int PITCH_MAX = 127;

    public static final int OCTAVE_MIN = 0;

    public static final int OCTAVE_STD = 4;

    public static final int OCTAVE_MAX = 10;

    private static final Logger LOGGER = Logger.getLogger(Note.class.getName());

    public enum Alteration {

        FLAT, SHARP
    }

    public static final String[] notesFlat = { "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B" };

    /** Available for general use. */
    public static final String[] notesSharp = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };

    /** The pitch of the note (0-127). */
    protected int pitch;

    /** The symbolic duration of the note. */
    protected SymbolicDuration symbolicDuration;

    /** The duration in beats of the note. */
    protected float beatDuration;

    /** To memorize if we use the sharp or flat notes. */
    protected Alteration alterationDisplay;

    /** The listeners for changes in this model. */
    protected transient SwingPropertyChangeSupport pcs = new SwingPropertyChangeSupport(this);

    public Note() {
        this(PITCH_STD);
    }

    /**
     * Create a note with a default beatDuration=1. use FLAT symbols if any.
     *
     * @param p The pitch of the note.
     */
    public Note(int p) {
        setPitch(p, Alteration.FLAT);
        setSymbolicDuration(SymbolicDuration.QUARTER);
    }

    /**
     * Create a Note with a pitch and a beat duration. Use FLAT symbol by default.
     *
     * @param p The pitch of the note.
     * @param bd The beat duration of the note.
     */
    public Note(int p, float bd) {
        setPitch(p, Alteration.FLAT);
        setBeatDuration(bd);
    }

    /**
     * Create a Note with a pitch and a symbolic duration. Use FLAT symbols by default.
     *
     * @param p The pitch of the note.
     * @param sd The symbolic duration of the note.
     */
    public Note(int p, SymbolicDuration sd) {
        this(p, sd, Alteration.FLAT);
    }

    /**
     * Create a Note with a pitch, a symbolic duration and an alteration if any.
     *
     * @param p The pitch of the note.
     * @param sd The symbolic duration of the note.
     * @param alt The alteration to use if any. Note.SHARP or Note.FLAT.
     */
    public Note(int p, SymbolicDuration sd, Alteration alt) {
        setPitch(p, alt);
        setSymbolicDuration(sd);
    }

    /**
     * Build a note from a string specification with a default
     * symbolicDuration=QUARTER.<br>.
     *
     * @param s A string like "C" or "Eb3"
     *
     * @throws ParseException If syntax error in string specification.
     *
     * @see setPitch()
     */
    public Note(String s) throws ParseException {
        setSymbolicDuration(SymbolicDuration.QUARTER);
        setPitch(s);
    }

    /**
     * Set the pitch of the note from a string. If octave is not specified use
     * Const.OCTAVE_STD.
     *
     * @param s A string like "C#", "F", "Eb6" or "Ab2" as returned by toString().
     *
     * @throws ParseException If syntax error in string specification.
     */
    public void setPitch(String s) throws ParseException {
        String str = s.trim();
        Alteration alt = Alteration.FLAT;
        if ((str.length() == 0) || (str.length() > 4)) {
            throw new ParseException("Invalid Note specification (\"" + s + "\")", 0);
        }
        String degreeStr = str;
        String octaveStr = null;
        char lastChar = str.charAt(str.length() - 1);
        if ((str.length() > 1) && (lastChar >= '0') && (lastChar <= '9')) {
            int index = ((str.charAt(1) == 'b') || (str.charAt(1) == 'B') || (str.charAt(1) == '#')) ? 2 : 1;
            degreeStr = str.substring(0, index);
            octaveStr = str.substring(index);
        }
        int relPitch = -1;
        for (int i = 0; i < notesFlat.length; i++) {
            if (degreeStr.compareToIgnoreCase(notesFlat[i]) == 0) {
                relPitch = i;
                alt = Alteration.FLAT;
                break;
            }
            if (degreeStr.compareToIgnoreCase(notesSharp[i]) == 0) {
                relPitch = i;
                alt = Alteration.SHARP;
                break;
            }
        }
        if (relPitch == -1) {
            throw new ParseException("Invalid Note specification \"" + s + "\"", 0);
        }
        int o = OCTAVE_STD;
        if (octaveStr != null) {
            try {
                o = Integer.parseInt(octaveStr);
            } catch (NumberFormatException e) {
                throw new ParseException("Invalid Note specification (\"" + s + "\")", 0);
            }
        }
        if (!checkOctave(o)) {
            throw new ParseException("Invalid Note specification (\"" + s + "\")", 0);
        }
        setPitch((o * 12) + relPitch, alt);
    }

    /**
     * Set the note from a pitch, and specify the alteration display to use if required.
     * @param p int The pitch of the note.
     * @param alt int Note.SHARP or Note.FLAT
     */
    public void setPitch(int p, Alteration alt) {
        if (!checkPitch(p)) {
            throw new IllegalArgumentException("pitch=" + p + " alt=" + alt);
        }
        alterationDisplay = alt;
        if (p != pitch) {
            int save = pitch;
            pitch = p;
            pcs.firePropertyChange(PROPERTY_PITCH_CHANGED, save, pitch);
        }
    }

    /**
     * Set the note from a pitch. Use the FLAT alteration by default.
     * @param p int
     */
    public void setPitch(int p) {
        setPitch(p, Alteration.FLAT);
    }

    /**
     * Set the relative pitch of the note (does not change the octave nor the alteration).
     *
     * @param rp An integer from 0 to 11.
     */
    public void setRelativePitch(int rp) {
        if ((rp < 0) || (rp > 11)) {
            throw new IllegalArgumentException("rp=" + rp);
        }
        setPitch((getOctave() * 12) + rp, alterationDisplay);
    }

    public int getPitch() {
        return pitch;
    }

    /**
     * @return An integer between 0 and 11.
     */
    public int getRelativePitch() {
        return pitch % 12;
    }

    /**
     * Overridden to recreate the transient fields
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        pcs = new SwingPropertyChangeSupport(this);
    }

    public void setBeatDuration(float bd) {
        if (bd < 0) {
            throw new IllegalArgumentException("bd=" + bd);
        }
        if (beatDuration != bd) {
            float save = beatDuration;
            beatDuration = bd;
            symbolicDuration = SymbolicDuration.getSymbolicDuration(beatDuration);
            pcs.firePropertyChange(PROPERTY_DURATION_CHANGED, save, beatDuration);
        }
    }

    public double getBeatDuration() {
        return beatDuration;
    }

    public void setSymbolicDuration(SymbolicDuration sd) {
        if (sd != symbolicDuration) {
            float save = beatDuration;
            symbolicDuration = sd;
            beatDuration = symbolicDuration.getBeatDuration();
            pcs.firePropertyChange(PROPERTY_DURATION_CHANGED, save, beatDuration);
        }
    }

    public SymbolicDuration getSymbolicDuration() {
        return symbolicDuration;
    }

    public void setOctave(int o) {
        if ((o < OCTAVE_MIN) || (o > OCTAVE_MAX)) {
            throw new IllegalArgumentException("o=" + o);
        }
        setPitch((o * 12) + getRelativePitch(), alterationDisplay);
    }

    public int getOctave() {
        return pitch / 12;
    }

    public Alteration getAlterationDisplay() {
        return alterationDisplay;
    }

    public void setAlterationDisplay(Alteration alt) {
        if (alt != Alteration.FLAT && alt != Alteration.SHARP) {
            throw new IllegalArgumentException("alt=" + alt);
        }
        alterationDisplay = alt;
    }

    public void set(Note n) {
        if (n == null) {
            throw new IllegalArgumentException("n=" + n);
        }
        if ((pitch != n.pitch) || (beatDuration != n.beatDuration) || (alterationDisplay != n.alterationDisplay)) {
            int save = pitch;
            pitch = n.pitch;
            float saveDur = beatDuration;
            beatDuration = n.beatDuration;
            alterationDisplay = n.alterationDisplay;
            symbolicDuration = n.symbolicDuration;
            if (pitch != save) {
                pcs.firePropertyChange(PROPERTY_PITCH_CHANGED, save, pitch);
            }
            if (beatDuration != saveDur) {
                pcs.firePropertyChange(PROPERTY_DURATION_CHANGED, saveDur, beatDuration);
            }
        }
    }

    /**
     * @param n A Note.
     *
     * @return The relative ascendant interval in semitons towards n.
     */
    public int getRelativeAscInterval(Note n) {
        int delta = n.getRelativePitch() - getRelativePitch();
        if (delta < 0) {
            delta += 12;
        }
        return delta;
    }

    /**
     * @param n A Note.
     *
     * @return The ascendant interval in semitons towards n.
     */
    public int getRelativeDescInterval(Note n) {
        int delta = getRelativePitch() - n.getRelativePitch();
        if (delta < 0) {
            delta += 12;
        }
        return delta;
    }

    /**
     * Transpose the note from t semitons.
     * @param t Transposition value in positive/negative semitons.
     */
    public void transpose(int t) {
        if (t == 0) {
            return;
        }
        int save = pitch;
        pitch += t;
        if (!checkPitch(pitch)) {
            throw new IllegalArgumentException("Invalid transpose value (" + t + ")" + " pitch=" + this.getPitch());
        }
        pcs.firePropertyChange(PROPERTY_PITCH_CHANGED, save, pitch);
    }

    /**
     * Transpose the note from t semitons indepently of the octave.
     *
     * @param t Transposition value in positive/negative semitons.
     */
    public void transposeRelative(int t) {
        if ((t == 0) || ((t % 12) == 0)) {
            return;
        }
        int rp = getRelativePitch() + t;
        rp = (rp < 0) ? (rp + 12) : rp;
        rp = (rp > 11) ? (rp - 12) : rp;
        setRelativePitch(rp);
    }

    /**
     * Compare the relative pitch of 2 notes.
     * @param n Note The note to compare with this note.
     * @return boolean Return true if Note n has the same relative pitch than this note.
     */
    public boolean equalsRelativePitch(Note n) {
        return getRelativePitch() == n.getRelativePitch();
    }

    /**
     * Compare 2 objects. Accept MidiNote object and String objects.
     *
     * @return True if 2 notes have the same pitch and beatDuration.
     */
    @Override
    public boolean equals(Object o) {
        Note n = null;
        if (o instanceof Note) {
            n = (Note) o;
        } else if (o instanceof String) {
            try {
                n = new Note((String) o);
            } catch (ParseException e) {
                return false;
            }
        } else {
            throw new ClassCastException("o=" + o + " o.getClass()=" + o.getClass());
        }
        return ((pitch == n.pitch) && (beatDuration == n.beatDuration));
    }

    /**
     * @return True if 2 notes have the same pitch.
     */
    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Note)) {
            throw new ClassCastException("Invalid class to be compared to a Note : " + o);
        }
        int opitch = ((Note) o).pitch;
        if (opitch > pitch) {
            return -1;
        } else if (opitch < pitch) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Object clone() {
        Note n = new Note(pitch, symbolicDuration, alterationDisplay);
        for (PropertyChangeListener pcl : pcs.getPropertyChangeListeners()) {
            n.addPropertyChangeListener(pcl);
        }
        return n;
    }

    /**
     * Calculate a HashCode from the pitch and the beatDuration.
     *
     * @return The hash code for the Note.
     */
    @Override
    public int hashCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(pitch);
        sb.append(beatDuration);
        sb.append(alterationDisplay);
        return sb.hashCode();
    }

    /**
     * Same as toAbsoluteNoteString().
     */
    @Override
    public String toString() {
        return toAbsoluteNoteString();
    }

    /**
     * @return String A string describing the note and its duration in beats.
     */
    public String toFullString() {
        return toAbsoluteNoteString() + "#" + beatDuration;
    }

    /**
     * @return E.g. "Db" or "E" (octave independent).
     */
    public String toRelativeNoteString() {
        if (alterationDisplay == Alteration.FLAT) {
            return notesFlat[getRelativePitch()];
        } else {
            return notesSharp[getRelativePitch()];
        }
    }

    /**
     * @return E.g. "D3" (D of the 3rd octave).
     */
    public String toAbsoluteNoteString() {
        return toRelativeNoteString() + getOctave();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    public void addPropertyChangeListener(String p, PropertyChangeListener l) {
        pcs.addPropertyChangeListener(p, l);
    }

    public void removePropertyChangeListener(String p, PropertyChangeListener l) {
        pcs.removePropertyChangeListener(p, l);
    }

    public static boolean checkPitch(int p) {
        if ((p < PITCH_MIN) || (p > PITCH_MAX)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkOctave(int o) {
        if ((o < OCTAVE_MIN) || (o > OCTAVE_MAX)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Return Check if a pitch corresponds to keyboard white key (C major scale).
     */
    public static boolean isWhiteKey(int p) {
        int pitch = p % 12;
        if ((pitch == 1) || (pitch == 3) || (pitch == 6) || (pitch == 8) || (pitch == 10)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Return an array of notes whose pitch start from pitchFrom up to pitchTo (included).<br>
     * E.g., if pitchFrom=0 and pitchTo=12, return the array ["C0", "C#0", "D0"...."C1"]
     *
     * @param pitchFrom A positive integer.
     * @param pitchTo A positive integer.
     *
     * @return An array of Note objects.
     */
    public static Note[] getChromaticNotesArray(int pitchFrom, int pitchTo) {
        if ((pitchFrom > pitchTo) || (pitchFrom < 0) || (pitchTo < 0)) {
            throw new IllegalArgumentException("pitchFrom=" + pitchFrom + " pitchTo=" + pitchTo);
        }
        Note[] notes = new Note[pitchTo - pitchFrom + 1];
        for (int i = 0; i < notes.length; i++) {
            notes[i] = new Note(pitchFrom + i);
        }
        return notes;
    }
}
