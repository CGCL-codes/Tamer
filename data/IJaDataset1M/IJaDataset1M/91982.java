package javax.sound.midi.util;

import javax.sound.midi.ShortMessage;

public class NoteOn extends NoteMessage {

    @Override
    protected int getCommand() {
        return ShortMessage.NOTE_ON;
    }
}
