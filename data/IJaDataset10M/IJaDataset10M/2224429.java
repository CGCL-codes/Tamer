package com.evolution.player.jamendo.tests;

import static org.junit.Assert.*;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;
import com.evolution.player.internal.jamendo.core.JamendoUtils;
import com.evolution.player.internal.jamendo.core.search.JamendoAlbum;
import com.evolution.player.internal.jamendo.core.search.JamendoArtist;
import com.evolution.player.internal.jamendo.core.search.JamendoDB;
import com.evolution.player.internal.jamendo.core.search.JamendoDBLoader;

/**
 * @since 0.7
 */
public class JamendoDBTests {

    @Test
    public void loadDB() throws Exception {
        JamendoDBLoader loader = new JamendoDBLoader();
        JamendoDB db = (JamendoDB) loader.load(new NullProgressMonitor());
        assertDB(db);
    }

    @Test
    public void accessDB() throws Exception {
        JamendoDB db = JamendoUtils.getDB(false);
        assertDB(db);
    }

    private void assertDB(JamendoDB db) {
        assertNotNull(db);
        JamendoArtist artist = db.getArtist("Talco");
        assertNotNull(artist);
        assertEquals("Talco", artist.getName());
        assertEquals(5515, artist.getJamendoId());
        JamendoAlbum album = artist.getAlbum("Combat Circus");
        assertNotNull(album);
        assertEquals("Combat Circus", album.getName());
        assertEquals(4818, album.getJamendoId());
    }
}
