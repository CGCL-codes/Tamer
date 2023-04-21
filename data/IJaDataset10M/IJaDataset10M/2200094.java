package imp.data;

import imp.ImproVisor;
import imp.com.InsertPartCommand;
import imp.gui.Notate;
import imp.util.ErrorLog;
import imp.util.Preferences;
import imp.util.Trace;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Track;

/**
 * An extension of the Part class that contains only Note (or, by extension, 
 * Rest) objects.  This is useful to contain drawing functions that are
 * specific to Notes, such as ties and accidentals.
 * @see         Note
 * @see         Rest
 * @see         Part
 * @author      Stephen Jones
 */
public class MelodyPart extends Part {

    /**
   *
   * This is a "fudge-factor" to keep display from getting weird
   * during extraction. It limits the smallest note to a set
   * duration.  If the copied duration is less, then it is artificially
   * set to this value. Otherwise, the duration could be, e.g. 1,
   * which creates a really weird display with 120 sub-divisions, and
   * from which it is hard to recover.
   */
    public static int MIN_EXTRACT_DURATION = BEAT / 4;

    public static int magicFactor = 4;

    /**
   * Lowest pitch in the MelodyPart
   */
    private int lowestPitch = 127;

    /**
   * Highest pitch in the MelodyPart
   */
    private int highestPitch = 0;

    private static int[] knownRestValue = { 0, 10, 12, 15, 20, 24, 30, 40, 60, 80, 120, 160, 240, 480 };

    private boolean fudgeEnding = false;

    /**
   * Volume to be explicitly given for one-time melodies, for
   * use with entered melodies.  "Playback" melodies will instead
   * rely on the volume slider in the toolhar.
   */
    public int getSpecifiedVolume() {
        return volume;
    }

    /**
   * Creates an empty MelodyPart.
   */
    public MelodyPart() {
        super();
        this.volume = ImproVisor.getEntryVolume();
    }

    /** 
   * Creates a MelodyPart with the given size.
   * @param size      the number of slots in the MelodyPart
   */
    public MelodyPart(int size) {
        super(size);
        Trace.log(3, "creating new melody part of size " + size);
        this.volume = ImproVisor.getEntryVolume();
    }

    /**
   * Creates a MelodyPart from a given string of notes
   * @param notes     the list of notes
   */
    public MelodyPart(String notes) {
        super();
        this.volume = ImproVisor.getEntryVolume();
        String[] noteList = notes.split(" ");
        for (int i = 0; i < Array.getLength(noteList); ++i) {
            addNote(NoteSymbol.makeNoteSymbol(noteList[i]).toNote());
        }
    }

    /**
   * Adds a Note to the end of the MelodyPart, extending its length.
   * @param note      the Note to add
      */
    public void addNote(Note note) {
        if (!note.isRest()) {
            if (note.getPitch() < lowestPitch) {
                lowestPitch = note.getPitch();
            }
            if (note.getPitch() > highestPitch) {
                highestPitch = note.getPitch();
            }
        }
        addUnit(note);
    }

    /**
   * Adds a Rest to the end of the MelodyPart, extending its length.
   * @param rest      the Rest to add
   */
    public void addRest(Rest rest) {
        addUnit(rest);
    }

    /**
   * Sets the specified slot to the given note.
   * @param slotIndex         the index of the slot to set at
   * @param note              the note to put at the slot index
   */
    public void setNote(int slotIndex, Note note) {
        if (slotIndex < 0) {
            slotIndex = 0;
        }
        if (note != null && note.nonRest()) {
            int currentMeasure = slotIndex / measureLength;
            int stopIndex = measureLength * (currentMeasure + 2);
            if (stopIndex < size && getNextIndex(slotIndex) > stopIndex) {
                setRest(stopIndex);
            }
            if (note.getPitch() < lowestPitch) {
                lowestPitch = note.getPitch();
            } else if (note.getPitch() > highestPitch) {
                highestPitch = note.getPitch();
            }
        }
        setUnit(slotIndex, note);
    }

    /**
   *
   */
    public synchronized void setNoteAndLength(int slotIndex, Note note, Notate parent) {
        int origLength = note.getRhythmValue();
        if (slotIndex + origLength >= size) {
            int measureLength = getMeasureLength();
            double lengthInMeasures = (slotIndex + origLength - size) / (double) measureLength;
            int measuresToAdd = (int) Math.ceil(lengthInMeasures);
            Part newMeasures = new Part(measuresToAdd * measureLength);
            (new InsertPartCommand(parent, this, size, newMeasures)).execute();
        }
        setRest(slotIndex);
        int freeSlots = getFreeSlots(slotIndex);
        int nextIndex = slotIndex + freeSlots;
        while (freeSlots < origLength) {
            setRest(slotIndex + freeSlots);
            freeSlots = getFreeSlots(slotIndex);
        }
        synchronized (slots) {
            mergeFreeSlots(slotIndex);
            setNote(slotIndex, note);
        }
        if (note.getRhythmValue() > origLength) {
            setRest(slotIndex + origLength);
        }
    }

    /**
   * Sets the specified slot to the given rest
   * @param slotIndex         the index of the slot to set at
   * @param rest              the rest to put at the slot index
   */
    public void setRest(int slotIndex, Rest rest) {
        Trace.log(2, "setRest at " + slotIndex + " to " + rest);
        setUnit(slotIndex, rest);
    }

    /**
   * sets a Rest at the given slot index
   * @param slotIndex         the index of the slot to set at
   */
    public void setRest(int slotIndex) {
        setRest(slotIndex, new Rest());
    }

    /**
   * Returns the note at the given slot index
   * @param slotIndex         the index of the note to get
   * @return Note             the Note at the given index
   */
    public Note getNote(int slotIndex) {
        return (Note) getUnit(slotIndex);
    }

    /**
   * Returns the Note after the indicated slot index.
   * @param slotIndex         the index to start searching at
   * @return Note             the Note after the given index
   */
    public Note getNextNote(int slotIndex) {
        return (Note) getNextUnit(slotIndex);
    }

    /**
   * Returns the Note before the indicated slot index.
   * @param slotIndex         the index to start searching at
   * @return Note             the Note before the given index
   */
    public Note getPrevNote(int slotIndex) {
        return (Note) getPrevUnit(slotIndex);
    }

    /**
   * Gets the lowest pitch in the MelodyPart
   * @return int              lowest pitch
   */
    public int getLowestPitch() {
        return lowestPitch;
    }

    /**
   * Gets the highest pitch in the MelodyPart
   * @return int              highest pitch
   */
    public int getHighestPitch() {
        return highestPitch;
    }

    /**
   * Returns the number of slots from the index to the next Note.
   * @param index     the index to get the free slots
   * @return int      the number of free slots
   */
    public int getFreeSlots(int index) {
        if (getNote(index) != null && getNote(index).nonRest()) {
            return 0;
        }
        if (index < 0) {
            return size;
        }
        int nextIndex = getNextIndex(index);
        while (nextIndex < size - 1) {
            if (getNote(nextIndex) != null && getNote(nextIndex).nonRest()) {
                return nextIndex - index;
            }
            nextIndex = getNextIndex(nextIndex);
        }
        return size - index;
    }

    public int getFreeSlotsFromEnd() {
        int slotIndex = getSize() - 1;
        int restSlots = 0;
        Note current;
        while (slotIndex >= 0) {
            current = getNote(slotIndex);
            if (current != null) {
                if (current.nonRest()) {
                    return restSlots;
                }
                restSlots += current.getRhythmValue();
            }
            slotIndex--;
        }
        if (restSlots != getSize()) {
            Trace.log(0, "Possible error calculating number of rests from the end of the piece in Part.getRestSlotsFromEnd");
        }
        return restSlots;
    }

    public synchronized void mergeFreeSlots(int index) {
        if (index < 0) {
            return;
        }
        if (getNote(index) == null) {
            index = getPrevIndex(index);
            if (getNote(index).nonRest()) {
                return;
            }
        } else if (getNote(index).nonRest()) {
            return;
        }
        Note firstNote = getNote(index);
        int nextIndex = getNextIndex(index);
        Note nextNote = getNote(nextIndex);
        while (nextIndex < size - 1) {
            if (nextNote != null && nextNote.nonRest()) {
                return;
            }
            slots.set(nextIndex, null);
            unitCount--;
            firstNote.setRhythmValue(firstNote.getRhythmValue() + nextNote.getRhythmValue());
            nextIndex = getNextIndex(nextIndex);
            nextNote = getNote(nextIndex);
        }
    }

    /**
   * Returns an exact copy of this Part
   * @return Part   copy
   */
    @Override
    public MelodyPart copy() {
        return copy(0);
    }

    /**
   * Returns an exact copy of this Part from startingIndex
   * @return Part   copy
   */
    public MelodyPart copy(int startingIndex) {
        return copy(startingIndex, size - 1);
    }

    /**
   * Returns an exact copy of this Part from startingIndex to endingIndex
   * @return 
   */
    public MelodyPart copy(int startingIndex, int endingIndex) {
        int newSize = endingIndex + 1 - startingIndex;
        if (newSize <= 0) {
            return new MelodyPart(0);
        }
        int newUnitCount = 0;
        try {
            MelodyPart newPart = new MelodyPart(newSize);
            int i = startingIndex;
            if (slots.get(startingIndex) == null) {
                i++;
                for (; i <= endingIndex; i++) {
                    if (slots.get(i) != null) {
                        break;
                    }
                }
                newPart.slots.set(0, new Rest(i - startingIndex));
                unitCount = 1;
            }
            for (; i <= endingIndex && i < size; i++) {
                Unit unit = slots.get(i);
                if (unit != null) {
                    unit = unit.copy();
                    newUnitCount++;
                    if (i + unit.getRhythmValue() - 1 > endingIndex) {
                        unit.setRhythmValue(endingIndex - i + 1);
                    }
                }
                newPart.slots.set(i - startingIndex, unit);
            }
            newPart.unitCount = newUnitCount;
            newPart.title = title;
            newPart.volume = volume;
            newPart.keySig = keySig;
            newPart.metre[0] = metre[0];
            newPart.metre[1] = metre[1];
            newPart.beatValue = beatValue;
            newPart.measureLength = measureLength;
            newPart.swing = swing;
            newPart.instrument = instrument;
            return newPart;
        } catch (Error e) {
            ErrorLog.log(ErrorLog.FATAL, "Not enough memory to copy part of size " + newSize + ".");
            return null;
        }
    }

    public int getPitchSounding(int index) {
        Note curr = this.getNote(index);
        if (curr != null) {
            return curr.getPitch();
        }
        int prevIndex = 0;
        try {
            prevIndex = this.getPrevIndex(index);
        } catch (Exception e) {
            System.out.println("error in MelodyPart.getPitchSounding: index: " + index);
        }
        Note prevNote = this.getNote(prevIndex);
        if (prevNote.isRest()) {
            return -1;
        }
        if (prevNote.getRhythmValue() > (index - prevIndex)) {
            return prevNote.getPitch();
        }
        return -1;
    }

    /**
   * Returns a reverse copy of this Part
   * @return Part   copy
   */
    public MelodyPart copyReverse() {
        Trace.log(3, "copying in  reverse melody part of size " + size);
        MelodyPart newPart = extractReverse(0, size);
        newPart.unitCount = unitCount;
        newPart.title = title;
        newPart.volume = volume;
        newPart.keySig = keySig;
        newPart.metre[0] = metre[0];
        newPart.metre[1] = metre[1];
        newPart.beatValue = beatValue;
        newPart.measureLength = measureLength;
        newPart.swing = swing;
        newPart.instrument = instrument;
        return newPart;
    }

    public void dump(PrintStream out) {
        PartIterator i = iterator();
        try {
            while (i.hasNext()) {
                Note note = (Note) i.next();
                out.print(note.getRhythmValue());
                out.print(" ");
                out.print(note.getPitch());
                out.println();
            }
        } catch (Exception e) {
        }
    }

    /**
   * Changes Notes in the Part so ties are proper and the Part
   * can be drawn.
   */
    public void makeTies() {
        PartIterator i = iterator();
        while (i.hasNext()) {
            int j = i.nextIndex();
            try {
                Note n = (Note) i.next();
                tieThis(j, n, 0);
            } catch (Exception e) {
                return;
            }
        }
    }

    /**
   * This is a recursive routine, originally created by Martin Hunt, I think.
   @param slotIndex
   @param note
   @param level
   */
    public void tieThis(int slotIndex, Note note, int level) {
        if (note == null) {
            Trace.log(2, "Warning: tieThis attempted to tie a null note.  This is probably a bug.");
            return;
        }
        int rhythmValue = note.getRhythmValue();
        int knownRestIndex = java.util.Arrays.binarySearch(knownRestValue, rhythmValue);
        int knownNoteIndex = java.util.Arrays.binarySearch(knownNoteValue, rhythmValue);
        int knownTupletIndex = java.util.Arrays.binarySearch(knownTupletValue, rhythmValue);
        int knownRhythmIndex = note.isRest() ? knownRestIndex : knownNoteIndex;
        int currentMeasure = (slotIndex / measureLength) * measureLength;
        int nextMeasure = currentMeasure + measureLength;
        int currentBeat = (slotIndex / beatValue) * beatValue;
        int nextBeat = currentBeat + beatValue;
        int firstNoteRV = -1;
        int secondNoteRV = -1;
        if (slotIndex + rhythmValue <= nextMeasure && (knownTupletIndex >= 0)) {
            return;
        }
        if (slotIndex % beatValue != 0 && slotIndex + rhythmValue > nextBeat) {
            firstNoteRV = nextBeat - slotIndex;
            secondNoteRV = rhythmValue - firstNoteRV;
        } else if (slotIndex + rhythmValue > nextMeasure) {
            firstNoteRV = nextMeasure - slotIndex;
            secondNoteRV = rhythmValue - firstNoteRV;
        } else if (rhythmValue > beatValue && (slotIndex + rhythmValue) % beatValue != 0) {
            secondNoteRV = rhythmValue % beatValue;
            firstNoteRV = rhythmValue - secondNoteRV;
        } else if (!note.isRest() && rhythmValue < beatValue) {
            if (knownRhythmIndex < 0) {
                knownRhythmIndex = -knownRhythmIndex - 2;
                if (rhythmValue < beatValue) {
                    for (int i = 2; i < 20; i++) {
                        int div = beatValue / i;
                        if ((rhythmValue / div) * div == rhythmValue) {
                            firstNoteRV = div * (i / 2);
                            secondNoteRV = rhythmValue - firstNoteRV;
                            break;
                        }
                    }
                }
            }
        } else if (note.isRest() && (knownRhythmIndex = java.util.Arrays.binarySearch(knownRestValue, rhythmValue)) < 0) {
            if (rhythmValue < beatValue) {
                for (int i = 2; i < 20; i++) {
                    int div = beatValue / i;
                    if ((rhythmValue / div) * div == rhythmValue) {
                        firstNoteRV = div;
                        secondNoteRV = rhythmValue - div;
                        break;
                    }
                }
            }
        }
        if ((firstNoteRV > measureLength || (firstNoteRV == -1 && rhythmValue > measureLength)) && !note.isRest()) {
            firstNoteRV = measureLength;
            secondNoteRV = rhythmValue - firstNoteRV;
        }
        int[] knownRhythmValue = note.isRest() ? knownRestValue : knownNoteValue;
        if (firstNoteRV == -1 && (knownRhythmIndex = java.util.Arrays.binarySearch(knownRhythmValue, rhythmValue)) < 0) {
            knownRhythmIndex = -knownRhythmIndex - 2;
            int remainder = rhythmValue - knownRhythmValue[knownRhythmIndex];
            if (rhythmValue > beatValue) {
                for (int i = knownRhythmIndex; i > 2; i--) {
                    if ((knownRhythmValue[i] / beatValue) * beatValue == knownRhythmValue[i]) {
                        firstNoteRV = (knownRhythmValue[i] / beatValue) * beatValue;
                        secondNoteRV = rhythmValue - firstNoteRV;
                        break;
                    }
                }
            } else {
                if (knownRhythmIndex > 0 && remainder >= knownRhythmValue[1]) {
                    firstNoteRV = knownRhythmValue[knownRhythmIndex];
                    secondNoteRV = remainder;
                } else {
                }
            }
        }
        if (firstNoteRV <= 0 || secondNoteRV <= 0) {
            return;
        }
        if (!note.isTied()) {
            note.setTie(true);
            note.setFirstTie(true);
        }
        note.setRhythmValue(firstNoteRV);
        Note newNote = note.copy();
        newNote.setFirstTie(false);
        newNote.setAccidental(Accidental.NOTHING);
        newNote.setRhythmValue(secondNoteRV);
        slots.set(slotIndex + firstNoteRV, newNote);
        unitCount++;
        tieThis(slotIndex, note, level + 1);
        tieThis(slotIndex + firstNoteRV, newNote, level + 1);
    }

    /**
   * Edits the Accidental field on each Note to represent what should be
   * drawn.
   */
    public void makeAccidentals() {
        ArrayList<Accidental> accidentalVector = getKeySigVector();
        for (int i = 0; i < size; i++) {
            int thisBeat = beatValue * (i / beatValue);
            if (thisBeat == i && (i / beatValue) % metre[0] == 0) {
                accidentalVector = getKeySigVector();
            }
            Note note;
            if (getUnit(i) == null || ((Note) getUnit(i)).isRest()) {
                continue;
            }
            note = (Note) getUnit(i);
            int pitch = note.getPitch();
            if (note.getAccidental() == Accidental.SHARP) {
                note.setDrawnPitch(pitch - 1);
            } else if (note.getAccidental() == Accidental.FLAT) {
                note.setDrawnPitch(pitch + 1);
            } else {
                note.setDrawnPitch(pitch);
            }
            if (note.getAccidental() == accidentalVector.get(note.getDrawnPitch())) {
                note.setAccidental(Accidental.NOTHING);
            } else {
                accidentalVector.set(note.getDrawnPitch(), note.getAccidental());
            }
        }
    }

    /**
   * Creates a new Track for this Part on the specified channel, and adds
   * it to the specified Sequence, called by Score.render.
   * @param seq     the Sequence to add a Track to
   * @param ch      the channel to put the Track on
   */
    public long render(MidiSequence seq, int ch, long time, Track track, int transposition, int endLimitIndex) throws InvalidMidiDataException {
        boolean sendBankSelect = Preferences.getMidiSendBankSelect();
        PartIterator i = iterator();
        track.add(MidiSynth.createProgramChangeEvent(ch, instrument, time));
        if (sendBankSelect) {
            track.add(MidiSynth.createBankSelectEventMSB(0, time));
            track.add(MidiSynth.createBankSelectEventLSB(0, time));
        }
        endLimitIndex *= magicFactor;
        while (i.hasNext() && Style.limitNotReached(time, endLimitIndex)) {
            Note note = (Note) i.next();
            time = note.render(seq.getSequence(), track, time, ch, transposition, sendBankSelect);
        }
        return time;
    }

    /**
   * Returns an ArrayList of Accidentals corresponding to all possible pitches.
   * The weird thing about this is that we only end up marking the 
   * accidental independent pitches: C1, D1, E1, G4, A4, etc.
   * This is to make an easy mapping between the pitches as they are
   * stored for playback and elements of this Vector.
   * @return ArrayList<Accidental>       the accidental vector
   */
    public ArrayList<Accidental> getKeySigVector() {
        ArrayList<Accidental> keySigVector = new ArrayList<Accidental>(TOTALPITCHES);
        for (int i = 0; i < TOTALPITCHES; i++) {
            keySigVector.add(Accidental.NOTHING);
        }
        switch(keySig) {
            case CBMAJOR:
                for (int i = 0; i < TOTALPITCHES; i++) {
                    if (i % SEMITONES == MODC || i % SEMITONES == MODF || i % SEMITONES == MODG || i % SEMITONES == MODD || i % SEMITONES == MODA || i % SEMITONES == MODE || i % SEMITONES == MODB) {
                        keySigVector.set(i, Accidental.FLAT);
                    } else {
                        keySigVector.set(i, Accidental.NATURAL);
                    }
                }
                break;
            case GBMAJOR:
                for (int i = 0; i < TOTALPITCHES; i++) {
                    if (i % SEMITONES == MODC || i % SEMITONES == MODG || i % SEMITONES == MODD || i % SEMITONES == MODA || i % SEMITONES == MODE || i % SEMITONES == MODB) {
                        keySigVector.set(i, Accidental.FLAT);
                    } else {
                        keySigVector.set(i, Accidental.NATURAL);
                    }
                }
                break;
            case DBMAJOR:
                for (int i = 0; i < TOTALPITCHES; i++) {
                    if (i % SEMITONES == MODG || i % SEMITONES == MODD || i % SEMITONES == MODA || i % SEMITONES == MODE || i % SEMITONES == MODB) {
                        keySigVector.set(i, Accidental.FLAT);
                    } else {
                        keySigVector.set(i, Accidental.NATURAL);
                    }
                }
                break;
            case ABMAJOR:
                for (int i = 0; i < TOTALPITCHES; i++) {
                    if (i % SEMITONES == MODD || i % SEMITONES == MODA || i % SEMITONES == MODE || i % SEMITONES == MODB) {
                        keySigVector.set(i, Accidental.FLAT);
                    } else {
                        keySigVector.set(i, Accidental.NATURAL);
                    }
                }
                break;
            case EBMAJOR:
                for (int i = 0; i < TOTALPITCHES; i++) {
                    if (i % SEMITONES == MODA || i % SEMITONES == MODE || i % SEMITONES == MODB) {
                        keySigVector.set(i, Accidental.FLAT);
                    } else {
                        keySigVector.set(i, Accidental.NATURAL);
                    }
                }
                break;
            case BBMAJOR:
                for (int i = 0; i < TOTALPITCHES; i++) {
                    if (i % SEMITONES == MODE || i % SEMITONES == MODB) {
                        keySigVector.set(i, Accidental.FLAT);
                    } else {
                        keySigVector.set(i, Accidental.NATURAL);
                    }
                }
                break;
            case FMAJOR:
                for (int i = 0; i < TOTALPITCHES; i++) {
                    if (i % SEMITONES == MODB) {
                        keySigVector.set(i, Accidental.FLAT);
                    } else {
                        keySigVector.set(i, Accidental.NATURAL);
                    }
                }
                break;
            case CMAJOR:
                for (int i = 0; i < TOTALPITCHES; i++) {
                    keySigVector.set(i, Accidental.NATURAL);
                }
                break;
            case GMAJOR:
                for (int i = 0; i < TOTALPITCHES; i++) {
                    if (i % SEMITONES == MODF) {
                        keySigVector.set(i, Accidental.SHARP);
                    } else {
                        keySigVector.set(i, Accidental.NATURAL);
                    }
                }
                break;
            case DMAJOR:
                for (int i = 0; i < TOTALPITCHES; i++) {
                    if (i % SEMITONES == MODC || i % SEMITONES == MODF) {
                        keySigVector.set(i, Accidental.SHARP);
                    } else {
                        keySigVector.set(i, Accidental.NATURAL);
                    }
                }
                break;
            case AMAJOR:
                for (int i = 0; i < TOTALPITCHES; i++) {
                    if (i % SEMITONES == MODC || i % SEMITONES == MODF || i % SEMITONES == MODG) {
                        keySigVector.set(i, Accidental.SHARP);
                    } else {
                        keySigVector.set(i, Accidental.NATURAL);
                    }
                }
                break;
            case EMAJOR:
                for (int i = 0; i < TOTALPITCHES; i++) {
                    if (i % SEMITONES == MODC || i % SEMITONES == MODF || i % SEMITONES == MODG || i % SEMITONES == MODD) {
                        keySigVector.set(i, Accidental.SHARP);
                    } else {
                        keySigVector.set(i, Accidental.NATURAL);
                    }
                }
                break;
            case BMAJOR:
                for (int i = 0; i < TOTALPITCHES; i++) {
                    if (i % SEMITONES == MODC || i % SEMITONES == MODF || i % SEMITONES == MODG || i % SEMITONES == MODD || i % SEMITONES == MODA) {
                        keySigVector.set(i, Accidental.SHARP);
                    } else {
                        keySigVector.set(i, Accidental.NATURAL);
                    }
                }
                break;
            case FSMAJOR:
                for (int i = 0; i < TOTALPITCHES; i++) {
                    if (i % SEMITONES == MODC || i % SEMITONES == MODF || i % SEMITONES == MODG || i % SEMITONES == MODD || i % SEMITONES == MODA || i % SEMITONES == MODE) {
                        keySigVector.set(i, Accidental.SHARP);
                    } else {
                        keySigVector.set(i, Accidental.NATURAL);
                    }
                }
                break;
            case CSMAJOR:
                for (int i = 0; i < TOTALPITCHES; i++) {
                    if (i % SEMITONES == MODC || i % SEMITONES == MODF || i % SEMITONES == MODG || i % SEMITONES == MODD || i % SEMITONES == MODA || i % SEMITONES == MODE || i % SEMITONES == MODB) {
                        keySigVector.set(i, Accidental.SHARP);
                    } else {
                        keySigVector.set(i, Accidental.NATURAL);
                    }
                }
                break;
        }
        return keySigVector;
    }

    /**
   * Sets whether in extracting a melodypart, the ending should be truncated
   * if necessary
   * @param b   boolean whether or not to truncate the endings
   */
    public void truncateEndings(boolean b) {
        fudgeEnding = b;
    }

    /**
   * Returns a MelodyPart that contains the Units within the slot range specified.
   * @param first     the first slot in the range
   * @param last      the last slot in the range
   * @return MelodyPart     the MelodyPart that contains the extracted chunk
   */
    @Override
    public MelodyPart extract(int first, int last) {
        MelodyPart newPart = new MelodyPart();
        int i = first;
        int lastUnitIndex = first;
        if (getUnit(first) == null) {
            for (i = first + 1; i <= last; i++) {
                if (getUnit(i) != null) {
                    break;
                }
            }
            newPart.addNote(new Rest(i - first));
        }
        for (; i <= last; i++) {
            if (getUnit(i) != null) {
                newPart.addUnit((Note) getUnit(i).copy());
                lastUnitIndex = i;
            }
        }
        if (fudgeEnding) {
            Unit lastUnit = newPart.getPrevUnit(newPart.getSize());
            if (lastUnit != null && lastUnitIndex + lastUnit.getRhythmValue() > last) {
                int oldRhythmValue = lastUnit.getRhythmValue();
                lastUnit.setRhythmValue(last + 1 - lastUnitIndex);
                int amountTruncated = oldRhythmValue - lastUnit.getRhythmValue();
                newPart.setSize(newPart.size() - amountTruncated);
            }
        }
        return newPart;
    }

    /**
   * Returns a MelodyPart that contains the Units within the slot range specified,
   *         but in reverse order.
   * @param first     the first slot in the range
   * @param last      the last slot in the range
   * @return MelodyPart     the MelodyPart that contains the extracted chunk
   */
    public MelodyPart extractReverse(int first, int last) {
        MelodyPart newPart = new MelodyPart();
        for (int i = last; i >= first; i--) {
            if (getUnit(i) != null) {
                newPart.addUnit(getUnit(i).copy());
            }
        }
        return newPart;
    }

    /**
   * Returns a MelodyPart that contains the Units within the slot range specified,
   *         but time-warped by num/denom
   * @param first     the first slot in the range
   * @param last      the last slot in the range
   * @return MelodyPart     the MelodyPart that contains the extracted chunk
   */
    public MelodyPart extractTimeWarped(int first, int last, int num, int denom) {
        MelodyPart newPart = new MelodyPart();
        Trace.log(2, "extractTimeWarped from " + first + " to " + last + " by " + num + "/" + denom);
        for (int i = first; i < last; i++) {
            if (getUnit(i) != null) {
                Unit newUnit = getUnit(i).copy();
                newUnit.setRhythmValue((newUnit.getRhythmValue() * num) / denom);
                newPart.addUnit(newUnit);
            }
        }
        newPart.addUnit(new Rest(15));
        return newPart;
    }

    /**
   * Returns a MelodyPart that contains the Units within the slot range specified,
   *         but inverted.
   * Inversion is with respect to the highest and lowest notes in the series
   * and is done by ordering the notes, not using a mathematical formula.
   *
   * @param first     the first slot in the range
   * @param last      the last slot in the range
   * @return MelodyPart     the MelodyPart that contains the extracted chunk
   */
    public MelodyPart extractInverse(int first, int last) {
        LinkedList<Note> ordered = new LinkedList<Note>();
        for (int i = first; i <= last; i++) {
            Unit unit = getUnit(i);
            if (unit != null && unit instanceof Note) {
                Note note = (Note) unit;
                int pitch = note.getPitch();
                if (pitch != REST) {
                    boolean found = false;
                    ListIterator<Note> it = ordered.listIterator(0);
                    int position = 0;
                    while (!found && it.hasNext()) {
                        Note element = it.next();
                        int thatPitch = element.getPitch();
                        if (pitch < thatPitch) {
                            ordered.add(position, note);
                            found = true;
                        } else if (thatPitch == pitch) {
                            found = true;
                        }
                        position++;
                    }
                    if (!found) {
                        ordered.add(note);
                    }
                }
            }
        }
        Object orderedArray = ordered.toArray();
        int length = Array.getLength(orderedArray);
        MelodyPart newPart = new MelodyPart();
        for (int i = first; i <= last; i++) {
            if (getUnit(i) != null) {
                Note note = (Note) getUnit(i);
                Note newNote = null;
                int pitch = note.getPitch();
                if (pitch == REST) {
                    newNote = note.copy();
                } else {
                    boolean found = false;
                    for (int j = 0; !found && j < length; j++) {
                        if (((Note) Array.get(orderedArray, j)).getPitch() == pitch) {
                            Note inverse = (Note) Array.get(orderedArray, length - 1 - j);
                            newNote = inverse.copy();
                            newNote.setRhythmValue(note.getRhythmValue());
                            found = true;
                            break;
                        }
                    }
                    assert (found);
                }
                newPart.addUnit(newNote);
            }
        }
        return newPart;
    }

    public Note getLastNote() {
        int tracker = this.getPrevIndex(size);
        Note n = this.getNote(tracker);
        while (n.isRest()) {
            tracker = this.getPrevIndex(tracker);
            n = this.getNote(tracker);
        }
        return n;
    }

    /**
     * The only current use is in LickgenFrame.
     * @param selectionStart
     * @param numSlots
     * @returns whether a melody is empty from selectionStart to selectionStart + numSlots
     */
    public boolean melodyIsEmpty(int selectionStart, int numSlots) {
        if (selectionStart < 0) return false;
        int tracker = this.getPrevIndex(selectionStart);
        Note n = this.getNote(tracker);
        if (n != null && (!n.isRest()) && n.getRhythmValue() > selectionStart - tracker) {
            return false;
        }
        n = this.getNote(selectionStart);
        if (n != null && (!n.isRest())) return false;
        tracker = this.getNextIndex(selectionStart);
        while (tracker < selectionStart + numSlots) {
            n = this.getNote(tracker);
            tracker = this.getNextIndex(tracker);
            if (n == null) return true;
            if (!n.isRest()) return false;
        }
        return true;
    }

    public int getInitialBeatsRest() {
        int count = 0;
        for (Unit note : slots) {
            if (note != null && !((Note) note).isRest()) {
                break;
            }
            count++;
        }
        return count / BEAT;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        int n = slots.size();
        int currentVolume = 127;
        for (int i = 0; i < n; i++) {
            Note note = (Note) slots.get(i);
            if (note != null) {
                if (note.getVolume() != currentVolume) {
                    currentVolume = note.getVolume();
                    buffer.append("v");
                    buffer.append(currentVolume);
                    buffer.append(" ");
                }
                buffer.append(note.toLeadsheet());
                buffer.append(" ");
            }
        }
        return buffer.toString();
    }
}
