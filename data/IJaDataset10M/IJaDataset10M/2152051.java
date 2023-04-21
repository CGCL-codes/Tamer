package uk.org.toot.midi.sequence.edit;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.InvalidMidiDataException;
import uk.org.toot.midi.message.PitchMsg;
import uk.org.toot.midi.sequence.MidiTrack;
import uk.org.toot.midi.sequence.MidiNote;

public class TrackSelection implements Selection, Cloneable {

    private MidiTrack track;

    ArrayList<MidiNote> noteList;

    public TrackSelection(MidiTrack track) {
        this.track = track;
        noteList = new ArrayList<MidiNote>();
    }

    public TrackSelection(MidiTrack track, MidiNote note) {
        this(track);
        noteList.add(note);
    }

    public TrackSelection(MidiTrack track, List<MidiNote> notes) {
        this(track);
        noteList.addAll(notes);
    }

    public int size() {
        return noteList.size();
    }

    public Iterator iterator() {
        return noteList.iterator();
    }

    public boolean contains(MidiNote note) {
        return noteList.contains(note);
    }

    public boolean select(MidiNote note) {
        if (note == null) return false;
        return contains(note) ? noteList.remove(note) : noteList.add(note);
    }

    public MidiTrack getTrack() {
        return track;
    }

    public boolean cut() {
        if (size() == 0) return false;
        int cut = 0;
        for (MidiNote note : noteList) {
            if (track.remove(note.on)) cut++;
            if (track.remove(note.off)) cut++;
        }
        return cut > 0;
    }

    public boolean paste() {
        if (size() == 0) return false;
        int paste = 0;
        for (MidiNote note : noteList) {
            if (track.add(note.on)) paste++;
            if (track.add(note.off)) paste++;
        }
        return paste > 0;
    }

    public boolean transpose(int semitones) {
        if (semitones == 0) return true;
        for (MidiNote note : noteList) {
            try {
                PitchMsg.transpose(note.on.getMessage(), semitones);
                PitchMsg.transpose(note.off.getMessage(), semitones);
            } catch (InvalidMidiDataException imde) {
            }
        }
        return true;
    }

    protected void translate(long ticks) {
        boolean in = cut();
        for (MidiNote note : noteList) {
            note.on.setTick(note.on.getTick() + ticks);
            note.off.setTick(note.off.getTick() + ticks);
        }
        if (in) paste();
    }

    public boolean move(long ticks, int semitones) {
        if (semitones != 0) transpose(semitones);
        if (ticks != 0) translate(ticks);
        return true;
    }

    public Object deepCopy() {
        try {
            TrackSelection copy = (TrackSelection) this.clone();
            for (MidiNote note : copy.noteList) {
                note.on = new MidiEvent((MidiMessage) note.on.getMessage().clone(), note.on.getTick());
                note.off = new MidiEvent((MidiMessage) note.off.getMessage().clone(), note.off.getTick());
            }
            return copy;
        } catch (CloneNotSupportedException cnse) {
        }
        return null;
    }

    public Object clone() throws CloneNotSupportedException {
        TrackSelection cloned = (TrackSelection) super.clone();
        cloned.noteList = new ArrayList<MidiNote>();
        for (int t = 0; t < noteList.size(); t++) {
            cloned.noteList.add((MidiNote) (noteList.get(t).clone()));
        }
        return cloned;
    }
}
