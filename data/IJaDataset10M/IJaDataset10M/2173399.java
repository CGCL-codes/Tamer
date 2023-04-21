package org.freelords.sound;

/** A simple AudioListener that can be checked for having been called. */
public class TestAudioListener implements AudioListener {

    private boolean called = false;

    @Override
    public void playerFinished(AudioPlayer player) {
        called = true;
    }

    public boolean hasBeenCalled() {
        return called;
    }
}
