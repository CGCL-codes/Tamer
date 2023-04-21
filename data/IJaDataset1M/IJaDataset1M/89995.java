package org.n3rd.carajo.tests.library;

import junitx.util.PrivateAccessor;
import org.n3rd.carajo.library.entry.Track;
import org.n3rd.carajo.tests.TestBase;

public class TrackFactoryTest extends TestBase {

    public void testGet() {
        Track baz = tf.get(arf.get(FOO_BAR), "Bazra");
        assertNotNull(baz);
        assertTrue(baz instanceof Track);
        assertEquals("Bazra", baz.getTitle());
    }

    public void testSingletons() {
        assertSame(tf.get(arf.get(FOO_BAR), "Foodle"), tf.get(arf.get(FOO_BAR), "Foodle"));
    }

    public void testGetUntitledTrack() {
        Track untitled = tf.get(arf.get(FOO_BAR), null);
        assertSame(tf.getUntitled(arf.get(FOO_BAR)), untitled);
        assertTrue(untitled.isUntitled());
        assertSame(untitled, tf.get(arf.get(FOO_BAR), null));
    }

    public void testGetUntitledTrackFail() {
        try {
            String unknownStr = (String) PrivateAccessor.getField(Track.class, "UNKNOWN");
            tf.get(arf.get(FOO_BAR), unknownStr);
            fail();
        } catch (NoSuchFieldException e) {
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void testGetDifferentUntitledTracks() {
        Track unknownFoo = tf.get(arf.get("Sir Foo"), null);
        Track unknownBar = tf.get(arf.get("Lord Bar"), null);
        assertFalse(unknownFoo.equals(unknownBar));
    }

    public void testGetTracks() {
        assertEquals(0, tf.getAll(arf.get(FOO_BAR)).length);
        tf.get(arf.get(FOO_BAR), "Foodle");
        assertEquals(1, tf.getAll(arf.get(FOO_BAR)).length);
        tf.get(arf.get(FOO_BAR), "Foodle");
        assertEquals(1, tf.getAll(arf.get(FOO_BAR)).length);
        tf.get(arf.get(FOO_BAR), "Bazra");
        assertEquals(2, tf.getAll(arf.get(FOO_BAR)).length);
    }

    public void testGetTrackSorting() {
        Track[] tracks = { tf.get(arf.get(FOO_BAR), "Apes"), tf.get(arf.get(FOO_BAR), "Hooters"), tf.get(arf.get(FOO_BAR), "Zebras") };
        Track[] result = tf.getAll(arf.get(FOO_BAR));
        assertEquals(tracks.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertSame(tracks[i], result[i]);
        }
    }
}
