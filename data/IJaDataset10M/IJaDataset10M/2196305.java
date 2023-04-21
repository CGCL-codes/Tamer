package codesounding.jmusic;

import codesounding.BasicProcessor;
import jm.midi.MidiSynth;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;

public class IrritatingPianists extends BasicProcessor {

    private MidiSynth synth;

    private Score score;

    private Part p;

    private int currPhrase = 0;

    private int maxPhrase = 3;

    protected void playNote(int NOT_USED, int pitch, double rythm) {
        if (score == null) {
            score = new Score();
            p = new Part(Part.ACOUSTIC_GRAND);
            for (int i = 0; i < maxPhrase; i++) {
                p.add(new Phrase());
            }
            score.add(p);
            currPhrase = 0;
        }
        if (currPhrase >= maxPhrase) {
            currPhrase = 0;
        }
        p.getPhrase(currPhrase).add(new Note(pitch, rythm));
        currPhrase++;
        if (score.getPartArray()[0].getPhraseArray()[0].getNoteArray().length > 200) {
            if (synth == null) {
                synth = new MidiSynth();
            }
            try {
                synth.play(score);
            } catch (Exception e) {
                e.printStackTrace();
            }
            score = null;
        }
    }

    public void getVarDeclaration() {
        playNote(Part.FLUTE, Part.a4, Part.CROTCHET);
    }

    public void getStartBlock() {
        playNote(Part.VIOLA, Part.b4, Part.CROTCHET);
    }

    public void getEndBlock() {
        playNote(Part.VIOLA, Part.c4, Part.CROTCHET);
    }

    public void getIfStatement() {
        playNote(Part.VIOLA, Part.d4, Part.CROTCHET);
    }

    public void getForStatement() {
        playNote(Part.DRUM, Part.e4, Part.CROTCHET);
    }

    public void getDoStatement() {
        playNote(Part.FLUTE, Part.f4, Part.CROTCHET);
    }

    public void getWhileStatement() {
        playNote(Part.STAR_THEME, Part.A4, Part.CROTCHET);
    }

    public void getReturnStatement() {
        playNote(Part.STAR_THEME, Part.B4, Part.CROTCHET);
    }

    public void getBreakStatement() {
        playNote(Part.STAR_THEME, Part.C4, Part.CROTCHET);
    }

    public void getContinueStatement() {
        playNote(Part.STAR_THEME, Part.D4, Part.CROTCHET);
    }

    public void getThrowStatement() {
        playNote(Part.STAR_THEME, Part.E4, Part.CROTCHET);
    }
}
