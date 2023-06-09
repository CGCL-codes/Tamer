package net.sf.beezle.sushi.fs;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SettingsTest {

    @Test(expected = IllegalArgumentException.class)
    public void invalidEncoding() {
        new Settings("nosuchencoding");
    }

    @Test
    public void encoding() {
        encode("");
        encode("abc");
        encode(" ");
    }

    private void encode(String str) {
        Settings settings;
        settings = new Settings();
        assertEquals(str, settings.string(settings.bytes(str)));
    }
}
