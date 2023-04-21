package mtn.sevenuplive.modes;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import mtn.sevenuplive.m4l.M4LMidiOut;
import mtn.sevenuplive.m4l.Note;
import mtn.sevenuplive.main.MonomeUp;
import mtn.sevenuplive.modes.events.ClearDisplayEvent;
import mtn.sevenuplive.modes.events.ClearNavEvent;
import mtn.sevenuplive.modes.events.Event;
import mtn.sevenuplive.modes.events.EventDispatcherImpl;
import mtn.sevenuplive.modes.events.EventListener;
import mtn.sevenuplive.modes.events.MenuFocusEvent;
import mtn.sevenuplive.modes.events.UpdateDisplayEvent;
import mtn.sevenuplive.modes.events.UpdateNavEvent;
import mtn.sevenuplive.scales.Scale;
import mtn.sevenuplive.scales.ScaleName;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * @author Adam Ribaudo
 * Melodizer Class.
 * 
 * Contains the logic involved in recording and playing back sequences of melodies for a MonomeUp.
 */
public class MelodizerModel extends EventDispatcherImpl implements PlayContext, EventListener {

    /** 
	 * This is used to record the key state when a recorded pattern
	 * is triggered. And use it in transpose mode to compute the key offset  
	 */
    public int startingKey[];

    public int lastKey[];

    /** This is currently active key for each slot */
    public int key[];

    /** This is the scale offset/position in degrees for each slot */
    public int offset[];

    public int displayNote[][];

    public enum eMelodizerMode {

        KEYBOARD, CLIP, NONE, POSITION
    }

    ;

    public eMelodizerMode currentMode = eMelodizerMode.KEYBOARD;

    public eMelodizerMode altMode = eMelodizerMode.KEYBOARD;

    /** 
	 * This is used to record the offset state when a recorded pattern
	 * is triggered. And use it in transpose mode to compute the offset  
	 */
    public int startingOffset[];

    public int lastOffset[];

    public Hashtable<Integer, NoteSequence> sequences;

    private boolean[] cuedIndex;

    private boolean[] isRecording;

    public boolean transpose = false;

    public boolean transposeDirty[];

    public int clipNotes[][];

    public boolean[][] heldNote;

    public boolean[][] newHeldNote;

    public M4LMidiOut midiMelodyOut[];

    private Scale melodyScale;

    private Scale clipScale = new Scale(ScaleName.Chromatic);

    private int recMode = ModeConstants.MEL_ON_BUTTON_PRESS;

    /** Transpose groups for slots 0-7. -1 value means no group */
    private int[] transposeGroup;

    /** 
	 * These Note off messages are delayed until next note off or on is played in that sequence.
	 * This helps transposition retriggering to appear smoother
	 * Keyed by sequence # 
	 */
    private HashMap<Integer, ArrayList<Note>> delayedNoteOffs = new HashMap<Integer, ArrayList<Note>>();

    /** Delay the sending off note offs on transposed notes for a sequence */
    private boolean[] delayNoteOffs;

    private int _navRow;

    public MelodizerModel(int _navRow, M4LMidiOut _midiMelodyOut[], int grid_width, int grid_height) {
        midiMelodyOut = _midiMelodyOut;
        displayNote = new int[7][128];
        key = new int[7];
        transposeGroup = new int[7];
        for (int i = 0; i < transposeGroup.length; i++) transposeGroup[i] = -1;
        offset = new int[7];
        delayNoteOffs = new boolean[7];
        transposeDirty = new boolean[7];
        startingKey = new int[7];
        startingOffset = new int[7];
        lastKey = new int[7];
        lastOffset = new int[7];
        cuedIndex = new boolean[7];
        isRecording = new boolean[7];
        sequences = new Hashtable<Integer, NoteSequence>();
        heldNote = new boolean[7][128];
        clipNotes = new int[7][128];
        newHeldNote = new boolean[7][128];
        melodyScale = new Scale(ScaleName.Major);
        this._navRow = _navRow;
        subscribe(new MenuFocusEvent(), this);
    }

    public boolean isRecording(int slot) {
        return isRecording[slot];
    }

    public void setIsRecording(int slot, boolean value) {
        isRecording[slot] = value;
        sendEvent(new UpdateNavEvent(slot));
    }

    public void onEvent(Event e) {
        if (e.getType().equals(MenuFocusEvent.MENU_FOCUS_EVENT)) {
            onMenuFocusChange((MenuFocusEvent) e);
        }
    }

    /**
	 * Called when NavMenu change is being cued, aborted or committed
	 */
    public void onMenuFocusChange(MenuFocusEvent event) {
        if (event.oldIndex == _navRow) {
            if (event.type == MenuFocusEvent.eMenuFocusEvent.MENU_FOCUS_CHANGE_ABORTED) {
                swapModes();
            }
        }
    }

    /**
	 * Reset the notes being displayed in all views attached to this slot 
	 * @param slot
	 */
    public void resetDisplayNotes(int slot) {
        for (int i = 0; i < 128; i++) {
            displayNote[slot][i] = DisplayGrid.OFF;
        }
        sendEvent(new UpdateDisplayEvent(slot));
    }

    /**
	 * If note is out of midi range 1-128
	 * return 0 
	 * @param note
	 * @return
	 */
    private int clipRange(int note) {
        if (note < 0 || note > 128) return 0; else return note;
    }

    /**
	 * Calculate the note under a pad in the grid
	 * Taking into account the scale and the degree 
	 * offset within the scale.
	 * 
	 * @param x
	 * @param y
	 * @param sequence which sequence are we operating on
	 * @return
	 */
    public int convertGridPositionToNote(int x, int y, int sequence) {
        int note = (((8 - y) * 12 - 12) + (Math.abs((x + offset[sequence]) / melodyScale.Degrees.length) * 12) + melodyScale.Degrees[((x + offset[sequence]) % melodyScale.Degrees.length)] + key[sequence]);
        return clipRange(note);
    }

    /**
	 * Calculate the note under a pad in the grid
	 * Taking into account the scale only 
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
    private int convertGridPositionToNoteNoOffset(int x, int y, int key) {
        int note = (((8 - y) * 12 - 12) + melodyScale.Degrees[x % melodyScale.Degrees.length] + key);
        return clipRange(note);
    }

    /**
	 * Calculate the note under a pad in the grid
	 * Taking into account the Context 
	 * 
	 * @param x
	 * @param y
	 * @param tc
	 * @return
	 */
    private int convertGridPositionToNoteWithContext(int x, int y, TranspositionContext tc) {
        int note = (((8 - y) * 12 - 12) + (Math.abs((x + tc.localOffset) / melodyScale.Degrees.length) * 12) + melodyScale.Degrees[((x + tc.localOffset) % melodyScale.Degrees.length)] + tc.key + tc.localKeyOffset);
        return clipRange(note);
    }

    /**
	 * Calculate the note under a pad in the grid
	 * Taking into account it's clip launch mode 
	 * This should be C Major Scale
	 * @param x
	 * @param y
	 * @return
	 */
    public int convertGridPositionToClipNote(int x, int y) {
        int note = (((8 - y) * 12 - 12) + clipScale.Degrees[x % clipScale.Degrees.length]);
        return clipRange(note);
    }

    /**
	 * Calculate the note under a pad in the grid
	 * Taking into account the scale and the degree 
	 * offset within the scale.
	 * 
	 * @param note 0-127
	 * @param sequence which sequence are we operating on
	 * @return First grid position, higher coordinate top/left if duplicates, null if not found
	 */
    public GridPosition convertNoteToGridPosition(int note, int sequence) {
        int gridNote;
        for (int j = 0; j < 7; j++) {
            for (int k = -2; k < 10; k++) {
                gridNote = convertGridPositionToNote(j, k, sequence);
                if (gridNote == note) {
                    return new GridPosition(this.melodyScale, j, k);
                }
            }
        }
        return null;
    }

    /**
	 * Calculate the note under a pad in the grid
	 * Taking into account the scale only 
	 * 
	 * @param note 0-127
	 * @param key
	 * @return First grid position, higher coordinate top/left if duplicates, null if not found
	 */
    private GridPosition convertNoteToGridPositionNoOffset(int note, int key) {
        int gridNote;
        for (int j = 0; j < 7; j++) {
            for (int k = -2; k < 10; k++) {
                gridNote = convertGridPositionToNoteNoOffset(j, k, key);
                if (gridNote == note) {
                    return new GridPosition(this.melodyScale, j, k);
                }
            }
        }
        return null;
    }

    public int getKeyFromCoords(int x, int y) {
        if (x == 0 && y == 7) return 0; else if (x == 1 && y == 6) return 1; else if (x == 1 && y == 7) return 2; else if (x == 2 && y == 6) return 3; else if (x == 2 && y == 7) return 4; else if (x == 3 && y == 7) return 5; else if (x == 4 && y == 6) return 6; else if (x == 4 && y == 7) return 7; else if (x == 5 && y == 6) return 8; else if (x == 5 && y == 7) return 9; else if (x == 6 && y == 6) return 10; else if (x == 6 && y == 7) return 11; else return 0;
    }

    public void heartbeat() {
        ArrayList<Note> noteList;
        NoteSequence s;
        Hashtable<Integer, ArrayList<Note>> notePackage = new Hashtable<Integer, ArrayList<Note>>();
        Integer index;
        for (Enumeration<Integer> els = sequences.keys(); els.hasMoreElements(); ) {
            index = Integer.class.cast(els.nextElement());
            s = sequences.get(index);
            if (transposeDirty[index] && transpose) {
                ArrayList<Note> notesHeld = s.getHeldNotesAtPlayedPitch(index);
                for (int i = 0; i < notesHeld.size(); i++) {
                    if (delayNoteOffs[index]) {
                        ArrayList<Note> delayedNotes = delayedNoteOffs.get(index);
                        if (delayedNotes == null) {
                            delayedNotes = new ArrayList<Note>();
                            delayedNoteOffs.put(index, delayedNotes);
                        }
                        delayedNotes.add(notesHeld.get(i));
                    } else {
                        midiMelodyOut[index].sendNoteOff(notesHeld.get(i));
                    }
                    displayNote(index, notesHeld.get(i).getPitch(), DisplayGrid.OFF);
                }
            }
            noteList = s.heartbeat();
            if (noteList != null) {
                notePackage.put(index, noteList);
            }
            if (transposeDirty[index] && transpose) {
                transposeDirty[index] = false;
                markStartTransposeOffsets(index);
            }
        }
        Note note;
        for (Enumeration<Integer> els = notePackage.keys(); els.hasMoreElements(); ) {
            index = Integer.class.cast(els.nextElement());
            noteList = notePackage.get(index);
            for (int i = 0; i < noteList.size(); i++) {
                note = noteList.get(i);
                if (note.getVelocity() > 0) {
                    sendDelayedNoteOffs(index);
                    midiMelodyOut[index].sendNoteOn(note);
                    displayNote(index, note.getPitch(), DisplayGrid.SOLID);
                } else {
                    sendDelayedNoteOffs(index);
                    midiMelodyOut[index].sendNoteOff(note);
                    displayNote(index, note.getPitch(), DisplayGrid.OFF);
                }
                updateDisplayGrid(index);
            }
        }
    }

    /**
	 * Send any delayed note offs
	 * @param index
	 */
    private void sendDelayedNoteOffs(int index) {
        ArrayList<Note> delayedNotes = delayedNoteOffs.get(index);
        if (delayedNotes == null) return;
        for (Note note : delayedNotes) {
            midiMelodyOut[index].sendNoteOff(note);
        }
        delayedNoteOffs.put(index, new ArrayList<Note>());
    }

    /**
	 * Send events to our views to update this slot
	 * @param slot slot to update or -1 means update All
	 */
    public void updateDisplayGrid(int slot) {
        sendEvent(new UpdateDisplayEvent(slot));
    }

    /**
	 * Locator event for one pattern slot
	 * @param slot
	 */
    public void locatorEvent(int slot) {
        if (sequences.containsKey(slot)) {
            sequences.get(slot).locatorEvent();
        }
    }

    /**
	 * Locator event for all slots
	 */
    public void locatorEvent() {
        for (int i = 0; i < 7; i++) {
            locatorEvent(i);
        }
    }

    /**
	 * Send events to our views to update this slot
	 * @param slot slot to update or -1 means update All
	 */
    public void updateNavGrid(int slot) {
        sendEvent(new UpdateNavEvent(slot));
    }

    /**
	 * Send events to our views to clearnav grid
	 */
    public void clearNavGrid() {
        sendEvent(new ClearNavEvent());
    }

    /**
	 * Send events to our views to clear display grid
	 */
    public void clearDisplayGrid() {
        sendEvent(new ClearDisplayEvent());
    }

    /**
	 * Display this note in a certain way
	 * @param slot slot to update or -1 means update All
	 * @param pitch pitch to set in display
	 * @param displaystate for example  DisplayGrid.SOLID or DisplayGrid.OFF
	 */
    public void displayNote(int slot, int pitch, int displaystate) {
        displayNote[slot][pitch] = displaystate;
    }

    /**
	 * Return the display value of a note
	 * @param slot
	 * @param pitch
	 * @return
	 */
    public int getDisplayNote(int slot, int pitch) {
        return displayNote[slot][pitch];
    }

    /**
	 * Beginning a cue creates a new sequence if one does not exist at the specified index.  Otherwise, it begins cueing on the existing sequence.
	 * 
	 * @param seqIndex
	 */
    public void beginCue(int seqIndex) {
        markSequenceOffsets(seqIndex);
        if (!sequences.containsKey(seqIndex)) {
            sequences.put(seqIndex, new NoteSequence(seqIndex, this));
            setRecMode(recMode);
        }
        sequences.get(seqIndex).beginCue();
        cuedIndex[seqIndex] = true;
    }

    public void endRecording(int slot) {
        sequences.get(slot).endRecording();
        snapToMarkedSequenceOffsets(slot);
        cuedIndex = new boolean[7];
    }

    /**
	 * Sends a note to the currently cued sequence in a slot.  If no sequence is cued, the note will be ignored.
	 * 
	 * @param note
	 */
    public void addEvent(int slot, Note note) {
        if (cuedIndex[slot]) {
            sequences.get(slot).addEvent(note);
        }
    }

    /**
	 * Play to particular sequence
	 * @param seqIndex
	 * @return returns true if the sequence exists and has started playing and false otherwise
	 */
    public boolean playSeq(int seqIndex) {
        if (sequences.containsKey(seqIndex)) {
            sequences.get(seqIndex).play();
            return true;
        } else return false;
    }

    public void stopSeq(int seqIndex) {
        if (sequences.containsKey(seqIndex)) {
            NoteSequence sequence = sequences.get(seqIndex);
            sequence.stop();
            ArrayList<Note> noteList;
            noteList = sequence.getHeldNotesAtPlayedPitch(seqIndex);
            for (int i = 0; i < noteList.size(); i++) {
                midiMelodyOut[seqIndex].sendNoteOff(noteList.get(i));
                displayNote(seqIndex, noteList.get(i).getPitch(), DisplayGrid.OFF);
            }
            sendDelayedNoteOffs(seqIndex);
            updateDisplayGrid(seqIndex);
        }
    }

    public int getSeqStatus(int index) {
        if (sequences.containsKey(index)) return sequences.get(index).getStatus(); else return -1;
    }

    public Element toXMLElement(String ElementName) {
        Element xmlMelodizer = new Element(ElementName);
        xmlMelodizer.setAttribute(new Attribute("scale", melodyScale.Name.toString()));
        xmlMelodizer.setAttribute(new Attribute("clipMode", currentMode == eMelodizerMode.CLIP ? "true" : "false"));
        xmlMelodizer.setAttribute(new Attribute("melodizerMode", currentMode.toString()));
        xmlMelodizer.setAttribute(new Attribute("altMode", altMode.toString()));
        xmlMelodizer.setAttribute(new Attribute("transpose", Boolean.toString(transpose)));
        xmlMelodizer.setAttribute(new Attribute("recMode", Integer.toString(recMode)));
        String sustainString = "";
        for (int i = 0; i < this.delayNoteOffs.length; i++) {
            sustainString += delayNoteOffs[i];
            if (i != delayNoteOffs.length - 1) sustainString += ",";
        }
        xmlMelodizer.setAttribute(new Attribute("sustain", sustainString));
        String groupString = "";
        for (int i = 0; i < transposeGroup.length; i++) {
            groupString += transposeGroup[i];
            if (i != transposeGroup.length - 1) groupString += ",";
        }
        xmlMelodizer.setAttribute(new Attribute("groups", groupString));
        String keyString = "";
        for (int i = 0; i < key.length; i++) {
            keyString += key[i];
            if (i != key.length - 1) keyString += ",";
        }
        xmlMelodizer.setAttribute(new Attribute("key", keyString));
        String offsetString = "";
        for (int i = 0; i < offset.length; i++) {
            offsetString += offset[i];
            if (i != offset.length - 1) offsetString += ",";
        }
        xmlMelodizer.setAttribute(new Attribute("offset", offsetString));
        String startingOffsetString = "";
        for (int i = 0; i < startingOffset.length; i++) {
            startingOffsetString += startingOffset[i];
            if (i != startingOffset.length - 1) startingOffsetString += ",";
        }
        xmlMelodizer.setAttribute(new Attribute("startingOffset", startingOffsetString));
        String startingKeyString = "";
        for (int i = 0; i < startingKey.length; i++) {
            startingKeyString += startingKey[i];
            if (i != startingKey.length - 1) startingKeyString += ",";
        }
        xmlMelodizer.setAttribute(new Attribute("startingKey", startingKeyString));
        Element xmlSequence;
        int seqIndex;
        NoteSequence sequence;
        for (Enumeration<Integer> els = sequences.keys(); els.hasMoreElements(); ) {
            seqIndex = Integer.class.cast(els.nextElement());
            sequence = sequences.get(seqIndex);
            xmlSequence = sequence.toJDOMXMLElement();
            xmlSequence.setAttribute(new Attribute("key", ((Integer) key[seqIndex]).toString()));
            xmlMelodizer.addContent(xmlSequence);
        }
        return xmlMelodizer;
    }

    @SuppressWarnings("unchecked")
    public void loadXMLElement(Element xmlMelodizer) {
        cuedIndex = new boolean[7];
        sequences = new Hashtable<Integer, NoteSequence>();
        melodyScale = new Scale(ScaleName.valueOf(xmlMelodizer.getAttribute("scale").getValue()));
        try {
            if (xmlMelodizer.getAttribute("clipMode").getBooleanValue() == true) {
                currentMode = eMelodizerMode.CLIP;
            }
            if (xmlMelodizer.getAttribute("melodizerMode") != null) {
                currentMode = eMelodizerMode.valueOf(xmlMelodizer.getAttribute("melodizerMode").getValue());
            }
            if (xmlMelodizer.getAttribute("altMode") != null) {
                altMode = eMelodizerMode.valueOf(xmlMelodizer.getAttribute("altMode").getValue());
            }
            if (xmlMelodizer.getAttribute("transpose") != null) {
                transpose = Boolean.parseBoolean(xmlMelodizer.getAttribute("transpose").getValue());
            }
            if (xmlMelodizer.getAttribute("recMode") != null) {
                recMode = Integer.parseInt(xmlMelodizer.getAttribute("recMode").getValue());
            }
        } catch (Throwable t) {
        }
        try {
            String sustainString = xmlMelodizer.getAttribute("sustain").getValue();
            int i = 0;
            for (String strSustain : sustainString.split(",")) {
                delayNoteOffs[i] = Boolean.parseBoolean(strSustain);
                i++;
            }
        } catch (Throwable t) {
        }
        try {
            String groupString = xmlMelodizer.getAttribute("groups").getValue();
            int i = 0;
            for (String strGroup : groupString.split(",")) {
                transposeGroup[i] = Integer.parseInt(strGroup);
                i++;
            }
        } catch (Throwable t) {
        }
        try {
            String keyString = xmlMelodizer.getAttribute("key").getValue();
            int i = 0;
            for (String strKey : keyString.split(",")) {
                key[i] = Integer.parseInt(strKey);
                i++;
            }
        } catch (Throwable t) {
        }
        try {
            String offsetString = xmlMelodizer.getAttribute("offset").getValue();
            int j = 0;
            for (String strOffset : offsetString.split(",")) {
                offset[j] = Integer.parseInt(strOffset);
                j++;
            }
        } catch (Throwable t) {
        }
        try {
            String startingOffsetString = xmlMelodizer.getAttribute("startingOffset").getValue();
            int k = 0;
            for (String strStartingOffset : startingOffsetString.split(",")) {
                startingOffset[k] = Integer.parseInt(strStartingOffset);
                k++;
            }
        } catch (Throwable t) {
        }
        try {
            String startingKeyString = xmlMelodizer.getAttribute("startingKey").getValue();
            int l = 0;
            for (String strStartingKey : startingKeyString.split(",")) {
                startingKey[l] = Integer.parseInt(strStartingKey);
                l++;
            }
        } catch (Throwable t) {
        }
        List<Element> xmlSequences;
        NoteSequence sequence;
        Integer index;
        xmlSequences = xmlMelodizer.getChildren();
        for (Element xmlSequence : xmlSequences) {
            index = Integer.parseInt(xmlSequence.getAttributeValue("index"));
            key[index] = Integer.parseInt(xmlSequence.getAttributeValue("key"));
            sequence = new NoteSequence(index, this);
            setRecMode(recMode);
            sequence.loadJDOMXMLElement(xmlSequence);
            if (!sequence.isEmpty()) sequence.setStatus(MonomeUp.STOPPED);
            sequences.put(index, sequence);
        }
        updateDisplayGrid(-1);
    }

    public void reset() {
        for (int i = 0; i < 7; i++) stopSeq(i);
    }

    public void clipLaunch(int pitch, int vel, int channel) {
        if (vel == 0 || vel == 64) {
            clipNotes[channel][pitch] = DisplayGrid.OFF;
        } else if (vel == 125) {
            clipNotes[channel][pitch] = DisplayGrid.FASTBLINK;
        } else if (vel == 126) {
            clipNotes[channel][pitch] = DisplayGrid.SLOWBLINK;
        } else if (vel == 127 || vel == 1) {
            clipNotes[channel][pitch] = DisplayGrid.SOLID;
        }
        updateDisplayGrid(channel);
    }

    public Scale getScale() {
        return melodyScale;
    }

    public void setScale(Scale newScale) {
        melodyScale = newScale;
    }

    public void setRecMode(int _recMode) {
        recMode = _recMode;
        Integer sequenceIndex;
        for (Enumeration<Integer> els = sequences.keys(); els.hasMoreElements(); ) {
            sequenceIndex = Integer.class.cast(els.nextElement());
            sequences.get(sequenceIndex).setMelRecMode(recMode);
        }
    }

    public int getRecMode() {
        return recMode;
    }

    public void extNoteOn(Note note, int channel) {
        int melodizerChannel = channel - 8;
        if (note.getVelocity() > 0 && note.getStatus() == 144) {
            heldNote[melodizerChannel][note.getPitch()] = true;
            midiMelodyOut[melodizerChannel].sendNoteOn(note);
            displayNote(melodizerChannel, note.getPitch(), DisplayGrid.SOLID);
            addEvent(melodizerChannel, note);
        } else {
            Note releaseNote = new Note(note.getPitch(), 0, 0);
            midiMelodyOut[melodizerChannel].sendNoteOff(releaseNote);
            heldNote[melodizerChannel][note.getPitch()] = false;
            displayNote(melodizerChannel, note.getPitch(), DisplayGrid.OFF);
            addEvent(melodizerChannel, releaseNote);
        }
        updateDisplayGrid(-1);
    }

    public eMelodizerMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(eMelodizerMode currentMode) {
        this.currentMode = currentMode;
        updateNavGrid(-1);
        updateDisplayGrid(-1);
    }

    public void setAltMode(eMelodizerMode altCurrentMode) {
        this.altMode = altCurrentMode;
    }

    public eMelodizerMode getAltMode() {
        return altMode;
    }

    public void swapModes() {
        eMelodizerMode oldCurrentMode = currentMode;
        setCurrentMode(this.altMode);
        setAltMode(oldCurrentMode);
    }

    public boolean getTranspose() {
        return transpose;
    }

    public void setTranspose(boolean transpose) {
        clearDisplayGrid();
        if (transpose) {
            for (int i = 0; i < 7; i++) {
                transposeDirty[i] = true;
            }
        }
        this.transpose = transpose;
    }

    public void setTransposeGroup(int slotNum, int group) {
        transposeGroup[slotNum] = group;
    }

    public int getTransposeGroup(int slotNum) {
        return transposeGroup[slotNum];
    }

    public void setTransposeSustain(int slotNum, boolean value) {
        delayNoteOffs[slotNum] = value;
    }

    public boolean getTransposeSustain(int slotNum) {
        return delayNoteOffs[slotNum];
    }

    /**
	 * Mark the state of key and offsets on a sequence
	 * @param seqIndex
	 */
    private void markSequenceOffsets(int seqIndex) {
        startingOffset[seqIndex] = offset[seqIndex];
        startingKey[seqIndex] = key[seqIndex];
        markStartTransposeOffsets(seqIndex);
    }

    /**
	 * Move to the last marked sequence offsets
	 * @param seqIndex
	 */
    private void snapToMarkedSequenceOffsets(int seqIndex) {
        offset[seqIndex] = startingOffset[seqIndex];
        key[seqIndex] = startingKey[seqIndex];
    }

    public void markStartTransposeOffsets(int seqIndex) {
        lastKey[seqIndex] = key[seqIndex];
        lastOffset[seqIndex] = offset[seqIndex];
    }

    public void markStartTransposeOffsets() {
        for (int seqIndex = 0; seqIndex < 7; seqIndex++) {
            lastKey[seqIndex] = key[seqIndex];
            lastOffset[seqIndex] = offset[seqIndex];
        }
    }

    /**
	 * Perform pitch transformation if
	 * requested on running note sequences
	 * 
	 */
    public ArrayList<Note> transpose(ArrayList<Note> notes, int transpositionIndex) {
        if (!transpose || notes == null) return notes;
        ArrayList<Note> newNotes = new ArrayList<Note>();
        Note newNote;
        for (Note note : notes) {
            newNote = transposeWithContext(note, getTranspositionContext(transpositionIndex));
            if (newNote != null) newNotes.add(newNote);
        }
        return newNotes;
    }

    public Note transposeWithContext(Note note, TranspositionContext tc) {
        int pitch = note.getPitch();
        pitch = pitch + tc.localKeyOffset;
        GridPosition pos = convertNoteToGridPositionNoOffset(pitch, tc.key);
        if (pos != null) {
            GridPosition newpos = pos.offsetX(tc.localOffset);
            pitch = convertGridPositionToNoteNoOffset(newpos.x, newpos.y, tc.key);
            return new Note(pitch, note.getVelocity(), note.getLength());
        }
        return null;
    }

    public TranspositionContext getTranspositionContext(int sequence) {
        return new TranspositionContext(sequence, offset[sequence] - startingOffset[sequence], key[sequence] - startingKey[sequence], key[sequence]);
    }

    public static void main(String args[]) {
        try {
            MelodizerModel.GridPosition grid = new MelodizerModel.GridPosition(new Scale(ScaleName.Chromatic), 0, 0);
            if (!testScaleArithmetic(grid)) throw new Exception("Chromatic 0,0 test failed");
            grid = new MelodizerModel.GridPosition(new Scale(ScaleName.Chromatic), 1, -1);
            if (!testScaleArithmetic(grid)) throw new Exception("Chromatic -1,1 test failed");
            grid = new MelodizerModel.GridPosition(new Scale(ScaleName.Chromatic), 7, -10);
            if (!testScaleArithmetic(grid)) throw new Exception("Chromatic -10,10 test failed");
            grid = new MelodizerModel.GridPosition(new Scale(ScaleName.Pentatonic), 0, 0);
            if (!testScaleArithmetic(grid)) throw new Exception("Pentatonic 0,0 test failed");
            grid = new MelodizerModel.GridPosition(new Scale(ScaleName.Pentatonic), 1, -1);
            if (!testScaleArithmetic(grid)) throw new Exception("Pentatonic -1,1 test failed");
            grid = new MelodizerModel.GridPosition(new Scale(ScaleName.Pentatonic), 7, -10);
            if (!testScaleArithmetic(grid)) throw new Exception("Pentatonic -10,10 test failed");
            System.out.println("Tests succeeded!!");
        } catch (Exception e) {
            System.err.println("Test failed!! " + e);
        }
    }

    private static boolean testScaleArithmetic(MelodizerModel.GridPosition grid) {
        MelodizerModel.GridPosition gridStart = grid;
        for (int i = 0; i < 64; i++) {
            grid = grid.offsetX(1);
            System.out.println(grid);
        }
        for (int i = 0; i < 64; i++) {
            grid = grid.offsetX(-1);
            System.out.println(grid);
        }
        for (int i = 0; i < 64; i++) {
            grid = grid.offsetX(-3);
            System.out.println(grid);
        }
        for (int i = 0; i < 64; i++) {
            grid = grid.offsetX(3);
            System.out.println(grid);
        }
        for (int i = 0; i < 64; i++) {
            grid = grid.offsetX(6);
            System.out.println(grid);
        }
        for (int i = 0; i < 64; i++) {
            grid = grid.offsetX(-6);
            System.out.println(grid);
        }
        for (int i = 0; i < 64; i++) {
            grid = grid.offsetX(7);
            System.out.println(grid);
        }
        for (int i = 0; i < 64; i++) {
            grid = grid.offsetX(-7);
            System.out.println(grid);
        }
        return gridStart.equals(grid);
    }

    public static class GridPosition {

        private static final int DIM_X = 7;

        private int degrees = 7;

        private Scale scale;

        public int x;

        public int y;

        public GridPosition(Scale scale, int x, int y) {
            this.scale = scale;
            if (scale != null) this.degrees = scale.Degrees.length;
            int width = Math.min(degrees, DIM_X);
            this.x = (x % width);
            this.y = y - Math.abs(x / width);
        }

        /** 
		 * Add x to the grid position
		 * Return a new grid representing the new position.
		 * Existing grid is untouched
		 */
        public GridPosition offsetX(int offset_x) {
            int new_x = 0;
            int new_y = 0;
            int width = Math.min(degrees, DIM_X);
            int multiplier = Math.round(Math.signum(offset_x + x));
            if (multiplier < 0) {
                new_x = width + ((offset_x + x) % width);
                if (Math.abs((offset_x + x) / width) == 0) new_y = y + 1 + (Math.abs((offset_x + x) / width) * multiplier); else new_y = y + 1 - (Math.abs((offset_x + x) / width) * multiplier);
            } else if (multiplier == 0) {
                new_x = 0;
                new_y = y;
            } else {
                new_x = (offset_x + x) % width;
                new_y = y - (Math.abs((offset_x + x) / width) * multiplier);
            }
            return new GridPosition(this.scale, new_x, new_y);
        }

        public boolean equals(GridPosition pos) {
            if (pos.x == this.x && pos.y == this.y) return true; else return false;
        }

        public String toString() {
            return Integer.toString(x) + "," + Integer.toString(y);
        }
    }
}
