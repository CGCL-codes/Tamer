package soapdust.wsdl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.TestCase;
import org.xml.sax.SAXException;

public class WsdlParserTest extends TestCase {

    public void testAssociatesDefinitionWithNs() throws MalformedURLException, SAXException, IOException, ParserConfigurationException {
        WebServiceDescription result = new WsdlParser(new URL("file:test/soapdust/wsdl/with-simple-message.wsdl")).parse();
        assertNotNull(result.getDefinition("definitionNS"));
    }

    public void testParseWsdlWithElementWhoseTypeNameIsTheSameButInDifferentNamespaceDoNotStackOverflow() throws MalformedURLException, SAXException, IOException, ParserConfigurationException {
        new WsdlParser(new URL("file:test/soapdust/wsdl/type-element-whose-type-has-the-same-name.wsdl")).parse();
    }

    public void testParseWsdlWithImportStatement() throws MalformedURLException, SAXException, IOException, ParserConfigurationException {
        WebServiceDescription result = new WsdlParser(new URL("file:test/soapdust/wsdl/import-wsdl.wsdl")).parse();
        assertNotNull(result.getDefinition("definitionNS").operations.get("testOperation1"));
    }
}
