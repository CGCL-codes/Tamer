package org.apache.axis2.jaxws.description;

import junit.framework.TestCase;
import org.apache.axis2.jaxws.spi.ServiceDelegate;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.lang.reflect.Method;

/**
 * Directly test the Description classes built via annotations without a WSDL file.
 * These tests focus on combinations of the following:
 * - A generic service (no annotations)
 * - A generated service (annotations)
 * - An SEI
 */
public class AnnotationDescriptionTests extends TestCase {

    public void testCreateService() {
        String namespaceURI = "http://ws.apache.org/axis2/tests";
        String localPart = "EchoServiceAnnotated";
        Service service = Service.create(null, new QName(namespaceURI, localPart));
        ServiceDelegate serviceDelegate = DescriptionTestUtils2.getServiceDelegate(service);
        ServiceDescription serviceDescription = serviceDelegate.getServiceDescription();
        String portLocalPart = "EchoServiceAnnotatedPort";
        QName portQName = new QName(namespaceURI, portLocalPart);
        DocumentLiteralWrappedProxy dlwp = service.getPort(portQName, DocumentLiteralWrappedProxy.class);
        EndpointDescription endpointDescription = serviceDescription.getEndpointDescription(portQName);
        assertNotNull("Endpoint not created ", endpointDescription);
        EndpointInterfaceDescription endpointInterfaceDescription = endpointDescription.getEndpointInterfaceDescription();
        assertNotNull("EndpointInterface not created", endpointInterfaceDescription);
        EndpointDescription[] fromSEIClass = serviceDescription.getEndpointDescription(DocumentLiteralWrappedProxy.class);
        assertEquals(1, fromSEIClass.length);
        assertEquals(endpointDescription, fromSEIClass[0]);
        OperationDescription[] operationResultArray = endpointInterfaceDescription.getOperation((QName) null);
        assertNull(operationResultArray);
        operationResultArray = endpointInterfaceDescription.getOperation(new QName("", ""));
        assertNull(operationResultArray);
        OperationDescription operationResult = endpointInterfaceDescription.getOperation((Method) null);
        assertNull(operationResult);
        Method[] seiMethods = DocumentLiteralWrappedProxy.class.getMethods();
        operationResultArray = endpointInterfaceDescription.getOperations();
        assertEquals("Number of SEI methods and operations did not match", seiMethods.length, operationResultArray.length);
        QName javaMethodQName = new QName("", "invokeAsync");
        operationResultArray = endpointInterfaceDescription.getOperation(javaMethodQName);
        assertNull(operationResultArray);
        javaMethodQName = new QName("", "invoke");
        operationResultArray = endpointInterfaceDescription.getOperation(javaMethodQName);
        assertNotNull(operationResultArray);
        assertEquals(3, operationResultArray.length);
        operationResult = endpointInterfaceDescription.getOperation(seiMethods[0]);
        assertNotNull(operationResult);
        operationResult = endpointInterfaceDescription.getOperation(this.getClass().getMethods()[0]);
        assertNull(operationResult);
    }
}
