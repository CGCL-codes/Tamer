package codesounding.jmusic;

import codesounding.BasicProcessor;
import jm.midi.MidiSynth;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;

public class SynthSyncopate extends BasicProcessor {

    private MidiSynth synth = new MidiSynth();

    private int counter = 0;

    protected void playNote(int instrument, int pitch) {
        counter++;
        if (counter >= 100) {
            counter = 0;
            Phrase ph = new Phrase(new Note(pitch, Part.THIRTYSECOND_NOTE_TRIPLET));
            Part p = new Part(instrument);
            p.add(ph);
            Score score = new Score();
            score.add(p);
            try {
                synth.play(score);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getVarDeclaration() {
        playNote(Part.FLUTE, Part.a4);
    }

    public void getStartBlock() {
        playNote(Part.VIOLA, Part.b4);
    }

    public void getEndBlock() {
        playNote(Part.GUITAR, Part.c4);
    }

    public void getIfStatement() {
        playNote(Part.TRUMPET, Part.d4);
    }

    public void getForStatement() {
        playNote(Part.DRUM, Part.e4);
    }

    public void getDoStatement() {
        playNote(Part.BASS_DRUM_1, Part.f4);
    }

    public void getWhileStatement() {
        playNote(Part.BASS, Part.A4);
    }

    public void getReturnStatement() {
        playNote(Part.PIANO, Part.B4);
    }

    public void getBreakStatement() {
        playNote(Part.PANFLUTE, Part.C4);
    }

    public void getContinueStatement() {
        playNote(Part.SAXOPHONE, Part.D4);
    }

    public void getThrowStatement() {
        playNote(Part.VIOLIN_CELLO, Part.E4);
    }
}
