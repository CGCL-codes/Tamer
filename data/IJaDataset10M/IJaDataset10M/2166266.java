package org.apache.axis2.jaxws.addressing.util;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.addressing.AddressingConstants.Final;
import org.apache.axis2.addressing.AddressingConstants.Submission;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.addressing.EndpointReferenceHelper;
import org.apache.axis2.jaxws.addressing.SubmissionEndpointReference;
import org.custommonkey.xmlunit.XMLTestCase;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import java.io.StringReader;

/**
 * This class tests the enpoint reference classes used in the JAX-WS 2.1 API.
 */
public class EndpointReferenceUtilsTests extends XMLTestCase {

    private static final OMFactory OMF = OMAbstractFactory.getOMFactory();

    private static final QName ELEMENT200508 = new QName(Final.WSA_NAMESPACE, "EndpointReference", "wsa");

    private static final QName ELEMENT200408 = new QName(Submission.WSA_NAMESPACE, "EndpointReference", "wsa");

    private static final String EPR200508 = "<wsa:EndpointReference xmlns:axis2=\"http://ws.apache.org/namespaces/axis2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" axis2:AttrExt=\"123456789\">" + "<wsa:Address>http://www.w3.org/2005/08/addressing/anonymous</wsa:Address>" + "<wsa:ReferenceParameters xmlns:fabrikam=\"http://example.com/fabrikam\">" + "<fabrikam:CustomerKey>123456789</fabrikam:CustomerKey>" + "<fabrikam:ShoppingCart>ABCDEFG</fabrikam:ShoppingCart>" + "</wsa:ReferenceParameters>" + "<wsa:Metadata>" + "<axis2:MetaExt axis2:AttrExt=\"123456789\">123456789</axis2:MetaExt>" + "</wsa:Metadata>" + "<axis2:EPRExt axis2:AttrExt=\"123456789\">123456789</axis2:EPRExt>" + "</wsa:EndpointReference>";

    private static final String EPR200408 = "<wsa:EndpointReference xmlns:axis2=\"http://ws.apache.org/namespaces/axis2\" xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\" axis2:AttrExt=\"123456789\">" + "<wsa:Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/anonymous</wsa:Address>" + "<wsa:ReferenceParameters xmlns:fabrikam=\"http://example.com/fabrikam\">" + "<fabrikam:CustomerKey>123456789</fabrikam:CustomerKey>" + "<fabrikam:ShoppingCart>ABCDEFG</fabrikam:ShoppingCart>" + "</wsa:ReferenceParameters>" + "<wsa:PortType>axis2:Jane</wsa:PortType>" + "<wsa:ServiceName PortName=\"Fred\">axis2:John</wsa:ServiceName>" + "<axis2:EPRExt axis2:AttrExt=\"123456789\">123456789</axis2:EPRExt>" + "</wsa:EndpointReference>";

    public EndpointReferenceUtilsTests(String name) {
        super(name);
    }

    public void test200508ConversionStartingFromAxis2() throws Exception {
        XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(EPR200508));
        StAXOMBuilder builder = new StAXOMBuilder(parser);
        OMElement omElement = builder.getDocumentElement();
        EndpointReference axis2EPR = EndpointReferenceHelper.fromOM(omElement);
        W3CEndpointReference jaxwsEPR = (W3CEndpointReference) EndpointReferenceUtils.convertFromAxis2(axis2EPR, Final.WSA_NAMESPACE);
        assertXMLEqual(EPR200508, jaxwsEPR.toString());
        EndpointReference axis2Result = EndpointReferenceUtils.createAxis2EndpointReference("");
        String addressingNamespace = EndpointReferenceUtils.convertToAxis2(axis2Result, jaxwsEPR);
        OMElement eprElement = EndpointReferenceHelper.toOM(OMF, axis2Result, ELEMENT200508, addressingNamespace);
        assertXMLEqual(EPR200508, eprElement.toString());
    }

    public void test200408ConversionStartingFromAxis2() throws Exception {
        XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(EPR200408));
        StAXOMBuilder builder = new StAXOMBuilder(parser);
        OMElement omElement = builder.getDocumentElement();
        EndpointReference axis2EPR = EndpointReferenceHelper.fromOM(omElement);
        SubmissionEndpointReference jaxwsEPR = (SubmissionEndpointReference) EndpointReferenceUtils.convertFromAxis2(axis2EPR, Submission.WSA_NAMESPACE);
        assertXMLEqual(EPR200408, jaxwsEPR.toString());
        EndpointReference axis2Result = EndpointReferenceUtils.createAxis2EndpointReference("");
        String addressingNamespace = EndpointReferenceUtils.convertToAxis2(axis2Result, jaxwsEPR);
        OMElement eprElement = EndpointReferenceHelper.toOM(OMF, axis2Result, ELEMENT200408, addressingNamespace);
        assertXMLEqual(EPR200408, eprElement.toString());
    }

    public void test200408ConversionStartingFromJAXWS() throws Exception {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        dbfac.setNamespaceAware(true);
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document jaxwsDoc = docBuilder.parse(new InputSource(new StringReader(EPR200408)));
        Source source = new DOMSource(jaxwsDoc);
        SubmissionEndpointReference jaxwsEPR = new SubmissionEndpointReference(source);
        EndpointReference axis2EPR = EndpointReferenceUtils.createAxis2EndpointReference("");
        String addressingNamespace = EndpointReferenceUtils.convertToAxis2(axis2EPR, jaxwsEPR);
        OMElement eprElement = EndpointReferenceHelper.toOM(OMF, axis2EPR, ELEMENT200408, addressingNamespace);
        assertXMLEqual(EPR200408, eprElement.toString());
        SubmissionEndpointReference jaxwsResult = (SubmissionEndpointReference) EndpointReferenceUtils.convertFromAxis2(axis2EPR, Submission.WSA_NAMESPACE);
        assertXMLEqual(EPR200408, jaxwsResult.toString());
    }

    public void testFailures() throws Exception {
        try {
            EndpointReferenceUtils.convertFromAxis2((EndpointReference) null, Final.WSA_NAMESPACE);
            fail("Expected a failure.");
        } catch (Exception e) {
        }
        try {
            EndpointReferenceUtils.convertFromAxis2((EndpointReference) null, Submission.WSA_NAMESPACE);
            fail("Expected a failure.");
        } catch (Exception e) {
        }
        try {
            EndpointReferenceUtils.convertFromAxis2((EndpointReference) null, null);
            fail("Expected a failure.");
        } catch (Exception e) {
        }
        try {
            EndpointReference axis2EPR = EndpointReferenceUtils.createAxis2EndpointReference("");
            EndpointReferenceUtils.convertToAxis2(axis2EPR, null);
            fail("Expected a failure.");
        } catch (Exception e) {
        }
    }
}
