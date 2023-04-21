package capoeira.berimbau.tab;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

public class TabMidiChannel {

    private static MidiChannel channel;

    static {
        Synthesizer synthesizer = null;
        Instrument[] instruments = null;
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
        } catch (Exception e) {
        }
        Soundbank sb = synthesizer.getDefaultSoundbank();
        if (sb != null) {
            instruments = synthesizer.getDefaultSoundbank().getInstruments();
            synthesizer.loadInstrument(instruments[0]);
            MidiChannel[] midiChannels = synthesizer.getChannels();
            channel = midiChannels[0];
        }
    }

    public static void play(int noteNumber) {
    }

    public static void main(String[] args) {
        new TabMidiChannel();
    }
}
