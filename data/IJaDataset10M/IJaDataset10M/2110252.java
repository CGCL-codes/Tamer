package watij;

import static watij.finders.FinderFactory.*;
import watij.elements.Buttons;
import junit.framework.Test;
import junit.framework.TestSuite;
import java.io.*;
import java.util.Properties;

public class UnicodeTest extends WatijTestCase {

    private Properties properties = new Properties();

    protected void setUp() throws Exception {
        super.setUp();
        ie.goTo(HTML_ROOT + "unicode.html");
        InputStream is = new FileInputStream(HTML_ROOT + "..\\unicode.properties");
        try {
            properties.load(is);
            is.close();
        } catch (IOException e) {
            fail("Unable to load unicode.properties from the 'res' directory");
        }
        try {
            is.close();
        } catch (IOException e) {
        }
    }

    public void testFrench() throws Exception {
        Buttons b = ie.buttons();
        String text = properties.getProperty("french");
        assertEquals("French button text is not the same as the resource", b.get(0).text(), text);
        assertTrue("Text finder should find French button", ie.button(text(text)).exists());
        assertTrue("Xpath finder should find French button", ie.button(xpath("//BUTTON[text() = '" + text + "']")).exists());
    }

    public void testSimplifiedChinese() throws Exception {
        Buttons b = ie.buttons();
        String text = properties.getProperty("simplified_chinese");
        assertEquals("Simplified Chinese button text is not the same as the resource", b.get(1).text(), text);
        assertTrue("Text finder should find Simplified Chinese button", ie.button(text(text)).exists());
        assertTrue("Xpath finder should find Simplified Chinese button", ie.button(xpath("//BUTTON[text() = '" + text + "']")).exists());
    }

    public static Test suite() {
        return new TestSuite(UnicodeTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(UnicodeTest.suite());
    }
}
