package imp.gui;

import imp.data.BassPatternElement;

/**
 *
 * @author keller
 */
public class PianoRollBassBar extends PianoRollBar {

    private BassPatternElement element;

    public PianoRollBassBar(int startSlot, BassPatternElement element, int volume, boolean volumeImplied, PianoRoll pianoRoll) {
        super(PianoRoll.BASS_ROW, startSlot, element.getSlots(), PianoRoll.BASSCOLOR, PianoRoll.BARBORDERCOLOR, volume, volumeImplied, pianoRoll.getGrid(), pianoRoll);
        this.element = element;
    }

    public PianoRollBassBar(PianoRollBassBar bar) {
        this(bar.startSlot, bar.element, bar.volume, bar.volumeImplied, bar.pianoRoll);
    }

    /**
 * Over-rides copy in PianoRollBar
 */
    @Override
    public PianoRollBassBar copy() {
        return new PianoRollBassBar(this);
    }

    public void setBassParameters(BassPatternElement.BassNoteType noteType, int duration, BassPatternElement.AccidentalType accidental, int degree, BassPatternElement.DirectionType direction) {
        element.setNoteType(noteType);
        element.setAccidental(accidental);
        element.setDegree(degree);
        element.setDirection(direction);
        setNumSlots(duration);
    }

    @Override
    public void setNumSlots(int slots) {
        super.setNumSlots(slots);
        element.setDuration(slots);
    }

    public BassPatternElement.BassNoteType getNoteType() {
        return element.getNoteType();
    }

    public BassPatternElement.AccidentalType getAccidental() {
        return element.getAccidental();
    }

    public String getAccidentalString() {
        return element.getAccidentalString();
    }

    public int getDegree() {
        return element.getDegree();
    }

    public BassPatternElement.DirectionType getDirection() {
        return element.getDirection();
    }

    public String getDirectionString() {
        return element.getDirectionString();
    }

    @Override
    public Object getText() {
        return element.getText();
    }

    public int getSlots() {
        return element.getSlots();
    }

    public BassPatternElement getElementCopy() {
        return element.getCopy();
    }
}
