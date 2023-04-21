package cn.edu.wuse.musicxml.sound;

public class ControlChangeEvent extends MusicXmlMidiEvent {

    private static final long serialVersionUID = 1L;

    private ControlChange controlChange;

    public ControlChangeEvent(Object source, ControlChange controlChange) {
        super(source);
        this.controlChange = controlChange;
    }

    public ControlChange getControlChange() {
        return controlChange;
    }

    public void setControlChange(ControlChange controlChange) {
        this.controlChange = controlChange;
    }
}
