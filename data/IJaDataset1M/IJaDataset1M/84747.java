package net.sf.derquinsej.i18n;

import static net.sf.derquinsej.i18n.Locales.fromString;
import static net.sf.derquinsej.i18n.Locales.safeFromString;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import java.util.Locale;
import org.testng.annotations.Test;

/**
 * Tests for Locales
 * @author Andres Rodriguez
 */
public class LocalesTest {

    @Test
    public void testOk() {
        assertEquals(fromString("en"), new Locale("en"));
        assertEquals(fromString("en_EN"), new Locale("en", "EN"));
        assertEquals(fromString("en_EN_WIN"), new Locale("en", "EN", "WIN"));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void checkNullValue() {
        fromString(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void bad01() {
        fromString("");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void bad02() {
        fromString("_");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void bad03() {
        fromString("1_2_3_4_5");
    }

    @Test
    public void testSafe() {
        assertNull(safeFromString(null));
        assertNull(safeFromString(""));
        assertNotNull(safeFromString("en"));
        assertEquals(safeFromString("en_EN"), new Locale("en", "EN"));
    }
}
