package com.sun.mmedia;

import javax.microedition.media.MediaException;

public class QSoundTonePlayer implements TonePlayer {

    private QSoundSynthPerformance qsSP;

    public QSoundTonePlayer() {
        qsSP = new QSoundSynthPerformance();
        qsSP.setChannel(5);
    }

    public void playTone(int note, int duration, int volume) throws MediaException {
        if ((note < 0) || (note > 127)) throw new IllegalArgumentException("illegal note");
        if (duration <= 0) throw new IllegalArgumentException("duration must be positive");
        if (volume < 0) volume = 0; else if (volume > 100) volume = 100;
        qsSP.playTone(note, duration, volume);
    }
}
