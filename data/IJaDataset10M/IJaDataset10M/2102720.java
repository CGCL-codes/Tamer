package net.galluzzo.wave.orthopermubot.permutation;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class FatFingeringTest {

    protected FatFingering fatFingering = new FatFingering(new FakeKeyboard());

    @Test
    public void fatFingerFirstChar() {
        assertEquals("xbcde", fatFingering.permute("abcde", 0));
    }

    @Test
    public void fatFingerMiddleChar() {
        assertEquals("abxde", fatFingering.permute("abcde", 2));
    }

    @Test
    public void fatFingerLastChar() {
        assertEquals("abcdx", fatFingering.permute("abcde", 4));
    }
}
