package org.jsresources.apps.esemiso.data;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import org.jsresources.apps.esemiso.Debug;

public class MidiSettings {

    public static final MidiDevice.Info[] EMPTY_MIDI_DEVICE_INFO_ARRAY = new MidiDevice.Info[0];

    /** This variable is holding the singleton instance of this class.
	    @see #getInstance()
	*/
    private static MidiSettings sm_instance;

    private static final String SEQUENCER_KEY = "sequencer";

    private static final String OUTPUT_KEY = "output";

    private static final boolean SEQUENCER_TYPE = true;

    private static final boolean OUTPUT_TYPE = false;

    private MidiDevice.Info[] m_aSequencerInfos;

    private MidiDevice.Info m_sequencerInfo;

    private MidiDevice.Info[] m_aOutputDeviceInfos;

    private MidiDevice.Info m_outputDeviceInfo;

    private Preferences m_userPreferences;

    public MidiSettings() {
        m_userPreferences = Preferences.userNodeForPackage(this.getClass());
        String strDevice;
        List infos;
        strDevice = m_userPreferences.get(SEQUENCER_KEY, null);
        if (strDevice != null) {
            m_sequencerInfo = getMidiDeviceInfo(strDevice);
        } else {
            infos = getMidiDeviceInfo(SEQUENCER_TYPE);
            if (infos.size() > 0) {
                m_sequencerInfo = (MidiDevice.Info) infos.get(0);
            } else {
                throw new RuntimeException("no sequencers available");
            }
        }
        strDevice = m_userPreferences.get(OUTPUT_KEY, null);
        if (strDevice != null) {
            m_outputDeviceInfo = getMidiDeviceInfo(strDevice);
        } else {
            infos = getMidiDeviceInfo(OUTPUT_TYPE);
            if (infos.size() > 0) {
                m_sequencerInfo = (MidiDevice.Info) infos.get(0);
            } else {
                throw new RuntimeException("no midi devices available");
            }
        }
    }

    public static synchronized MidiSettings getInstance() {
        if (sm_instance == null) {
            sm_instance = new MidiSettings();
        }
        return sm_instance;
    }

    public MidiDevice.Info[] getSequencers() {
        if (m_aSequencerInfos == null) {
            m_aSequencerInfos = getMidiDeviceInfo(SEQUENCER_TYPE).toArray(EMPTY_MIDI_DEVICE_INFO_ARRAY);
        }
        return m_aSequencerInfos;
    }

    public void setSequencer(MidiDevice.Info sequencer) {
        m_sequencerInfo = sequencer;
    }

    public MidiDevice.Info getSequencer() {
        return m_sequencerInfo;
    }

    public MidiDevice.Info[] getOutputs() {
        if (m_aOutputDeviceInfos == null) {
            m_aOutputDeviceInfos = getMidiDeviceInfo(OUTPUT_TYPE).toArray(EMPTY_MIDI_DEVICE_INFO_ARRAY);
        }
        return m_aOutputDeviceInfos;
    }

    public void setOutput(MidiDevice.Info outputDeviceInfo) {
        m_outputDeviceInfo = outputDeviceInfo;
    }

    public MidiDevice.Info getOutput() {
        return m_outputDeviceInfo;
    }

    public void savePreferences() throws BackingStoreException {
        storePreference(SEQUENCER_KEY, getSequencer());
        storePreference(OUTPUT_KEY, getOutput());
        m_userPreferences.flush();
    }

    private void storePreference(String strKey, MidiDevice.Info info) {
        if (info != null) {
            String strDeviceName = info.getName();
            m_userPreferences.put(strKey, strDeviceName);
        }
    }

    /** Obtain a list of available MidiDevices.
		If bSequencer is true, only sequencers are returned.
		If bSequencer is false, only devices that are not sequencers and have
		Receivers are returned.
	 */
    private static List<MidiDevice.Info> getMidiDeviceInfo(boolean bSequencer) {
        MidiDevice.Info[] aInfos = MidiSystem.getMidiDeviceInfo();
        List<MidiDevice.Info> filteredInfos = new ArrayList<MidiDevice.Info>();
        for (int i = 0; i < aInfos.length; i++) {
            try {
                MidiDevice dev = MidiSystem.getMidiDevice(aInfos[i]);
                if ((bSequencer && dev instanceof Sequencer) || (!bSequencer && !(dev instanceof Sequencer) && dev.getMaxReceivers() != 0)) {
                    filteredInfos.add(aInfos[i]);
                }
            } catch (Exception e) {
                if (Debug.getTraceAllExceptions()) Debug.out(e);
            }
        }
        return filteredInfos;
    }

    /** Obtain the Info object for a MidiDevice with a given name.
	 *	This method tries to return a MidiDevice.Info whose name
	 *	matches the passed name. If no matching MidiDevice.Info is
	 *	found, null is returned.
	 */
    private static MidiDevice.Info getMidiDeviceInfo(String strDeviceName) {
        MidiDevice.Info[] aInfos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < aInfos.length; i++) {
            if (aInfos[i].getName().equals(strDeviceName)) {
                return aInfos[i];
            }
        }
        return null;
    }
}
