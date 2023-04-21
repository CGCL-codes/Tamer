package com.ibm.wsdl.tck.http;

import com.ibm.wsdl.tck.util.*;
import java.io.*;
import javax.wsdl.*;
import javax.wsdl.extensions.*;
import javax.wsdl.extensions.http.*;
import javax.wsdl.factory.*;
import javax.xml.namespace.*;
import junit.framework.*;

/**
 * This class implements a series of tests used to check
 * javax.wsdl.extensions.http.HTTPAddress implementations for compliance
 * with the JWSDL specification.
 *
 * @author Matthew J. Duftler (duftler@us.ibm.com)
 */
public class HTTPAddressTest extends TestCase {

    private static final String TEST_CANDIDATE_PROPERTY_NAME = "javax.wsdl.factory.WSDLFactoryCandidate";

    private static final String NS_URI_HTTP = "http://schemas.xmlsoap.org/wsdl/http/";

    private WSDLFactory factory = null;

    private ExtensionRegistry extReg = null;

    private HTTPAddress httpAddress = null;

    protected void setUp() throws WSDLException, IOException {
        String testCandidateName = System.getProperty(TEST_CANDIDATE_PROPERTY_NAME);
        if (testCandidateName == null) {
            throw new IllegalArgumentException("System property '" + TEST_CANDIDATE_PROPERTY_NAME + "' must be specified to run " + "test suite.");
        }
        factory = WSDLFactory.newInstance(testCandidateName);
        Assert.assertNotNull("WSDLFactory should not be null.", factory);
        extReg = factory.newPopulatedExtensionRegistry();
        Assert.assertNotNull("ExtensionRegistry should not be null.", extReg);
        QName httpAddressQName = new QName(NS_URI_HTTP, "address");
        httpAddress = (HTTPAddress) TCKUtils.createExtension(extReg, Port.class, httpAddressQName, HTTPAddress.class);
    }

    /**
   * Test HTTPAddress.setLocationURI(...) and HTTPAddress.getLocationURI().
   */
    public void testSetGetLocationURI() {
        String locationURI = "urn:abcdef";
        httpAddress.setLocationURI(locationURI);
        String locationURI2 = httpAddress.getLocationURI();
        Assert.assertTrue("HTTPAddress.getLocationURI() did not return " + "the same location URI that was set using " + "HTTPAddress.setLocationURI(...).", locationURI.equals(locationURI2));
    }
}
