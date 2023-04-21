package it.diamonds.tests.engine.audio;

import it.diamonds.engine.audio.SoundException;
import junit.framework.TestCase;

public class TestSoundException extends TestCase {

    public void testSoundException() {
        try {
            throw new SoundException();
        } catch (SoundException e) {
            return;
        }
    }

    public void testSoundExceptionWithMessage() {
        try {
            throw new SoundException("exception test message");
        } catch (SoundException e) {
            return;
        }
    }
}
