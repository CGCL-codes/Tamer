package ren.gui.seqEdit;

import java.awt.Dimension;
import jm.music.data.Phrase;
import ren.util.PO;

/**
 *
 * @author  Rene Wooller
 */
public class NoteToGraphicsConverter {

    private int PPBAR = (int) (ren.gui.lookFeel.CustomDimensions.screenSize.getWidth() / 6.8);

    private double pixelsPerBeat = (int) (PPBAR / 4);

    private int pixelsPerTone = 12;

    private int lowestNote = 12, highestNote = 108, numberOfBeatsViewed = 4;

    private double quantise = 0.25, shuffle = 0.0;

    private int pixelsPerQuantisedSlot = (int) (quantise * pixelsPerBeat);

    /** Creates a new instance of NoteToGraphicsConverter */
    public NoteToGraphicsConverter() {
    }

    public Dimension getDimensions() {
        return new Dimension((int) (this.getViewedPixels() + 0.5), this.getPixelsPerTone() * this.getNoteRange());
    }

    public void setNumberOfBeatsViewed(int bv) {
        this.numberOfBeatsViewed = bv;
    }

    public double getViewedPixels() {
        return numberOfBeatsViewed * pixelsPerBeat;
    }

    public double getPixelsViewed() {
        return numberOfBeatsViewed * pixelsPerBeat;
    }

    public double getPixelsPerBar() {
        return pixelsPerBeat * 4;
    }

    public void setPixelsPerBar(double ppb) {
        this.setPixelsPerBeat(ppb / 4.0);
    }

    public double getPixelsPerBeat() {
        return pixelsPerBeat;
    }

    public void setPixelsPerBeat(double ppb) {
        pixelsPerBeat = ppb;
    }

    public int getPixelsPerTone() {
        return pixelsPerTone;
    }

    public void setPixelsPerTone(int ppt) {
        pixelsPerTone = ppt;
    }

    public int getLowestNote() {
        return lowestNote;
    }

    public void setLowestNote(int ln) {
        lowestNote = ln;
    }

    public int getHighestNote() {
        return highestNote;
    }

    public void setHighestNote(int hn) {
        highestNote = hn;
    }

    public int getNoteRange() {
        return highestNote - lowestNote;
    }

    public void setQuantise(double quantise) {
        this.quantise = quantise;
    }

    public double getQuantise() {
        return this.quantise;
    }

    public double getShuffle() {
        return this.shuffle;
    }

    public int getQuantiseInPixels() {
        return (int) (this.getQuantise() * this.getPixelsPerBeat());
    }

    public int getX(double beat) {
        return (int) (beat * pixelsPerBeat);
    }

    public int getX(Phrase notePhrase) {
        return (int) (notePhrase.getStartTime() * pixelsPerBeat);
    }

    public int getDegree(int y) {
        return (this.getNoteRange() / 2 - ((int) ((y + (pixelsPerTone / 2.0)) / pixelsPerTone)));
    }

    public int getPitch(int y) {
        return this.getLowestNote() + (this.getNoteRange() - (int) (y / this.getPixelsPerTone() + 0.5));
    }

    public double getBeatsX(int x) {
        if (this.getQuantise() != 0) return ((int) ((x * 1.0 / pixelsPerBeat * 1.0) / this.getQuantise() + 0.5) * this.getQuantise()); else {
            return (x * 1.0 / pixelsPerBeat * 1.0);
        }
    }

    public int getY(Phrase notePhrase) {
        return (int) ((getNoteRange() * pixelsPerTone) - ((notePhrase.getNote(0).getPitch() - lowestNote) * pixelsPerTone));
    }

    public int getY(int pitch) {
        return (int) ((getNoteRange() * pixelsPerTone) - ((pitch - lowestNote) * pixelsPerTone));
    }

    public int getWidth(Phrase notePhrase) {
        return (int) (notePhrase.getNote(0).getDuration() * pixelsPerBeat);
    }

    public int getHeight() {
        return pixelsPerTone;
    }

    public int getPitchRange() {
        return (highestNote - lowestNote);
    }

    public int getYRange() {
        return getPitchRange() * pixelsPerTone;
    }

    public void changeNoteDuration(Phrase notePhrase, int x) {
        double dur = (double) (x * 1.0 / this.pixelsPerBeat * 1.0);
        dur += notePhrase.getNote(0).getDuration();
        if (dur < 0.1) dur = 0.1;
        notePhrase.getNote(0).setDuration(dur);
    }

    public void changeNoteStartTime(Phrase notePhrase, int x) {
        double st = (double) (x * 1.0 / this.pixelsPerBeat * 1.0);
        st = ((int) ((st + notePhrase.getStartTime()) * 100000)) / 100000.0;
        if (st < 0) st = 0;
        notePhrase.setStartTime(st);
    }

    public void quantise(Phrase notePhrase) {
        if (this.getQuantise() != 0) {
            notePhrase.setStartTime(((int) (notePhrase.getStartTime() / this.getQuantise())) * this.getQuantise());
        }
    }

    public void setNotePos(Phrase notePhrase, int x, int y) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > this.getPixelsViewed()) x = (int) (this.getPixelsViewed() + 0.5);
        if (y > this.getNoteRange() * pixelsPerTone) y = getNoteRange() * pixelsPerTone;
        double realStartTime = (double) (x * 1.0 / this.pixelsPerBeat * 1.0);
        notePhrase.setStartTime(realStartTime);
        this.quantise(notePhrase);
        notePhrase.getNote(0).setPitch((this.getNoteRange() - ((int) ((y + (pixelsPerTone / 2.0)) / pixelsPerTone))) + lowestNote);
    }
}
