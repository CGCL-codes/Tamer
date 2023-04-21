package dmxeffects.sound;

import com.trolltech.qt.core.QObject;

/**
 * @author chris
 * 
 */
public class Player extends QObject implements Runnable {

    public transient Signal0 playingSignal = new Signal0();

    public transient Signal0 stoppedSignal = new Signal0();

    /**
     * Create a new instance of this class
     */
    public Player() {
        super();
    }

    /**
     * Run when the containing Thread starts
     */
    public void run() {
    }

    /**
     * Handle a track beinq cued
     * 
     * @param track
     *                Track to cue.
     */
    public void cueTrack(final SoundTrack track) {
    }

    /**
     * Handle play signal.
     */
    public void play() {
        playingSignal.emit();
    }

    /**
     * Handle stop signal
     */
    public void stop() {
        stoppedSignal.emit();
    }
}
