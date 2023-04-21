package com.ibm.wsdl.tck;

import javax.wsdl.*;
import javax.wsdl.factory.*;
import javax.xml.namespace.*;
import javax.xml.parsers.*;
import junit.framework.*;
import org.w3c.dom.*;

/**
 * This class implements a series of tests used to check
 * javax.wsdl.Service implementations for compliance
 * with the JWSDL specification.
 *
 * @author Matthew J. Duftler (duftler@us.ibm.com)
 */
public class ServiceTest extends TestCase {

    private static final String TEST_CANDIDATE_PROPERTY_NAME = "javax.wsdl.factory.WSDLFactoryCandidate";

    private static final String NS_URI_WSDL = "http://schemas.xmlsoap.org/wsdl/";

    private Definition definition = null;

    private Service service = null;

    protected void setUp() throws WSDLException {
        String testCandidateName = System.getProperty(TEST_CANDIDATE_PROPERTY_NAME);
        if (testCandidateName == null) {
            throw new IllegalArgumentException("System property '" + TEST_CANDIDATE_PROPERTY_NAME + "' must be specified to run " + "test suite.");
        }
        WSDLFactory factory = WSDLFactory.newInstance(testCandidateName);
        Assert.assertNotNull("WSDLFactory should not be null.", factory);
        definition = factory.newDefinition();
        Assert.assertNotNull("Definition should not be null.", definition);
        service = definition.createService();
        Assert.assertNotNull("Service should not be null.", service);
    }

    /**
   * Test Service.addPort(...), Service.getPort(...)
   * and Service.removePort(...).
   */
    public void testAddGetRemovePort() {
        Port port = definition.createPort();
        Assert.assertNotNull("Port should not be null.", port);
        port.setName("abcdef");
        service.addPort(port);
        Port port2 = service.getPort("abcdef");
        Assert.assertTrue("Service.getPort(...) did not return " + "the same Port that was added using " + "Service.addPort(...).", port == port2);
        Port port3 = service.getPort("abcdef");
        Assert.assertTrue("Service.removePort(...) did not return " + "the same Port that was added using " + "Service.addPort(...).", port == port3);
    }

    /**
   * Test Service.setDocumentationElement(...) and
   * Service.getDocumentationElement().
   */
    public void testSetGetDocumentationElement() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element el = doc.createElementNS(NS_URI_WSDL, "wsdl:documentation");
        service.setDocumentationElement(el);
        Element el2 = service.getDocumentationElement();
        Assert.assertTrue("Service.getDocumentationElement() did not return " + "the same Element that was set using " + "Service.setDocumentationElement(...).", el == el2);
    }

    /**
   * Test Service.setQName(...) and Service.getQName().
   */
    public void testSetGetQName() {
        QName qname = new QName("urn:abc", "def");
        service.setQName(qname);
        QName qname2 = service.getQName();
        Assert.assertTrue("Service.getQName() did not return " + "the same QName that was set using " + "Service.setQName(...).", qname.equals(qname2));
    }
}
