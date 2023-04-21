package com.tjoris.midigateway;

class MidiEntry {

    private static final String[] kNOTES = new String[] { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };

    private final IMidiAction fAction;

    private final String fName;

    private int fNote = -1;

    private String fNoteString;

    public MidiEntry(final IMidiAction action, final Configuration configuration) {
        fAction = action;
        fName = fAction.getName();
        final String note = configuration.getUserProperty(fAction.getID());
        if (note != null) {
            setNote(Integer.parseInt(note));
        }
    }

    public String getName() {
        return fName;
    }

    public synchronized int getNote() {
        return fNote;
    }

    public synchronized String getNoteString() {
        return fNoteString;
    }

    public synchronized void setNote(final int note) {
        fNote = note;
        if (note < 0) {
            fNoteString = null;
        } else if (note < 128) {
            final int index = fNote % (byte) 0x0C;
            final int octave = (fNote / (byte) 0x0C) - 2;
            fNoteString = kNOTES[index] + octave;
        } else {
            fNoteString = "CC " + (note - 128);
        }
    }

    public IMidiAction getAction() {
        return fAction;
    }
}
