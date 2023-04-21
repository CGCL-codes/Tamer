package jarcade.utilities;

import java.net.URL;
import java.util.HashMap;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Audio {

    private HashMap<String, Clip> samples = new HashMap<String, Clip>();

    private HashMap<String, Sequence> midis = new HashMap<String, Sequence>();

    private Sequencer player;

    public Audio() {
    }

    public void shutdown() {
        for (Clip clip : samples.values()) {
            clip.stop();
            clip.close();
        }
        try {
            player.stop();
            player.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadSample(String uid, URL url) throws Exception {
        AudioInputStream input = AudioSystem.getAudioInputStream(url.openStream());
        Clip line = null;
        DataLine.Info info = new DataLine.Info(Clip.class, input.getFormat());
        if (!AudioSystem.isLineSupported(info)) {
            throw new javax.sound.sampled.UnsupportedAudioFileException(url.toExternalForm());
        }
        line = (Clip) AudioSystem.getLine(info);
        line.open(input);
        samples.put(uid, line);
    }

    public void playSample(String uid) {
        Clip clip = samples.get(uid);
        if (clip.isActive()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public void loopSample(String uid, int count) {
        Clip clip = samples.get(uid);
        if (clip.isActive()) {
            clip.stop();
        }
        if (count < 0) {
            count = Clip.LOOP_CONTINUOUSLY;
        }
        clip.loop(count);
    }

    public void stopSample(String uid) {
        samples.get(uid).stop();
        samples.get(uid).close();
    }

    public void loadMIDI(String uid, URL url) throws Exception {
        Sequence sequence = MidiSystem.getSequence(url.openStream());
        midis.put(uid, sequence);
    }

    public void playMIDI(String uid) throws Exception {
        play_midi(midis.get(uid), 0);
    }

    public void loopMIDI(String uid, int count) throws Exception {
        if (count < 0) {
            count = Sequencer.LOOP_CONTINUOUSLY;
        }
        play_midi(midis.get(uid), count);
    }

    private void play_midi(Sequence song, int loopCount) throws Exception {
        player = MidiSystem.getSequencer();
        if (player.isRunning()) {
            player.stop();
            player.close();
        }
        player.setSequence(song);
        player.setLoopCount(loopCount);
        player.open();
        player.start();
    }

    public void stopMIDI(String uid) throws MidiUnavailableException {
        if (player.isRunning()) {
            player.stop();
        }
    }
}
