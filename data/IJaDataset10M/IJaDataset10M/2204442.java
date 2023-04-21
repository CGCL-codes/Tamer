package rafa.midi.saiph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import rafa.midi.saiph.gen.TrackGenerator;
import rafa.midi.saiph.gui.SequenceGUI;
import rafa.midi.saiph.values.Values;

/**
 * A wrapper around a MIDI sequence (or score).
 * @author rafa
 */
public class Sequence implements MIDI, MusicXML, TrackListener {

    /**
	 * The underlying MIDI sequence
	 */
    private javax.sound.midi.Sequence midiSequence;

    /**
	 * List of tracks (parts) which form this sequence (score).
	 */
    private List tracks;

    protected TrackGenerator tsTrackGenerator;

    protected Track tsTrack;

    /**
	 * GUI for this sequence.
	 */
    private SequenceGUI gui;

    public Sequence() throws InvalidMidiDataException {
        midiSequence = new javax.sound.midi.Sequence(javax.sound.midi.Sequence.PPQ, Values.RESOLUTION);
        tracks = new ArrayList();
        javax.sound.midi.Track tsMidiTrack = midiSequence.createTrack();
        tsTrackGenerator = new TrackGenerator(tsMidiTrack);
        tsTrack = tsTrackGenerator.getTrack();
    }

    public boolean addTrack(Track track) {
        boolean ok = tracks.add(track);
        if (ok) {
            track.addTrackListener(this);
            getGui().addTrack(track);
        }
        return ok;
    }

    public boolean removeTrack(Track track) {
        javax.sound.midi.Track midiTrack = track.getMidiTrack();
        boolean ok1 = midiSequence.deleteTrack(midiTrack);
        boolean ok2 = tracks.remove(track);
        gui.removeTrack(track);
        return ok1 && ok2;
    }

    public Track removeTrack(int index) {
        Track track = (Track) tracks.remove(index);
        midiSequence.deleteTrack(track.getMidiTrack());
        gui.removeTrack(track);
        return track;
    }

    public Object toMidi(long startTime) throws InvalidMidiDataException {
        return midiSequence;
    }

    public List toMusicXML(Context ctx, Document doc) {
        Element scoreElement = doc.createElement(MusicXML.SCORE_PARTWISE);
        Element identElement = doc.createElement(MusicXML.IDENTIFICATION);
        Element encElement = doc.createElement(MusicXML.ENCODING);
        Element swElement = doc.createElement(MusicXML.SOFTWARE);
        Text saiphVersion = doc.createTextNode(Saiph.getFullVersion());
        scoreElement.appendChild(identElement).appendChild(encElement).appendChild(swElement).appendChild(saiphVersion);
        Element partListElement = doc.createElement(MusicXML.PART_LIST);
        scoreElement.appendChild(partListElement);
        for (int i = 0; i < tracks.size(); i++) {
            Track track = (Track) tracks.get(i);
            Element scorePartElement = doc.createElement(MusicXML.SCORE_PART);
            scorePartElement.setAttribute(MusicXML.ATTR_ID, ctx.getPartId());
            Element partNameElement = doc.createElement(MusicXML.PART_NAME);
            Text partName = doc.createTextNode(track.getGenerator().getName());
            partListElement.appendChild(scorePartElement).appendChild(partNameElement).appendChild(partName);
            Element partElement = (Element) track.toMusicXML(ctx, doc).get(0);
            scoreElement.appendChild(partElement);
            ctx.advancePart();
        }
        return Collections.singletonList(scoreElement);
    }

    /**
	 * @return Returns the gui.
	 */
    public SequenceGUI getGui() {
        if (gui == null) {
            gui = new SequenceGUI(this);
        }
        return gui;
    }

    /**
	 * @return Returns the tracks.
	 */
    public List getTracks() {
        return tracks;
    }

    public void trackChanged() {
        getGui().validate();
    }
}
