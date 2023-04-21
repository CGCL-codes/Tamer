package test.org.formaria.xml.basic;

import java.io.InputStreamReader;
import java.io.Reader;
import jmunit.framework.cldc11.AssertionFailedException;
import jmunit.framework.cldc11.TestCase;
import org.formaria.xml.XmlElement;
import org.formaria.xml.basic.BasicXmlParser;

/**
 * @author luano
 */
public class BasicXmlParserTest extends TestCase {

    public BasicXmlParserTest() {
        super(1, "BasicXmlParserTest");
    }

    public void test(int testNumber) throws Throwable {
        switch(testNumber) {
            case 0:
                testParser();
                break;
        }
    }

    public void testParser() throws AssertionFailedException {
        BasicXmlParser parser = new BasicXmlParser();
        Reader r = new InputStreamReader(getClass().getResourceAsStream("TestPage.xml"));
        XmlElement xml = parser.parse(r);
        assertTrue(xml != null);
    }
}
