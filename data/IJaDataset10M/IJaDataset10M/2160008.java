package org.apache.axis2.jaxws.message;

import junit.framework.TestCase;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMOutputFormat;
import org.apache.axiom.om.OMSourcedElement;
import org.apache.axiom.om.impl.llom.OMSourcedElementImpl;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.apache.axis2.datasource.jaxb.JAXBCustomBuilder;
import org.apache.axis2.datasource.jaxb.JAXBDSContext;
import org.apache.axis2.datasource.jaxb.JAXBDataSource;
import org.apache.axis2.jaxws.addressing.SubmissionEndpointReference;
import org.apache.axis2.jaxws.addressing.SubmissionEndpointReferenceBuilder;
import org.apache.axis2.jaxws.message.databinding.JAXBBlockContext;
import org.apache.axis2.jaxws.message.databinding.JAXBUtils;
import org.apache.axis2.jaxws.message.factory.JAXBBlockFactory;
import org.apache.axis2.jaxws.message.factory.MessageFactory;
import org.apache.axis2.jaxws.message.factory.SAAJConverterFactory;
import org.apache.axis2.jaxws.message.factory.XMLStringBlockFactory;
import org.apache.axis2.jaxws.message.util.SAAJConverter;
import org.apache.axis2.jaxws.registry.FactoryRegistry;
import org.apache.axis2.jaxws.unitTest.TestLogger;
import test.EchoStringResponse;
import test.ObjectFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.TreeSet;

/**
 * MessageTests
 * Tests to create and validate Message processing
 * These are not client/server tests.  
 * Instead the tests simulate the processing of a Message during client/server processing.
 */
public class MessageTests extends TestCase {

    private static final String soap11env = "http://schemas.xmlsoap.org/soap/envelope/";

    private static final String soap12env = "http://www.w3.org/2003/05/soap-envelope";

    private static final String sampleEnvelopeHead11 = "<soapenv:Envelope xmlns:soapenv=\"" + soap11env + "\">" + "<soapenv:Header /><soapenv:Body>";

    private static final String sampleEnvelopeHead12 = "<soapenv:Envelope xmlns:soapenv=\"" + soap12env + "\">" + "<soapenv:Header /><soapenv:Body>";

    private static final String sampleEnvelopeTail = "</soapenv:Body></soapenv:Envelope>";

    private static final String sampleText = "<pre:a xmlns:pre=\"urn://sample\">" + "<b>Hello</b>" + "<c>World</c>" + "</pre:a>";

    private static final String sampleDouble = "<pre:a xmlns:pre=\"urn://sample\">" + "<b>Hello</b>" + "<c>World</c>" + "</pre:a>" + "<pre:aa xmlns:pre=\"urn://sample\">" + "<b>Hello</b>" + "<c>World</c>" + "</pre:aa>";

    private static final String sampleEnvelope11 = sampleEnvelopeHead11 + sampleText + sampleEnvelopeTail;

    private static final String sampleEnvelope12 = sampleEnvelopeHead12 + sampleText + sampleEnvelopeTail;

    private static final String sampleJAXBText = "<echoStringResponse xmlns=\"http://test\">" + "<echoStringReturn>sample return value</echoStringReturn>" + "</echoStringResponse>";

    private static final String sampleJAXBEnvelope11 = sampleEnvelopeHead11 + sampleJAXBText + sampleEnvelopeTail;

    private static final String sampleJAXBEnvelope12 = sampleEnvelopeHead12 + sampleJAXBText + sampleEnvelopeTail;

    private static final String sampleEnvelopeNoHeader11 = "<soapenv:Envelope xmlns:soapenv=\"" + soap11env + "\">" + "<soapenv:Body>" + sampleText + "</soapenv:Body></soapenv:Envelope>";

    private static final String sampleEnvelopeNoHeader12 = "<soapenv:Envelope xmlns:soapenv=\"" + soap12env + "\">" + "<soapenv:Body>" + sampleText + "</soapenv:Body></soapenv:Envelope>";

    private static final QName sampleQName = new QName("urn://sample", "a");

    private static XMLInputFactory inputFactory = XMLInputFactory.newInstance();

    private W3CEndpointReference w3cEPR;

    private SubmissionEndpointReference subEPR;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        W3CEndpointReferenceBuilder w3cBuilder = new W3CEndpointReferenceBuilder();
        w3cBuilder = w3cBuilder.address("http://somewhere.com/somehow");
        w3cBuilder = w3cBuilder.serviceName(new QName("http://test", "TestService"));
        w3cBuilder = w3cBuilder.endpointName(new QName("http://test", "TestPort"));
        w3cEPR = w3cBuilder.build();
        SubmissionEndpointReferenceBuilder subBuilder = new SubmissionEndpointReferenceBuilder();
        subBuilder = subBuilder.address("http://somewhere.com/somehow");
        subBuilder = subBuilder.serviceName(new QName("http://test", "TestService"));
        subBuilder = subBuilder.endpointName(new QName("http://test", "TestPort"));
        subEPR = subBuilder.build();
    }

    public MessageTests() {
        super();
    }

    public MessageTests(String arg0) {
        super(arg0);
    }

    /**
     * Create a Block representing an XMLString and simulate a normal Dispatch<String> flow. In
     * addition the test makes sure that the XMLString block is not expanded during this process.
     * (Expanding the block degrades performance).
     * 
     * @throws Exception
     */
    public void testStringOutflow() throws Exception {
        MessageFactory mf = (MessageFactory) FactoryRegistry.getFactory(MessageFactory.class);
        Message m = mf.create(Protocol.soap11);
        XMLStringBlockFactory f = (XMLStringBlockFactory) FactoryRegistry.getFactory(XMLStringBlockFactory.class);
        Block block = f.createFrom(sampleText, null, null);
        m.setBodyBlock(block);
        boolean isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "SPINE".equals(m.getXMLPartContentType()));
        org.apache.axiom.soap.SOAPEnvelope env = (org.apache.axiom.soap.SOAPEnvelope) m.getAsOMElement();
        isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "OM".equals(m.getXMLPartContentType()));
        OMElement o = env.getBody().getFirstElement();
        assertTrue(o instanceof OMSourcedElementImpl);
        assertTrue(((OMSourcedElementImpl) o).isExpanded() == false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        env.serializeAndConsume(baos, new OMOutputFormat());
        String newText = baos.toString();
        TestLogger.logger.debug(newText);
        assertTrue(newText.contains(sampleText));
        assertTrue(newText.contains("soap"));
        assertTrue(newText.contains("Envelope"));
        assertTrue(newText.contains("Body"));
    }

    /**
     * Create a Block representing an XMLString and simulate a normal Dispatch<String> flow with an
     * application handler.
     * 
     * @throws Exception
     */
    public void testStringOutflow2() throws Exception {
        MessageFactory mf = (MessageFactory) FactoryRegistry.getFactory(MessageFactory.class);
        Message m = mf.create(Protocol.soap11);
        XMLStringBlockFactory f = (XMLStringBlockFactory) FactoryRegistry.getFactory(XMLStringBlockFactory.class);
        Block block = f.createFrom(sampleText, null, null);
        m.setBodyBlock(block);
        SOAPEnvelope soapEnvelope = m.getAsSOAPEnvelope();
        boolean isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "SOAPENVELOPE".equals(m.getXMLPartContentType()));
        String name = soapEnvelope.getBody().getFirstChild().getLocalName();
        assertTrue("a".equals(name));
        assertTrue(block.isConsumed());
        OMElement om = m.getAsOMElement();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        om.serializeAndConsume(baos, new OMOutputFormat());
        String newText = baos.toString();
        TestLogger.logger.debug(newText);
        assertTrue(newText.contains(sampleText));
        assertTrue(newText.contains("soap"));
        assertTrue(newText.contains("Envelope"));
        assertTrue(newText.contains("Body"));
    }

    /**
     * Create a Block representing an empty XMLString and simulate a 
     * normal Dispatch<String> flow with an application handler.
     * @throws Exception
     * 
     * DISABLED THIS TEST. THE TEST IS NOT VALID BECAUSE AN ATTEMPT WAS 
     * MADE TO ADD A BLOCK THAT IS NOT AN ELEMENT.
     */
    public void _testStringOutflowEmptyString() throws Exception {
        MessageFactory mf = (MessageFactory) FactoryRegistry.getFactory(MessageFactory.class);
        Message m = mf.create(Protocol.soap11);
        XMLStringBlockFactory f = (XMLStringBlockFactory) FactoryRegistry.getFactory(XMLStringBlockFactory.class);
        String whiteSpaceText = "<!-- Comment -->";
        Block block = f.createFrom(whiteSpaceText, null, null);
        m.setBodyBlock(block);
        SOAPEnvelope soapEnvelope = m.getAsSOAPEnvelope();
        boolean isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "SOAPENVELOPE".equals(m.getXMLPartContentType()));
        assertTrue(soapEnvelope.getBody().getFirstChild() == null);
        assertTrue(block.isConsumed());
        OMElement om = m.getAsOMElement();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        om.serializeAndConsume(baos, new OMOutputFormat());
        String newText = baos.toString();
        TestLogger.logger.debug(newText);
        assertTrue(newText.contains("soap"));
        assertTrue(newText.contains("Envelope"));
        assertTrue(newText.contains("Body"));
    }

    /**
     * Create a Block representing an XMLString with 2 elements and simulate a 
     * normal Dispatch<String> flow with an application handler.
     * @throws Exception
     * 
     * @REVIEW This test is disabled because (a) it fails and (b) we don't believe this
     * is allowed due by WSI.
     */
    public void _testStringOutflowDoubleElement() throws Exception {
        MessageFactory mf = (MessageFactory) FactoryRegistry.getFactory(MessageFactory.class);
        Message m = mf.create(Protocol.soap11);
        XMLStringBlockFactory f = (XMLStringBlockFactory) FactoryRegistry.getFactory(XMLStringBlockFactory.class);
        Block block = f.createFrom(this.sampleDouble, null, null);
        m.setBodyBlock(block);
        SOAPEnvelope soapEnvelope = m.getAsSOAPEnvelope();
        boolean isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "SOAPENVELOPE".equals(m.getXMLPartContentType()));
        String name = soapEnvelope.getBody().getFirstChild().getLocalName();
        assertTrue("a".equals(name));
        name = soapEnvelope.getBody().getLastChild().getLocalName();
        assertTrue("aa".equals(name));
        assertTrue(block.isConsumed());
        OMElement om = m.getAsOMElement();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        om.serializeAndConsume(baos, new OMOutputFormat());
        String newText = baos.toString();
        TestLogger.logger.debug(newText);
        assertTrue(newText.contains(sampleText));
        assertTrue(newText.contains("soap"));
        assertTrue(newText.contains("Envelope"));
        assertTrue(newText.contains("Body"));
    }

    /**
     * Create a Block representing an XMLString and simulate a normal Dispatch<String> input flow
     * 
     * @throws Exception
     */
    public void testStringInflow_soap11() throws Exception {
        _testStringInflow(sampleEnvelope11);
    }

    public void testStringInflow_soap12() throws Exception {
        _testStringInflow(sampleEnvelope12);
    }

    public void _testStringInflow(String sampleEnvelope) throws Exception {
        StringReader sr = new StringReader(sampleEnvelope);
        XMLStreamReader inflow = inputFactory.createXMLStreamReader(sr);
        StAXSOAPModelBuilder builder = new StAXSOAPModelBuilder(inflow, null);
        OMElement omElement = builder.getSOAPEnvelope();
        MessageFactory mf = (MessageFactory) FactoryRegistry.getFactory(MessageFactory.class);
        Message m = mf.createFrom(omElement, null);
        boolean isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "OM".equals(m.getXMLPartContentType()));
        XMLStringBlockFactory blockFactory = (XMLStringBlockFactory) FactoryRegistry.getFactory(XMLStringBlockFactory.class);
        Block block = m.getBodyBlock(null, blockFactory);
        Object bo = block.getBusinessObject(true);
        assertTrue(bo instanceof String);
        assertTrue(block.isConsumed());
        assertTrue(sampleText.equals(bo.toString()));
    }

    /**
     * Create a Block representing an XMLString and simulate a normal Dispatch<String> input flow
     * with a JAX-WS Handler
     * 
     * @throws Exception
     */
    public void testStringInflow2_soap11() throws Exception {
        _testStringInflow2(sampleEnvelope11);
    }

    public void testStringInflow2_soap12() throws Exception {
        javax.xml.soap.MessageFactory mf = null;
        try {
            mf = getSAAJConverter().createMessageFactory(soap12env);
        } catch (Exception e) {
        }
        if (mf != null) {
            _testStringInflow2(sampleEnvelope12);
        }
    }

    public void _testStringInflow2(String sampleEnvelope) throws Exception {
        StringReader sr = new StringReader(sampleEnvelope);
        XMLStreamReader inflow = inputFactory.createXMLStreamReader(sr);
        StAXSOAPModelBuilder builder = new StAXSOAPModelBuilder(inflow, null);
        OMElement omElement = builder.getSOAPEnvelope();
        MessageFactory mf = (MessageFactory) FactoryRegistry.getFactory(MessageFactory.class);
        Message m = mf.createFrom(omElement, null);
        boolean isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "OM".equals(m.getXMLPartContentType()));
        SOAPEnvelope soapEnvelope = m.getAsSOAPEnvelope();
        isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "SOAPENVELOPE".equals(m.getXMLPartContentType()));
        String name = soapEnvelope.getBody().getFirstChild().getLocalName();
        assertTrue("a".equals(name));
        XMLStringBlockFactory blockFactory = (XMLStringBlockFactory) FactoryRegistry.getFactory(XMLStringBlockFactory.class);
        Block block = m.getBodyBlock(null, blockFactory);
        Object bo = block.getBusinessObject(true);
        assertTrue(bo instanceof String);
        assertTrue(block.isConsumed());
        assertTrue(sampleText.equals(bo.toString()));
    }

    /**
     * Create a Block representing an XMLString and simulate a normal Dispatch<String> input flow
     * with a JAX-WS Handler that needs the whole Message
     * 
     * @throws Exception
     */
    public void testStringInflow3_soap11() throws Exception {
        _testStringInflow3(sampleEnvelope11);
    }

    public void testStringInflow3_soap12() throws Exception {
        javax.xml.soap.MessageFactory mf = null;
        try {
            mf = getSAAJConverter().createMessageFactory(soap12env);
        } catch (Exception e) {
        }
        if (mf != null) {
            _testStringInflow3(sampleEnvelope12);
        }
    }

    public void _testStringInflow3(String sampleEnvelope) throws Exception {
        StringReader sr = new StringReader(sampleEnvelope);
        XMLStreamReader inflow = inputFactory.createXMLStreamReader(sr);
        StAXSOAPModelBuilder builder = new StAXSOAPModelBuilder(inflow, null);
        OMElement omElement = builder.getSOAPEnvelope();
        MessageFactory mf = (MessageFactory) FactoryRegistry.getFactory(MessageFactory.class);
        Message m = mf.createFrom(omElement, null);
        boolean isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "OM".equals(m.getXMLPartContentType()));
        SOAPMessage sm = m.getAsSOAPMessage();
        isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "SOAPENVELOPE".equals(m.getXMLPartContentType()));
        String name = sm.getSOAPBody().getFirstChild().getLocalName();
        assertTrue("a".equals(name));
        XMLStringBlockFactory blockFactory = (XMLStringBlockFactory) FactoryRegistry.getFactory(XMLStringBlockFactory.class);
        Block block = m.getBodyBlock(null, blockFactory);
        Object bo = block.getBusinessObject(true);
        assertTrue(bo instanceof String);
        assertTrue(block.isConsumed());
        assertTrue(sampleText.equals(bo.toString()));
    }

    /**
     * Create a Block representing an XMLString, but this time use one that
     * doesn't have a &lt;soap:Header&gt; element in it.
     * @throws Exception
     */
    public void testStringInflow4_soap11() throws Exception {
        _testStringInflow4(sampleEnvelopeNoHeader11);
    }

    public void testStringInflow4_soap12() throws Exception {
        _testStringInflow4(sampleEnvelopeNoHeader12);
    }

    public void _testStringInflow4(String sampleEnvelopeNoHeader) throws Exception {
        StringReader sr = new StringReader(sampleEnvelopeNoHeader);
        XMLStreamReader inflow = inputFactory.createXMLStreamReader(sr);
        StAXSOAPModelBuilder builder = new StAXSOAPModelBuilder(inflow, null);
        OMElement omElement = builder.getSOAPEnvelope();
        MessageFactory mf = (MessageFactory) FactoryRegistry.getFactory(MessageFactory.class);
        Message m = mf.createFrom(omElement, null);
        boolean isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "OM".equals(m.getXMLPartContentType()));
        XMLStringBlockFactory blockFactory = (XMLStringBlockFactory) FactoryRegistry.getFactory(XMLStringBlockFactory.class);
        Block block = m.getBodyBlock(null, blockFactory);
        Object bo = block.getBusinessObject(true);
        assertTrue(bo instanceof String);
        assertTrue(block.isConsumed());
        assertTrue(sampleText.equals(bo.toString()));
    }

    /**
     * Create a JAXBBlock containing a JAX-B business object 
     * and simulate a normal Dispatch<Object> output flow
     * @throws Exception
     */
    public void testJAXBOutflow() throws Exception {
        MessageFactory mf = (MessageFactory) FactoryRegistry.getFactory(MessageFactory.class);
        Message m = mf.create(Protocol.soap11);
        JAXBBlockFactory bf = (JAXBBlockFactory) FactoryRegistry.getFactory(JAXBBlockFactory.class);
        ObjectFactory of = new ObjectFactory();
        EchoStringResponse obj = of.createEchoStringResponse();
        obj.setEchoStringReturn("sample return value");
        JAXBBlockContext context = new JAXBBlockContext(EchoStringResponse.class.getPackage().getName());
        Block block = bf.createFrom(obj, context, null);
        m.setBodyBlock(block);
        boolean isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "SPINE".equals(m.getXMLPartContentType()));
        org.apache.axiom.soap.SOAPEnvelope env = (org.apache.axiom.soap.SOAPEnvelope) m.getAsOMElement();
        isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "OM".equals(m.getXMLPartContentType()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        env.serializeAndConsume(baos, new OMOutputFormat());
        String newText = baos.toString();
        TestLogger.logger.debug(newText);
        assertTrue(newText.contains(sampleJAXBText));
        assertTrue(newText.contains("soap"));
        assertTrue(newText.contains("Envelope"));
        assertTrue(newText.contains("Body"));
    }

    /**
     * Create a JAXBBlock containing a JAX-B business object 
     * and simulate a normal Dispatch<Object> output flow
     * @throws Exception
     */
    public void testJAXBOutflow_W3CEndpointReference() throws Exception {
        MessageFactory mf = (MessageFactory) FactoryRegistry.getFactory(MessageFactory.class);
        Message m = mf.create(Protocol.soap11);
        JAXBBlockFactory bf = (JAXBBlockFactory) FactoryRegistry.getFactory(JAXBBlockFactory.class);
        W3CEndpointReference obj = w3cEPR;
        Class[] classes = new Class[] { W3CEndpointReference.class };
        JAXBBlockContext context = new JAXBBlockContext("javax.xml.ws.wsaddressing");
        System.out.println("JAXBContext= " + context);
        Block block = bf.createFrom(obj, context, null);
        m.setBodyBlock(block);
        boolean isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "SPINE".equals(m.getXMLPartContentType()));
        org.apache.axiom.soap.SOAPEnvelope env = (org.apache.axiom.soap.SOAPEnvelope) m.getAsOMElement();
        isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "OM".equals(m.getXMLPartContentType()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        env.serializeAndConsume(baos, new OMOutputFormat());
        String newText = baos.toString();
        System.out.println(newText);
        assertTrue(newText.contains("http://somewhere.com/somehow"));
        assertTrue(newText.contains("soap"));
        assertTrue(newText.contains("Envelope"));
        assertTrue(newText.contains("Body"));
    }

    /**
     * Same as JAXBOutputflow, but has an additional check
     * to make sure that the JAXB serialization is deferrred
     * until the actual serialization of the message.
     * @throws Exception
     */
    public void testJAXBOutflowPerf() throws Exception {
        MessageFactory mf = (MessageFactory) FactoryRegistry.getFactory(MessageFactory.class);
        Message m = mf.create(Protocol.soap11);
        JAXBBlockFactory bf = (JAXBBlockFactory) FactoryRegistry.getFactory(JAXBBlockFactory.class);
        ObjectFactory of = new ObjectFactory();
        EchoStringResponse obj = of.createEchoStringResponse();
        obj.setEchoStringReturn("sample return value");
        JAXBBlockContext context = new JAXBBlockContext(EchoStringResponse.class.getPackage().getName());
        Block block = bf.createFrom(obj, context, null);
        m.setBodyBlock(block);
        boolean isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "SPINE".equals(m.getXMLPartContentType()));
        org.apache.axiom.soap.SOAPEnvelope env = (org.apache.axiom.soap.SOAPEnvelope) m.getAsOMElement();
        isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "OM".equals(m.getXMLPartContentType()));
        OMElement o = env.getBody().getFirstElement();
        assertTrue(o instanceof OMSourcedElementImpl);
        assertTrue(((OMSourcedElementImpl) o).isExpanded() == false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        env.serializeAndConsume(baos, new OMOutputFormat());
        String newText = baos.toString();
        TestLogger.logger.debug(newText);
        assertTrue(newText.contains(sampleJAXBText));
        assertTrue(newText.contains("soap"));
        assertTrue(newText.contains("Envelope"));
        assertTrue(newText.contains("Body"));
    }

    private final int NO_PERSIST = 0;

    private final int PERSIST = 1;

    private final int SAVE_AND_PERSIST = 2;

    public void testJAXBInflow_soap11() throws Exception {
        _testJAXBInflow(sampleJAXBEnvelope11, NO_PERSIST, false);
    }

    public void testJAXBInflow_soap11_withCustomBuilder() throws Exception {
        _testJAXBInflow(sampleJAXBEnvelope11, NO_PERSIST, true);
    }

    public void testJAXBInflow_soap12() throws Exception {
        _testJAXBInflow(sampleJAXBEnvelope12, NO_PERSIST, false);
    }

    public void testJAXBInflow_soap11_withPersist() throws Exception {
        _testJAXBInflow(sampleJAXBEnvelope11, PERSIST, false);
    }

    public void testJAXBInflow_soap12_withPersist() throws Exception {
        _testJAXBInflow(sampleJAXBEnvelope12, PERSIST, false);
    }

    public void testJAXBInflow_soap11_withSaveAndPersist() throws Exception {
        _testJAXBInflow(sampleJAXBEnvelope11, SAVE_AND_PERSIST, false);
    }

    public void testJAXBInflow_soap12_withSaveAndPersist() throws Exception {
        _testJAXBInflow(sampleJAXBEnvelope12, SAVE_AND_PERSIST, false);
    }

    public void _testJAXBInflow(String sampleJAXBEnvelope, int persist, boolean installJAXBCustomBuilder) throws Exception {
        StringReader sr = new StringReader(sampleJAXBEnvelope);
        XMLStreamReader inflow = inputFactory.createXMLStreamReader(sr);
        StAXSOAPModelBuilder builder = new StAXSOAPModelBuilder(inflow, null);
        OMElement omElement = builder.getSOAPEnvelope();
        JAXBDSContext jds = null;
        if (installJAXBCustomBuilder) {
            jds = new JAXBDSContext(EchoStringResponse.class.getPackage().getName());
            JAXBCustomBuilder jcb = new JAXBCustomBuilder(jds);
            builder.registerCustomBuilderForPayload(jcb);
        }
        MessageFactory mf = (MessageFactory) FactoryRegistry.getFactory(MessageFactory.class);
        Message m = mf.createFrom(omElement, null);
        boolean isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "OM".equals(m.getXMLPartContentType()));
        if (installJAXBCustomBuilder) {
            JAXBContext context = jds.getJAXBContext();
            int contextPointer = System.identityHashCode(context);
            context = null;
            System.gc();
            TreeSet<String> packages = new TreeSet<String>();
            packages.add(EchoStringResponse.class.getPackage().getName());
            context = JAXBUtils.getJAXBContext(packages);
            int contextPointer2 = System.identityHashCode(context);
            context = null;
            context = JAXBUtils.getJAXBContext(packages);
            int contextPointer3 = System.identityHashCode(context);
            assertTrue(contextPointer3 == contextPointer2);
        }
        String saveMsgText = "";
        if (persist == SAVE_AND_PERSIST) {
            saveMsgText = m.getAsOMElement().toString();
        }
        Object customBuiltObject = null;
        if (installJAXBCustomBuilder) {
            OMElement om = m.getAsOMElement();
            om = ((org.apache.axiom.soap.SOAPEnvelope) om).getBody().getFirstElement();
            if (om instanceof OMSourcedElement && ((OMSourcedElement) om).getDataSource() instanceof JAXBDataSource) {
                customBuiltObject = ((JAXBDataSource) ((OMSourcedElement) om).getDataSource()).getObject();
            }
        }
        JAXBBlockFactory bf = (JAXBBlockFactory) FactoryRegistry.getFactory(JAXBBlockFactory.class);
        JAXBBlockContext context = new JAXBBlockContext(EchoStringResponse.class.getPackage().getName());
        Block b = m.getBodyBlock(context, bf);
        isFault = m.isFault();
        assertTrue(!isFault);
        assertTrue("XMLPart Representation is " + m.getXMLPartContentType(), "SPINE".equals(m.getXMLPartContentType()));
        Object bo = b.getBusinessObject(false);
        m.setPostPivot();
        if (persist == SAVE_AND_PERSIST) {
            sr = new StringReader(saveMsgText);
            XMLStreamReader saveMsgReader = inputFactory.createXMLStreamReader(sr);
            builder = new StAXSOAPModelBuilder(saveMsgReader, null);
            omElement = builder.getSOAPEnvelope();
            m = mf.createFrom(omElement, null);
        }
        if (installJAXBCustomBuilder) {
            assertTrue(customBuiltObject == bo);
        }
        assertNotNull(bo);
        assertTrue(bo instanceof EchoStringResponse);
        EchoStringResponse esr = (EchoStringResponse) bo;
        assertNotNull(esr.getEchoStringReturn());
        assertTrue(esr.getEchoStringReturn().equals("sample return value"));
        if (persist == PERSIST) {
            String persistMsg = m.getAsOMElement().toString();
            assertTrue(persistMsg.contains("Body"));
            assertTrue(persistMsg.contains("echoStringResponse"));
            assertTrue(persistMsg.contains("sample return value"));
        } else if (persist == SAVE_AND_PERSIST) {
            String persistMsg = m.getAsOMElement().toString();
            assertTrue(persistMsg.contains("Body"));
            assertTrue(persistMsg.contains("echoStringResponse"));
            assertTrue(persistMsg.contains("sample return value"));
        }
        org.apache.axiom.soap.SOAPEnvelope env = (org.apache.axiom.soap.SOAPEnvelope) m.getAsOMElement();
        QName qName = new QName("uri://fake", "fake");
        env.getBody().getFirstChildWithName(qName);
    }

    SAAJConverter converter = null;

    private SAAJConverter getSAAJConverter() {
        if (converter == null) {
            SAAJConverterFactory factory = (SAAJConverterFactory) FactoryRegistry.getFactory(SAAJConverterFactory.class);
            converter = factory.getSAAJConverter();
        }
        return converter;
    }
}
