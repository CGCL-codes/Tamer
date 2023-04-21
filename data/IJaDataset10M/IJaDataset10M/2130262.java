package fedora.server.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.custommonkey.xmlunit.SimpleXpathEngine;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;
import fedora.test.FedoraTestCase;

/**
 * @author Edwin Shin
 */
public class TestServerConfiguration extends FedoraTestCase {

    private static final File FCFG_BASE = new File("src/fcfg/server/fedora-base.fcfg");

    private static final String NS_FCFG_PREFIX = "fcfg";

    private DocumentBuilder builder;

    private ByteArrayOutputStream out;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestServerConfiguration.class);
    }

    protected void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        SimpleXpathEngine.registerNamespace(NS_FCFG_PREFIX, NS_FCFG);
        XMLUnit.setIgnoreWhitespace(false);
    }

    protected void tearDown() throws Exception {
        SimpleXpathEngine.clearNamespaces();
        XMLUnit.setIgnoreWhitespace(false);
        out.close();
    }

    public void testApplyProperties() throws Exception {
        ServerConfiguration config = getServerConfiguration();
        String testVal = "9999";
        String xpath = "/" + NS_FCFG_PREFIX + ":server/" + NS_FCFG_PREFIX + ":param[@name='fedoraServerPort'][@value='" + testVal + "']";
        Properties props = new Properties();
        props.put("server.fedoraServerPort", testVal);
        config.serialize(out);
        assertXpathNotExists(xpath, getDocument(out));
        out.reset();
        props.put("server.fedoraServerPort", testVal);
        config.applyProperties(props);
        config.serialize(out);
        assertXpathExists(xpath, getDocument(out));
    }

    public void testSerialize() throws Exception {
        ServerConfiguration config = getServerConfiguration();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        config.serialize(out);
        Document original = builder.parse(FCFG_BASE);
        Document generated = builder.parse(new ByteArrayInputStream(out.toByteArray()));
        XMLUnit.setIgnoreWhitespace(true);
        assertXMLEqual(original, generated);
    }

    private Document getDocument(ByteArrayOutputStream out) throws Exception {
        return builder.parse(new ByteArrayInputStream(out.toByteArray()));
    }
}
