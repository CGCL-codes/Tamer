package Population.Individuals.Phenotype.IGA.Graph.Compose;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 *
 * @author 郝国生  HAO Guo-Sheng, HAO Guo Sheng, HAO GuoSheng
 */
public class MakeMidi {

    Sequencer sequencer;

    Sequence sequence;

    Track track;

    int tracklength;

    public static int NOTESIZE = 100;

    int[][] noteCode = new int[NOTESIZE][32];

    public MakeMidi() {
        setUpMidi();
    }

    private void setUpMidi() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setnoteCode(int[][] tem, int length) {
        for (int i = 0; i < NOTESIZE; i++) {
            for (int j = 0; j <= length + 1; j++) {
                noteCode[i][j] = tem[i][j];
            }
        }
        tracklength = length;
    }

    public void buildTrackAndStart() {
        int[] trackList = null;
        sequence.deleteTrack(track);
        track = sequence.createTrack();
        for (int i = 0; i <= tracklength; i = i + 2) {
            System.out.println("BIANMA");
            int length = 1;
            for (int j = 1; j < NOTESIZE; j++) {
                if (noteCode[j][i] != 0) length++; else break;
            }
            int[] trackTime = new int[length];
            trackList = new int[length];
            for (int j = 0; j < length; j++) {
                trackList[j] = noteCode[j][i];
                trackTime[j] = noteCode[j][i + 1];
                System.out.println(trackList[j]);
                System.out.println(trackTime[j]);
            }
            makeTracks(trackList, trackTime, length);
            track.add(makeEvent(176, trackList[0], 127, 0, length));
        }
        track.add(makeEvent(192, 0, 1, 0, 15));
        try {
            sequencer.setSequence(sequence);
            sequencer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeTracks(int[] list, int[] listTmie, int length) {
        int channel = list[0] - 1;
        for (int i = 1; i < length; i++) {
            int key = list[i];
            if (key != 0) {
                track.add(makeEvent(144, channel, key, 92, i));
                if (listTmie[i] == 200) sequencer.setTempoInBPM(60); else if (listTmie[i] == 400) sequencer.setTempoInBPM(30); else sequencer.setTempoInBPM(15);
                track.add(makeEvent(128, channel, key, 92, i + 1));
            }
        }
    }

    private MidiEvent makeEvent(int command, int chanel, int one, int two, int ticks) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(command, chanel, one, two);
            event = new MidiEvent(a, ticks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }
}
