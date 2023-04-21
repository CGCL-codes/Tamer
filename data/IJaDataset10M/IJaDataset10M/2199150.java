package jpianotrain.midi;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import jpianotrain.ApplicationContext;
import jpianotrain.VersionInformation;
import jpianotrain.staff.Note;
import org.apache.log4j.Logger;

/**
 * Separate Thread to send Midi messages to
 * a configured receiver. This is necessary
 * to have a time source and to avoid a
 * lock for the GUI while playing.
 *
 * @since 0
 * @author Alexander Methke
 */
@VersionInformation(lastChanged = "$LastChangedDate: 2008-11-20 16:23:22 -0500 (Thu, 20 Nov 2008) $", authors = { "Alexander Methke" }, revision = "$LastChangedRevision: 26 $", lastEditor = "$LastChangedBy: onkobu $", id = "$Id")
public class MidiThread extends Thread {

    private static final Logger log = Logger.getLogger(MidiThread.class);

    public static ShortMessage CLOCK_MESSAGE;

    public static ShortMessage SYSTEM_RESET_MESSAGE;

    public static ShortMessage ALIVE_MESSAGE;

    public static enum TEST_MODES {

        TEST_CHORD, TEST_DRUMS, TEST_MELODY
    }

    ;

    protected MidiThread(MidiDevice dIn, MidiDevice dOut) {
        this();
        setKeyboardInDevice(dIn);
        setMidiOutDevice(dOut);
    }

    protected MidiThread() {
        if (CLOCK_MESSAGE == null) {
            CLOCK_MESSAGE = new ShortMessage();
            SYSTEM_RESET_MESSAGE = new ShortMessage();
            ALIVE_MESSAGE = new ShortMessage();
            try {
                CLOCK_MESSAGE.setMessage(ShortMessage.TIMING_CLOCK);
                SYSTEM_RESET_MESSAGE.setMessage(ShortMessage.SYSTEM_RESET);
                ALIVE_MESSAGE.setMessage(ShortMessage.ACTIVE_SENSING);
            } catch (Exception ex) {
            }
        }
    }

    public static MidiThread getInstance() {
        if (instance == null) {
            instance = new MidiThread();
            instance.start();
        }
        return instance;
    }

    public static MidiThread getTestInstance(Synthesizer synth, Instrument instrument, TEST_MODES tm) {
        MidiThread t = new MidiThread();
        t.setSynthesizer(synth);
        t.setInstrument(instrument);
        t.setTestMode(tm);
        t.initTestMessages();
        return t;
    }

    public static MidiThread getTestInstance(MidiDevice dev, int channel, TEST_MODES tm, int progNo) {
        MidiThread t = new MidiThread();
        t.setMidiOutDevice(dev);
        t.setChannel(channel);
        t.setTestMode(tm);
        t.setProgramNumber(progNo);
        t.initTestMessages();
        return t;
    }

    public void run() {
        while (runFlag) {
            try {
                sleep(100);
            } catch (Exception ex) {
            }
            if (runOnce) {
                break;
            }
        }
        cleanUp();
    }

    public void dataReceived() {
        receivedData = true;
    }

    public boolean isDataReceived() {
        return receivedData;
    }

    public void setRunFlag(boolean state) {
        runFlag = state;
    }

    public boolean isRunning() {
        return runFlag;
    }

    public void play(Note[] notes) throws Exception {
        for (Note n : notes) {
            play(n);
        }
    }

    public void setBPM(double bpm) {
        this.bpm = bpm;
    }

    public double getBPM() {
        return bpm;
    }

    public double getMillisecondsPerWholeNote() {
        return (getBPM() * 60 * 1000) / 4;
    }

    public void play(Note n) throws Exception {
        emitNoteOn(n.getPitch());
        try {
            sleep(Math.round(getMillisecondsPerWholeNote() * n.getDuration().getFactor()));
        } catch (InterruptedException ex) {
        }
        emitNoteOff(n.getPitch());
    }

    protected void cleanUp() {
        if (getMidiOutDevice() != null) {
            getMidiOutDevice().close();
        }
        if (getSynthesizer() != null) {
            getSynthesizer().getChannels()[0].allNotesOff();
        }
    }

    public void setChannel(int ch) {
        channel = ch;
    }

    public int getChannel() {
        return channel;
    }

    public boolean initTestMessages() {
        try {
            ShortMessage sm = new ShortMessage();
            sm.setMessage(ShortMessage.SYSTEM_RESET, 0, 0);
            enqueueEvent(sm);
            if (programNumber != -1) {
                sm = new ShortMessage();
                sm.setMessage(ShortMessage.PROGRAM_CHANGE, programNumber, 0);
                enqueueEvent(sm);
            }
            switch(testMode) {
                case TEST_DRUMS:
                    {
                        addDrumEvents(channel);
                    }
                    break;
                case TEST_CHORD:
                    {
                        addChordEvents(channel);
                    }
                    break;
                case TEST_MELODY:
                    {
                        addMelodyEvents(channel);
                    }
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean init(MidiDevice inDev, MidiDevice outDev) {
        try {
            setKeyboardInDevice(inDev);
            setMidiOutDevice(outDev);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean init(MidiDevice keyDev, MidiDevice outDev, MidiDevice metroDev) {
        try {
            setMetronomeDevice(metroDev);
            setMidiOutDevice(outDev);
            setKeyboardInDevice(keyDev);
            setInstrument(null);
            setSynthesizer(null);
        } catch (Exception ex) {
            log.error("failed to hook onto midi device(s)", ex);
            return false;
        }
        return true;
    }

    public boolean init(MidiDevice keyDev, Synthesizer synth, Instrument inst) {
        try {
            setSynthesizer(synth);
            setInstrument(inst);
            setKeyboardInDevice(keyDev);
            setMidiOutDevice(null);
            setMetronomeDevice(null);
        } catch (Exception ex) {
            log.error("failed to hook onto synthesizer", ex);
            return false;
        }
        return true;
    }

    public void emitNoteOn(int note) throws Exception {
        assertValidMidiValue(note, "note pitch");
        ShortMessage sm = new ShortMessage();
        try {
            sm.setMessage(ShortMessage.NOTE_ON, note, 100);
            enqueueEvent(sm);
        } catch (Exception ex) {
            log.error("failed emitting note on", ex);
            throw ex;
        }
    }

    public void emitNoteOff(int note) {
        assertValidMidiValue(note, "note pitch");
        ShortMessage sm = new ShortMessage();
        try {
            sm.setMessage(ShortMessage.NOTE_OFF, note, 100);
            enqueueEvent(sm);
        } catch (Exception ex) {
            log.error("failed emitting note off", ex);
        }
    }

    protected void assertValidMidiValue(int val, String valueName) {
        if (val < 0 || val > 128) {
            throw new IllegalArgumentException("Value for " + valueName + " may range only from 0..127, " + val + " is therefore invalid");
        }
    }

    private void addDrumEvents(int channel) {
        ShortMessage sm;
        try {
            for (int i = 0; i < 30; i++) {
                sm = new MessageWrapper(250);
                sm.setMessage(ShortMessage.NOTE_ON, channel, 40 + i, 100);
                enqueueEvent(sm);
                enqueueEvent(new MessageWrapper(50, true));
                sm = new ShortMessage();
                sm.setMessage(ShortMessage.NOTE_OFF, channel, 40 + i, 100);
                enqueueEvent(sm);
            }
        } catch (Exception ex) {
            log.error("failed to add drum events", ex);
        }
    }

    private void addChordEvents(int channel) {
        try {
            MultiMessage mm = new MultiMessage(1000, ShortMessage.NOTE_ON);
            mm.addMessage(channel, 64, 100);
            mm.addMessage(channel, 67, 100);
            mm.addMessage(channel, 70, 100);
            enqueueEvent(mm);
            mm = new MultiMessage(0, ShortMessage.NOTE_OFF);
            mm.addMessage(channel, 64, 100);
            mm.addMessage(channel, 67, 100);
            mm.addMessage(channel, 70, 100);
            enqueueEvent(mm);
        } catch (Exception ex) {
            log.error("failed to add chord events", ex);
        }
    }

    private void addMelodyEvents(int channel) {
        try {
            ShortMessage sm = null;
            sm = new MessageWrapper(750);
            sm.setMessage(ShortMessage.NOTE_ON, channel, 64, 100);
            enqueueEvent(sm);
            sm = new MessageWrapper(0, true);
            sm.setMessage(ShortMessage.NOTE_OFF, channel, 64, 100);
            enqueueEvent(sm);
            sm = new MessageWrapper(500);
            sm.setMessage(ShortMessage.NOTE_ON, channel, 70, 100);
            enqueueEvent(sm);
            sm = new MessageWrapper(0, true);
            sm.setMessage(ShortMessage.NOTE_OFF, channel, 70, 100);
            enqueueEvent(sm);
            sm = new MessageWrapper(250);
            sm.setMessage(ShortMessage.NOTE_ON, channel, 70, 100);
            enqueueEvent(sm);
            sm = new MessageWrapper(0, true);
            sm.setMessage(ShortMessage.NOTE_OFF, channel, 70, 100);
            enqueueEvent(sm);
            sm = new MessageWrapper(1000);
            sm.setMessage(ShortMessage.NOTE_ON, channel, 67, 100);
            enqueueEvent(sm);
            sm = new MessageWrapper(0, true);
            sm.setMessage(ShortMessage.NOTE_OFF, channel, 67, 100);
            enqueueEvent(sm);
        } catch (Exception ex) {
            log.error("failed to add melody events", ex);
        }
    }

    protected void setMetronomeDevice(MidiDevice dev) {
        if (metronomeDevice != null && dev != metronomeDevice && metronomeDevice.isOpen()) {
            try {
                metronomeDevice.close();
            } catch (Exception ex) {
                log.error("failed to close old metronome device", ex);
            }
        }
        metronomeDevice = dev;
        if (metronomeDevice != null && !metronomeDevice.isOpen()) {
            try {
                metronomeDevice.open();
            } catch (Exception ex) {
                log.error("failed to init new metronome device", ex);
            }
        }
    }

    public MidiDevice getMetronomeDevice() {
        return metronomeDevice;
    }

    public MidiDevice getKeyboardDevice() {
        return readerInstance.getKeyboardInDevice();
    }

    public MidiDevice getMidiOutDevice() {
        return outDevice;
    }

    protected void setMidiOutDevice(MidiDevice dev) {
        if (outDevice != null && dev != outDevice && outDevice.isOpen()) {
            try {
                outDevice.close();
            } catch (Exception ex) {
                log.error("failed to close old out device", ex);
            }
        }
        outDevice = dev;
        if (outDevice != null && !outDevice.isOpen()) {
            try {
                outDevice.open();
            } catch (Exception ex) {
                log.error("failed to open new out device", ex);
            }
        }
    }

    protected synchronized void enqueueEvent(MidiMessage msg) throws Exception {
        MidiDevice dev = getMidiOutDevice();
        if (ApplicationContext.getInstance().isSpoolMidi()) {
            log.debug("sending " + MidiReader.formatMidiMessage(msg));
        }
        if (receiver == null) {
            if (dev == null) {
                if (!synthesizer.isOpen()) {
                    synthesizer.open();
                }
                receiver = synthesizer.getReceiver();
                if (instrument != null) {
                    synthesizer.getChannels()[0].programChange(instrument.getPatch().getBank(), instrument.getPatch().getProgram());
                }
            } else {
                receiver = dev.getReceiver();
            }
        }
        if (msg instanceof MultiMessage) {
            MultiMessage mMsg = (MultiMessage) msg;
            MessageWrapper lastMsg = null;
            for (MessageWrapper mw : mMsg.getMessages()) {
                receiver.send(mw, -1);
                lastMsg = mw;
            }
            if (lastMsg != null && lastMsg.getCommand() == ShortMessage.NOTE_ON) {
                sleep(lastMsg.getDurationMs());
            }
        } else if (msg instanceof MessageWrapper) {
            MessageWrapper mw = (MessageWrapper) msg;
            if (mw.isPause()) {
                sleep(mw.getDurationMs());
            } else {
                receiver.send(mw, -1);
                if (mw.getCommand() == ShortMessage.NOTE_ON) {
                    sleep(mw.getDurationMs());
                }
            }
        } else {
            log.debug("raw midi message");
            receiver.send(msg, -1);
        }
    }

    /**
	 * Getter for property testMode.
	 * @return Value of property testMode.
	 */
    public TEST_MODES getTestMode() {
        return this.testMode;
    }

    /**
	 * Setter for property testMode.
	 * @param testMode New value of property testMode.
	 */
    public void setTestMode(TEST_MODES testMode) {
        this.testMode = testMode;
    }

    /**
	 * Getter for property programNumber.
	 * @return Value of property programNumber.
	 */
    public int getProgramNumber() {
        return this.programNumber;
    }

    /**
	 * Setter for property programNumber.
	 * @param programNumber New value of property programNumber.
	 */
    public void setProgramNumber(int programNumber) {
        this.programNumber = programNumber;
    }

    /**
	 * Getter for property runOnce.
	 * @return Value of property runOnce.
	 */
    public boolean isRunOnce() {
        return this.runOnce;
    }

    /**
	 * Setter for property runOnce.
	 * @param runOnce New value of property runOnce.
	 */
    public void setRunOnce(boolean runOnce) {
        this.runOnce = runOnce;
    }

    /**
	 * Getter for property keyInDevice.
	 * @return Value of property keyInDevice.
	 */
    public MidiDevice getKeyInDevice() {
        return this.keyInDevice;
    }

    /**
	 * Setter for property keyInDevice.
	 * @param keyInDevice New value of property keyInDevice.
	 */
    public void setKeyboardInDevice(MidiDevice keyInDevice) {
        if (readerInstance == null) {
            readerInstance = new MidiReader();
            readerInstance.start();
        }
        readerInstance.setKeyboardInDevice(keyInDevice);
        this.keyInDevice = keyInDevice;
    }

    public Synthesizer getSynthesizer() {
        return synthesizer;
    }

    public void setSynthesizer(Synthesizer synthesizer) {
        this.synthesizer = synthesizer;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    private static MidiThread instance;

    private static MidiReader readerInstance;

    private boolean runFlag = true;

    private int channel;

    private MidiDevice outDevice;

    private MidiDevice metronomeDevice;

    /**
	 * Holds value of property testMode.
	 */
    private TEST_MODES testMode;

    /**
	 * Holds value of property programNumber.
	 */
    private int programNumber = -1;

    /**
	 * Holds value of property runOnce.
	 */
    private boolean runOnce = false;

    private double bpm;

    /**
	 * Holds value of property keyInDevice.
	 */
    private MidiDevice keyInDevice;

    private boolean receivedData;

    private Receiver receiver;

    private Synthesizer synthesizer;

    private Instrument instrument;
}
