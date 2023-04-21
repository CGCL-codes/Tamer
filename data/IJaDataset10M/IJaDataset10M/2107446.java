package net.kano.partypad.objects.processors;

import net.kano.partypad.notes.NoteTools;
import net.kano.partypad.notes.AbsoluteNote;
import net.kano.partypad.pipeline.AbstractSingleValueProcessor;
import net.kano.partypad.pipeline.PipelineObjectInfo;
import net.kano.partypad.pipeline.ports.PortInfo;
import static net.kano.partypad.pipeline.PipelineTools.TYPEINFO_DOUBLE;

public class NearestNoteRounder extends AbstractSingleValueProcessor<Double> {

    public static final PipelineObjectInfo OBJECT_INFO = new PipelineObjectInfo("note-rounder", "Nearest Note", "Rounds a frequency to the nearest note");

    public NearestNoteRounder() {
        super(OBJECT_INFO);
        setPorts(TYPEINFO_DOUBLE, TYPEINFO_DOUBLE, new PortInfo("frequency", "Pitch", this), new PortInfo("frequency", "Note Pitch", "Pitch", "", this));
    }

    public Double process(Double input) {
        System.out.println("input: " + input);
        if (input == null) return null;
        AbsoluteNote note = NoteTools.findClosestNote(input);
        System.out.println("note: " + note);
        if (note == null) return null;
        return note.getFrequency();
    }
}
