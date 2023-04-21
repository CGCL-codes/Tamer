package phex.xml.thex;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import phex.common.log.NLogger;

public class ThexHashTreeCodecTest extends TestCase {

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public void testToAndFrom() throws IOException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<!DOCTYPE hashtree SYSTEM \"http://open-content.net/spec/thex/thex.dtd\">" + "<hashtree>" + "<file size='1146045066' segmentsize='1024'/>" + "<digest algorithm='http://www.w3.org/2000/09/xmldsig#sha1' outputsize='20'/>" + "<serializedtree depth='22' type='http://open-content.net/spec/thex/breadthfirst' uri='uuid:09233523-345b-4351-b623-5dsf35sgs5d6'/>" + "</hashtree>";
        ThexHashTree hashTree = ThexHashTreeCodec.parseThexHashTreeXML(new ByteArrayInputStream(xml.getBytes()));
        byte[] output = ThexHashTreeCodec.generateThexHashTreeXML(hashTree);
        hashTree = ThexHashTreeCodec.parseThexHashTreeXML(new ByteArrayInputStream(output));
    }

    public void testParsing() throws IOException {
        InputStream inStream = ClassLoader.getSystemResourceAsStream("phex/xml/thex/ThexTest.xml");
        Assert.assertNotNull(inStream);
        ThexHashTree hashTree = ThexHashTreeCodec.parseThexHashTreeXML(inStream);
        assertEquals("1000", hashTree.getFileSize());
        inStream = ClassLoader.getSystemResourceAsStream("phex/xml/thex/ThexTest2.xml");
        Assert.assertNotNull(inStream);
        hashTree = ThexHashTreeCodec.parseThexHashTreeXML(inStream);
        assertEquals("1698304", hashTree.getFileSize());
    }
}
