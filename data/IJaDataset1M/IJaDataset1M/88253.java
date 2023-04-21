package net.sourceforge.strategema.games;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sourceforge.strategema.games.FileBasedDictionary.Case;
import java.util.Locale;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileBasedDictionaryTest {

    private static Dictionary dict;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        dict = new FileBasedDictionary("share/dict/sowpods.txt", "ISO-8859-1", Case.UPPERCASE, Locale.UK);
    }

    @Test
    public void testGetDigest() {
        final byte[] hash = dict.getDigest();
        assertEquals(hash.length, 32);
    }

    @Test
    public void testIsInDictionary() {
        assertTrue(dict.isInDictionary("chicken"));
        assertFalse(dict.isInDictionary("dsfghjycv"));
        assertFalse(dict.isInDictionary("alpha10"));
    }
}
