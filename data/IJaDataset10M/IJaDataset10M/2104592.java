package jbprocessor;

import static org.junit.Assert.assertTrue;
import javax.xml.xpath.XPathConstants;
import org.junit.Before;
import org.junit.Test;

public class JprocessWsdlTest4 extends JprocessWsdlTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void Test() {
        super.test();
    }

    public JprocessWsdlTest4() {
        super();
        targetNamespace = "wsdltests";
    }

    @Override
    protected boolean validateOutput() {
        boolean isValid = false;
        try {
            javax.xml.xpath.XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
            javax.xml.xpath.XPath xpath = factory.newXPath();
            xpath.setNamespaceContext(new TestNamespaceContext());
            String param1Name = (String) xpath.evaluate("/wsdl:definitions/wsdl:message[@name=\"Method4\"]/wsdl:part[1]/@name", new org.xml.sax.InputSource(fileName), XPathConstants.STRING);
            assertTrue(param1Name != null && param1Name.equals("param1"));
            String param1Type = (String) xpath.evaluate("/wsdl:definitions/wsdl:message[@name=\"Method4\"]/wsdl:part[1]/@type", new org.xml.sax.InputSource(fileName), XPathConstants.STRING);
            assertTrue(param1Type != null && param1Type.equals("xsd:string"));
            String param2Name = (String) xpath.evaluate("/wsdl:definitions/wsdl:message[@name=\"Method4\"]/wsdl:part[2]/@name", new org.xml.sax.InputSource(fileName), XPathConstants.STRING);
            assertTrue(param2Name != null && param2Name.equals("param2"));
            String param2Type = (String) xpath.evaluate("/wsdl:definitions/wsdl:message[@name=\"Method4\"]/wsdl:part[2]/@type", new org.xml.sax.InputSource(fileName), XPathConstants.STRING);
            assertTrue(param2Type != null && param2Type.equals("xsd:string"));
            String resultName = (String) xpath.evaluate("/wsdl:definitions/wsdl:message[@name=\"Method4Response\"]/wsdl:part[1]/@name", new org.xml.sax.InputSource(fileName), XPathConstants.STRING);
            assertTrue(resultName != null && resultName.equals("Method4Response"));
            isValid = true;
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            assertTrue(false);
        }
        return isValid;
    }
}
