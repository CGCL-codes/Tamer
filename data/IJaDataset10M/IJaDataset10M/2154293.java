package examples;

import javax.sound.midi.MidiMessage;
import engine.api.MidiEffect;
import engine.utils.Note;

/**
 * ExampleEffect just translate each note to the next octave, 
 * 
 * 
 * @author Marc Haussaire
 *
 */
public class ExampleEffect extends MidiEffect {

    public void send(MidiMessage message, long timeStamp) {
        Note n = new Note(message);
        if (!n.valid) {
            sendToAll(message, timeStamp);
            return;
        }
        n.note = (n.note + 12) % 128;
        sendToAll(n.toMsg(), timeStamp);
    }
}
