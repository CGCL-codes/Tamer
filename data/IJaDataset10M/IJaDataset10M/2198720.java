package com.ajoniec.rhytm;

import com.sun.lwuit.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

/**
 *
 * @author Adam Joniec
 */
public class RhytmBar {

    private Vector notes = new Vector();

    public RhytmBar(RhytmBar objectToCopy) {
        for (int i = 0; i < objectToCopy.numNotes(); ++i) {
            notes.addElement(new RhytmNote(objectToCopy.getNote(i)));
        }
    }

    public RhytmBar(RhytmNote notes[]) {
        for (int i = 0; i < notes.length; ++i) {
            this.notes.addElement(notes[i]);
        }
    }

    public static RhytmBar getDefault(RhytmTimeSignature ts) {
        RhytmNote n[] = new RhytmNote[ts.getBeatsPerBar()];
        n[0] = new RhytmNote(true, true);
        for (int i = 1; i < ts.getBeatsPerBar(); ++i) {
            n[i] = new RhytmNote();
        }
        return new RhytmBar(n);
    }

    public RhytmNote getNote(int index) {
        if (index < 0 || index > notes.size()) return null;
        return (RhytmNote) notes.elementAt(index);
    }

    public void setNote(RhytmNote note, int index) {
        if (index < 0 || index > notes.size()) return;
        notes.setElementAt(note, index);
    }

    public int numNotes() {
        return notes.size();
    }

    public void setBeatsPerBar(int beatPerBar, RhytmNote note) {
        if (beatPerBar > notes.size()) {
            for (int i = 0; i < beatPerBar - notes.size(); i++) {
                notes.addElement(new RhytmNote(note));
            }
        } else {
            notes.setSize(beatPerBar);
        }
    }

    public byte[] flattern() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(notes.size());
        for (int i = 0; i < notes.size(); ++i) {
            RhytmNote note = (RhytmNote) notes.elementAt(i);
            try {
                out.write(note.flattern());
            } catch (IOException ex) {
                Log.p("" + ex.getMessage());
            }
        }
        return out.toByteArray();
    }

    public int expand(byte from[], int offset) {
        notes.removeAllElements();
        int size = from[offset++];
        for (int i = 0; i < size; ++i) {
            RhytmNote note = new RhytmNote();
            offset = note.expand(from, offset);
            notes.addElement(note);
        }
        return offset;
    }

    public String toString() {
        String str = "";
        for (int i = 0; i < notes.size(); ++i) {
            str += notes.elementAt(i).toString();
        }
        return str;
    }
}
