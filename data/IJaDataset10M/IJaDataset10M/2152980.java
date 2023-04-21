package be.lassi.control.midi;

import javax.sound.midi.MidiMessage;

public interface MidiMessageListener {

    void received(MidiMessage message);
}
