package uk.co.simphoney.music.wrapper;

import java.util.TreeSet;
import java.util.Vector;
import uk.co.simphoney.music.rep.Maps;
import uk.co.simphoney.music.rep.PhraseAt;
import uk.co.simphoney.music.rep.PulseSequence;
import uk.co.simphoney.music.virtualband.Player;
import uk.co.simphoney.music.virtualband.VirtualBand;
import com.frinika.sequencer.model.MidiLane;
import com.frinika.sequencer.model.MidiPart;
import com.frinika.sequencer.model.MultiEvent;
import com.frinika.sequencer.model.NoteEvent;
import com.frinika.sequencer.model.Part;

/**
 * 
 *  Converts frinika rep to GA rep
 * 
 * @author pjl
 *
 */
public class ProjectPlayer extends Player {

    int id;

    public ProjectPlayer(MidiLane lane, VirtualBand band, int id) {
        super(lane, band);
        this.id = id;
    }

    public void buildProtoPhrases() {
        System.out.println(" build proto phrase " + lane.getName());
        double ticksPerBeat = lane.getProject().getTicksPerBeat();
        protoPhrases = new TreeSet<PhraseAt>();
        for (Part part : lane.getParts()) {
            if (!(part instanceof MidiPart)) continue;
            Vector<NoteEvent> notes = new Vector<NoteEvent>();
            for (MultiEvent ev : ((MidiPart) part).getMultiEvents()) {
                if (ev instanceof NoteEvent) notes.add((NoteEvent) ev);
            }
            PulseSequence timing = new PulseSequence(notes.size());
            int seq[] = new int[notes.size()];
            int vel[] = new int[notes.size()];
            int i = 0;
            double startTick = part.getStartTick();
            for (NoteEvent note : notes) {
                double on = (note.getStartTick() - startTick) / ticksPerBeat;
                double dur = note.getDuration() / ticksPerBeat;
                timing.setPulse(i, on, dur);
                seq[i] = note.getNote();
                vel[i] = note.getVelocity();
                i++;
            }
            protoPhrases.add(new PhraseAt(id, part.getStartTick() / ticksPerBeat, timing, seq, vel, Maps.unitSequence, part.getDurationInTicks() / ticksPerBeat));
        }
    }
}
