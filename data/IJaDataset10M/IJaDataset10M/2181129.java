package net.javacrumbs.springws.test.simple;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import net.javacrumbs.springws.test.AbstractMessageTest;
import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.context.WsTestContextHolder;
import net.javacrumbs.springws.test.generator.DefaultResponseGenerator;
import net.javacrumbs.springws.test.lookup.SimpleResourceLookup;
import net.javacrumbs.springws.test.validator.SchemaRequestValidator;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.validation.XmlValidator;
import org.xml.sax.SAXException;

public class WsMockControlTest extends AbstractMessageTest {

    private static final String URI = "http://example.org";

    private static final Map<String, String> NS_MAP = Collections.singletonMap("ns", "http://www.example.org/schema");

    @Test
    public void testExpectAndReturn() throws IOException {
        MockWebServiceMessageSender sender = (MockWebServiceMessageSender) new WsMockControl().expectRequest("xml/control-message-test.xml").returnResponse("mock-responses/test/default-response.xml").createMock();
        assertNotNull(sender);
        assertEquals(2, sender.getRequestProcessors().size());
        SimpleResourceLookup lookup1 = (SimpleResourceLookup) ((XmlCompareRequestValidator) extractRequestProcessor(sender, 0)).getControlResourceLookup();
        assertEquals("control-message-test.xml", lookup1.getResultResource().getFilename());
        assertTrue(((XmlCompareRequestValidator) extractRequestProcessor(sender, 0)).isIgnoreWhitespace());
        SimpleResourceLookup lookup2 = (SimpleResourceLookup) ((DefaultResponseGenerator) extractRequestProcessor(sender, 1)).getResourceLookup();
        assertEquals("default-response.xml", lookup2.getResultResource().getFilename());
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMessageSender(sender);
        StringResult responseResult = new StringResult();
        template.sendSourceAndReceiveToResult(URI, createMessage("xml/valid-message.xml").getPayloadSource(), responseResult);
    }

    @Test
    public void testExpectAndReturnXsltTemplate() throws IOException, SAXException {
        MockWebServiceMessageSender sender = (MockWebServiceMessageSender) new WsMockControl().useXsltTemplateProcessor().expectRequest("xml/control-message-test.xml").returnResponse("mock-responses/test/different-response.xml").createMock();
        doTemplateTest(sender);
    }

    @Test
    public void testExpectAndReturnFreemarkerTemplate() throws IOException, SAXException {
        MockWebServiceMessageSender sender = (MockWebServiceMessageSender) new WsMockControl().useFreeMarkerTemplateProcessor().expectRequest("xml/control-message-test.xml").returnResponse("mock-responses/test/freemarker-response.xml").createMock();
        doTemplateTest(sender);
    }

    private void doTemplateTest(MockWebServiceMessageSender sender) throws IOException, SAXException {
        assertNotNull(sender);
        assertEquals(2, sender.getRequestProcessors().size());
        WsTestContextHolder.getTestContext().setAttribute("number", 2);
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMessageSender(sender);
        StringResult responseResult = new StringResult();
        template.sendSourceAndReceiveToResult(URI, createMessage("xml/valid-message2.xml").getPayloadSource(), responseResult);
        Diff diff = XMLUnit.compareXML(new InputStreamReader(new ClassPathResource("xml/resolved-different-response.xml").getInputStream()), responseResult.toString());
        assertTrue(diff.toString(), diff.similar());
    }

    @Test
    public void testWrapSimple() {
        RequestProcessor processor = createMock(RequestProcessor.class);
        replay(processor);
        WsMockControl control = new WsMockControl();
        control.addRequestProcessor(processor);
        LimitingRequestProcessorWrapper processorWrapper = (LimitingRequestProcessorWrapper) control.getRequestProcessors().get(0);
        assertEquals(processor.toString(), processorWrapper.getRequestProcessorDescription());
        assertEquals(1, processorWrapper.getMinNumberOfProcessedRequests());
        assertEquals(1, processorWrapper.getMaxNumberOfProcessedRequests());
        verify(processor);
    }

    @Test(expected = IllegalStateException.class)
    public void testTimesNoProcessor() {
        new WsMockControl().times(0, 1);
    }

    @Test
    public void testWrapTimes() {
        WsMockControl control = new WsMockControl();
        control.expectRequest("xml/does-not-exist.xml").times(0, 5);
        LimitingRequestProcessorWrapper processorWrapper = (LimitingRequestProcessorWrapper) control.getRequestProcessors().get(0);
        assertEquals(0, processorWrapper.getMinNumberOfProcessedRequests());
        assertEquals(5, processorWrapper.getMaxNumberOfProcessedRequests());
        assertEquals("expectRequest(class path resource [xml/does-not-exist.xml])", processorWrapper.getRequestProcessorDescription());
    }

    @Test
    public void testWrapAtLeastOnce() {
        WsMockControl control = new WsMockControl();
        control.failIf("//ns:number!=1", NS_MAP).atLeastOnce();
        LimitingRequestProcessorWrapper processorWrapper = (LimitingRequestProcessorWrapper) control.getRequestProcessors().get(0);
        assertEquals(1, processorWrapper.getMinNumberOfProcessedRequests());
        assertEquals(Integer.MAX_VALUE, processorWrapper.getMaxNumberOfProcessedRequests());
        assertEquals("failIf(\"//ns:number!=1\")", processorWrapper.getRequestProcessorDescription());
    }

    @Test
    public void testWrapAnyTimes() {
        WsMockControl control = new WsMockControl();
        control.assertThat("//ns:number=1", NS_MAP).anyTimes();
        LimitingRequestProcessorWrapper processorWrapper = (LimitingRequestProcessorWrapper) control.getRequestProcessors().get(0);
        assertEquals(0, processorWrapper.getMinNumberOfProcessedRequests());
        assertEquals(Integer.MAX_VALUE, processorWrapper.getMaxNumberOfProcessedRequests());
        assertEquals("assertThat(\"//ns:number=1\")", processorWrapper.getRequestProcessorDescription());
    }

    @Test
    public void testWrapTimes1() {
        WsMockControl control = new WsMockControl();
        control.returnResponse("mock-responses/test/default-response.xml").times(5);
        LimitingRequestProcessorWrapper processorWrapper = (LimitingRequestProcessorWrapper) control.getRequestProcessors().get(0);
        assertEquals(5, processorWrapper.getMinNumberOfProcessedRequests());
        assertEquals(5, processorWrapper.getMaxNumberOfProcessedRequests());
        assertEquals("returnResponse(class path resource [mock-responses/test/default-response.xml])", processorWrapper.getRequestProcessorDescription());
    }

    @Test
    public void testWrapOnce() {
        WsMockControl control = new WsMockControl();
        control.throwException(new WsTestException("Test error")).once();
        LimitingRequestProcessorWrapper processorWrapper = (LimitingRequestProcessorWrapper) control.getRequestProcessors().get(0);
        assertEquals(1, processorWrapper.getMinNumberOfProcessedRequests());
        assertEquals(1, processorWrapper.getMaxNumberOfProcessedRequests());
        assertEquals("throwException(\"Test error\")", processorWrapper.getRequestProcessorDescription());
    }

    private RequestProcessor extractRequestProcessor(MockWebServiceMessageSender sender, int index) {
        return ((LimitingRequestProcessorWrapper) sender.getRequestProcessors().get(index)).getWrappedRequestProcessor();
    }

    @Test(expected = WebServiceIOException.class)
    public void testExpectResourceNotFound() throws IOException {
        MockWebServiceMessageSender sender = (MockWebServiceMessageSender) new WsMockControl().expectRequest("xml/does-not-exist.xml").returnResponse("mock-responses/test/default-response.xml").createMock();
        assertNotNull(sender);
        assertEquals(2, sender.getRequestProcessors().size());
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMessageSender(sender);
        StringResult responseResult = new StringResult();
        template.sendSourceAndReceiveToResult(URI, createMessage("xml/valid-message.xml").getPayloadSource(), responseResult);
    }

    @Test(expected = WsTestException.class)
    public void testVerifyUri() throws IOException, URISyntaxException {
        MockWebServiceMessageSender sender = (MockWebServiceMessageSender) new WsMockControl().expectUri(new URI("http://example.com")).returnResponse("mock-responses/test/default-response.xml").createMock();
        assertNotNull(sender);
        assertEquals(2, sender.getRequestProcessors().size());
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMessageSender(sender);
        StringResult responseResult = new StringResult();
        template.sendSourceAndReceiveToResult(URI, createMessage("xml/valid-message.xml").getPayloadSource(), responseResult);
    }

    @Test(expected = WsTestException.class)
    public void testXPathValidation() throws IOException {
        Map<String, String> nsMap = NS_MAP;
        MockWebServiceMessageSender sender = (MockWebServiceMessageSender) new WsMockControl().expectRequest("xml/control-message-test.xml").failIf("//ns:number!=1", nsMap).returnResponse("mock-responses/test/default-response.xml").createMock();
        assertNotNull(sender);
        assertEquals(3, sender.getRequestProcessors().size());
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMessageSender(sender);
        StringResult responseResult = new StringResult();
        template.sendSourceAndReceiveToResult(URI, createMessage("xml/valid-message.xml").getPayloadSource(), responseResult);
    }

    @Test(expected = WsTestException.class)
    public void testXPathAssertion() throws IOException {
        MockWebServiceMessageSender sender = (MockWebServiceMessageSender) new WsMockControl().expectRequest("xml/control-message-test.xml").assertThat("//ns:number=1", NS_MAP).returnResponse("mock-responses/test/default-response.xml").createMock();
        assertNotNull(sender);
        assertEquals(3, sender.getRequestProcessors().size());
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMessageSender(sender);
        StringResult responseResult = new StringResult();
        template.sendSourceAndReceiveToResult(URI, createMessage("xml/valid-message.xml").getPayloadSource(), responseResult);
    }

    @Test(expected = WsTestException.class)
    public void testExpectAndThrow() throws IOException {
        MockWebServiceMessageSender sender = (MockWebServiceMessageSender) new WsMockControl().expectRequest("xml/control-message-test.xml").throwException(new WsTestException("Test error")).createMock();
        assertNotNull(sender);
        assertEquals(2, sender.getRequestProcessors().size());
        SimpleResourceLookup lookup1 = (SimpleResourceLookup) ((XmlCompareRequestValidator) extractRequestProcessor(sender, 0)).getControlResourceLookup();
        assertEquals("control-message-test.xml", lookup1.getResultResource().getFilename());
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMessageSender(sender);
        StringResult responseResult = new StringResult();
        template.sendSourceAndReceiveToResult(URI, createMessage("xml/valid-message.xml").getPayloadSource(), responseResult);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateEmpty() {
        new WsMockControl().createMock();
    }

    @Test(expected = WsTestException.class)
    public void testVerifyNoCall() {
        WsMockControl control = new WsMockControl().throwException(new WsTestException("Test error"));
        control.verify();
    }

    @Test
    public void testValidateSchema() {
        String schema = "xml/schema.xsd";
        WsMockControl control = new WsMockControl().validateSchema(schema);
        MockWebServiceMessageSender sender = (MockWebServiceMessageSender) control.createMock();
        assertEquals(1, sender.getRequestProcessors().size());
        SchemaRequestValidator validator = (SchemaRequestValidator) extractRequestProcessor(sender, 0);
        assertArrayEquals(new Resource[] { new ClassPathResource(schema) }, validator.getSchemas());
        assertNotNull(validator.getValidator());
    }

    @Test
    public void testValidateGeneric() throws IOException {
        XmlValidator xmlValidator = createMock(XmlValidator.class);
        replay(xmlValidator);
        WsMockControl control = new WsMockControl().validate(xmlValidator);
        MockWebServiceMessageSender sender = (MockWebServiceMessageSender) control.createMock();
        assertEquals(1, sender.getRequestProcessors().size());
        SchemaRequestValidator validator = (SchemaRequestValidator) extractRequestProcessor(sender, 0);
        assertSame(xmlValidator, validator.getValidator());
        verify(xmlValidator);
    }

    @Test
    public void testIgnoreWhitespace() {
        WsMockControl control = new WsMockControl().ignoreWhitespace(false).expectRequest("xml/control-message-test.xml");
        MockWebServiceMessageSender sender = (MockWebServiceMessageSender) control.createMock();
        assertFalse(((XmlCompareRequestValidator) extractRequestProcessor(sender, 0)).isIgnoreWhitespace());
    }

    @Test
    public void testAddInterceptor() {
        WsMockControl control = new WsMockControl().addInterceptor(new PayloadLoggingInterceptor()).throwException(new RuntimeException());
        MockWebServiceMessageSender sender = (MockWebServiceMessageSender) control.createMock();
        assertEquals(1, sender.getInterceptors().size());
        assertEquals(PayloadLoggingInterceptor.class, sender.getInterceptors().get(0).getClass());
    }

    @Test
    public void testSetAttributeInContext() {
        String value = "value" + System.currentTimeMillis();
        String name = "Test attribute";
        new WsMockControl().setTestContextAttribute(name, value).throwException(new RuntimeException());
        assertEquals(value, WsTestContextHolder.getTestContext().getAttribute(name));
    }
}
