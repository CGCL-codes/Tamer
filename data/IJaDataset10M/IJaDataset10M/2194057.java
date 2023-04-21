package gui;

import java.awt.Color;
import java.util.Hashtable;
import junit.framework.TestCase;
import color.ColorWheel;
import color.HSLColor;

/**
 * Class test for thee HSLColorLabel.
 * 
 * @see gui.HSLColorLabel
 * @author Desprez Jean-Marc
 * 
 */
public class HSLColorLabelTest extends TestCase {

    /**
   * Try to add a HSLColorLabel into a Hashtable and get it.
   */
    public void testHash() {
        Hashtable<HSLColorLabel, Integer> table = new Hashtable<HSLColorLabel, Integer>();
        HSLColorLabel label = new HSLColorLabel(new HSLColor(123456), true);
        Integer i = table.get(label);
        if (i == null) {
            table.put(label, new Integer(1));
            i = table.get(label);
            assertNotNull(i);
            assertEquals(i, new Integer(1));
        } else {
            fail();
        }
    }

    /**
   * Test the equals function.
   */
    public void testEquals() {
        HSLColor color = new HSLColor(123456);
        assertEquals(color, color);
        assertFalse(new HSLColorLabel(color, true).equals(new HSLColorLabel(color, true)));
        assertFalse(new HSLColorLabel(color, true).equals(null));
        assertFalse(new HSLColorLabel(color, true).equals(this));
    }

    /**
   * Test a linked color in a HSLColorLabel.
   */
    public void testLink() {
        ColorWheel wheel = new ColorWheel(255, 0, 0);
        HSLColorLabel label = new HSLColorLabel(wheel.getWheel()[0], true);
        assertTrue(label.getColor() == wheel.getWheel()[0]);
        Color color = new Color(0, 255, 0);
        wheel.setMainColor(color);
        assertTrue(label.getColor() == wheel.getWheel()[0]);
    }
}
