package calclipse.lib.lcd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.IOException;
import misc.TestSerializedObject;
import org.junit.Test;

public class DigitSerializationTest {

    public DigitSerializationTest() {
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        final Digit digit = new Digit();
        digit.setDigitSize(new Dimension(100, 100));
        digit.setInsets(new Insets(100, 200, 300, 400));
        digit.setThickness(500);
        digit.setTransparent(true);
        digit.setSelfUpdating(false);
        final TestSerializedObject ser = new TestSerializedObject(digit);
        final Digit digit2 = (Digit) ser.deSerialize();
        assertEquals("digitSize", new Dimension(100, 100), digit2.getDigitSize());
        assertEquals("insets", new Insets(100, 200, 300, 400), digit2.getInsets());
        assertEquals("thickness", 500, digit2.getThickness());
        assertTrue("transparent", digit2.isTransparent());
        assertFalse("updating", digit2.isSelfUpdating());
    }
}
