package com.lemu.music;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequencer;
import jm.midi.MidiSynth;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import com.lemu.leco.play.JSPlayer;

public class TestMus {

    public static MidiSynth ms;

    public static Score s;

    public static void main(String[] args) {
        TestMus t = new TestMus();
    }

    public TestMus() {
    }

    private void javaSoundTest() {
        jm.util.Play.midi(mkn(60, 4.0, 127));
    }

    private void initMidiSynth() {
        ms = new MidiSynth();
    }

    private void midiSynthTest() {
        initMidiSynth();
        JSPlayer jsp = new JSPlayer();
        s = score();
        Thread thread = new Thread() {

            public void run() {
                try {
                    ms.play(s);
                } catch (InvalidMidiDataException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        Thread thread2 = new Thread() {

            public void run() {
                long tickPos = 0;
                while (true) {
                    Sequencer seq = ms.getSequencer();
                    if (seq == null) continue;
                    if (seq.getTickPosition() != tickPos) {
                        tickPos = seq.getTickPosition();
                        System.out.println("beats = : " + ms.getBeats() + "  tick : " + tickPos);
                    }
                }
            }
        };
    }

    public Score score() {
        Score s = new Score();
        s.add(part());
        return s;
    }

    public Score score(int numNotes) {
        Score s = new Score("score" + Math.random() + " n" + numNotes);
        s.add(part(numNotes));
        return s;
    }

    public Part part() {
        Part p = new Part();
        p.add(phrase(400));
        return p;
    }

    public Part part(int numNotes) {
        Part p = new Part();
        p.add(phrase(numNotes));
        return p;
    }

    public Phrase phrase(int notes) {
        Phrase phr = new Phrase(0.0);
        for (int i = 0; i < notes; i++) {
            phr.add(randNote());
        }
        return phr;
    }

    public Note randNote() {
        return new Note((int) (Math.random() * 20.0) + 60, 0.25, this.pitchOffset + (int) (Math.random() * 40.0));
    }

    public Note mkn(int pitch, double length, int dyn) {
        return new Note(pitch, length, dyn);
    }

    public int pitchOffset = 80;
}
