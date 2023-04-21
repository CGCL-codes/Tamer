package com.ibm.wsdl.tck;

import java.io.IOException;
import java.util.Set;
import javax.wsdl.Binding;
import javax.wsdl.BindingFault;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Port;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensionDeserializer;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.extensions.ExtensionSerializer;
import javax.wsdl.extensions.UnknownExtensionDeserializer;
import javax.wsdl.extensions.UnknownExtensionSerializer;
import javax.wsdl.extensions.http.HTTPAddress;
import javax.wsdl.extensions.http.HTTPBinding;
import javax.wsdl.extensions.http.HTTPOperation;
import javax.wsdl.extensions.http.HTTPUrlEncoded;
import javax.wsdl.extensions.http.HTTPUrlReplacement;
import javax.wsdl.extensions.mime.MIMEContent;
import javax.wsdl.extensions.mime.MIMEMimeXml;
import javax.wsdl.extensions.mime.MIMEMultipartRelated;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPFault;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.extensions.soap12.SOAP12Address;
import javax.wsdl.extensions.soap12.SOAP12Binding;
import javax.wsdl.extensions.soap12.SOAP12Body;
import javax.wsdl.extensions.soap12.SOAP12Fault;
import javax.wsdl.extensions.soap12.SOAP12Header;
import javax.wsdl.extensions.soap12.SOAP12Operation;
import javax.wsdl.factory.WSDLFactory;
import javax.xml.namespace.QName;
import junit.framework.Assert;
import junit.framework.TestCase;
import com.ibm.wsdl.tck.util.TCKUtils;

/**
 * This class implements a series of tests used to check
 * javax.wsdl.extensions.ExtensionRegistry implementations for compliance
 * with the JWSDL specification.
 *
 * @author Matthew J. Duftler (duftler@us.ibm.com)
 */
public class ExtensionRegistryTest extends TestCase {

    private static final String TEST_CANDIDATE_PROPERTY_NAME = "javax.wsdl.factory.WSDLFactoryCandidate";

    private static final String NS_URI_SOAP = "http://schemas.xmlsoap.org/wsdl/soap/";

    private static final String NS_URI_SOAP12 = "http://schemas.xmlsoap.org/wsdl/soap12/";

    private static final String NS_URI_HTTP = "http://schemas.xmlsoap.org/wsdl/http/";

    private static final String NS_URI_MIME = "http://schemas.xmlsoap.org/wsdl/mime/";

    private WSDLFactory factory = null;

    private ExtensionRegistry extReg = null;

    protected void setUp() throws WSDLException, IOException {
        String testCandidateName = System.getProperty(TEST_CANDIDATE_PROPERTY_NAME);
        if (testCandidateName == null) {
            throw new IllegalArgumentException("System property '" + TEST_CANDIDATE_PROPERTY_NAME + "' must be specified to run " + "test suite.");
        }
        factory = WSDLFactory.newInstance(testCandidateName);
        Assert.assertNotNull("WSDLFactory should not be null.", factory);
        extReg = factory.newPopulatedExtensionRegistry();
        Assert.assertNotNull("ExtensionRegistry should not be null.", extReg);
    }

    /**
   * Test ExtensionRegistry.querySerializer(...),
   * ExtensionRegistry.queryDeserializer(...),
   * and ExtensionRegistry.createExtension(...), to ensure that
   * serializers, deserializers, and Java types have been registered for
   * all the specification-defined SOAP extensibility elements.
   */
    public void testSOAPMappings() {
        Class[] parentTypes = new Class[] { Binding.class, BindingOperation.class, BindingInput.class, BindingInput.class, BindingOutput.class, BindingOutput.class, BindingFault.class, Port.class };
        QName[] elementTypes = new QName[] { new QName(NS_URI_SOAP, "binding"), new QName(NS_URI_SOAP, "operation"), new QName(NS_URI_SOAP, "body"), new QName(NS_URI_SOAP, "header"), new QName(NS_URI_SOAP, "body"), new QName(NS_URI_SOAP, "header"), new QName(NS_URI_SOAP, "fault"), new QName(NS_URI_SOAP, "address") };
        Class[] extTypes = new Class[] { SOAPBinding.class, SOAPOperation.class, SOAPBody.class, SOAPHeader.class, SOAPBody.class, SOAPHeader.class, SOAPFault.class, SOAPAddress.class };
        testMappings(parentTypes, elementTypes, extTypes);
    }

    /**
   * Test ExtensionRegistry.querySerializer(...),
   * ExtensionRegistry.queryDeserializer(...),
   * and ExtensionRegistry.createExtension(...), to ensure that
   * serializers, deserializers, and Java types have been registered for
   * all the specification-defined SOAP 1.2 extensibility elements.
   */
    public void testSOAP12Mappings() {
        Class[] parentTypes = new Class[] { Binding.class, BindingOperation.class, BindingInput.class, BindingInput.class, BindingOutput.class, BindingOutput.class, BindingFault.class, Port.class };
        QName[] elementTypes = new QName[] { new QName(NS_URI_SOAP12, "binding"), new QName(NS_URI_SOAP12, "operation"), new QName(NS_URI_SOAP12, "body"), new QName(NS_URI_SOAP12, "header"), new QName(NS_URI_SOAP12, "body"), new QName(NS_URI_SOAP12, "header"), new QName(NS_URI_SOAP12, "fault"), new QName(NS_URI_SOAP12, "address") };
        Class[] extTypes = new Class[] { SOAP12Binding.class, SOAP12Operation.class, SOAP12Body.class, SOAP12Header.class, SOAP12Body.class, SOAP12Header.class, SOAP12Fault.class, SOAP12Address.class };
        testMappings(parentTypes, elementTypes, extTypes);
    }

    /**
   * Test ExtensionRegistry.querySerializer(...),
   * ExtensionRegistry.queryDeserializer(...),
   * and ExtensionRegistry.createExtension(...), to ensure that
   * serializers, deserializers, and Java types have been registered for
   * all the specification-defined HTTP extensibility elements.
   */
    public void testHTTPMappings() {
        Class[] parentTypes = new Class[] { Binding.class, BindingOperation.class, BindingInput.class, BindingInput.class, Port.class };
        QName[] elementTypes = new QName[] { new QName(NS_URI_HTTP, "binding"), new QName(NS_URI_HTTP, "operation"), new QName(NS_URI_HTTP, "urlEncoded"), new QName(NS_URI_HTTP, "urlReplacement"), new QName(NS_URI_HTTP, "address") };
        Class[] extTypes = new Class[] { HTTPBinding.class, HTTPOperation.class, HTTPUrlEncoded.class, HTTPUrlReplacement.class, HTTPAddress.class };
        testMappings(parentTypes, elementTypes, extTypes);
    }

    /**
   * Test ExtensionRegistry.querySerializer(...),
   * ExtensionRegistry.queryDeserializer(...),
   * and ExtensionRegistry.createExtension(...), to ensure that
   * serializers, deserializers, and Java types have been registered for
   * all the specification-defined MIME extensibility elements.
   */
    public void testMIMEMappings() {
        Class[] parentTypes = new Class[] { BindingInput.class, BindingInput.class, BindingInput.class, BindingOutput.class, BindingOutput.class, BindingOutput.class };
        QName[] elementTypes = new QName[] { new QName(NS_URI_MIME, "content"), new QName(NS_URI_MIME, "multipartRelated"), new QName(NS_URI_MIME, "mimeXml"), new QName(NS_URI_MIME, "content"), new QName(NS_URI_MIME, "multipartRelated"), new QName(NS_URI_MIME, "mimeXml") };
        Class[] extTypes = new Class[] { MIMEContent.class, MIMEMultipartRelated.class, MIMEMimeXml.class, MIMEContent.class, MIMEMultipartRelated.class, MIMEMimeXml.class };
        testMappings(parentTypes, elementTypes, extTypes);
    }

    private void testMappings(Class[] parentTypes, QName[] elementTypes, Class[] extTypes) {
        for (int i = 0; i < parentTypes.length; i++) {
            try {
                ExtensionSerializer serializer = extReg.querySerializer(parentTypes[i], elementTypes[i]);
                Assert.assertNotNull("No ExtensionSerializer found " + "to serialize a '" + elementTypes[i] + "' element in the context of a '" + parentTypes[i].getName() + "'.", serializer);
                Assert.assertFalse("No ExtensionSerializer found " + "to serialize a '" + elementTypes[i] + "' element in the context of a '" + parentTypes[i].getName() + "'.", serializer instanceof UnknownExtensionSerializer);
            } catch (WSDLException e) {
                Assert.fail("No ExtensionSerializer found " + "to serialize a '" + elementTypes[i] + "' element in the context of a '" + parentTypes[i].getName() + "'.");
            }
            try {
                ExtensionDeserializer deserializer = extReg.queryDeserializer(parentTypes[i], elementTypes[i]);
                Assert.assertNotNull("No ExtensionDeserializer found " + "to deserialize a '" + elementTypes[i] + "' element in the context of a '" + parentTypes[i].getName() + "'.", deserializer);
                Assert.assertFalse("No ExtensionDeserializer found " + "to deserialize a '" + elementTypes[i] + "' element in the context of a '" + parentTypes[i].getName() + "'.", deserializer instanceof UnknownExtensionDeserializer);
            } catch (WSDLException e) {
                Assert.fail("No ExtensionDeserializer found " + "to deserialize a '" + elementTypes[i] + "' element in the context of a '" + parentTypes[i].getName() + "'.");
            }
            TCKUtils.createExtension(extReg, parentTypes[i], elementTypes[i], extTypes[i]);
        }
    }

    private void testGetAllowableExtensions(Class parentType, QName[] elementTypes) throws WSDLException, IOException {
        setUp();
        Set exts = extReg.getAllowableExtensions(parentType);
        for (int i = 0; i < elementTypes.length; i++) {
            Assert.assertTrue("The set retrieved by calling " + "ExtensionRegistry.getAllowableExtensions(" + parentType.getName() + ".class) does not " + "contain the QName '" + elementTypes[i] + "'.", (exts != null && exts.contains(elementTypes[i])));
        }
    }

    /**
   * Test ExtensionRegistry.getAllowableExtensions(...) to ensure that
   * all the specification-defined extensions (SOAP, HTTP, and MIME)
   * are registered to be allowed in the correct locations.
   */
    public void testGetAllowableExtensions() throws WSDLException, IOException {
        testGetAllowableExtensions(Binding.class, new QName[] { new QName(NS_URI_SOAP, "binding"), new QName(NS_URI_SOAP12, "binding"), new QName(NS_URI_HTTP, "binding") });
        testGetAllowableExtensions(BindingOperation.class, new QName[] { new QName(NS_URI_SOAP, "operation"), new QName(NS_URI_SOAP12, "operation"), new QName(NS_URI_HTTP, "operation") });
        testGetAllowableExtensions(BindingInput.class, new QName[] { new QName(NS_URI_SOAP, "body"), new QName(NS_URI_SOAP, "header"), new QName(NS_URI_SOAP12, "body"), new QName(NS_URI_SOAP12, "header"), new QName(NS_URI_HTTP, "urlEncoded"), new QName(NS_URI_HTTP, "urlReplacement"), new QName(NS_URI_MIME, "content"), new QName(NS_URI_MIME, "multipartRelated"), new QName(NS_URI_MIME, "mimeXml") });
        testGetAllowableExtensions(BindingOutput.class, new QName[] { new QName(NS_URI_SOAP, "body"), new QName(NS_URI_SOAP, "header"), new QName(NS_URI_MIME, "content"), new QName(NS_URI_MIME, "multipartRelated"), new QName(NS_URI_MIME, "mimeXml") });
        testGetAllowableExtensions(BindingFault.class, new QName[] { new QName(NS_URI_SOAP, "fault") });
        testGetAllowableExtensions(Port.class, new QName[] { new QName(NS_URI_SOAP, "address"), new QName(NS_URI_HTTP, "address") });
    }

    /**
   * Test ExtensionRegistry.setDefaultSerializer(...) and
   * ExtensionRegistry.getDefaultSerializer(). Test that default
   * value is an UnknownExtensionSerializer instance.
   */
    public void testSetGetDefaultSerializer() {
        ExtensionSerializer serializer = extReg.getDefaultSerializer();
        Assert.assertTrue("Default value returned by " + "ExtensionRegistry.getDefaultSerializer() " + "should be an UnknownExtensionSerializer.", serializer instanceof UnknownExtensionSerializer);
        extReg.setDefaultSerializer(null);
        ExtensionSerializer serializer2 = extReg.getDefaultSerializer();
        Assert.assertNull("ExtensionRegistry.getDefaultSerializer() " + "did not return null, even though " + "ExtensionRegistry.setDefaultSerializer(null) " + "was called.", serializer2);
    }

    /**
   * Test ExtensionRegistry.setDefaultDeserializer(...) and
   * ExtensionRegistry.getDefaultDeserializer(). Test that default
   * value is an UnknownExtensionDeserializer instance.
   */
    public void testSetGetDefaultDeserializer() {
        ExtensionDeserializer deserializer = extReg.getDefaultDeserializer();
        Assert.assertTrue("Default value returned by " + "ExtensionRegistry.getDefaultDeserializer() " + "should be an UnknownExtensionDeserializer.", deserializer instanceof UnknownExtensionDeserializer);
        extReg.setDefaultDeserializer(null);
        ExtensionDeserializer deserializer2 = extReg.getDefaultDeserializer();
        Assert.assertNull("ExtensionRegistry.getDefaultDeserializer() " + "did not return null, even though " + "ExtensionRegistry.setDefaultDeserializer(null) " + "was called.", deserializer2);
    }
}
