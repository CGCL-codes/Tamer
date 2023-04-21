package net.sf.doolin.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.text.ParseException;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link net.sf.doolin.util.PatternFormat}.
 * 
 * @author Damien Coraboeuf
 */
public class TestPatternFormat {

    /**
	 * Format to be used
	 */
    private PatternFormat format;

    /**
	 * Creates a format to test.
	 */
    @Before
    public void setUp() {
        this.format = new PatternFormat("Test*Format");
    }

    /**
	 * Test method for {@link PatternFormat#format(Object...)}
	 */
    @Test
    public void testFormat() {
        String value = this.format.format("1");
        assertEquals("Test1Format", value);
    }

    /**
	 * Test method for {@link PatternFormat#accept(String)}
	 */
    @Test
    public void testAccept() {
        String value = "Test1Format";
        boolean result = this.format.accept(value);
        assertTrue(result);
    }

    /**
	 * Test method for {@link PatternFormat#parse(String)}
	 * 
	 * @throws ParseException
	 *             If the parsing cannot be done.
	 */
    @Test
    public void testParse() throws ParseException {
        String value = "Test1Format";
        Object[] components = this.format.parse(value);
        assertEquals(1, components.length);
        assertEquals("1", components[0]);
    }

    /**
	 * Test method for {@link PatternFormat#toString()}
	 */
    @Test
    public void testToString() {
        assertEquals("Test*Format", this.format.toString());
    }

    /**
	 * Test method for {@link PatternFormat#equals(Object)}
	 */
    @Test
    public void testEqualsObject() {
        PatternFormat other = new PatternFormat("Test*Format");
        assertEquals(this.format, other);
    }

    /**
	 * Test method for {@link PatternFormat#hashCode()}
	 */
    @Test
    public void testHashCode() {
        String pattern = "Test*Format";
        PatternFormat other = new PatternFormat(pattern);
        int patternHashCode = pattern.hashCode();
        assertEquals(patternHashCode, other.hashCode());
    }
}
