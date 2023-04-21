package be.lassi.pdf;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import org.testng.annotations.Test;

/**
 * Tests class <code>HeaderLevel</code>.
 */
public class HeaderLevelTestCase {

    @Test
    public void testGetNumber() {
        HeaderLevel level = new HeaderLevel();
        assertNull(level.getNumber("p"));
        assertEquals(level.getNumber("h1"), "1");
        assertEquals(level.getNumber("h2"), "1.1");
        assertEquals(level.getNumber("h2"), "1.2");
        assertEquals(level.getNumber("h3"), "1.2.1");
        assertEquals(level.getNumber("h3"), "1.2.2");
        assertEquals(level.getNumber("h2"), "1.3");
        assertEquals(level.getNumber("h2"), "1.4");
        assertEquals(level.getNumber("h3"), "1.4.1");
        assertEquals(level.getNumber("h1"), "2");
    }
}
