package de.frewert.vboxj.gui.swing;

import static org.junit.Assert.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import javax.sound.sampled.AudioFileFormat;
import org.junit.Before;
import org.junit.Test;

/**
 * <pre>
 * Copyright (C) 2010 Carsten Frewert. All Rights Reserved.
 * 
 * The VBox/J package (de.frewert.vboxj.*) is distributed under
 * the terms of the Artistic license.
 * </pre>
 * @author Carsten Frewert
 * &lt;<a href="mailto:frewert@users.sourceforge.net">
 * frewert@users.sourceforge.net</a>&gt;
 */
public class WaveFileFilterTest {

    private WaveFileFilter filter;

    @Before
    public void setUp() throws Exception {
        filter = new WaveFileFilter();
    }

    @Test
    public void testGetExtension() {
        assertEquals(".wav", filter.getExtension());
    }

    @Test
    public void testGetAudioFileType() {
        assertEquals(AudioFileFormat.Type.WAVE, filter.getAudioFileType());
    }

    @Test
    public void testAcceptDirectory() throws URISyntaxException {
        File dir = fileFromResource("/testDir");
        assertTrue("directory should be accepted", filter.accept(dir));
    }

    @Test
    public void testAcceptFile() throws URISyntaxException {
        File noWave = fileFromResource("/testDir/silence.au");
        assertFalse("file should NOT be accepted", filter.accept(noWave));
        File wave = fileFromResource("/testDir/silence.wav");
        assertTrue("file should be accepted", filter.accept(wave));
    }

    @Test
    public void testGetDescription() {
        assertTrue(filter.getDescription().contains(filter.getExtension()));
    }

    private File fileFromResource(String res) throws URISyntaxException {
        URL url = WaveFileFilterTest.class.getResource(res);
        File file = new File(url.toURI());
        return file;
    }
}
