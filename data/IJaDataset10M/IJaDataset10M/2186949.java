package flattree.xml.sax;

import java.io.StringWriter;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;
import flattree.xml.AbstractXMLTest;

/**
 * Test for {@link FlatHandler}.
 */
public class FlatHandlerTest extends AbstractXMLTest {

    public void test() throws Exception {
        InputSource input = new InputSource(getXML());
        StringWriter string = new StringWriter();
        DefaultHandler handler = new FlatHandler(getRoot(), string);
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse(input, handler);
        assertEqual(getFlat(), string.toString());
    }
}
