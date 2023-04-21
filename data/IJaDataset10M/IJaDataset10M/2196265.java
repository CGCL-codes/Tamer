package br.com.musiclabs.test;

import br.com.music.Duration;
import br.com.music.player.Metronome;
import br.com.music.rhythm.TimeSignature;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Test;

public class RhythmTest {

    @Test
    public void testTimeSignature() {
        try {
            TimeSignature sig = new TimeSignature(TimeSignature.QUATERNARY, Duration.SEMIBREVE);
            Metronome metronome = new Metronome(sig);
            System.out.println(metronome);
        } catch (Exception ex) {
        }
    }
}
