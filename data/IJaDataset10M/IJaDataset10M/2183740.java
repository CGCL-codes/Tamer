package org.tritonus.midi.device.alsa;

import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Instrument;
import javax.sound.midi.Soundbank;
import javax.sound.midi.VoiceStatus;
import javax.sound.midi.Patch;
import org.tritonus.share.GlobalInfo;
import org.tritonus.share.TDebug;
import org.tritonus.share.midi.TMidiDevice;

public class AlsaSynthesizer extends AlsaMidiDevice implements Synthesizer {

    private static final MidiChannel[] EMPTY_MIDICHANNEL_ARRAY = new MidiChannel[0];

    private static final VoiceStatus[] EMPTY_VOICESTATUS_ARRAY = new VoiceStatus[0];

    private List<MidiChannel> m_channels;

    private int m_nVoices;

    public AlsaSynthesizer(int nClient, int nPort, int nVoices) {
        super(new TMidiDevice.Info("ALSA Synthesizer (" + nClient + ":" + nPort + ")", GlobalInfo.getVendor(), "Synthesizer based on the ALSA sequencer", GlobalInfo.getVersion()), nClient, nPort, false, true);
        m_nVoices = nVoices;
        m_channels = new ArrayList<MidiChannel>();
    }

    protected void openImpl() {
        super.openImpl();
        m_channels.clear();
        Receiver receiver = null;
        try {
            receiver = this.getReceiver();
        } catch (MidiUnavailableException e) {
            if (TDebug.TraceAllExceptions) {
                TDebug.out(e);
            }
        }
        for (int i = 0; i < 16; i++) {
            MidiChannel channel = new AlsaMidiChannel(receiver, i);
            m_channels.add(channel);
        }
    }

    protected void closeImpl() {
        super.closeImpl();
    }

    public int getMaxPolyphony() {
        return m_nVoices;
    }

    public long getLatency() {
        return -1L;
    }

    public MidiChannel[] getChannels() {
        return m_channels.toArray(EMPTY_MIDICHANNEL_ARRAY);
    }

    public VoiceStatus[] getVoiceStatus() {
        return EMPTY_VOICESTATUS_ARRAY;
    }

    public boolean isSoundbankSupported(Soundbank soundbank) {
        return false;
    }

    public boolean loadInstrument(Instrument instrument) {
        return false;
    }

    public void unloadInstrument(Instrument instrument) {
    }

    public boolean remapInstrument(Instrument from, Instrument to) {
        return false;
    }

    public Soundbank getDefaultSoundbank() {
        return null;
    }

    public Instrument[] getAvailableInstruments() {
        return null;
    }

    public Instrument[] getLoadedInstruments() {
        return null;
    }

    public boolean loadAllInstruments(Soundbank soundbank) {
        return false;
    }

    public void unloadAllInstruments(Soundbank soundbank) {
    }

    public boolean loadInstruments(Soundbank soundbank, Patch[] aPatches) {
        return false;
    }

    public void unloadInstruments(Soundbank soundbank, Patch[] aPatches) {
    }
}
