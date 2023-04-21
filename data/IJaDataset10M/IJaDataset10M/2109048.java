package be.lassi.control.device;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import java.io.IOException;
import org.testng.annotations.Test;
import be.lassi.support.ObjectBuilder;
import be.lassi.support.ObjectTest;
import be.lassi.xml.Xml;

/**
 * Tests class <code>Button</code>.
 */
public class ButtonTestCase {

    @Test
    public void equals() {
        Button button1 = new Button("name1", 1);
        Button button2 = new Button("name1", 1);
        Button button3 = new Button("name1", 2);
        Button button4 = new Button("name2", 1);
        assertTrue(button1.equals(button2));
        assertFalse(button1.equals(button3));
        assertFalse(button1.equals(button4));
    }

    @Test
    public void storable() throws IOException {
        String xml1 = "<button name=\"name\" number=\"1\"/>\n";
        Button button1 = new Button("name", 1);
        String xml2 = Xml.toString(button1);
        Object button2 = Xml.toObject(xml2);
        Object button3 = Xml.toObject(xml1);
        assertEquals(button1, button2);
        assertEquals(button1, button3);
    }

    @Test
    public void object() {
        ObjectBuilder b = new ObjectBuilder() {

            public Object getObject1() {
                return new Button("name", 1);
            }

            public Object getObject2() {
                return new Button("name", 2);
            }
        };
        ObjectTest.test(b);
    }
}
