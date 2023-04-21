package grame.midishare;

/**
The class of all MidiException.
*/
public class MidiException extends Exception {

    /**
     * Constructor.
     */
    public MidiException() {
        super();
    }

    /**
     * Constructor with error number.
     */
    public MidiException(int rc) {
        super("MIDI error return code: " + rc);
    }

    /**
     * Constructor with string.
     */
    public MidiException(String string) {
        super("MIDI error: " + string);
    }
}
