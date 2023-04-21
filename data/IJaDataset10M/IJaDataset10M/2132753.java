package com.ibm.wsdl.tck.soap12;

import java.io.IOException;
import javax.wsdl.BindingOperation;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.extensions.soap12.SOAP12Operation;
import javax.wsdl.factory.WSDLFactory;
import javax.xml.namespace.QName;
import junit.framework.Assert;
import junit.framework.TestCase;
import com.ibm.wsdl.tck.util.TCKUtils;

/**
 * This class implements a series of tests used to check
 * javax.wsdl.extensions.soap12.SOAP12Operation implementations
 * for compliance with the JWSDL specification.
 *
 * @author Matthew J. Duftler (duftler@us.ibm.com)
 */
public class SOAP12OperationTest extends TestCase {

    private static final String TEST_CANDIDATE_PROPERTY_NAME = "javax.wsdl.factory.WSDLFactoryCandidate";

    private static final String NS_URI_SOAP12 = "http://schemas.xmlsoap.org/wsdl/soap12/";

    private WSDLFactory factory = null;

    private ExtensionRegistry extReg = null;

    private SOAP12Operation soapOperation = null;

    protected void setUp() throws WSDLException, IOException {
        String testCandidateName = System.getProperty(TEST_CANDIDATE_PROPERTY_NAME);
        if (testCandidateName == null) {
            throw new IllegalArgumentException("System property '" + TEST_CANDIDATE_PROPERTY_NAME + "' must be specified to run " + "test suite.");
        }
        factory = WSDLFactory.newInstance(testCandidateName);
        Assert.assertNotNull("WSDLFactory should not be null.", factory);
        extReg = factory.newPopulatedExtensionRegistry();
        Assert.assertNotNull("ExtensionRegistry should not be null.", extReg);
        QName soapOperationQName = new QName(NS_URI_SOAP12, "operation");
        soapOperation = (SOAP12Operation) TCKUtils.createExtension(extReg, BindingOperation.class, soapOperationQName, SOAP12Operation.class);
    }

    /**
   * Test SOAP12Operation.setSoapActionURI(...) and
   * SOAP12Operation.getSoapActionURI(...).
   */
    public void testSetGetSoapActionURI() {
        String soapActionURI = "urn:abcdef";
        soapOperation.setSoapActionURI(soapActionURI);
        String soapActionURI2 = soapOperation.getSoapActionURI();
        Assert.assertTrue("SOAP12Operation.getSoapActionURI() did not return " + "the same soapAction URI that was set using " + "SOAP12Operation.setSoapActionURI(...).", soapActionURI.equals(soapActionURI2));
    }

    /**
   * Test SOAP12Operation.setSoapActionRequired(...) and
   * SOAP12Operation.getSoapActionRequired(...).
   */
    public void testSetGetSoapActionRequired() {
        Boolean soapActionRequired = Boolean.TRUE;
        soapOperation.setSoapActionRequired(soapActionRequired);
        Boolean soapActionRequired2 = soapOperation.getSoapActionRequired();
        Assert.assertTrue("SOAP12Operation.getSoapActionRequired() did not return " + "the same value that was set using " + "SOAP12Operation.setSoapActionRequired(...).", soapActionRequired.equals(soapActionRequired2));
    }

    /**
   * Test SOAP12Operation.setStyle(...) and SOAP12Operation.getStyle(...).
   */
    public void testSetGetStyle() {
        String style = "abcdef";
        soapOperation.setStyle(style);
        String style2 = soapOperation.getStyle();
        Assert.assertTrue("SOAP12Operation.getStyle() did not return " + "the same style that was set using " + "SOAP12Operation.setStyle(...).", style.equals(style2));
    }
}
