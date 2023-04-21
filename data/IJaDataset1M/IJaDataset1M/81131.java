package org.fredy.id3tidy.util;

import java.io.File;
import static org.junit.Assert.*;
import org.fredy.id3tidy.M3UInfo;
import org.junit.Test;

public class M3UUtilsTest {

    @Test
    public void testGetM3UInfoWithAbsolutePath() {
        M3UInfo m3uInfo = M3UUtils.getM3UInfo(new File("mp3/Your Artist - Your Title.mp3"), true);
        assertEquals("Your Artist", m3uInfo.getArtist());
        assertEquals("Your Title", m3uInfo.getTitle());
        String currentDir = new File(".").getAbsolutePath();
        assertEquals(currentDir.substring(0, currentDir.length() - 1) + "mp3" + File.separator + "Your Artist - Your Title.mp3", m3uInfo.getPath());
        assertEquals(2, m3uInfo.getTrackLength());
    }

    @Test
    public void testGetM3UInfoWithRelativePath() {
        M3UInfo m3uInfo = M3UUtils.getM3UInfo(new File("mp3/Your Artist - Your Title.mp3"), false);
        assertEquals("Your Artist", m3uInfo.getArtist());
        assertEquals("Your Title", m3uInfo.getTitle());
        assertEquals("Your Artist - Your Title.mp3", m3uInfo.getPath());
        assertEquals(2, m3uInfo.getTrackLength());
    }
}
