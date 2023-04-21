package org.apache.axis2.jaxws.description;

import junit.framework.TestCase;
import org.apache.axis2.jaxws.description.builder.MDQConstants;
import org.apache.log4j.BasicConfigurator;
import javax.jws.WebService;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceProvider;

public class AnnotationProviderImplDescriptionTests extends TestCase {

    static {
        BasicConfigurator.configure();
    }

    public void testBasicProvider() {
        ServiceDescription serviceDesc = DescriptionFactory.createServiceDescription(BasicProviderTestImpl.class);
        assertNotNull(serviceDesc);
        EndpointDescription[] endpointDesc = serviceDesc.getEndpointDescriptions();
        assertNotNull(endpointDesc);
        assertEquals(1, endpointDesc.length);
        EndpointDescriptionJava testEndpointDesc = (EndpointDescriptionJava) endpointDesc[0];
        assertNotNull(testEndpointDesc);
        assertEquals(Service.Mode.MESSAGE, testEndpointDesc.getAnnoServiceModeValue());
        assertEquals("http://www.w3.org/2003/05/soap/bindings/HTTP/", testEndpointDesc.getAnnoBindingTypeValue());
        assertEquals("", testEndpointDesc.getAnnoWebServiceWSDLLocation());
        assertEquals("BasicProviderTestImplService", testEndpointDesc.getAnnoWebServiceServiceName());
        assertEquals("BasicProviderTestImplPort", testEndpointDesc.getAnnoWebServicePortName());
        assertEquals("http://description.jaxws.axis2.apache.org/", testEndpointDesc.getAnnoWebServiceTargetNamespace());
    }

    public void testBasicProviderWithJMS() {
        ServiceDescription serviceDesc = DescriptionFactory.createServiceDescription(BasicProviderJMSTestImpl.class);
        assertNotNull(serviceDesc);
        EndpointDescription[] endpointDesc = serviceDesc.getEndpointDescriptions();
        assertNotNull(endpointDesc);
        assertEquals(1, endpointDesc.length);
        EndpointDescriptionJava testEndpointDesc = (EndpointDescriptionJava) endpointDesc[0];
        assertNotNull(testEndpointDesc);
        assertEquals(Service.Mode.MESSAGE, testEndpointDesc.getAnnoServiceModeValue());
        assertEquals(MDQConstants.SOAP12JMS_BINDING, testEndpointDesc.getAnnoBindingTypeValue());
        assertEquals("", testEndpointDesc.getAnnoWebServiceWSDLLocation());
        assertEquals("BasicProviderJMSTestImplService", testEndpointDesc.getAnnoWebServiceServiceName());
        assertEquals("BasicProviderJMSTestImplPort", testEndpointDesc.getAnnoWebServicePortName());
        assertEquals("http://description.jaxws.axis2.apache.org/", testEndpointDesc.getAnnoWebServiceTargetNamespace());
    }

    public void testWebServiceProvider() {
        ServiceDescription serviceDesc = DescriptionFactory.createServiceDescription(WebServiceProviderTestImpl.class);
        assertNotNull(serviceDesc);
        EndpointDescription[] endpointDesc = serviceDesc.getEndpointDescriptions();
        assertNotNull(endpointDesc);
        assertEquals(1, endpointDesc.length);
        EndpointDescriptionJava testEndpointDesc = (EndpointDescriptionJava) endpointDesc[0];
        assertNotNull(testEndpointDesc);
        assertEquals(Service.Mode.PAYLOAD, testEndpointDesc.getAnnoServiceModeValue());
        assertEquals("http://www.w3.org/2003/05/soap/bindings/HTTP/", testEndpointDesc.getAnnoBindingTypeValue());
        assertEquals("http://wsdl.test", testEndpointDesc.getAnnoWebServiceWSDLLocation());
        assertEquals("ProviderService", testEndpointDesc.getAnnoWebServiceServiceName());
        assertEquals("ProviderServicePort", testEndpointDesc.getAnnoWebServicePortName());
        assertEquals("http://namespace.test", testEndpointDesc.getAnnoWebServiceTargetNamespace());
    }

    public void testWebServiceProviderWithJMS() {
        ServiceDescription serviceDesc = DescriptionFactory.createServiceDescription(WebServiceProviderJMSTestImpl.class);
        assertNotNull(serviceDesc);
        EndpointDescription[] endpointDesc = serviceDesc.getEndpointDescriptions();
        assertNotNull(endpointDesc);
        assertEquals(1, endpointDesc.length);
        EndpointDescriptionJava testEndpointDesc = (EndpointDescriptionJava) endpointDesc[0];
        assertNotNull(testEndpointDesc);
        assertEquals(Service.Mode.PAYLOAD, testEndpointDesc.getAnnoServiceModeValue());
        assertEquals(MDQConstants.SOAP12JMS_BINDING, testEndpointDesc.getAnnoBindingTypeValue());
        assertEquals("http://wsdl.test", testEndpointDesc.getAnnoWebServiceWSDLLocation());
        assertEquals("ProviderService", testEndpointDesc.getAnnoWebServiceServiceName());
        assertEquals("ProviderServicePort", testEndpointDesc.getAnnoWebServicePortName());
        assertEquals("http://namespace.test", testEndpointDesc.getAnnoWebServiceTargetNamespace());
    }

    public void testDefaultServiceModeProvider() {
        ServiceDescription serviceDesc = DescriptionFactory.createServiceDescription(DefaultServiceModeProviderTestImpl.class);
        assertNotNull(serviceDesc);
        EndpointDescription[] endpointDesc = serviceDesc.getEndpointDescriptions();
        assertNotNull(endpointDesc);
        assertEquals(1, endpointDesc.length);
        EndpointDescriptionJava testEndpointDesc = (EndpointDescriptionJava) endpointDesc[0];
        assertEquals(Service.Mode.PAYLOAD, testEndpointDesc.getAnnoServiceModeValue());
        assertEquals("http://schemas.xmlsoap.org/wsdl/soap/http", testEndpointDesc.getAnnoBindingTypeValue());
    }

    public void testNoServiceModeProvider() {
        ServiceDescription serviceDesc = DescriptionFactory.createServiceDescription(NoServiceModeProviderTestImpl.class);
        assertNotNull(serviceDesc);
        EndpointDescription[] endpointDesc = serviceDesc.getEndpointDescriptions();
        assertNotNull(endpointDesc);
        assertEquals(1, endpointDesc.length);
        EndpointDescriptionJava testEndpointDesc = (EndpointDescriptionJava) endpointDesc[0];
        assertEquals(javax.xml.ws.Service.Mode.PAYLOAD, testEndpointDesc.getAnnoServiceModeValue());
        assertEquals(javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING, testEndpointDesc.getAnnoBindingTypeValue());
    }

    public void testNoWebServiceProvider() {
        try {
            DescriptionFactory.createServiceDescription(NoWebServiceProviderTestImpl.class);
            fail("Expected WebServiceException not caught");
        } catch (WebServiceException e) {
        } catch (Exception e) {
            e.printStackTrace();
            fail("Wrong exception caught.  Expected WebServiceException but caught " + e);
        }
    }

    public void testBothWebServiceAnnotations() {
        try {
            DescriptionFactory.createServiceDescription(BothWebServiceAnnotationTestImpl.class);
            fail("Expected WebServiceException not caught");
        } catch (WebServiceException e) {
        } catch (Exception e) {
            fail("Wrong exception caught.  Expected WebServiceException but caught " + e);
        }
    }

    public void testServiceModeOnNonProvider() {
        ServiceDescription serviceDesc = DescriptionFactory.createServiceDescription(WebServiceSEITestImpl.class);
        assertNotNull(serviceDesc);
        EndpointDescription[] endpointDesc = serviceDesc.getEndpointDescriptions();
        assertNotNull(endpointDesc);
        assertEquals(1, endpointDesc.length);
        EndpointDescriptionJava testEndpointDesc = (EndpointDescriptionJava) endpointDesc[0];
        assertNull(testEndpointDesc.getAnnoServiceModeValue());
        assertEquals(javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING, testEndpointDesc.getAnnoBindingTypeValue());
    }
}

@ServiceMode(value = Service.Mode.MESSAGE)
@WebServiceProvider()
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
class BasicProviderTestImpl implements Provider<SOAPMessage> {

    public BasicProviderTestImpl() {
    }

    public SOAPMessage invoke(SOAPMessage obj) {
        return null;
    }
}

@ServiceMode(value = Service.Mode.MESSAGE)
@WebServiceProvider()
@BindingType(MDQConstants.SOAP12JMS_BINDING)
class BasicProviderJMSTestImpl implements Provider<SOAPMessage> {

    public BasicProviderJMSTestImpl() {
    }

    public SOAPMessage invoke(SOAPMessage obj) {
        return null;
    }
}

@ServiceMode(value = Service.Mode.PAYLOAD)
@WebServiceProvider(serviceName = "ProviderService", portName = "ProviderServicePort", targetNamespace = "http://namespace.test", wsdlLocation = "http://wsdl.test")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
class WebServiceProviderTestImpl implements Provider<String> {

    public WebServiceProviderTestImpl() {
    }

    public String invoke(String obj) {
        return null;
    }
}

@ServiceMode(value = Service.Mode.PAYLOAD)
@WebServiceProvider(serviceName = "ProviderService", portName = "ProviderServicePort", targetNamespace = "http://namespace.test", wsdlLocation = "http://wsdl.test")
@BindingType(MDQConstants.SOAP12JMS_BINDING)
class WebServiceProviderJMSTestImpl implements Provider<String> {

    public WebServiceProviderJMSTestImpl() {
    }

    public String invoke(String obj) {
        return null;
    }
}

@ServiceMode()
@WebServiceProvider()
@BindingType()
class DefaultServiceModeProviderTestImpl implements Provider<String> {

    public DefaultServiceModeProviderTestImpl() {
    }

    public String invoke(String obj) {
        return null;
    }
}

@WebServiceProvider()
class NoServiceModeProviderTestImpl implements Provider<Source> {

    public NoServiceModeProviderTestImpl() {
    }

    public Source invoke(Source obj) {
        return null;
    }
}

@ServiceMode(value = Service.Mode.MESSAGE)
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
class NoWebServiceProviderTestImpl implements Provider<SOAPMessage> {

    public NoWebServiceProviderTestImpl() {
    }

    public SOAPMessage invoke(SOAPMessage obj) {
        return null;
    }
}

@ServiceMode(value = Service.Mode.MESSAGE)
@WebService()
@WebServiceProvider()
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
class BothWebServiceAnnotationTestImpl implements Provider<SOAPMessage> {

    public BothWebServiceAnnotationTestImpl() {
    }

    public SOAPMessage invoke(SOAPMessage obj) {
        return null;
    }
}

@WebService()
class WebServiceSEITestImpl {

    public String echo(String s) {
        return "From WebServiceSEITestImpl " + "s";
    }
}
