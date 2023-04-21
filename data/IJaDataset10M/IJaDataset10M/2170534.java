package com.timenes.clips.platform.jse.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import com.timenes.clips.platform.midi.MidiException;

public class MidiSystemImpl implements com.timenes.clips.platform.midi.MidiSystem {

    /** The current midi-out device in use */
    static MidiDevice midiOutDev;

    /** The reciever object of the current midi device */
    static Receiver midiRec;

    /** The instruments of each channel */
    private int instr[] = new int[16];

    private int onCount = 0;

    private int offCount = 0;

    /** Returns a list of available midi devices */
    public String[] getDeviceList() {
        MidiDevice.Info[] minfo = MidiSystem.getMidiDeviceInfo();
        String[] list = new String[minfo.length];
        for (int i = 0; i < minfo.length; i++) {
            list[i] = minfo[i].getName();
        }
        return list;
    }

    /** Not implemented yet */
    public void openMidiInDevice(int devNo) {
        throw new RuntimeException("Not implmented yet...");
    }

    /** Tries to open the device with th provided name */
    public void openMidiOutDevice(String devName) throws MidiException {
        String devList[] = getDeviceList();
        int index = -1;
        for (int i = 0; i < devList.length; i++) {
            if (devList[i].startsWith(devName)) {
                index = i;
            }
        }
        if (index > 0) openMidiOutDevice(index);
    }

    /**
	 * Closes the currently open midi-out device (if any) and opens the device
	 * of the provided device number (same order as in device list)
	 */
    public void openMidiOutDevice(int devNo) throws MidiException {
        try {
            MidiDevice.Info[] minfo = MidiSystem.getMidiDeviceInfo();
            if (midiOutDev != null && midiOutDev.isOpen()) {
                midiOutDev.close();
                midiOutDev = null;
            }
            if (devNo < 0 || devNo >= minfo.length) {
                return;
            }
            midiOutDev = MidiSystem.getMidiDevice(minfo[devNo]);
            midiOutDev.open();
            midiRec = midiOutDev.getReceiver();
            for (int i = 0; i < instr.length; i++) {
                setInstrument(i, instr[i]);
            }
        } catch (MidiUnavailableException e) {
            throw new MidiException(e);
        }
    }

    /** Closes the currently open midi device (if any) */
    public void closeDevice() {
        if (midiOutDev != null && midiOutDev.isOpen()) {
            midiOutDev.close();
        }
    }

    /** Returns the index in the device list of the current midi out device */
    public int getMidiInDeviceIndex() {
        return 0;
    }

    /** Returns the index in the device list of the current midi out device */
    public int getMidiOutDeviceIndex() {
        if (midiOutDev == null) return -1;
        MidiDevice.Info[] minfo = MidiSystem.getMidiDeviceInfo();
        int index = -1;
        for (int i = 0; i < minfo.length; i++) {
            if (minfo[i].getName().equals(midiOutDev.getDeviceInfo().getName())) index = i;
        }
        return index;
    }

    /** Relays the provided message to the underlying midi system */
    public void sendMsg(ShortMessage sm) {
        midiRec.send(sm, -1);
        if (sm.getCommand() == ShortMessage.NOTE_ON) onCount++;
        if (sm.getCommand() == ShortMessage.NOTE_OFF) offCount++;
    }

    /** Plays the provided note */
    public void noteOn(int channel, int pitch, int volume) {
        ShortMessage sm = new ShortMessage();
        try {
            sm.setMessage(ShortMessage.NOTE_ON, channel, pitch, volume);
            midiRec.send(sm, -1);
            onCount++;
        } catch (NullPointerException e) {
            System.err.println("Midi Out Device not set: " + e);
        } catch (InvalidMidiDataException e) {
            System.err.println("Bad note (on): " + e);
            System.err.println("   channel = " + Integer.toString(channel));
            System.err.println("   pitch   = " + Integer.toString(pitch));
            System.err.println("   volume  = " + Integer.toString(volume));
        }
    }

    /** Kills any notes playing at the provided channel and pitch */
    public void noteOff(int channel, int pitch) {
        ShortMessage sm = new ShortMessage();
        try {
            sm.setMessage(ShortMessage.NOTE_OFF, channel, pitch, 0);
            midiRec.send(sm, -1);
            offCount++;
        } catch (NullPointerException e) {
            System.err.println("Midi Out Device not set: " + e);
        } catch (InvalidMidiDataException e) {
            System.err.println("Bad note (off): " + e);
            System.err.println("   channel = " + Integer.toString(channel));
            System.err.println("   pitch   = " + Integer.toString(pitch));
        }
    }

    /** Sets the current pitch bend value for the specified channel */
    public void pitchBend(int channel, int pitchBend) {
        ShortMessage sm = new ShortMessage();
        try {
            sm.setMessage(ShortMessage.PITCH_BEND, channel, pitchBend, 0);
            midiRec.send(sm, -1);
            offCount++;
        } catch (NullPointerException e) {
            System.err.println("Midi Out Device not set: " + e);
        } catch (InvalidMidiDataException e) {
            System.err.println("Bad note (pitchBend): " + e);
            System.err.println("   channel = " + Integer.toString(channel));
            System.err.println("   pitch   = " + Integer.toString(pitchBend));
        }
    }

    /** Sets the instrument for the provided channel */
    public void setInstrument(int channel, int instrument) {
        ShortMessage sm = new ShortMessage();
        try {
            sm.setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, instrument);
            midiRec.send(sm, -1);
        } catch (Exception e) {
            System.err.println("Bad instrument:");
            System.err.println("   channel    = " + channel);
            System.err.println("   instrument = " + instrument);
        }
        if (channel >= 0 && channel < instr.length) {
            instr[channel] = instrument;
        } else {
            System.err.println("Could not store instrument:");
            System.err.println("   channel    = " + channel);
            System.err.println("   instrument = " + instrument);
        }
    }

    /** Returns the instrument for the provided channel */
    public int getInstrument(int channel) {
        if (channel >= 0 && channel < instr.length) {
            return instr[channel];
        } else return 0;
    }

    public void allNotesOff() {
        System.out.println("MidiEnvironment.allNotesOff");
        for (int c = 0; c < 16; c++) {
            for (int n = 0; n < 127; n++) {
                noteOff(c, n);
            }
        }
    }
}
