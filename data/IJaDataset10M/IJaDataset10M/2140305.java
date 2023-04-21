package uk.co.simphoney.music.ga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import uk.co.simphoney.music.util.NoteIterable;
import uk.co.simphoney.music.util.Util;
import com.frinika.project.ProjectContainer;
import com.frinika.sequencer.model.Lane;
import com.frinika.sequencer.model.MidiLane;
import com.frinika.sequencer.model.MidiPart;
import com.frinika.sequencer.model.MultiEvent;
import com.frinika.sequencer.model.NoteEvent;
import com.frinika.sequencer.model.Part;

public class GAMorph {

    public static void morphProject(ProjectContainer projectA, ProjectContainer projectB) {
        HashMap<MidiLane, MidiLane> roleMap = new HashMap<MidiLane, MidiLane>();
        List<Lane> lanesA = projectA.getProjectLane().getFamilyLanes();
        List<Lane> lanesB = projectB.getProjectLane().getFamilyLanes();
        Iterator<Lane> iter = lanesA.iterator();
        while (iter.hasNext()) {
            Lane lane = iter.next();
            if (!(lane instanceof MidiLane) || ((MidiLane) lane).isDrumLane()) iter.remove();
        }
        iter = lanesB.iterator();
        while (iter.hasNext()) {
            Lane lane = iter.next();
            if (!(lane instanceof MidiLane) || ((MidiLane) lane).isDrumLane()) iter.remove();
        }
        int cnt = 0;
        TreeSet<MultiEvent> notesB = new TreeSet<MultiEvent>();
        for (Lane laneB : lanesB) {
            for (Part partB : laneB.getParts()) {
                notesB.addAll(((MidiPart) partB).getMultiEvents());
            }
        }
        TreeMap<Long, List<NoteEvent>> map = Util.buildOnSetMap(notesB);
        for (Lane laneA : lanesA) {
            MidiLane laneB = roleMap.get(laneA);
            for (Part partA : laneA.getParts()) {
                morphPart(partA, map);
            }
        }
    }

    public static void morphPart(Part part, TreeMap<Long, List<NoteEvent>> role) {
        NoteIterable notes = new NoteIterable(((MidiPart) part).getMultiEvents());
        ArrayList<NoteEvent> notesCopy = new ArrayList<NoteEvent>();
        for (NoteEvent note : notes) {
            notesCopy.add(note);
        }
        for (NoteEvent note : notesCopy) {
            Entry<Long, List<NoteEvent>> palette = role.floorEntry(note.getStartTick());
            if (palette == null) palette = role.ceilingEntry(note.getStartTick());
            int dis = 129;
            int pitch = -1;
            for (NoteEvent nB : palette.getValue()) {
                int pitch2 = nB.getNote();
                int d2 = Math.abs(note.getNote() - pitch2);
                if (d2 < dis) {
                    dis = d2;
                    pitch = pitch2;
                }
            }
            if (pitch != -1) {
                System.out.println("Morhing " + note.getNote() + " --> " + pitch);
                note.getPart().remove(note);
                note.setNote(pitch);
                note.getPart().add(note);
            }
        }
    }

    public static void morphAll(TreeMap<Long, List<NoteEvent>> mapA, TreeMap<Long, List<NoteEvent>> mapB, long chunkSize) {
        GACursor curA = new GACursor(mapA, chunkSize);
        GACursor curB = new GACursor(mapB, chunkSize);
        while (curA.hasMoreChunks()) {
            GAChunk chunkA = curA.getNextChunk();
            GAChunk chunkB = curB.getNextChunk();
            morph(chunkA, chunkB);
        }
    }

    public static void morphChunk(GAChunk chunkA, GAChunk chunkB) {
    }

    private static void morph(GAChunk chunkA, GAChunk chunkB) {
    }
}
