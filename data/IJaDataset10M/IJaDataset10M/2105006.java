package promidi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

class MidiInDevice extends MidiDevice implements Receiver {

    private final Transmitter inputTrans;

    private final MidiIO promidiContext;

    /**
	 * Contains the states of the 16 midi channels for a device.
	 * true if open otherwise false.
	 */
    private final MidiIn[] midiIns = new MidiIn[16];

    /**
	 * Initializes a new MidiIn.
	 * @param libContext
	 * @param midiDevice
	 * @throws MidiUnavailableException
	 */
    MidiInDevice(final MidiIO libContext, final javax.sound.midi.MidiDevice midiDevice, final int deviceNumber) {
        super(midiDevice, deviceNumber);
        this.promidiContext = libContext;
        try {
            inputTrans = midiDevice.getTransmitter();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException();
        }
    }

    String getName() {
        return midiDevice.getDeviceInfo().getName();
    }

    void open() {
        super.open();
        inputTrans.setReceiver(this);
    }

    void openMidiChannel(final int i_midiChannel) {
        if (midiIns[i_midiChannel] == null) midiIns[i_midiChannel] = new MidiIn(i_midiChannel, promidiContext);
    }

    void closeMidiChannel(final int i_midiChannel) {
        midiIns[i_midiChannel] = null;
    }

    void plug(final Object i_object, final String i_methodName, final int i_midiChannel) {
        open();
        openMidiChannel(i_midiChannel);
        midiIns[i_midiChannel].plug(i_object, i_methodName, -1);
    }

    void plug(final Object i_object, final String i_methodName, final int i_midiChannel, final int i_value) {
        open();
        openMidiChannel(i_midiChannel);
        midiIns[i_midiChannel].plug(i_object, i_methodName, i_value);
    }

    /**
	 * Sorts the incoming MidiIO data in the different Arrays.
	 * @invisible
	 * @param message MidiMessage
	 * @param deltaTime long
	 */
    public void send(final MidiMessage message, final long deltaTime) {
        final ShortMessage shortMessage = (ShortMessage) message;
        final int midiChannel = shortMessage.getChannel();
        if (midiIns[midiChannel] == null) return;
        final int midiCommand = shortMessage.getCommand();
        final int midiData1 = shortMessage.getData1();
        final int midiData2 = shortMessage.getData2();
        if (midiCommand == MidiEvent.NOTE_ON && midiData2 > 0) {
            final Note note = new Note(midiData1, midiData2);
            midiIns[midiChannel].sendNoteOn(note, deviceNumber, midiChannel);
        } else if (midiCommand == MidiEvent.NOTE_OFF || midiData2 == 0) {
            final Note note = new Note(midiData1, midiData2);
            midiIns[midiChannel].sendNoteOff(note, deviceNumber, midiChannel);
        } else if (midiCommand == MidiEvent.CONTROL_CHANGE) {
            final Controller controller = new Controller(midiData1, midiData2);
            midiIns[midiChannel].sendController(controller, deviceNumber, midiChannel);
        } else if (midiCommand == MidiEvent.PROGRAM_CHANGE) {
            final ProgramChange programChange = new ProgramChange(midiData1);
            midiIns[midiChannel].sendProgramChange(programChange, deviceNumber, midiChannel);
        }
    }
}
