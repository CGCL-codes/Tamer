package jbprocessor;

import static org.junit.Assert.assertTrue;
import javax.xml.xpath.XPathConstants;
import org.junit.Before;
import org.junit.Test;

public class JprocessWsdlTest5 extends JprocessWsdlTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void Test() {
        super.test();
    }

    public JprocessWsdlTest5() {
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
            String inMsgName = (String) xpath.evaluate("/wsdl:definitions/wsdl:portType[@name=\"WebService5\"]/wsdl:operation[@name=\"method5\"]/wsdl:input/@message", new org.xml.sax.InputSource(fileName), XPathConstants.STRING);
            assertTrue(inMsgName != null && inMsgName.endsWith(":method5"));
            String outMsgName = (String) xpath.evaluate("/wsdl:definitions/wsdl:portType[@name=\"WebService5\"]/wsdl:operation[@name=\"method5\"]/wsdl:output/@message", new org.xml.sax.InputSource(fileName), XPathConstants.STRING);
            assertTrue(outMsgName != null && outMsgName.endsWith(":method5Response"));
            String param1Name = (String) xpath.evaluate("/wsdl:definitions/wsdl:message[@name=\"method5\"]/wsdl:part[1]/@name", new org.xml.sax.InputSource(fileName), XPathConstants.STRING);
            assertTrue(param1Name != null && param1Name.equals("m5p1"));
            String resultName = (String) xpath.evaluate("/wsdl:definitions/wsdl:message[@name=\"method5Response\"]/wsdl:part[1]/@name", new org.xml.sax.InputSource(fileName), XPathConstants.STRING);
            assertTrue(resultName != null && resultName.equals("result5"));
            isValid = true;
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            assertTrue(false);
        }
        return isValid;
    }
}
